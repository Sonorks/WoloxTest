package wolox.test.jpa.albums;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AlbumPrivilegesRepository extends JpaRepository<AlbumPrivilegesData, Integer> {

    @Query(value = "SELECT USER_ID FROM ALBUM_PRIVILEGES WHERE ALBUM_ID=?1 AND WRITE_PRIV=?2", nativeQuery = true)
    List<Integer> getUsersByAlbumIdAndWrite(String albumId, boolean privilege);

    @Query(value = "SELECT USER_ID FROM ALBUM_PRIVILEGES WHERE ALBUM_ID=?1 AND READ_PRIV=?2", nativeQuery = true)
    List<Integer> getUsersByAlbumIdAndRead(String albumId, boolean privilege);

    boolean existsByAlbumIdAndUserId(int albumId, int userId);

}
