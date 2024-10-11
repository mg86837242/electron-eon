package dev.sy.electroneon.user;

import dev.sy.electroneon.user.dto.UserDTO;
import dev.sy.electroneon.user.request.UserRegistrationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(
            @RequestBody UserRegistrationRequest request
    ) {
        UserDTO registeredUser = userService.registerUser(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path(
                "/{id}").buildAndExpand(registeredUser.id()).toUri();

        return ResponseEntity.created(location).body(registeredUser);
    }
}
