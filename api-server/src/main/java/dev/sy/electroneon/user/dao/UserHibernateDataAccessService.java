package dev.sy.electroneon.user.dao;

import dev.sy.electroneon.user.Role;
import dev.sy.electroneon.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserHibernateDataAccessService implements UserDao {

    @PersistenceContext
    private EntityManager em;

    public UserHibernateDataAccessService() {
    }

    @Override
    public List<User> selectAllUsers() {
        String jpql = "SELECT u from User u";

        return em.createQuery(jpql, User.class).getResultList();
    }

    @Override
    public Optional<User> selectUserById(UUID userId) {
        return Optional.ofNullable(em.find(User.class, userId));
    }

    @Override
    public Optional<User> selectUserByEmail(String email) {
        String jpql = "SELECT u FROM User u WHERE u.email = :email";

        return em
                .createQuery(jpql, User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    @Override
    public boolean existsUserById(UUID userId) {
        return selectUserById(userId).isPresent();
    }

    @Override
    public boolean existsUserByEmail(String email) {
        return selectUserByEmail(email).isPresent();
    }

    @Override
    @Transactional
    public void insertUser(User user) {
        em.persist(user);
    }

    @Override
    @Transactional
    public void updateUser(User update) {
        // Destructure the `update` object
        UUID userId = update.getId();
        String email = update.getEmail();
        String password = update.getPassword();
        String firstName = update.getFirstName();
        String lastName = update.getLastName();
        Role role = update.getRole();

        Optional<User> optionalUser = Optional.ofNullable(em.find(
                User.class,
                userId
        ));
        optionalUser.ifPresent(user -> {
            user.setEmail(email);
            user.setPassword(password);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setRole(role);
        });
    }

    @Override
    @Transactional
    public void deleteUserById(UUID userId) {
        Optional<User> optionalUser = Optional.ofNullable(em.find(
                User.class,
                userId
        ));
        optionalUser.ifPresent(user -> em.remove(user));
    }
}
