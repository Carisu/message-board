package ed.carisu.messageboard.satx.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional
public interface MessageBoardRepository extends JpaRepository<Message, UUID> {
    List<Message> findAllByCreatedTimestampDesc();
}
