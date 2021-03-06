package ed.carisu.messageboard.satx.io;

import ed.carisu.messageboard.satx.db.MessageBoard;
import ed.carisu.messageboard.satx.db.MessageBoardRepository;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final MessageBoardRepository repository;
    @Value("${ed.carisu.messageboard.messages.limit}")
    @Autowired
    private String limit;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Message> queryMessages() {
        log.debug("query");
        return repository.findOrderByCreatedTimestampDesc(Integer.parseInt(limit))
                .stream()
                .map(m -> new Message(m.getUsername(), m.getMessageBody()))
                .collect(Collectors.toList());
    }

    @PostMapping(path = "/{username}", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Void> postMessage(@PathVariable("username")String username, @RequestBody String messageBody) {
        log.debug("post " + username);
        validateUsername(username)
                .flatMap(u -> validateMessageBody(messageBody))
                .onSuccess(m -> repository.saveAndFlush(new MessageBoard(username, messageBody)))
                .get();
        return ResponseEntity.noContent().build();
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
