package com.example.identityservice.controller;

import com.example.identityservice.dto.request.ApiResponse;
import com.example.identityservice.dto.request.IntrospectRequest;
import com.example.identityservice.dto.request.Login;
import com.example.identityservice.dto.response.AuthenticationResponse;
import com.example.identityservice.dto.response.IntrospectResponse;
import com.example.identityservice.Utils.JwtUtil;
import com.example.identityservice.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
    AuthenticationService authenticationService;
    JwtUtil verifyJwtUtil;

    @PostMapping("/token")
    ApiResponse<AuthenticationResponse> login(@RequestBody Login loginRequest) {
        var result = authenticationService.authenticate(loginRequest);
        return ApiResponse.<AuthenticationResponse>builder()
                .result(result).build();
    }
    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> login(@RequestBody IntrospectRequest req) throws ParseException, JOSEException {
        var result = verifyJwtUtil.introspect(req);
        return ApiResponse.<IntrospectResponse>builder()
                .result(result).build();
    }
}
