package io.maxafrica.gpserver.security;

import io.maxafrica.gpserver.entities.User;
import io.maxafrica.gpserver.exceptions.ResourceNotFoundException;
import io.maxafrica.gpserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // TODO Auto-generated method stub

        User user = userRepository.findByEmailOrUsername(username, username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email : " + username)
                );
        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(String id) throws ResourceNotFoundException {

        User user = userRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new BadCredentialsException("Not authorized to access this resource")
        );
        return UserPrincipal.create(user);
    }

}
