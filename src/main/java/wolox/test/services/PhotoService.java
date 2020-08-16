package wolox.test.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import wolox.test.adapters.photos.client.PhotoClient;
import wolox.test.model.Album;
import wolox.test.model.Photo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhotoService {

    private PhotoClient client;
    private AlbumService albumService;

    @Autowired
    public PhotoService(PhotoClient client, AlbumService albumService){
        this.client = client;
        this.albumService = albumService;
    }

    public Mono<List<Photo>> getAllPhotos() {
        return client.getAllPhotos();
    }

    public Mono<Object> getPhotosByUserId(String userId) {
        Mono<List<Album>> albumsByUser = albumService.getAlbumsByUserId(userId);
        return albumsByUser.flatMap(albums ->{
            List<Mono<List<Photo>>> photosListMono = new ArrayList<>();
            albums.forEach(album -> photosListMono.add(client.getPhotosByAlbum(album.getId())));
            return Mono.zip(photosListMono, photosListArray-> Arrays.stream(photosListArray)
            .collect(Collectors.toList()));
        });
    }

}
