package wolox.test.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import wolox.test.adapters.albums.client.AlbumClient;
import wolox.test.model.Album;

import java.util.List;

@Service
public class AlbumService {

    private AlbumClient client;

    @Autowired
    public AlbumService(AlbumClient client) {
        this.client = client;
    }

    public Mono<List<Album>> getAllAlbums() {
        return client.getAllAlbums();
    }

    public Mono<List<Album>> getAlbumsByUserId(String userId) {
        return client.getAlbumsByUserId(userId);
    }
}
