package ed.carisu.messageboard.saescqrstx.io

import ed.carisu.messageboard.saescqrstx.GeneratorSpecification
import ed.carisu.messageboard.saescqrstx.q.Message
import ed.carisu.messageboard.saescqrstx.q.MessageQueryService
import io.vavr.collection.List
import io.vavr.control.Try
import org.springframework.context.ApplicationEventPublisher

class MessageControllerSpec extends GeneratorSpecification {
    static final MESSAGE_LIST = [new Tuple("user1","Second message"),
                           new Tuple("user2","A message from a new user"),
                           new Tuple("user1","First message")]
    static final RETURN_LIST = MESSAGE_LIST.collect { it as Message } as Message[]
    static final RESULT_LIST = MESSAGE_LIST.collect { it as Message }

    def stubController() {
        def publisher = Stub(ApplicationEventPublisher)
        def service = Stub(MessageQueryService)
        new MessageController(service, publisher)
    }


    def "Check username allows 1-10 chars"() {
        given: "a controller"
        def controller = stubController()

        and: "a username of {length} length and empty message body"
        def messageBody = ""

        when: "post message"
        controller.postMessage(username, messageBody)

        then: "no exception thrown"
        noExceptionThrown()

        where:
        username | length
        "0"|1
        "01"|2
        "012"|3
        "0123"|4
        "01234"|5
        "012345"|6
        "0123456"|7
        "01234567"|8
        "012345678"|9
        "0123456789"|10
    }

    def "Check username does not allow 0 or >10 chars"() {
        given: "a controller"
        def controller = stubController()

        and: "a username of {length} length and empty message body"
        def messageBody = ""

        when: "post message"
        controller.postMessage(username, messageBody)

        then: "exception thrown"
        thrown(InvalidUsernameException)

        where:
        username | length
        "" | 0
        "01234567890" | 11
        "abcdefghijklmnopqrstuvxyz" | 26
    }

    def "Check message body allows 0 to 1000 chars"() {
        given: "a controller"
        def controller = stubController()

        and: "a message body of {length} characters and default username"
        def username = "user"
        def messageBody = messageBodyGenerator(length)

        when: "post message"
        controller.postMessage(username, messageBody)

        then: "no exception thrown"
        noExceptionThrown()
        messageBody.length() == length

        where:
        length | _
        0 | _
        500 | _
        1000 | _
    }

    def "Check message body doesn't allow >1000 chars" () {
        given: "a controller"
        def controller = stubController()

        and: "a message body of {length} characters and default username"
        def username = "user"
        def messageBody = messageBodyGenerator(length)

        when: "post message"
        controller.postMessage(username, messageBody)

        then: "no exception thrown"
        thrown InvalidMessageBodyException

        where:
        length | _
        1001 | _
        5000 | _
    }

    def "Check correct query response" () {
        given: "a controller"
        def publisher = Stub(ApplicationEventPublisher)
        def service = Stub(MessageQueryService) {
            query() >> Try.success(List.of(RETURN_LIST))
        }
        def controller = new MessageController(service, publisher)

        expect:
        controller.queryMessages() == RESULT_LIST
    }

    def "Check the command is published"() {
        given: "a controller"
        def publisher = Mock(ApplicationEventPublisher)
        def service = Mock(MessageQueryService)
        def controller = new MessageController(service, publisher)

        when: "send command to controller"
        controller.postMessage(u, m)

        then:
        1 * publisher.publishEvent({ it.username == u && it.messageBody == m })

        where:
        goes << (1..10).findAll()
        u = usernameGenerator()
        m = messageBodyGenerator()
    }
}
