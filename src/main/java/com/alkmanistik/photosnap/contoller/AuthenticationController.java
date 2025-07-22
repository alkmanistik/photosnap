package com.alkmanistik.photosnap.contoller;

import com.alkmanistik.photosnap.dto.request.AuthenticationRequest;
import com.alkmanistik.photosnap.dto.request.RefreshTokenRequest;
import com.alkmanistik.photosnap.dto.request.RegisterRequest;
import com.alkmanistik.photosnap.dto.response.AuthenticationResponse;
import com.alkmanistik.photosnap.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public AuthenticationResponse register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return authenticationService.register(request);
    }

    @PostMapping("/authenticate")
    public AuthenticationResponse authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/refresh-token")
    public AuthenticationResponse refreshToken(
            @RequestBody RefreshTokenRequest request
    ) {
        return authenticationService.refreshToken(request);
    }
}