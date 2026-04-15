package com.example.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public class AuthDTOs {

    @Data
    public static class SignupRequest {
        @NotBlank private String name;
        @Email @NotBlank private String email;
        @NotBlank @Size(min = 6) private String password;
        private String mobile;
    }

    @Data
    public static class LoginRequest {
        @Email @NotBlank private String email;
        @NotBlank private String password;
    }

    @Data
    public static class GoogleLoginRequest {
        @NotBlank private String idToken;
    }

    @Data
    public static class AuthResponse {
        private String token;
        private String email;
        private String name;

        public AuthResponse(String token, String email, String name) {
            this.token = token;
            this.email = email;
            this.name = name;
        }
    }
}
