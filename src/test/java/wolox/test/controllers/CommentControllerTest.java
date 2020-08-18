package wolox.test.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import wolox.test.model.Comment;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static wolox.test.WiremockUtil.createGetMock;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestApplication.class,
        initializers = ConfigFileApplicationContextInitializer.class)
@Profile("test")
@ActiveProfiles("test")
public class CommentControllerTest {

    @Autowired
    CommentController commentController;

    @Autowired
    ObjectMapper objectMapper;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.options().port(8082));

    @Test
    public void testGetCommentsByUserEmail() throws JsonProcessingException {
        String uri="/comments?email=julivas96@gmail.com";
        Comment comment = new Comment();
        comment.setBody("Test");
        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        String jsonBody = objectMapper.writeValueAsString(commentList);
        createGetMock(uri,jsonBody,200);
        ResponseEntity<Mono<List<Comment>>> res = commentController.getCommentsByUserEmail("julivas96@gmail.com");
        StepVerifier
                .create(res.getBody())
                .expectNextMatches(comments -> comments.size() == 1 && comments.iterator().next().getBody().equals("Test"))
        .verifyComplete();
    }

    @Test
    public void testGetCommentsByUserEmailWhenIsEmpty() throws JsonProcessingException {
        String uri="/comments?email=julivas96@gmail.com";
        List<Comment> commentList = new ArrayList<>();
        String jsonBody = objectMapper.writeValueAsString(commentList);
        createGetMock(uri,jsonBody,200);
        ResponseEntity<Mono<List<Comment>>> res = commentController.getCommentsByUserEmail("julivas96@gmail.com");
        StepVerifier
                .create(res.getBody())
                .expectNextMatches(comments -> comments.size() == 0)
                .verifyComplete();
    }

    @Test
    public void testGetCommentsByUserEmailWithInvalidEmail(){
        ResponseEntity<Mono<List<Comment>>> res = commentController.getCommentsByUserEmail("julivas96-gmail.com");
        assertTrue(res.getStatusCode().equals(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void testGetCommentsByName() throws JsonProcessingException {
        String uri="/comments?name=testname";
        Comment comment = new Comment();
        comment.setBody("Test");
        List<Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        String jsonBody = objectMapper.writeValueAsString(commentList);
        createGetMock(uri,jsonBody,200);
        ResponseEntity<Mono<List<Comment>>> res = commentController.getCommentsByName("testname");
        StepVerifier
                .create(res.getBody())
                .expectNextMatches(comments -> comments.size() == 1 && comments.iterator().next().getBody().equals("Test"))
                .verifyComplete();
    }

    @Test
    public void testGetCommentsByNameWhenItsEmpty() throws JsonProcessingException {
        String uri="/comments?name=testname";
        List<Comment> commentList = new ArrayList<>();
        String jsonBody = objectMapper.writeValueAsString(commentList);
        createGetMock(uri,jsonBody,200);
        ResponseEntity<Mono<List<Comment>>> res = commentController.getCommentsByName("testname");
        StepVerifier
                .create(res.getBody())
                .expectNextMatches(comments -> comments.size() == 0)
                .verifyComplete();
    }

    @Test
    public void testGetCommentsByNameWithInvalidName(){
        ResponseEntity<Mono<List<Comment>>> res = commentController.getCommentsByUserEmail("inv@lid");
        assertTrue(res.getStatusCode().equals(HttpStatus.BAD_REQUEST));
    }
}