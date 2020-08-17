package wolox.test.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import wolox.test.model.Album;
import wolox.test.services.AlbumService;

import java.util.List;

import static wolox.test.utils.ResponseUtil.getBadRequestResponse;
import static wolox.test.utils.ResponseUtil.getOkResponse;
import static wolox.test.utils.ValidatorUtil.validateUserOrAlbumId;

@RestController
@RequestMapping("v1/albums")
public class AlbumController {

    private AlbumService service;

    public AlbumController(AlbumService service){
        this.service=service;
    }

    @GetMapping("")
    public ResponseEntity<Mono<List<Album>>> getAllAlbums(){
        return getOkResponse(this.service.getAllAlbums());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Mono<List<Album>>> getAlbumsByUserId(@PathVariable("userId") String userId){
        if(validateUserOrAlbumId(userId)) {
            return getOkResponse(this.service.getAlbumsByUserId(userId));
        }
        return getBadRequestResponse();
    }

    @PostMapping("/register")
    public ResponseEntity<Mono<Boolean>> registerAlbumWithUserAndPrivileges(@RequestBody AlbumPrivileges albumPrivileges){
        if(service.validateAlbumIdAndUserId(albumPrivileges.getAlbumId(), albumPrivileges.getUserId())) {
            return getOkResponse(service.saveAlbumWithUserAndPrivileges(albumPrivileges.getAlbumId(), albumPrivileges.getUserId(), albumPrivileges.isRead(), albumPrivileges.isWrite()));
        }
        return getBadRequestResponse();
    }

    @PutMapping("/update")
    public ResponseEntity<Mono<Boolean>> registarAlbumWithUserAndPrivileges(@RequestBody AlbumPrivileges albumPrivileges){
        if(service.validateAlbumIdAndUserId(albumPrivileges.getAlbumId(), albumPrivileges.getUserId())) {
            return getOkResponse(service.updateAlbumWithUserAndPrivileges(albumPrivileges.getAlbumId(), albumPrivileges.getUserId(), albumPrivileges.isRead(), albumPrivileges.isWrite()));
        }
        return getBadRequestResponse();
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
