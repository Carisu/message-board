package ed.carisu.messageboard.saescqrstx.q;

import ed.carisu.messageboard.saescqrstx.db.MessageBoardEvent;
import ed.carisu.messageboard.saescqrstx.db.MessageBoardQuery;
import ed.carisu.messageboard.saescqrstx.db.MessageBoardQueryRepository;
import io.vavr.collection.List;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MessageQueryService {
    private final MessageBoardQueryRepository repository;
    private final MessageQueryMapper mapper;

    public Try<List<Message>> query() {
        return  Try.of(() -> repository.getOne(MessageBoardQuery.queryId()))
                .flatMap(mapper::convertQueryToList);
    }

    @EventListener
    @Transactional
    public Try<Void> updateQuery(MessageBoardEvent event) {
        return query().map(l -> l.insert(0, new Message(event.getUsername(), event.getMessageBody()))
                        .take(10))
                .flatMap(mapper::convertListToQuery)
                .map(repository::saveAndFlush)
                .map(q -> null);
    }
}
