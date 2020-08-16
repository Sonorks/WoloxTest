package wolox.test.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Comment {
    private int postId;
    private int id;
    private String name;
    private String email;
    private String body;
}
