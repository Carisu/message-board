package ed.carisu.messageboard.satx.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface MessageBoardRepository extends JpaRepository<Message, UUID> {

    @Query(value = "SELECT * FROM MESSAGE_BOARD ORDER BY CREATED_TIMESTAMP DESC LIMIT ?1", nativeQuery = true)
    List<Message> findAllOrderByCreatedTimestampLimitedToDesc(int limit);
}
