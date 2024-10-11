package dev.sy.electroneon.user.dao;

import dev.sy.electroneon.user.User;
import dev.sy.electroneon.user.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Primary
public class UserJpaDataAccessService implements UserDao {

    private final UserRepository userRepository;

    public UserJpaDataAccessService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> selectAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> selectUserById(UUID userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> selectUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsUserById(UUID userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void insertUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(User update) {
        userRepository.save(update);
    }

    @Override
    public void deleteUserById(UUID userId) {
        userRepository.deleteById(userId);
    }
}
