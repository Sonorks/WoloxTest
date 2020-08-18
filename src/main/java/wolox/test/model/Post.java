package wolox.test.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Post {
    private int userId;
    private int id;
    private String title;
    private String body;
}
