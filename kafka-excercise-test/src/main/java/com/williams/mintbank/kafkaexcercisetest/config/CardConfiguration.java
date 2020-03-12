package com.williams.mintbank.kafkaexcercisetest.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Builder
@Component
@ConfigurationProperties(prefix = "card")
@Data
@NoArgsConstructor
public class CardConfiguration {

    private String baseUrl;
}
