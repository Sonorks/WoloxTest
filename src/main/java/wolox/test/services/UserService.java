package wolox.test.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import wolox.test.adapters.users.client.UserClient;
import wolox.test.jpa.albums.AlbumPrivilegesRepository;
import wolox.test.model.User;

import java.util.ArrayList;
import java.util.List;

import static wolox.test.utils.ValidatorUtil.validateUserOrAlbumId;

@Service
public class UserService {

    private static final String WRITE_PRIV = "write";
    private static final String READ_PRIV = "read";
    private final UserClient client;
    private final AlbumPrivilegesRepository repository;

    @Autowired
    public UserService(UserClient client, AlbumPrivilegesRepository repository){
        this.client = client;
        this.repository = repository;
    }

    public Mono<List<User>> getAllUsers() {
        return client.getAllUsers();
    }

    public Mono<List<Integer>> getUsersByAlbumAndPrivileges(String albumId, String privilege) {
        if(privilege.equals(WRITE_PRIV)){
            return Mono.just(repository.getUsersByAlbumIdAndWrite(albumId,true));
        } else if(privilege.equals(READ_PRIV)){
            return Mono.just(repository.getUsersByAlbumIdAndRead(albumId,true));
        } else {
            return Mono.just(new ArrayList<>());
        }
    }

    public boolean validateAlbumAndPrivilege(String albumId, String privilege) {
        return validateUserOrAlbumId(albumId)
                && (privilege.equals(WRITE_PRIV) || privilege.equals(READ_PRIV));
    }
}
