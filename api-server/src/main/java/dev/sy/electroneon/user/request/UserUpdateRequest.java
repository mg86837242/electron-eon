package dev.sy.electroneon.user.request;

import dev.sy.electroneon.user.Role;

public record UserUpdateRequest(
        String email,
        String password,
        String firstName,
        String lastName,
        Role role
) {
}
