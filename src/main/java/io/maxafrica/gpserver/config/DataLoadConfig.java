package io.maxafrica.gpserver.config;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoadConfig {

    @PostConstruct
    public void loadData() {
        // TODO: 9/23/2023 load data before here
    }
}
