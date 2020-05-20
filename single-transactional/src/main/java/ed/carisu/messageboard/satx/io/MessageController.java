package ed.carisu.messageboard.satx.io;

import ed.carisu.messageboard.satx.db.Message;
import ed.carisu.messageboard.satx.db.MessageBoardRepository;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public List<MessageDto> queryMessages() {
        return repository.findAllByCreatedTimestampDesc()
                .stream()
                .map(m -> new MessageDto(m.getUsername(), m.getMessageBody()))
                .collect(Collectors.toList());
    }

    @PostMapping("/{username}")
    public void postMessage(@PathParam("username")String username, @RequestBody String messageBody) {
        repository.saveAndFlush(new Message(username, messageBody));
    }
}
