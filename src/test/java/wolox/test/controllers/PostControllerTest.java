package wolox.test.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.common.Json;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import wolox.test.TestApplication;
import wolox.test.model.Post;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static wolox.test.WiremockUtil.createGetMock;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestApplication.class,
        initializers = ConfigFileApplicationContextInitializer.class)
@Profile("test")
@ActiveProfiles("test")
public class PostControllerTest {

    @Autowired
    PostController postController;

    @Autowired
    ObjectMapper objectMapper;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.options().port(8082));

    @Test
    public void testGetPostsByUserId() throws JsonProcessingException {
        String uri = "/posts?userId=1";
        Post post = new Post();
        post.setTitle("Test");
        List<Post> postList = new ArrayList<>();
        postList.add(post);
        String jsonBody = objectMapper.writeValueAsString(postList);
        createGetMock(uri,jsonBody,200);
        ResponseEntity<Mono<List<Post>>> res = postController.getPostsByUserId("1");
        StepVerifier.create(res.getBody()).expectNextMatches(posts -> posts.size() == 1 && posts.iterator().next().getTitle().equals("Test")).verifyComplete();
    }

    @Test
    public void testGetPostByUserIdWhenIdIsInvalid() throws JsonProcessingException{
        ResponseEntity<Mono<List<Post>>> res = postController.getPostsByUserId("a");
        assertTrue(res.getStatusCode().equals(HttpStatus.BAD_REQUEST));
    }
}
