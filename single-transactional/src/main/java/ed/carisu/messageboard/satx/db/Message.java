package ed.carisu.messageboard.satx.db;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class Message {
    @Id @GeneratedValue @Getter
    private UUID id;
    private String username;
    private String messageBody;
    private Instant createdTimestamp = Instant.now();

    public Message(String username, String messageBody) {
        this.username = username;
        this.messageBody = messageBody;
    }
}
