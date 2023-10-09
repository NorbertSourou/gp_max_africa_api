package io.maxafrica.gpserver.services.impl;

import io.maxafrica.gpserver.dto.ApiResponse;
import io.maxafrica.gpserver.dto.JwtAuthenticationResponse;
import io.maxafrica.gpserver.dto.LoginRequest;
import io.maxafrica.gpserver.dto.RegisterUser;
import io.maxafrica.gpserver.entities.Role;
import io.maxafrica.gpserver.entities.User;
import io.maxafrica.gpserver.entities.enums.UserType;
import io.maxafrica.gpserver.repositories.RoleRepository;
import io.maxafrica.gpserver.repositories.UserRepository;
import io.maxafrica.gpserver.security.TokenProvider;
import io.maxafrica.gpserver.security.UserPrincipal;
import io.maxafrica.gpserver.services.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthServiceImpl implements AuthService {


    // TODO: 10/9/2023 Review Load DaATA 
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final TokenProvider tokenProvider;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    @Value("${user.role}")
    private String userRole;


    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, TokenProvider tokenProvider, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ApiResponse createUser(RegisterUser registerUser) {
        Role role = roleRepository.findByName(userRole);
        User user = new User();
        user.setEmail(registerUser.getEmail());
        user.setUsername(registerUser.getFirstName());
        user.setFirstname(registerUser.getFirstName());
        user.setLastname(registerUser.getLastName());
        user.setPassword(this.passwordEncoder.encode(registerUser.getPassword()));
        user.setRole(role);
        user.setUserType(UserType.MOBILE_USER);
        userRepository.save(user);
        
        // TODO: 10/9/2023  : validate user account here        
        return new ApiResponse(true, user.getFirstname() + " " + user.getLastname() + " successfully create");
    }

    @Override
    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // TODO: 10/9/2023 Check if user account is validate 

//        UserPrincipal userP = (UserPrincipal) authentication.getPrincipal();
//
//        User user = userRepository.findById(userP.getId()).orElseThrow();

        SecurityContextHolder.getContext().setAuthentication(authentication);



        JwtAuthenticationResponse token = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(token);
    }
}
