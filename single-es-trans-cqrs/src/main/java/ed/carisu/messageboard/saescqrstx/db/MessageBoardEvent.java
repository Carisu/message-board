package ed.carisu.messageboard.saescqrstx.db;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.time.Instant;
import java.util.UUID;

@Entity(name = "MESSAGE_BOARD_EVENT")
@Data
@NoArgsConstructor
public class MessageBoardEvent {
    @Id @GeneratedValue @Getter
    private UUID id;
    private String username;
    private String messageBody;
    private Instant createdTimestamp;
    @SequenceGenerator(name = "EVENT_ORDER") @Getter
    private int seq;

    public MessageBoardEvent(String username, String messageBody, Instant createdTimestamp) {
        this.username = username;
        this.messageBody = messageBody;
        this.createdTimestamp = createdTimestamp;
    }
}
