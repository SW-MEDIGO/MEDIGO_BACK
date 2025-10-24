package com.example.oswc.me;

import com.example.oswc.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController @RequestMapping("/me")
public class MeController {
    @GetMapping
    public ResponseEntity<?> whoAmI(Authentication auth){
        return ResponseEntity.ok(ApiResponse.ok(Map.of("userId", auth.getName())));
    }
}
