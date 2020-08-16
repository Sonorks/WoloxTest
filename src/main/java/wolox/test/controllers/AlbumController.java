package wolox.test.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import wolox.test.model.Album;
import wolox.test.services.AlbumService;

import java.util.List;

@RestController
@RequestMapping("v1/albums")
public class AlbumController {

    private AlbumService service;

    public AlbumController(AlbumService service){
        this.service=service;
    }

    @GetMapping("")
    public Mono<List<Album>> getAllAlbums(){
        return this.service.getAllAlbums();
    }

    @GetMapping("/{userId}")
    public Mono<List<Album>> getAlbumsByUserId(@PathVariable("userId") String userId){
        return this.service.getAlbumsByUserId(userId);
    }
}
