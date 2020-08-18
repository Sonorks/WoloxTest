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
import wolox.test.model.User;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static wolox.test.WiremockUtil.createGetMock;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestApplication.class,
        initializers = ConfigFileApplicationContextInitializer.class)
@Profile("test")
@ActiveProfiles("test")
public class UserControllerTest {

    @Autowired
    UserController userController;

    @Autowired
    AlbumController albumController;

    @Autowired
    ObjectMapper objectMapper;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.options().port(8082));

    @Test
    public void testGetAllUsers() throws JsonProcessingException {
        String uri = "/users";
        User user = new User();
        user.setName("Test");
        List<User> userList = new ArrayList<>();
        userList.add(user);
        String jsonBody = objectMapper.writeValueAsString(userList);
        createGetMock(uri, jsonBody, 200);
        ResponseEntity<Mono<List<User>>> res = userController.getAllUsers();
        StepVerifier
                .create(res.getBody())
                .expectNextMatches(users -> users.size() == 1 && users.iterator().next().getName().equals("Test"))
                .verifyComplete();
    }

    @Test
    public void testGetUsersByAlbumAndPrivilegesWrite() {
        albumController.registerAlbumWithUserAndPrivileges(new AlbumController.AlbumPrivileges(1,1,true,true))
        .getBody().block();
        ResponseEntity<Mono<List<String>>> res = userController.getUsersByAlbumAndPrivileges("1","write");
        StepVerifier
                .create(res.getBody())
                .expectNextMatches(users -> users.size() == 1 && users.iterator().next().equals("1"))
                .verifyComplete();
    }

    @Test
    public void testGetUsersByAlbumAndPrivilegesRead() {
        albumController.registerAlbumWithUserAndPrivileges(new AlbumController.AlbumPrivileges(1,1,true,true))
                .getBody().block();
        ResponseEntity<Mono<List<String>>> res = userController.getUsersByAlbumAndPrivileges("1","read");
        StepVerifier
                .create(res.getBody())
                .expectNextMatches(users -> users.size() == 1 && users.iterator().next().equals("1"))
                .verifyComplete();
    }

    @Test
    public void testGetUsersByAlbumAndPrivilegesWithInvalidPriv() {
        albumController.registerAlbumWithUserAndPrivileges(new AlbumController.AlbumPrivileges(1,1,true,true))
                .getBody().block();
        ResponseEntity<Mono<List<String>>> res = userController.getUsersByAlbumAndPrivileges("1","invalid");
        assertTrue(res.getStatusCode().equals(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void testGetUsersByAlbumAndPrivilegesWhenThereIsNotUser() {
        ResponseEntity<Mono<List<String>>> res = userController.getUsersByAlbumAndPrivileges("10","write");
        StepVerifier
                .create(res.getBody())
                .expectNextMatches(users -> users.size() == 0)
                .verifyComplete();
    }
}
