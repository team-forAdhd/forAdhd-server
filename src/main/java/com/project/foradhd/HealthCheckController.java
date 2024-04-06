package com.project.foradhd;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1")
@RestController
public class HealthCheckController {

    @GetMapping("/health-check")
    public String checkServerHealth() {
        return "Server is OK";
    }
}
