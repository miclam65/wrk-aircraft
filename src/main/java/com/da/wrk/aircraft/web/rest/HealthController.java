package com.da.wrk.aircraft.web.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/health")
class HealthController {
    
    @GetMapping
    public RedirectView index() {
        return new RedirectView("/actuator/health");
    }
}