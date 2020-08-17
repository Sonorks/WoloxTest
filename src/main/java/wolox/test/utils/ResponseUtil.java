package wolox.test.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseUtil {
    public static <D> ResponseEntity<D> getOkResponse(D data){
        return new ResponseEntity<>(data, HttpStatus.OK);
    }

    public static ResponseEntity getBadRequestResponse(){
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
