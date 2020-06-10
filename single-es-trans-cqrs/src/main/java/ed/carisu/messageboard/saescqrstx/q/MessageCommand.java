package ed.carisu.messageboard.saescqrstx.q;

import lombok.Value;

import java.time.Instant;

@Value
public class MessageCommand {
    String username;
    String messageBody;
    Instant timestamp;
}
