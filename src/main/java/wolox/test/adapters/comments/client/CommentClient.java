package wolox.test.adapters.comments.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import wolox.test.adapters.WebClientBaseGET;
import wolox.test.model.Comment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CommentClient extends WebClientBaseGET<List<Comment>,String> {

    @Value("${endpoints.comment.all}")
    private String url;

    @Autowired
    private CommentClient(ClientHttpConnector clientHttpConnector){
        super(clientHttpConnector);
    }

    public Mono<List<Comment>> getCommentsByUserEmail(String userEmail) {
        String uri = url.concat("?email=").concat(userEmail);
        return this.getConsume(userEmail, uri);
    }

    @Override
    protected Mono<List<Comment>> mapResponse(ClientResponse response, String data) {
        return response.bodyToMono(Comment[].class)
                .map(Arrays::asList);
    }

    @Override
    protected Mono<List<Comment>> createErrorResponse(String data) {
        return Mono.just(new ArrayList<>());
    }

    public Mono<List<Comment>> getCommentsByName(String name) {
        String uri = url.concat("?name=").concat(name);
        return this.getConsume(name, uri);
    }
}
