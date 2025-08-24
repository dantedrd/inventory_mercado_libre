package com.mercadolibre.inventory.central.infrastructure.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "com.mercadolibre.inventory.central",
        includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")},
        useDefaultFilters = false)
public class UseCaseConfig {
}
