package com.williams.mintbank.kafkaexcercisetest.controller;

import com.williams.mintbank.kafkaexcercisetest.model.response.CardApiResponse;
import com.williams.mintbank.kafkaexcercisetest.model.response.CardHitResponse;
import com.williams.mintbank.kafkaexcercisetest.service.CardService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/cards-scheme")
public class CardController {


    private CardService cardService = new CardService();

    public String post(
            @PathVariable("messages") final String messages){
        System.out.print(messages);
//        kafkaTemplate.send(TOPIC, messages);
        return "Published Successfully";
    }


    @RequestMapping(path = "/verify/{bin}", produces = "application/json", method = RequestMethod.GET)
    public CardApiResponse getCardVerification(
            @PathVariable String bin) throws Exception {
        CardApiResponse cardApiResponse = cardService.getCardVerification(bin);
        return cardApiResponse;
    }

    @RequestMapping(path = "/stats", produces = "application/json" , method = RequestMethod.GET)
    public CardHitResponse getCardVerificationHitCount(
            @RequestParam("start") String start,
            @RequestParam("limit") String limit) throws Exception {
        CardHitResponse cardVerificationApiResponse = cardService.getCardVerificationHitCount(start,limit);
        return cardVerificationApiResponse;
    }
}
