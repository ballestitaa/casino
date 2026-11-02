package es.fempa;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.ArrayList;
import java.util.List;

@Named
@ApplicationScoped
public class UserService {
    private List<User> users = new ArrayList<>();

    @PostConstruct
    public void init() {
        users.add(new User("user", "pass","usuario de prueba", 100.0f,  "user@example.com", "user"));
    }

    public void register(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        return users;
    }
}
