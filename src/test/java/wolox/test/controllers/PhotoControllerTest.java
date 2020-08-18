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
import wolox.test.model.Album;
import wolox.test.model.Photo;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static wolox.test.WiremockUtil.createGetMock;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestApplication.class,
        initializers = ConfigFileApplicationContextInitializer.class)
@Profile("test")
@ActiveProfiles("test")
public class PhotoControllerTest {

    @Autowired
    PhotoController photoController;

    @Autowired
    ObjectMapper objectMapper;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.options().port(8082));

    @Test
    public void testGetAllPhotos() throws JsonProcessingException {
        String uri = "/photos";
        Photo photo = new Photo();
        photo.setTitle("Test");
        List<Photo> photosList = new ArrayList<>();
        photosList.add(photo);
        String jsonBody = objectMapper.writeValueAsString(photosList);
        createGetMock(uri,jsonBody,200);
        ResponseEntity<Mono<List<Photo>>> res = photoController.getAllPhotos();
        StepVerifier
                .create(res.getBody())
                .expectNextMatches(photos -> photos.size()==1 && photos.iterator().next().getTitle().equals("Test"))
                .verifyComplete();
    }

    @Test
    public void testGetPhotosByUserId() throws JsonProcessingException {
        String uriAlbum = "/albums?userId=1";
        Album album = new Album();
        album.setId(1);
        List<Album> albumsList = new ArrayList<>();
        albumsList.add(album);
        String jsonBodyAlbum = objectMapper.writeValueAsString(albumsList);
        createGetMock(uriAlbum, jsonBodyAlbum, 200);
        String uri = "/photos?albumId=1";
        Photo photo = new Photo();
        photo.setTitle("Test");
        List<Photo> photosList = new ArrayList<>();
        photosList.add(photo);
        String jsonBody = objectMapper.writeValueAsString(photosList);
        createGetMock(uri,jsonBody,200);
        ResponseEntity<Mono<Object>> res = photoController.getPhotosByUserId("1");
        StepVerifier
                .create(res.getBody())
                .expectNextMatches(photos -> ((List<List<Photo>>) photos).iterator().next().size() == 1)
                .verifyComplete();
    }

    @Test
    public void testGetPhotosByUserIdWhenThereIsNoPhotos() throws JsonProcessingException {
        String uriAlbum = "/albums?userId=1";
        Album album = new Album();
        album.setId(1);
        List<Album> albumsList = new ArrayList<>();
        albumsList.add(album);
        String jsonBodyAlbum = objectMapper.writeValueAsString(albumsList);
        createGetMock(uriAlbum, jsonBodyAlbum, 200);
        String uri = "/photos?albumId=1";
        List<Photo> photosList = new ArrayList<>();
        String jsonBody = objectMapper.writeValueAsString(photosList);
        createGetMock(uri,jsonBody,200);
        ResponseEntity<Mono<Object>> res = photoController.getPhotosByUserId("1");
        StepVerifier
                .create(res.getBody())
                .expectNextMatches(photos -> ((List<List<Photo>>) photos).iterator().next().isEmpty())
                .verifyComplete();
    }

    @Test
    public void testGetPhotosByUserIdWhenIdIsInvalid(){
        ResponseEntity<Mono<Object>> res = photoController.getPhotosByUserId("0");
        assertTrue(res.getStatusCode().equals(HttpStatus.BAD_REQUEST));
    }
}
