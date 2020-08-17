package wolox.test.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import wolox.test.adapters.posts.client.PostClient;
import wolox.test.model.Post;

import java.util.List;

@Service
public class PostService {

    private PostClient client;

    @Autowired
    public PostService(PostClient client){
        this.client = client;
    }

    public Mono<List<Post>> getPostsByUserId(String userId){
        return client.getPostsByUser(userId);
    }

}
