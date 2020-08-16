package wolox.test.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import wolox.test.model.Comment;
import wolox.test.services.CommentService;

import java.util.List;

@RestController
@RequestMapping("v1/comment")
public class CommentController {

    private CommentService service;

    public CommentController(CommentService service){
        this.service = service;
    }

    @GetMapping("/email/{userEmail}")
    public Mono<List<Comment>> getCommentsByUserEmail(@PathVariable("userEmail") String userEmail){
        return service.getCommentsByUserEmail(userEmail);
    }

    @GetMapping("/name/{name}")
    public Mono<List<Comment>> getCommentsByName(@PathVariable("name") String name){
        return service.getCommentsByName(name);
    }
}
