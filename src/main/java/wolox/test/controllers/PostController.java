package wolox.test.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import wolox.test.model.Post;
import wolox.test.services.PostService;

import java.util.List;

import static wolox.test.utils.ResponseUtil.getBadRequestResponse;
import static wolox.test.utils.ResponseUtil.getOkResponse;
import static wolox.test.utils.ValidatorUtil.validateUserOrAlbumId;

@RestController
@RequestMapping("v1/post")
public class PostController {

    private PostService service;

    @Autowired
    public PostController(PostService service){
        this.service = service;
    }

    @GetMapping(path="/{userId}")
    public ResponseEntity<Mono<List<Post>>> getPostsByUserId(@PathVariable("userId") String userId){
        if(validateUserOrAlbumId(userId)) {
            return getOkResponse(this.service.getPostsByUserId(userId));
        }
        return getBadRequestResponse();
    }

}
