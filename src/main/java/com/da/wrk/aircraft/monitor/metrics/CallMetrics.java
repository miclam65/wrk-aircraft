package com.da.wrk.aircraft.monitor.metrics;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CallMetrics {
    
    @Qualifier("CallCounter")
    private final Counter counter;

    public void increment() {
        counter.increment();
    }

    public long value() {
        return (long) counter.count();
    }
}
