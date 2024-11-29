package com.rip.alt.controllers.api;

import com.rip.alt.models.User;
import com.rip.alt.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
class UsersController {
    @Autowired
    private final UserService service;

    @GetMapping("/{id}")
    public User show(@PathVariable Long id) {
        return service.readUser(id);
    }

    @PostMapping("")
    public User create(@RequestBody User user) {
        return service.creatUser(user);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        return service.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public User delete(@PathVariable Long id) {
        return service.deleteUser(id);
    }


}
