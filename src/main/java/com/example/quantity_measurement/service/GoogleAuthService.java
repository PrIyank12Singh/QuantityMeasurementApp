package com.example.quantity_measurement.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.example.quantity_measurement.model.User;
import com.example.quantity_measurement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleAuthService {

    private static final String CLIENT_ID = "481397534301-3r9dl62f6m2ij565i79463d5276ig2oh.apps.googleusercontent.com";

    private final UserRepository userRepository;
    private final JwtService jwtService;

    public GoogleAuthService(UserRepository userRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public Map<String, Object> verifyToken(String tokenString) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance()
            )
            .setAudience(Collections.singletonList(CLIENT_ID))
            .build();

            GoogleIdToken idToken = verifier.verify(tokenString);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                String name = (String) payload.get("name");

                // ✅ Save user in DB if not exists
                User user = userRepository.findByEmail(email)
                        .orElseGet(() -> {
                            User newUser = new User();
                            newUser.setEmail(email);
                            newUser.setName(name);
                            return userRepository.save(newUser);
                        });

                // ✅ Generate JWT token
                String token = jwtService.generateToken(user.getEmail());

                // ✅ Response to frontend
                Map<String, Object> response = new HashMap<>();
                response.put("email", user.getEmail());
                response.put("name", user.getName());
                response.put("token", token);

                return response;

            } else {
                throw new RuntimeException("Invalid Google token");
            }

        } catch (Exception e) {
            throw new RuntimeException("Token verification failed: " + e.getMessage());
        }
    }
}