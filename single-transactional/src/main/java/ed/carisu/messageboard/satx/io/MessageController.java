package ed.carisu.messageboard.satx.io;

import ed.carisu.messageboard.satx.db.Message;
import ed.carisu.messageboard.satx.db.MessageBoardRepository;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

@RestController("message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageBoardRepository repository;
    @Value("${ed.carisu.messageboard.messages.limit}")
    @Autowired
    private String limit;

    @GetMapping
    public List<MessageDto> queryMessages() {
        return repository.findAllOrderByCreatedTimestampLimitedToDesc(Integer.parseInt(limit))
                .stream()
                .map(m -> new MessageDto(m.getUsername(), m.getMessageBody()))
                .collect(Collectors.toList());
    }

    @PostMapping("/{username}")
    public void postMessage(@PathParam("username")String username, @RequestBody String messageBody) {
        validateUsername(username)
                .flatMap(u -> validateMessageBody(messageBody))
                .onSuccess(m -> repository.saveAndFlush(new Message(username, messageBody)))
                .get();
    }

    private Try<String> validateUsername(String username) {
        return username.length() > 0 && username.length() <= 10
                ? Try.success(username)
                : Try.failure(InvalidUsernameException.ofWrongLength());
    }

    private Try<String> validateMessageBody(String messageBody) {
        return messageBody.length() <= 1000
                ? Try.success(messageBody)
                : Try.failure(InvalidMessageBodyException.ofWrongLength());
    }
}
