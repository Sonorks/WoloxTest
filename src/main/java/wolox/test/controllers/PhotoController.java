package wolox.test.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import wolox.test.model.Photo;
import wolox.test.services.PhotoService;

import java.util.List;

@RestController
@RequestMapping("v1/photos")
public class PhotoController {

    private PhotoService service;

    public PhotoController(PhotoService service){
        this.service = service;
    }

    @GetMapping("")
    public Mono<List<Photo>> getAllPhotos(){
        return this.service.getAllPhotos();
    }

    @GetMapping("/{userId}")
    public Mono<Object> getPhotosByUserId(@PathVariable("userId") String userId){
        return this.service.getPhotosByUserId(userId);
    }

}
