package wolox.test.adapters.albums.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import wolox.test.adapters.WebClientBaseGET;
import wolox.test.model.Album;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class AlbumClient extends WebClientBaseGET<List<Album>,String> {

    @Value("${endpoints.album.all}")
    private String url;

    @Autowired
    public AlbumClient(ClientHttpConnector clientHttpConnector) {
        super(clientHttpConnector);
    }


    public Mono<List<Album>> getAllAlbums() {
        return this.getConsume("",url);
    }

    @Override
    protected Mono<List<Album>> mapResponse(ClientResponse response, String data) {
        return response.bodyToMono(Album[].class)
                .map(Arrays::asList);
    }

    @Override
    protected Mono<List<Album>> createErrorResponse(String data) {
        return Mono.just(new ArrayList<>());
    }

    public Mono<List<Album>> getAlbumsByUserId(String userId) {
        String uri = url.concat("?userId=").concat(userId);
        return this.getConsume(userId,uri);
    }
}
