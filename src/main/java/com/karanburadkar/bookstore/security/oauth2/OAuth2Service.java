package com.karanburadkar.bookstore.security.oauth2;

import com.karanburadkar.bookstore.dto.LoginResponseDto;
import com.karanburadkar.bookstore.dto.SignupRequestDto;
import com.karanburadkar.bookstore.dto.SignupResponseDto;
import com.karanburadkar.bookstore.entity.Customer;
import com.karanburadkar.bookstore.entity.User;
import com.karanburadkar.bookstore.entity.type.AuthProvider;
import com.karanburadkar.bookstore.entity.type.RoleType;
import com.karanburadkar.bookstore.exception.authentication.UsernameNotFoundException;
import com.karanburadkar.bookstore.exception.authentication.WrongPasswordException;
import com.karanburadkar.bookstore.exception.database.CustomerNotFoundException;
import com.karanburadkar.bookstore.exception.database.PublisherNotFoundException;
import com.karanburadkar.bookstore.exception.database.UserNotFoundException;
import com.karanburadkar.bookstore.repository.CustomerRepository;
import com.karanburadkar.bookstore.repository.PublisherRepository;
import com.karanburadkar.bookstore.repository.UserRepository;
import com.karanburadkar.bookstore.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OAuth2Service {

    private static final Logger log = LoggerFactory.getLogger(OAuth2Service.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired private JwtService jwtService;

    public void resolveOAuth2(SignupRequestDto signupRequestDto) {
        User user = userRepository.findUserWithRolesByUsername(signupRequestDto.getUsername())
                .orElse(null);

        if (user != null) {
            login(user);
        }
        else {
            signup(signupRequestDto);
        }
    }

    private LoginResponseDto login(User user) {
        log.info("Login request executing");
        try {

            String accessToken = jwtService.generateAccessToken(user);
            String refreshToken = jwtService.updateRefreshToken(user);

            return buildLoginResponseDto(accessToken, refreshToken, user);

        } catch (InternalAuthenticationServiceException e) {
            log.error(e.getMessage());
            throw new UsernameNotFoundException("Account not found with this email");
        } catch (BadCredentialsException e) {
            log.error(e.getMessage());
            throw new WrongPasswordException("Wrong password");
        }
    }

    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {
        log.info("signup request executing");

        User user = createNewUserAccount(signupRequestDto.getUsername(), signupRequestDto.getPassword(), signupRequestDto.getProvider());

        StringBuilder name = new StringBuilder(signupRequestDto.getFirstname());
        if (signupRequestDto.getLastname() != null && !signupRequestDto.getLastname().isBlank()) {
            name.append(" ").append(signupRequestDto.getLastname());
        }
        Customer customer = new Customer(name.toString(), user, signupRequestDto.getPhoneNo());
        Customer newCustomer = customerRepository.save(customer);

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateNewRefreshToken(user);

        return new SignupResponseDto(true, user.getId(), user.getUsername(),
                user.getRoles().toString(), newCustomer.getId(), user.getProvider(),
                accessToken, refreshToken
        );
    }

    private User createNewUserAccount(String username, String password, AuthProvider provider) {
        return userRepository.save(new User(
                username,
                password,
                RoleType.CUSTOMER,
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

}
