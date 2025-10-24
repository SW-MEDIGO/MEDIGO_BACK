package com.example.oswc;

import com.example.oswc.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HealthController {
    @GetMapping("/health")
    ApiResponse<String> health() {
        return ApiResponse.ok("UP");
    }
}
