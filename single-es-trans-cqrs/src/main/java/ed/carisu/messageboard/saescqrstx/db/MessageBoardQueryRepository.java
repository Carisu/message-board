package ed.carisu.messageboard.saescqrstx.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageBoardQueryRepository extends JpaRepository<MessageBoardQuery, UUID> {
}
