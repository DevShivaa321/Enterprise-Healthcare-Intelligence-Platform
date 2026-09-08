package com.shivam.ehip.login;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.shivam.ehip.login.User;

@Repository
public class UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User getUserByLoginName(String loginName) {

        logger.info("Executing stored procedure: GetUserByLoginName for loginName={}", loginName);

        List<User> users = jdbcTemplate.query(
                "EXEC GetUserByLoginName ?",
                (rs, rowNum) -> {
                    User user = new User();
                    user.setUserId(rs.getInt("UserId"));
                    user.setLoginName(rs.getString("LoginName"));
                    user.setPassword(rs.getString("Password"));
                    user.setRole(rs.getString("Role"));
                    return user;
                },
                loginName
        );

        if (!users.isEmpty()) {
            logger.info("User found for loginName={}", loginName);
            return users.get(0);
        } else {
            logger.warn("No user found for loginName={}", loginName);
            return null;
        }
    }
}
