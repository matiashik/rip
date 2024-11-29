package com.rip.alt.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rip.alt.models.User;
import com.rip.alt.models.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    public final UserRepository repository;

    public User creatUser(User user) {
        return repository.save(user);
    }

    public User readUser(Long id) {
        return repository.findById(id).get();
    }

    public User updateUser(Long id, User user) {
        return repository.findById(id).map(u -> {
            u.setLogin(user.getLogin());
            u.setPassword(user.getPassword());
            return repository.save(u);
        }).orElseGet(() -> {
            return repository.save(user);
        });
    }

    public User deleteUser(Long id) {
        return repository.findById(id).map(user -> {
            repository.delete(user);
            return user;
        }).get();
    }

}
