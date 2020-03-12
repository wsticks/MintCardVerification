package com.williams.mintbank.kafkaexcercisetest.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardThirdPartyApiResponse {

    private String scheme;
    private String type;
    private String brand;
    private String prepaid;
    private CardThirdPartyNumberApiResponse number;
    private CardThirdPartyCountryApiResponse.CardThirdPartyCountryApiResponseBuilder country;
    private CardThirdPartyBankApiResponse bank;
}
