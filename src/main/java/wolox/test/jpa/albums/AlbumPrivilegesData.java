package wolox.test.jpa.albums;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name="ALBUM_PRIVILEGES", schema = "WOLOX")
@IdClass(AlbumPrivilegesId.class)
public class AlbumPrivilegesData {
    @Id
    private int albumId;
    @Id
    private int userId;
    private boolean readPriv;
    private boolean writePriv;
}

