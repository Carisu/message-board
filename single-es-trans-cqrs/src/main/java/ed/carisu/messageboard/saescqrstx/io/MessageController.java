package ed.carisu.messageboard.saescqrstx.io;

import ed.carisu.messageboard.saescqrstx.q.MessageCommand;
import ed.carisu.messageboard.saescqrstx.q.MessageDto;
import ed.carisu.messageboard.saescqrstx.q.MessageQueryService;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageQueryService messageQueryService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MessageDto> queryMessages() {
        return messageQueryService.query().get().asJava();
    }

    @PostMapping(path = "/{username}", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Void> postMessage(@PathVariable("username")String username, @RequestBody String messageBody) {
        validateUsername(username)
                .flatMap(u -> validateMessageBody(messageBody))
                .onSuccess(m -> applicationEventPublisher.publishEvent(new MessageCommand(username, messageBody, Instant.now())))
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
