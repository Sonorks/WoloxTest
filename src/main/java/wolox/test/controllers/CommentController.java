package wolox.test.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import wolox.test.model.Comment;
import wolox.test.services.CommentService;

import java.util.List;

import static wolox.test.utils.ResponseUtil.getBadRequestResponse;
import static wolox.test.utils.ResponseUtil.getOkResponse;
import static wolox.test.utils.ValidatorUtil.validateCommentName;
import static wolox.test.utils.ValidatorUtil.validateEmail;

@RestController
@RequestMapping("v1/comment")
public class CommentController {

    private CommentService service;

    public CommentController(CommentService service){
        this.service = service;
    }

    @GetMapping("/email/{userEmail}")
    public ResponseEntity<Mono<List<Comment>>> getCommentsByUserEmail(@PathVariable("userEmail") String userEmail){
        if(validateEmail(userEmail)) {
            return getOkResponse(service.getCommentsByUserEmail(userEmail));
        }
        return getBadRequestResponse();
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Mono<List<Comment>>> getCommentsByName(@PathVariable("name") String name){
        if(validateCommentName(name)) {
            return getOkResponse(service.getCommentsByName(name));
        }
        return getBadRequestResponse();
    }
}
