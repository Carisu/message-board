package ed.carisu.messageboard.saescqrstx.db;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity(name = "MESSAGE_BOARD_QUERY")
@Data
@NoArgsConstructor
public class MessageBoardQuery {
    private static final UUID SINGLE_ID = UUID.randomUUID();

    public static UUID queryId() {
        return SINGLE_ID;
    }

    @Id
    UUID id = SINGLE_ID;
    private String username1;
    private String messageBody1;
    private String username2;
    private String messageBody2;
    private String username3;
    private String messageBody3;
    private String username4;
    private String messageBody4;
    private String username5;
    private String messageBody5;
    private String username6;
    private String messageBody6;
    private String username7;
    private String messageBody7;
    private String username8;
    private String messageBody8;
    private String username9;
    private String messageBody9;
    private String username10;
    private String messageBody10;

    public MessageBoardQuery setAt1Chain(String username, String messageBody) {
        username1 = username;
        messageBody1 = messageBody;
        return this;
    }

    public MessageBoardQuery setAt2Chain(String username, String messageBody) {
        username2 = username;
        messageBody2 = messageBody;
        return this;
    }

    public MessageBoardQuery setAt3Chain(String username, String messageBody) {
        username3 = username;
        messageBody3 = messageBody;
        return this;
    }

    public MessageBoardQuery setAt4Chain(String username, String messageBody) {
        username4 = username;
        messageBody4 = messageBody;
        return this;
    }

    public MessageBoardQuery setAt5Chain(String username, String messageBody) {
        username5 = username;
        messageBody5 = messageBody;
        return this;
    }

    public MessageBoardQuery setAt6Chain(String username, String messageBody) {
        username6 = username;
        messageBody6 = messageBody;
        return this;
    }

    public MessageBoardQuery setAt7Chain(String username, String messageBody) {
        username7 = username;
        messageBody7 = messageBody;
        return this;
    }

    public MessageBoardQuery setAt8Chain(String username, String messageBody) {
        username8 = username;
        messageBody8 = messageBody;
        return this;
    }

    public MessageBoardQuery setAt9Chain(String username, String messageBody) {
        username9 = username;
        messageBody9 = messageBody;
        return this;
    }

    public MessageBoardQuery setAt10Chain(String username, String messageBody) {
        username10 = username;
        messageBody10 = messageBody;
        return this;
    }
}
