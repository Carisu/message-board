package ed.carisu.messageboard.saescqrstx.q

import ed.carisu.messageboard.saescqrstx.GeneratorSpecification
import ed.carisu.messageboard.saescqrstx.db.MessageBoardEvent
import ed.carisu.messageboard.saescqrstx.db.MessageBoardEventRepository
import org.springframework.context.ApplicationEventPublisher

import java.time.Instant

class MessageCommandServiceSpec extends GeneratorSpecification {
    def "no error on valid command"() {
        given: "the service"
        def publisher = Stub(ApplicationEventPublisher)
        def repository = Stub(MessageBoardEventRepository)
        def service = new MessageCommandService(repository, publisher)

        expect: "Try.success on call"
        service.messageCommandReceived(command).isSuccess()

        where:
        goes << (1..10).findAll()
        u = usernameGenerator()
        m = messageBodyGenerator()
        command = new MessageCommand(u, m, Instant.now())
    }

    def "The event sent to the repository should be published to queue" () {
        given: "the service"
        def publisher = Mock(ApplicationEventPublisher)
        def repository = Mock(MessageBoardEventRepository)
        def service = new MessageCommandService(repository, publisher)

        when: "command recevied"
        service.messageCommandReceived(command)

        then: "the event is published"
        1 * repository.saveAndFlush(event1) >> event2
        1 * publisher.publishEvent(event2)

        where:
        goes << (1..10).findAll()
        u = usernameGenerator()
        m = messageBodyGenerator()
        now = Instant.now()
        uuid = UUID.randomUUID()
        command = new MessageCommand(u, m, now)
        event1 = [id: null, username: u, messageBody: m, createdTimestamp: now, seq: 0] as MessageBoardEvent
        event2 = [id: uuid, username: u, messageBody: m, createdTimestamp: now, seq: 1] as MessageBoardEvent
    }
}
