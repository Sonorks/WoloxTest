package wolox.test.adapters.users.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import reactor.core.publisher.Mono;
import wolox.test.adapters.WebClientBaseGET;
import wolox.test.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class UserClient extends WebClientBaseGET<List<User>, String> {

    Logger logger = LoggerFactory.getLogger(UserClient.class);

    @Value("${endpoints.user.all}")
    private String url;

    @Autowired
    public UserClient(ClientHttpConnector clientHttpConnector){
        super(clientHttpConnector);
    }

    public Mono<List<User>> getAllUsers() {
        return this.getConsume("", url);
    }

    @Override
    protected Mono<List<User>> mapResponse(ClientResponse response, String data) {
        return response.bodyToMono(User[].class)
                .map(Arrays::asList);
    }

    @Override
    protected Mono<List<User>> createErrorResponse(String data) {
        logger.error("Error consuming users service for id: "+data);
        return Mono.just(new ArrayList<>());
    }

}
