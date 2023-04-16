package com.da.wrk.aircraft.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MetricsConfig {
    
    @Bean
    @Qualifier("CallCounter")
    public Counter callCounter(MeterRegistry meterRegistry) {
        return Counter
            .builder("call_counter")
            .tag("type", "Business")
            .description("Business call counter")
            .register(meterRegistry);
    }
}
