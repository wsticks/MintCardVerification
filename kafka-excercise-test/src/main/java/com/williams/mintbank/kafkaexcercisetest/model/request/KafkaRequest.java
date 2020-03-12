package com.williams.mintbank.kafkaexcercisetest.model.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.williams.mintbank.kafkaexcercisetest.model.response.CardApiResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KafkaRequest {

    private String scheme;
    private String type;
    private String bank;

    public static KafkaRequest fromCardVerificationResponse(
            CardApiResponse cardApiResponse){
        return KafkaRequest
                .builder()
                .bank(cardApiResponse.getPayload().getBank())
                .scheme(cardApiResponse.getPayload().getScheme())
                .type(cardApiResponse.getPayload().getType())
                .build();
    }
}
