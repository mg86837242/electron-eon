package dev.sy.electroneon.security;

import dev.sy.electroneon.user.User;
import dev.sy.electroneon.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserService implements UserDetailsService {

    private final UserRepository userRepository;

    public AuthUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Email " + username + " not found"
                ));

        return new AuthUser(user);
    }
}

// References:
// -- `UserDetailsService` is implemented in a separate class for the
// convenience of disabling it, o/w Spring Sec won't show the generated
// security pass, @see:
// https://stackoverflow.com/questions/64675502/spring-boot-starter-security-not-generating-default-password-in-logs
// https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#web.security