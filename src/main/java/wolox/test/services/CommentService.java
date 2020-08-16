package wolox.test.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import wolox.test.adapters.comments.client.CommentClient;
import wolox.test.model.Comment;

import java.util.List;

@Service
public class CommentService {

    private CommentClient client;

    @Autowired
    public CommentService(CommentClient client) {
        this.client = client;
    }

    public Mono<List<Comment>> getCommentsByUserEmail(String userEmail) {
        return client.getCommentsByUserEmail(userEmail);
    }

    public Mono<List<Comment>> getCommentsByName(String name) {
        return client.getCommentsByName(name);
    }
}
