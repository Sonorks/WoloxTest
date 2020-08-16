package wolox.test.adapters.photos.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import wolox.test.adapters.WebClientBaseGET;
import wolox.test.model.Photo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class PhotoClient extends WebClientBaseGET<List<Photo>, String> {

    @Value("${endpoints.photo.all}")
    private String url;

    @Autowired
    public PhotoClient(ClientHttpConnector clientHttpConnector){
        super(clientHttpConnector);
    }

    public Mono<List<Photo>> getAllPhotos() {
        return this.getConsume("",url);
    }


    @Override
    protected Mono<List<Photo>> mapResponse(ClientResponse response, String data) {
        return response.bodyToMono(Photo[].class)
                .map(Arrays::asList);
    }

    @Override
    protected Mono<List<Photo>> createErrorResponse(String data) {
        return Mono.just(new ArrayList<>());
    }

    public Mono<List<Photo>> getPhotosByAlbum(int albumId) {
        String uri = url.concat("?albumId=").concat(String.valueOf(albumId));
        return this.getConsume("userId", uri);
    }
}
