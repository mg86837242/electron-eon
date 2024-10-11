package dev.sy.electroneon.user.dao;

import dev.sy.electroneon.user.Role;
import dev.sy.electroneon.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserDaoTestUtil {

    private static final Logger LOG = LoggerFactory.getLogger(UserDaoTestUtil.class);
    private final UserDao ud;
    // NB Normally password would be hashed in the service layer, not in the
    // data access layer; this is just for testing purposes
    private final PasswordEncoder passwordEncoder;

    public UserDaoTestUtil(
            UserDao ud,
            PasswordEncoder passwordEncoder
    ) {
        this.ud = ud;
        this.passwordEncoder = passwordEncoder;
    }

    public void selectAllUsers() {
        List<User> users = ud.selectAllUsers();
        LOG.info(">> selectAllUsers() result:");
        if (users.isEmpty()) {
            LOG.info("None");
        } else {
            users.forEach(user -> LOG.info(String.valueOf(user)));
        }
    }

    public void selectUserById() {
        Optional<User> optionalUser = ud.selectUserById(
                UUID.fromString(
                        "1de64dad-aae0-4004-b4bc-f28c1f46589a"
                )
        );
        LOG.info(">> selectUserById() result:");
        LOG.info(String.valueOf(optionalUser));
    }

    public void selectUserByEmail() {
        Optional<User> optionalUser = ud.selectUserByEmail(
                "john@server.com"
        );
        LOG.info(">> selectUserByEmail() result:");
        LOG.info(String.valueOf(optionalUser));
    }

    public void insertUser() {
        User user1 = new User(
                UUID.fromString(
                        "1de64dad-aae0-4004-b4bc-f28c1f46589a"
                ),
                "john@server.com",
                passwordEncoder.encode("abcd1234"),
                "john",
                "doe",
                Role.ADMIN
        );
        User user2 = new User(
                UUID.fromString(
                        "1c6a97c1-da90-464a-95be-700549add2ea"
                ),
                "jane@server.com",
                passwordEncoder.encode("abcd1234"),
                "jane",
                "doe",
                Role.CUSTOMER
        );

        ud.insertUser(user1);
        ud.insertUser(user2);
    }

    public void updateUser() {
        User update = new User(
                UUID.fromString(
                        "1c6a97c1-da90-464a-95be-700549add2ea"
                ),
                "july@server.com",
                passwordEncoder.encode("abcd1234"),
                "july",
                "doe",
                Role.CUSTOMER
        );

        ud.updateUser(update);
    }

    public void deleteUserById() {
        ud.deleteUserById(
                UUID.fromString(
                        "1c6a97c1-da90-464a-95be-700549add2ea"
                )
        );
    }
}
