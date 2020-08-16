package wolox.test.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import wolox.test.adapters.users.client.UserClient;
import wolox.test.model.User;

import java.util.List;

@Service
public class UserService {

    private UserClient client;

    @Autowired
    public UserService(UserClient client){
        this.client = client;
    }

    public Mono<List<User>> getAllUsers() {
        return client.getAllUsers();
    }
}
