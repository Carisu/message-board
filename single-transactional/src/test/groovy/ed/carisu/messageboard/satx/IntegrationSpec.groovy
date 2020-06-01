package ed.carisu.messageboard.satx

import ed.carisu.messageboard.satx.db.MessageBoardRepository
import ed.carisu.messageboard.satx.io.MessageDto
import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spock.lang.Specification

@SpringBootTest
@AutoConfigureMockMvc
class IntegrationSpec extends Specification {
    @Autowired
    MessageBoardRepository repository
    @Autowired
    MockMvc mvc


    def setup() {
        repository.deleteAll()
    }

    def post(username, messageBody) {
        mvc.perform(MockMvcRequestBuilders.post("/message/" + username)
                .content(messageBody)
                .contentType(MediaType.TEXT_PLAIN)
                .characterEncoding("UTF-8"))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
    }

    def query() {
        def results = mvc.perform(MockMvcRequestBuilders.get("/message"))
        .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
        .andReturn()
        .response
        .contentAsString
        new JsonSlurper().parseText(results)
    }

    def "Query has all POST commands in reverse order up to ten"() {
        given: "#number POST commands"
        def expected = []
        (1..number).each {
            post("user", "message: " + it)
            expected += ["user", "message: " + it] as MessageDto
        }
        expected = expected.reverse()
        if (expected.size() > 10) {
            expected = expected.subList(0, 10)
        }

        when: "get query"
        def actual = query()

        then: "we have the expected results"
        actual == expected
        actual.size() <= 10

        where:
        number << (0..20).findAll()
    }

    def "Two separate POST commands have different create dates"() {
        given: "Two POST commands"
        post("user1", "first message")
        post("user2", "second message")

        when: "Retrieve messages from database"
        def results = repository.findAll()
        def user1 = results[0].username == "user1" ? results[0] : results[1]
        def user2 = results[0].username == "user2" ? results[0] : results[1]

        then: "First message has earlier time than second"
        user1.createdTimestamp < user2.createdTimestamp
    }

    def "All queries between two POST commands have the same lists"() {
        given: "#commands initial of POST commands"
        (1..commands).each {
            post("user", "message: " + it)
        }

        when: "#queries queries are made, followed by another POST command"
        def results = []
        (1..queries).each {
            results += query()
        }
        post("user", "final message")

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

    def "Each query has the same elements either side of 1-9 POST commands"() {
        given: "#initial POST commands followed by a query followed by #internal commands followed  by another query"
        (1..initial).each {
            post("user", "initial: " + it)
        }
        def firstQuery = controller.queryMessages()
        (1..internal).each {
            post("user", "internal: "+ it)
        }
        def secondQuery = query()

        when: "Get first #(10-internal) messages from first query and last #(10-internal) from second query"
        def firstResults = initial+internal <= 10
                ? firstQuery.subList(0, initial)
                : firstQuery.subList(0, 10-internal)
        def secondResults = initial+internal <= 10
                ? secondQuery.subList(internal, initial+internal)
                : secondQuery.subList(internal, 10)

        then: "The results are the same"
        firstResults == secondResults

        where:
        [i1, i2] << [[5, 10, 20], (1..9).findAll()].combinations()
        initial = i1 as int
        internal = i2 as int
    }
}
