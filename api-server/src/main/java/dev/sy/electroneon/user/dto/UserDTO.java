package dev.sy.electroneon.user.dto;

import dev.sy.electroneon.user.Role;

import java.util.UUID;

public record UserDTO(
        UUID id,
        String email,
        String firstName,
        String lastName,
        Role role
) {
}
