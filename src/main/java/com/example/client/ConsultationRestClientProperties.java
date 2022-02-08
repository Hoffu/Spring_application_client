package com.example.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="consultation")
@Data
public class ConsultationRestClientProperties {
    private String url;
    private String basePath;
}
