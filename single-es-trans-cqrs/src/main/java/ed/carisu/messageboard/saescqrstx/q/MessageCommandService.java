package ed.carisu.messageboard.saescqrstx.q;

import ed.carisu.messageboard.saescqrstx.db.MessageBoardEvent;
import ed.carisu.messageboard.saescqrstx.db.MessageBoardEventRepository;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageCommandService {
    private final MessageBoardEventRepository repository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @EventListener
    public Try<Void> messageCommandReceived(MessageCommand command) {
        final MessageBoardEvent event = new MessageBoardEvent(command.getUsername(),
                command.getMessageBody(), command.getTimestamp());
        return Try.of(() -> recordEvent(event));
    }

    private Void recordEvent(MessageBoardEvent event) {
        applicationEventPublisher.publishEvent(
                repository.saveAndFlush(event));
        return null;
    }
}
