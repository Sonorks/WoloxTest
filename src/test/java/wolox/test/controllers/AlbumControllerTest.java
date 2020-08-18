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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import wolox.test.TestApplication;
import wolox.test.model.Album;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static wolox.test.WiremockUtil.createGetMock;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestApplication.class,
        initializers = ConfigFileApplicationContextInitializer.class)
@Profile("test")
@ActiveProfiles("test")
public class AlbumControllerTest {

    @Autowired
    AlbumController albumController;

    @Autowired
    ObjectMapper objectMapper;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.options().port(8082));

    @Test
    public void testGetAllAlbums() throws JsonProcessingException {
        String uri = "/albums";
        Album album = new Album();
        album.setTitle("Test");
        List<Album> albumsList = new ArrayList<>();
        albumsList.add(album);
        String jsonBody = objectMapper.writeValueAsString(albumsList);
        createGetMock(uri, jsonBody, 200);
        ResponseEntity<Mono<List<Album>>> res = albumController.getAllAlbums();
        Mono<List<Album>> bodyRes = res.getBody();
        StepVerifier
                .create(bodyRes)
                .expectNextMatches(albums -> albums.size() == 1 && albums.iterator().next().getTitle().equals("Test"))
                .verifyComplete();
    }

    @Test
    public void testGetAlbumsByUserId() throws JsonProcessingException{
        String uri = "/albums?userId=1";
        Album album = new Album();
        album.setTitle("Test1");
        List<Album> albumsList = new ArrayList<>();
        albumsList.add(album);
        String jsonBody = objectMapper.writeValueAsString(albumsList);
        createGetMock(uri, jsonBody, 200);
        ResponseEntity<Mono<List<Album>>> res = albumController.getAlbumsByUserId("1");
        Mono<List<Album>> bodyRes = res.getBody();
        StepVerifier
                .create(bodyRes)
                .expectNextMatches(albums -> albums.size() == 1 && albums.iterator().next().getTitle().equals("Test1"))
                .verifyComplete();
    }

    @Test
    public void testGetAlbumsByUserIdWhenUserIdIsZeroAndValidationFails() throws JsonProcessingException{
        ResponseEntity<Mono<List<Album>>> res = albumController.getAlbumsByUserId("0");
        assertTrue(res.getStatusCode().equals(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void testGetAlbumsByUserIdWhenUserIdIsNegativeAndValidationFails() throws JsonProcessingException{
        ResponseEntity<Mono<List<Album>>> res = albumController.getAlbumsByUserId("-1");
        assertTrue(res.getStatusCode().equals(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void testRegisterAlbumWithUserAndPrivileges(){
        ResponseEntity<Mono<Boolean>> res = albumController.registerAlbumWithUserAndPrivileges(new AlbumController.AlbumPrivileges(1,1,true,true));
        StepVerifier
                .create(res.getBody())
                .expectNextMatches(response -> response.equals(true))
                .verifyComplete();
    }

    @Test
    public void testRegisterAlbumWithUserAndPrivilegesWhenAlbumIdAndUserIdAreZero(){
        ResponseEntity<Mono<Boolean>> res = albumController.registerAlbumWithUserAndPrivileges(new AlbumController.AlbumPrivileges(0,0,true,true));
        assertTrue(res.getStatusCode().equals(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void testUpdateAlbumWithUserAndPrivilegesWhenAlbumDoesNotExists(){
        ResponseEntity<Mono<Boolean>> res = albumController.updateAlbumWithUserAndPrivileges(new AlbumController.AlbumPrivileges(2,1,false,true));
        StepVerifier
                .create(res.getBody())
                .expectNextMatches(response -> response.equals(false))
                .verifyComplete();
    }

    @Test
    public void testUpdateAlbumWithUserAndPrivileges(){
        albumController.registerAlbumWithUserAndPrivileges(new AlbumController.AlbumPrivileges(1,1,true,true));
        ResponseEntity<Mono<Boolean>> res = albumController.updateAlbumWithUserAndPrivileges(new AlbumController.AlbumPrivileges(1,1,false,true));
        StepVerifier
                .create(res.getBody())
                .expectNextMatches(response -> response.equals(false))
                .verifyComplete();
    }

    @Test
    public void testUpdateAlbumWithUserAndPrivilegesWhenAlbumIdAndUserIdAreZero(){
        albumController.registerAlbumWithUserAndPrivileges(new AlbumController.AlbumPrivileges(1,1,true,true));
        ResponseEntity<Mono<Boolean>> res = albumController.updateAlbumWithUserAndPrivileges(new AlbumController.AlbumPrivileges(0,0,false,true));
        assertTrue(res.getStatusCode().equals(HttpStatus.BAD_REQUEST));
    }

}
