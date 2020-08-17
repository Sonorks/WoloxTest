package wolox.test.adapters.posts.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import wolox.test.adapters.WebClientBaseGET;
import wolox.test.model.Post;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class PostClient extends WebClientBaseGET<List<Post>, String> {

    Logger logger = LoggerFactory.getLogger(PostClient.class);

    @Value("${endpoints.post.by-user}")
    private String url;

    @Autowired
    public PostClient(ClientHttpConnector clientHttpConnector) {
        super(clientHttpConnector);
    }

    public Mono<List<Post>> getPostsByUser(String userId){
        String uri = url.concat(userId);
        return this.getConsume(userId, uri);
    }

    @Override
    protected Mono<List<Post>> mapResponse(ClientResponse response, String data) {
        return response.bodyToMono(Post[].class)
                .map(Arrays::asList);
    }


    @Override
    protected Mono<List<Post>> createErrorResponse(String data) {
        logger.error("Error consuming posts service for data: " + data);
        return Mono.just(new ArrayList<>());
    }
}
