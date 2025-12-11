package com.karanburadkar.bookstore.security.auth;

import com.karanburadkar.bookstore.dto.LoginRequestDto;
import com.karanburadkar.bookstore.dto.PublisherSignupRequestDto;
import com.karanburadkar.bookstore.dto.PublisherSignupResponseDto;
import com.karanburadkar.bookstore.dto.LoginResponseDto;
import com.karanburadkar.bookstore.dto.SignupRequestDto;
import com.karanburadkar.bookstore.dto.SignupResponseDto;
import com.karanburadkar.bookstore.dto.RefreshTokenResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.login(loginRequestDto));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) {
//        signupRequestDto.setProvider(AuthProvider.LOCAL);
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(signupRequestDto));
    }

    @PostMapping("/signup/publisher")
    public ResponseEntity<PublisherSignupResponseDto> signupAsPublisher(
            @RequestBody @Valid PublisherSignupRequestDto publisherSignupRequestDto
    ) {
//        publisherSignupRequestDto.setProvider("EmailPassword");
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.signup(publisherSignupRequestDto));
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<RefreshTokenResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        RefreshTokenResponse refreshTokenResponse = authService.refreshToken(request, response);
        return ResponseEntity.ok(refreshTokenResponse);
    }
}
