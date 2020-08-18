package wolox.test;

import com.github.tomakehurst.wiremock.client.WireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WiremockUtil {
    public static void createGetMock(String uri, String jsonBody, Integer responseCode) {
        stubFor(WireMock.get(urlEqualTo(uri))
                .willReturn(aResponse()
                        .withStatus(responseCode)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(jsonBody)));
    }
}
