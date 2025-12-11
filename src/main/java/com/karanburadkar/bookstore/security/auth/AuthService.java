package com.karanburadkar.bookstore.security.auth;

import com.karanburadkar.bookstore.dto.*;
import com.karanburadkar.bookstore.entity.type.AuthProvider;
import com.karanburadkar.bookstore.exception.database.UserNotFoundException;
import com.karanburadkar.bookstore.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import com.karanburadkar.bookstore.entity.Customer;
import com.karanburadkar.bookstore.entity.Publisher;
import com.karanburadkar.bookstore.entity.User;
import com.karanburadkar.bookstore.entity.type.RoleType;
import com.karanburadkar.bookstore.exception.authentication.WrongPasswordException;
import com.karanburadkar.bookstore.exception.database.CustomerNotFoundException;
import com.karanburadkar.bookstore.exception.database.PublisherNotFoundException;
import com.karanburadkar.bookstore.repository.CustomerRepository;
import com.karanburadkar.bookstore.repository.PublisherRepository;
import com.karanburadkar.bookstore.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.karanburadkar.bookstore.entity.type.RoleType.CUSTOMER;
import static com.karanburadkar.bookstore.entity.type.RoleType.PUBLISHER;

@Service
@Transactional
public class AuthService {

    private final CustomerRepository customerRepository;

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final UserRepository userRepository;
    private final PublisherRepository publisherRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            JwtService jwtService,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CustomerRepository customerRepository,
            PublisherRepository publisherRepository,
            AuthenticationManager authenticationManager) {

        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.publisherRepository = publisherRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
            );

            User user = (User) authenticate.getPrincipal();

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.updateRefreshToken(user);

            return buildLoginResponseDto(accessToken, refreshToken, user);
        } catch (InternalAuthenticationServiceException e) {
            log.error(e.getMessage());
            throw new UserNotFoundException("No user found with this mail, please create fresh account");
        } catch (BadCredentialsException e) {
            log.error(e.getMessage());
            throw new WrongPasswordException("Wrong password");
        }
    }

    @Transactional
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {

        validateUser(signupRequestDto.getUsername());

        User user = createNewUserAccount(
                signupRequestDto.getUsername(), signupRequestDto.getPassword(),
                CUSTOMER, signupRequestDto.getProvider()
        );

        StringBuilder name = new StringBuilder(signupRequestDto.getFirstname());
        if (!signupRequestDto.getLastname().isBlank()) {
            name.append(" ").append(signupRequestDto.getLastname());
        }

        Customer customer = new Customer(name.toString(), user, signupRequestDto.getPhoneNo());
        Customer save = customerRepository.save(customer);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateNewRefreshToken(user);

        return new SignupResponseDto(
                true, user.getId(),
                user.getUsername(),
                user.getRoles().toString(),
                save.getId(), user.getProvider(),
                accessToken, refreshToken
        );
    }

    @Transactional
    public PublisherSignupResponseDto signup(PublisherSignupRequestDto signupRequestDto) {

        validateUser(signupRequestDto.getUsername());

        User user = createNewUserAccount(signupRequestDto.getUsername(), signupRequestDto.getPassword(), PUBLISHER, signupRequestDto.getProvider());

        Publisher publisher = new Publisher(signupRequestDto.getName(), user, signupRequestDto.getPhoneNo());
        Publisher savedPublisher = publisherRepository.save(publisher);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateNewRefreshToken(user);

        return new PublisherSignupResponseDto(true, user.getRoles().toString(), savedPublisher.getId(),
                user.getUsername(), savedPublisher.getName(), user.getProvider(),
                accessToken, refreshToken
        );
    }

    private void validateUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user != null) {
            log.error("User already exist with this email");
            throw new IllegalArgumentException("User already exist with this email");
        }
    }

    private User createNewUserAccount(String username, String password, RoleType role, AuthProvider provider) {
        return userRepository.save(new User(
                username,
                passwordEncoder.encode(password),
                role,
                provider
        ));
    }

    private LoginResponseDto buildLoginResponseDto(String accessToken, String refreshToken, User user) {

        List<RoleType> roles = user.getRoles().stream().toList();
        List<Long> rolesIds = roles.stream().map(r -> {
            switch (r) {
                case CUSTOMER -> {
                    return customerRepository.findByUserId(
                            user.getId()).orElseThrow(() -> new CustomerNotFoundException(user.getId())).getId();
                }
                case PUBLISHER -> {
                    return publisherRepository.findByUserId(
                            user.getId()).orElseThrow(() -> new PublisherNotFoundException(user.getId())).getId();
                }
                case ADMIN, MANAGEMENT -> {
                    return userRepository.findById(
                            user.getId()).orElseThrow(
                            () -> new UserNotFoundException(user.getId())
                    ).getId();
                }
                default -> {
                    return null;
                }
            }
        }).collect(Collectors.toList());

        return new LoginResponseDto(user.getId(), roles, rolesIds, accessToken, refreshToken);
    }

    @Transactional
    public RefreshTokenResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {

        final String requestTokenHandler = request.getHeader("Authorization");

        String token = requestTokenHandler.substring(7);

        if (!jwtService.isValidRefreshToken(token)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String username = jwtService.getUsernameFromToken(token);

        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("username not found");
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String accessToken = jwtService.generateAccessToken(user);

        return new RefreshTokenResponse(accessToken, "");

    }
}
