package wolox.test.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import wolox.test.model.User;
import wolox.test.services.UserService;

import java.util.List;

@RestController
@RequestMapping("v1/users")
public class UserController {

    private UserService service;

    @Autowired
    public UserController(UserService service){
        this.service = service;
    }

    @GetMapping(path="")
    public Mono<List<User>> getAllUsers(){
        return this.service.getAllUsers();
    }

    @GetMapping("/{albumId}/{privilege}")
    public Mono<List<String>> getUsersByAlbumAndPrivileges(@PathVariable("albumId") String albumId, @PathVariable("privilege") String privilege){
        return this.service.getUsersByAlbumAndPrivileges(albumId, privilege);
    }


}
