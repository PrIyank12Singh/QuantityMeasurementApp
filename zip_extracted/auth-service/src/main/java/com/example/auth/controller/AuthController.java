package com.example.auth.controller;

import com.example.auth.dto.AuthDTOs;
import com.example.auth.service.UserAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserAuthService userAuthService;

    @PostMapping("/signup")
    public ResponseEntity<AuthDTOs.AuthResponse> signup(@Valid @RequestBody AuthDTOs.SignupRequest req) {
        return ResponseEntity.ok(userAuthService.signup(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthDTOs.AuthResponse> login(@Valid @RequestBody AuthDTOs.LoginRequest req) {
        return ResponseEntity.ok(userAuthService.login(req));
    }

    @PostMapping("/google")
    public ResponseEntity<AuthDTOs.AuthResponse> googleLogin(@RequestBody Map<String, String> body) {
        String idToken = body.get("idToken");
        if (idToken == null || idToken.isBlank()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(userAuthService.googleLogin(idToken));
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("Auth service is running");
    }
}
