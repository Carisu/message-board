package ed.carisu.messageboard.satx

import ed.carisu.messageboard.satx.db.MessageBoardRepository
import ed.carisu.messageboard.satx.io.MessageController
import ed.carisu.messageboard.satx.io.MessageDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class IntegrationSpec extends Specification {
    @Autowired
    private MessageBoardRepository repository
    @Autowired
    private MessageController controller

    def setup() {
        repository.deleteAll()
    }

    def "Query has aal POST commands in reverse order up to ten"() {
        given: "{number} POST commands"
        def expected = []
        [1..number].each {
            controller.postMessage("user", "message: " + it)
            expected += ["user", "message: " + it] as MessageDto
        }
        expected = expected.reverse().subList(0, 9)

        when: "get query"
        def actual = controller.queryMessages()

        then: "we have the expected results"
        expected == actual
        actual.size() <= 10

        where:
        number << [0..20]
    }

    def "Two separate POST commands have different create dates"() {
        given: "Two POST commands"
        controller.postMessage("user1", "first message")
        controller.postMessage("user2", "second message")

        when: "Retrieve messages from database"
        def results = repository.findAll()
        def user1 = results[0].username == "user1" ? results[0] : results[1]
        def user2 = results[0].username == "user2" ? results[0] : results[1]

        then: "First message has earleir time than second"
        user1.createdTimestamp < user2.createdTimestamp
    }

    def "All queries between two POST commands have the same lists"() {
        given: "{commands} initial of POST commands"
        [1..commands].each {
            controller.postMessage("user", "message: " + it)
        }

        when: "{queries} queries are made, followed by another POST command"
        def results = []
        [1..queries].each {
            results += controller.queryMessages()
        }
        controller.postMessage("user", "final message")

        then: "All queries are the same"
        results.each {
            it == results.first()
        }

        where:
        commands | queries
        1        | 2
        10       | 2
        10       | 10
        50       | 20
    }

    def "Each query has the sae elements either side of 1-9 POST commands"() {
        given: "{initial} POST commands followed by a query followed by {internal} commands followed  by another query"
        [1..initial].each {
            controller.postMessage("user", "random: " + UUID.randomUUID())
        }
        def firstQuery = controller.queryMessages()
        [1..internal].each {
            controller.postMessage("user", "random: "+ UUID.randomUUID())
        }
        def secondQuery = controller.queryMessages()

        when: "Get first {10-internal} messages from first query and last {10-internal} from second query"
        def firstResults = firstQuery.subList(0, 10-internal)
        def secondResults = secondQuery.subList(10-internal as int, 10)

        then: "The results are the same"
        firstResults == secondResults

        where:
        initial << [5, 10, 20]
        internal << [1..9]
    }
}
