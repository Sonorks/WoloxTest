package wolox.test.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import wolox.test.model.Photo;
import wolox.test.services.PhotoService;

import java.util.List;

import static wolox.test.utils.ResponseUtil.getBadRequestResponse;
import static wolox.test.utils.ResponseUtil.getOkResponse;
import static wolox.test.utils.ValidatorUtil.validateUserOrAlbumId;

@RestController
@RequestMapping("v1/photos")
public class PhotoController {

    private PhotoService service;

    public PhotoController(PhotoService service){
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<Mono<List<Photo>>> getAllPhotos(){
        return getOkResponse(this.service.getAllPhotos());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Mono<Object>> getPhotosByUserId(@PathVariable("userId") String userId){
        if(validateUserOrAlbumId(userId)) {
            return getOkResponse(this.service.getPhotosByUserId(userId));
        }
        return getBadRequestResponse();
    }

}
