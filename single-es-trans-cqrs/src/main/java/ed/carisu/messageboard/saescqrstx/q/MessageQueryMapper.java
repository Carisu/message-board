package ed.carisu.messageboard.saescqrstx.q;

import ed.carisu.messageboard.saescqrstx.db.MessageBoardQuery;
import io.vavr.collection.List;
import io.vavr.collection.Stream;
import io.vavr.control.Option;
import io.vavr.control.Try;
import org.springframework.stereotype.Component;

import static io.vavr.API.*;

@Component
public class MessageQueryMapper {
    public Try<List<Message>> convertQueryToList(MessageBoardQuery query) {
        return Try.of(() -> Stream.rangeClosed(1, 10)
                .map(i -> getDtoForMessageQuery(query, i))
                .filter(Option::isDefined)
                .map(Option::get)
                .toList());
    }

    public Try<MessageBoardQuery> convertListToQuery(List<Message> list) {
        return Try.of(() -> Stream.rangeClosed(1, list.length())
                .foldLeft(Option.some(new MessageBoardQuery()),
                        (q, i) -> appendDtoToQuery(q, list.get(i-1), i))
                .get());
    }

    private Option<Message> getDtoForMessageQuery(MessageBoardQuery query, int index) {
        return Match(index).option(
                Case($(1), new Message(query.getUsername1(), query.getMessageBody1())),
                Case($(2), new Message(query.getUsername2(), query.getMessageBody2())),
                Case($(3), new Message(query.getUsername3(), query.getMessageBody3())),
                Case($(4), new Message(query.getUsername4(), query.getMessageBody4())),
                Case($(5), new Message(query.getUsername5(), query.getMessageBody5())),
                Case($(6), new Message(query.getUsername6(), query.getMessageBody6())),
                Case($(7), new Message(query.getUsername7(), query.getMessageBody7())),
                Case($(8), new Message(query.getUsername8(), query.getMessageBody8())),
                Case($(9), new Message(query.getUsername9(), query.getMessageBody9())),
                Case($(10), new Message(query.getUsername10(), query.getMessageBody10()))
        ).filter(m -> m.getUsername() != null && m.getMessageBody() != null);
    }

    private Option<MessageBoardQuery> appendDtoToQuery(Option<MessageBoardQuery> query, Message message, int index) {
        return Match(index).of(
                Case($(1), i -> query.map(q -> setMessageAt1(q, message))),
                Case($(2), i -> query.map(q -> setMessageAt2(q, message))),
                Case($(3), i -> query.map(q -> setMessageAt3(q, message))),
                Case($(4), i -> query.map(q -> setMessageAt4(q, message))),
                Case($(5), i -> query.map(q -> setMessageAt5(q, message))),
                Case($(6), i -> query.map(q -> setMessageAt6(q, message))),
                Case($(7), i -> query.map(q -> setMessageAt7(q, message))),
                Case($(8), i -> query.map(q -> setMessageAt8(q, message))),
                Case($(9), i -> query.map(q -> setMessageAt9(q, message))),
                Case($(10), i -> query.map(q -> setMessageAt10(q, message))),
                Case($(), i -> Option.none())
        );
    }

    private MessageBoardQuery setMessageAt1(MessageBoardQuery query, Message message) {
        query.setUsername1(message.getUsername());
        query.setMessageBody1(message.getMessageBody());
        return query;
    }

    private MessageBoardQuery setMessageAt2(MessageBoardQuery query, Message message) {
        query.setUsername2(message.getUsername());
        query.setMessageBody2(message.getMessageBody());
        return query;
    }

    private MessageBoardQuery setMessageAt3(MessageBoardQuery query, Message message) {
        query.setUsername3(message.getUsername());
        query.setMessageBody3(message.getMessageBody());
        return query;
    }

    private MessageBoardQuery setMessageAt4(MessageBoardQuery query, Message message) {
        query.setUsername4(message.getUsername());
        query.setMessageBody4(message.getMessageBody());
        return query;
    }

    private MessageBoardQuery setMessageAt5(MessageBoardQuery query, Message message) {
        query.setUsername5(message.getUsername());
        query.setMessageBody5(message.getMessageBody());
        return query;
    }

    private MessageBoardQuery setMessageAt6(MessageBoardQuery query, Message message) {
        query.setUsername6(message.getUsername());
        query.setMessageBody6(message.getMessageBody());
        return query;
    }

    private MessageBoardQuery setMessageAt7(MessageBoardQuery query, Message message) {
        query.setUsername7(message.getUsername());
        query.setMessageBody7(message.getMessageBody());
        return query;
    }

    private MessageBoardQuery setMessageAt8(MessageBoardQuery query, Message message) {
        query.setUsername8(message.getUsername());
        query.setMessageBody8(message.getMessageBody());
        return query;
    }

    private MessageBoardQuery setMessageAt9(MessageBoardQuery query, Message message) {
        query.setUsername9(message.getUsername());
        query.setMessageBody9(message.getMessageBody());
        return query;
    }

    private MessageBoardQuery setMessageAt10(MessageBoardQuery query, Message message) {
        query.setUsername10(message.getUsername());
        query.setMessageBody10(message.getMessageBody());
        return query;
    }
}
