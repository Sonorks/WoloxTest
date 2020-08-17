package wolox.test.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/register")
    public Mono<Boolean> registerAlbumWithUserAndPrivileges(@RequestBody AlbumPrivileges albumPrivileges){
        return service.saveAlbumWithUserAndPrivileges(albumPrivileges.getAlbumId(), albumPrivileges.getUserId(), albumPrivileges.isRead(), albumPrivileges.isWrite());
    }

    @PutMapping("/update")
    public Mono<Boolean> registarAlbumWithUserAndPrivileges(@RequestBody AlbumPrivileges albumPrivileges){
        return service.updateAlbumWithUserAndPrivileges(albumPrivileges.getAlbumId(), albumPrivileges.getUserId(), albumPrivileges.isRead(), albumPrivileges.isWrite());
    }


    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    private static class AlbumPrivileges {
        private int albumId;
        private int userId;
        private boolean read;
        private boolean write;
    }
}
