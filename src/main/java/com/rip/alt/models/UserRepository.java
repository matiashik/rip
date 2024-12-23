package com.rip.alt.models;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Finds a user by their login.
     *
     * @param login login of the user
     * @return an {@link Optional} of the user if found, otherwise an empty {@link Optional}
     */
    @Query("SELECT u FROM User u WHERE u.login = :login")
    public Optional<User> findByLogin(String login);

    /**
     * Finds a user by their login and password.
     *
     * @param login    login of the user
     * @param password password of the user
     * @return an {@link Optional} of the user if found, otherwise an empty {@link Optional}
     */
    @Query("SELECT u FROM User u WHERE u.login = :login AND u.password = :password")
    public Optional<User> findByLoginAndPassword(String login, String password);
}
