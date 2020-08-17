package wolox.test.adapters;

import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public abstract class WebClientBaseGET<R, T> {

    private ClientHttpConnector clientHttpConnector;

    public WebClientBaseGET(ClientHttpConnector clientHttpConnector){
        this.clientHttpConnector = clientHttpConnector;
    }

    public Mono<R> getConsume(T data, String uri){
        WebClient client = WebClient.builder().clientConnector(clientHttpConnector)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024)).build();
        return client.get()
                .uri(uri)
                .exchange()
                .flatMap(response -> {
                    if(response.statusCode().is2xxSuccessful()) {
                        return mapResponse(response, data);
                    } else {
                        return createErrorResponse(data);
                    }
                });
    }

    protected abstract Mono<R> mapResponse(ClientResponse response, T data);

    protected abstract Mono<R> createErrorResponse(T data);

}
