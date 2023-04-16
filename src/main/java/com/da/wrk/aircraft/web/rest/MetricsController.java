package com.da.wrk.aircraft.web.rest;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.da.wrk.aircraft.monitor.metrics.CallMetrics;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/metrics")
class MetricsController {
    
    private final CallMetrics metrics;

    @GetMapping
    public RedirectView index() {
        return new RedirectView("/actuator/prometheus");
    }

    @GetMapping("counter")
    public String counter() throws UnknownHostException {
        final String hostName = InetAddress.getLocalHost().getCanonicalHostName();
        metrics.increment();
        return "hostName: " + hostName + "\nCounter value: " + metrics.value();
    }
}