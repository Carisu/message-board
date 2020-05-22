package ed.carisu.messageboard.satx.io

import ed.carisu.messageboard.satx.db.Message
import ed.carisu.messageboard.satx.db.MessageBoardRepository
import spock.lang.Specification

class MessageControllerSpec extends Specification {
    static MESSAGE_LIST = [["user1":"Second message"] as Tuple,
                           ["user2":"A message from a new user"] as Tuple,
                           ["user1":"First message"] as Tuple]
    static RETURN_LIST = MESSAGE_LIST.forEach {t -> t as Message}
    static RESULT_LIST = MESSAGE_LIST.forEach { t -> t as MessageDto}

    def stubController() {
        def repository = Stub(MessageBoardRepository)
        new MessageController(repository)
    }

    def stubControllerWithResults() {
        def repository = Stub(MessageBoardRepository)
        repository.findAllOrderByCreatedTimestampLimitedToDesc(_ as int) >> RETURN_LIST
        new MessageController(repository)
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
        "01234567890"|10
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
        def messageBody = new char[length].each { _ -> ' '} as String

        when: "post message"
        controller.postMessage(username, messageBody)

        then: "no exception thrown"
        noExceptionThrown()

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
