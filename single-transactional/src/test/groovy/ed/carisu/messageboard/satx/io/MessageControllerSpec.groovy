package ed.carisu.messageboard.satx.io

import ed.carisu.messageboard.satx.db.Message
import ed.carisu.messageboard.satx.db.MessageBoardRepository
import spock.lang.Specification

class MessageControllerSpec extends Specification {
    static final MESSAGE_LIST = [new Tuple("user1","Second message"),
                           new Tuple("user2","A message from a new user"),
                           new Tuple("user1","First message")]
    static final RETURN_LIST = MESSAGE_LIST.collect { it as Message }
    static final RESULT_LIST = MESSAGE_LIST.collect { it as MessageDto }

    def stubController() {
        def repository = Stub(MessageBoardRepository)
        new MessageController(repository)
    }

    def stubControllerWithResults() {
        def repository = Stub(MessageBoardRepository) {
            findOrderByCreatedTimestampDesc(_) >> RETURN_LIST
        }
        def controller = new MessageController(repository)
        controller.limit = "10"
        controller
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
        def messageBody = (new char[length].collect {' '} as char[]) as String

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
        def messageBody = new char[length].each { _ -> ' '} as String

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
        def controller = stubControllerWithResults()

        expect:
        controller.queryMessages() == RESULT_LIST
    }
}
