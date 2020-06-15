package ed.carisu.messageboard.saescqrstx.q;

import ed.carisu.messageboard.saescqrstx.db.MessageBoardEvent;
import ed.carisu.messageboard.saescqrstx.db.MessageBoardQuery;
import ed.carisu.messageboard.saescqrstx.db.MessageBoardQueryRepository;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import static io.vavr.API.*;

@Service
@RequiredArgsConstructor
public class MessageQueryService {
    private final MessageBoardQueryRepository repository;

    public Try<List<MessageDto>> convertQueryToList(MessageBoardQuery query) {
        return Try.of(() -> Stream.rangeClosed(1, 10)
                .map(i -> getDtoForMessageQuery(query, i))
                .filter(Option::isDefined)
                .map(Option::get)
                .toList());
    }

    public Try<MessageBoardQuery> convertListToQuery(List<MessageDto> list) {
        return Try.of(() -> Stream.rangeClosed(1, list.length())
                .foldLeft(Option.some(new MessageBoardQuery()),
                        (q, i) -> appendDtoToQuery(q, list.get(i-1), i))
                .get());
    }

    public Option<MessageDto> getDtoForMessageQuery(MessageBoardQuery query, int index) {
        return Match(index).option(
                Case($(1), new MessageDto(query.getUsername1(), query.getMessageBody1())),
                Case($(2), new MessageDto(query.getUsername2(), query.getMessageBody2())),
                Case($(3), new MessageDto(query.getUsername3(), query.getMessageBody3())),
                Case($(4), new MessageDto(query.getUsername4(), query.getMessageBody4())),
                Case($(5), new MessageDto(query.getUsername5(), query.getMessageBody5())),
                Case($(6), new MessageDto(query.getUsername6(), query.getMessageBody6())),
                Case($(7), new MessageDto(query.getUsername7(), query.getMessageBody7())),
                Case($(8), new MessageDto(query.getUsername8(), query.getMessageBody8())),
                Case($(9), new MessageDto(query.getUsername9(), query.getMessageBody9())),
                Case($(10), new MessageDto(query.getUsername10(), query.getMessageBody10()))
        ).filter(m -> m.getUsername() != null && m.getMessageBody() != null);
    }

    public Option<MessageBoardQuery> appendDtoToQuery(Option<MessageBoardQuery> query, MessageDto dto, int index) {
        return Match(index).of(
                Case($(1), i -> query.map(q -> q.setAt1Chain(dto.getUsername(), dto.getMessageBody()))),
                Case($(2), i -> query.map(q -> q.setAt2Chain(dto.getUsername(), dto.getMessageBody()))),
                Case($(3), i -> query.map(q -> q.setAt3Chain(dto.getUsername(), dto.getMessageBody()))),
                Case($(4), i -> query.map(q -> q.setAt4Chain(dto.getUsername(), dto.getMessageBody()))),
                Case($(5), i -> query.map(q -> q.setAt5Chain(dto.getUsername(), dto.getMessageBody()))),
                Case($(6), i -> query.map(q -> q.setAt6Chain(dto.getUsername(), dto.getMessageBody()))),
                Case($(7), i -> query.map(q -> q.setAt7Chain(dto.getUsername(), dto.getMessageBody()))),
                Case($(8), i -> query.map(q -> q.setAt8Chain(dto.getUsername(), dto.getMessageBody()))),
                Case($(9), i -> query.map(q -> q.setAt9Chain(dto.getUsername(), dto.getMessageBody()))),
                Case($(10), i -> query.map(q -> q.setAt10Chain(dto.getUsername(), dto.getMessageBody()))),
                Case($(), i -> Option.none())
        );
    }

    public Try<List<MessageDto>> query() {
        return  Try.of(() -> repository.getOne(MessageBoardQuery.queryId()))
                .flatMap(this::convertQueryToList);
    }

    @EventListener
    @Transactional
    public Try<Void> updateQuery(MessageBoardEvent event) {
        return query().map(l -> l.append(new MessageDto(event.getUsername(), event.getMessageBody())))
                .map(l -> l.takeRight(10))
                .flatMap(this::convertListToQuery)
                .map(repository::saveAndFlush)
                .map(q -> null);
    }
}
