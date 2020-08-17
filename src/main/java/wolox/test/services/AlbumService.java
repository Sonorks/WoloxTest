package wolox.test.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import wolox.test.adapters.albums.client.AlbumClient;
import wolox.test.jpa.albums.AlbumPrivilegesData;
import wolox.test.jpa.albums.AlbumPrivilegesRepository;
import wolox.test.model.Album;

import java.util.List;

@Service
public class AlbumService {

    private final AlbumClient client;
    private final AlbumPrivilegesRepository repository;

    @Autowired
    public AlbumService(AlbumClient client, AlbumPrivilegesRepository repository) {
        this.client = client;
        this.repository = repository;
    }

    public Mono<List<Album>> getAllAlbums() {
        return client.getAllAlbums();
    }

    public Mono<List<Album>> getAlbumsByUserId(String userId) {
        return client.getAlbumsByUserId(userId);
    }

    public Mono<Boolean> saveAlbumWithUserAndPrivileges(int albumId, int userId, boolean read, boolean write) {
        AlbumPrivilegesData albumPrivilege = getAlbumPrivilegesData(albumId, userId, read, write);
        return Mono.fromSupplier(()->repository.save(albumPrivilege))
                .subscribeOn(Schedulers.elastic())
                .thenReturn(true);
    }

    private AlbumPrivilegesData getAlbumPrivilegesData(int albumId, int userId, boolean read, boolean write) {
        AlbumPrivilegesData albumPrivilege = new AlbumPrivilegesData();
        albumPrivilege.setAlbumId(albumId);
        albumPrivilege.setUserId(userId);
        albumPrivilege.setReadPriv(read);
        albumPrivilege.setWritePriv(write);
        return albumPrivilege;
    }

    public Mono<Boolean> updateAlbumWithUserAndPrivileges(int albumId, int userId, boolean read, boolean write) {
        if(repository.existsByAlbumIdAndUserId(albumId, userId)){
            return this.saveAlbumWithUserAndPrivileges(albumId,userId,read,write);
        }
        return Mono.just(false);
    }

    public boolean validateAlbumIdAndUserId(int albumId, int userId) {
        return albumId > 0 && userId > 0;
    }
}
