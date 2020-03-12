package com.williams.mintbank.kafkaexcercisetest.service;

import com.williams.mintbank.kafkaexcercisetest.model.entity.Card;
import com.williams.mintbank.kafkaexcercisetest.model.request.KafkaRequest;
import com.williams.mintbank.kafkaexcercisetest.model.response.CardApiResponse;
import com.williams.mintbank.kafkaexcercisetest.model.response.CardApiResponseData;
import com.williams.mintbank.kafkaexcercisetest.model.response.CardHitResponse;
import com.williams.mintbank.kafkaexcercisetest.model.response.CardThirdPartyApiResponse;
import com.williams.mintbank.kafkaexcercisetest.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;
    private RestTemplate restTemplate = new RestTemplate();
    private static final String BASE_URL = "https://lookup.binlist.net/";
    private static final Integer DEFAULT_COUNT = 1;
//    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "com.ng.vela.even.card_verified";

    public CardService() {
    }

    public CardService(CardRepository cardRepository,
                       KafkaTemplate kafkaTemplate) {
        this.cardRepository = cardRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public CardApiResponse getCardVerification(
            String bin) throws Exception {
        String url = BASE_URL + bin;
        System.out.println("url " + url);
        CardThirdPartyApiResponse cardThirdPartyResponse =
                restTemplate.getForObject(url,CardThirdPartyApiResponse.class);
        System.out.println("response" + cardThirdPartyResponse);
        CardApiResponse cardApiResponse =
                castToCardApiResponse(cardThirdPartyResponse);
        saveResponse(cardApiResponse, bin);
        pushToKafka(cardApiResponse);
        return cardApiResponse;
    }

    public CardApiResponse castToCardApiResponse(CardThirdPartyApiResponse cardThirdPartyResponse){
        CardApiResponse cardApiResponse = new CardApiResponse();
        cardApiResponse.setSuccess("true");
        cardApiResponse.setPayload(castToCardApiResponseForPayload(cardThirdPartyResponse));
        return cardApiResponse;
    }

    public CardApiResponseData castToCardApiResponseForPayload(CardThirdPartyApiResponse cardThirdPartyResponse){
        CardApiResponseData cardApiResponseData = new CardApiResponseData();
        cardApiResponseData.setBank(cardThirdPartyResponse.getBank().getName());
        cardApiResponseData.setScheme(cardThirdPartyResponse.getScheme());
        cardApiResponseData.setType(cardThirdPartyResponse.getType());
        return cardApiResponseData;
    }

    public void saveResponse(CardApiResponse cardApiResponse,  String bin){
        System.out.println("Check if bin came here" + bin);
        List<Card> fetchCards = cardRepository.findAll();
        Card filteredCard = fetchCards.stream()
                .filter(fetchCard-> fetchCard.getBin().equals(bin))
                .findAny()
                .orElse(null);
        if(filteredCard == null){
            Card cardToSave = new Card();
            cardToSave.setBank(cardApiResponse.getPayload().getBank());
            cardToSave.setScheme(cardApiResponse.getPayload().getScheme());
            cardToSave.setType(cardApiResponse.getPayload().getType());
            cardToSave.setCount(String.valueOf(DEFAULT_COUNT));
            cardToSave.setBin(bin);
            cardRepository.save(cardToSave);
        }else {
            String existingCountValue = filteredCard.getCount();
            Integer newCount = Integer.parseInt(existingCountValue) + 1;
            String newCountValue = String.valueOf(newCount);
            filteredCard.setCount(newCountValue);
            cardRepository.save(filteredCard);
        }
    }

    public CardHitResponse getCardVerificationHitCount(String start, String limit)
    throws Exception{
        List<Card> fetchAllCardDetails = cardRepository.findAll();
        long count = fetchAllCardDetails.stream().count();
        List<String> datas = new ArrayList<>();
        fetchAllCardDetails
                .stream()
                .filter(cardDetail-> cardDetail.getId()== Integer.parseInt(start))
                .filter(cardDetail-> cardDetail.getId() <= Integer.parseInt(limit))
                .forEach(cardDetail->{
                    Map<String, String> data = new HashMap<>();
                    data.put(cardDetail.getBin(), cardDetail.getCount()) ;
                    datas.add(String.valueOf(data));
                });
        CardHitResponse  cardHitApiResponse = new CardHitResponse();
        cardHitApiResponse.setPayload(datas);
        cardHitApiResponse.setSuccess("true");
        cardHitApiResponse.setStart(start);
        cardHitApiResponse.setLimit(limit);
        cardHitApiResponse.setSize(String.valueOf(count));
        return cardHitApiResponse;
    }

    public void pushToKafka(CardApiResponse cardApiResponse){
        KafkaRequest kafkaRequest = KafkaRequest.fromCardVerificationResponse(cardApiResponse);
        kafkaTemplate.send(TOPIC, String.valueOf(kafkaRequest));
        System.out.println("Push Successful");
    }
}
