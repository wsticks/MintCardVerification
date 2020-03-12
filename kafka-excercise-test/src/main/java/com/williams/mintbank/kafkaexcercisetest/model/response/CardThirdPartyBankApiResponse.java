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
public class CardThirdPartyBankApiResponse {

    private String name;
    private String url;
    private String phone;
    private String city;



}
