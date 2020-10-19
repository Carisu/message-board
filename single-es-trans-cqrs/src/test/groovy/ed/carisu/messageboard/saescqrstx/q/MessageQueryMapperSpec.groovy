package ed.carisu.messageboard.saescqrstx.q

import ed.carisu.messageboard.saescqrstx.GeneratorSpecification
import ed.carisu.messageboard.saescqrstx.db.MessageBoardQuery
import ed.carisu.messageboard.saescqrstx.db.MessageBoardQueryRepository
import io.vavr.collection.List
import io.vavr.control.Option

class MessageQueryMapperSpec extends GeneratorSpecification {
    static randomQuery(int pos = 10) {
        new MessageBoardQuery().tap {
            q -> if (pos > 0) {
                (1..pos).each {
                    q.("username"+it) = usernameGenerator()
                    q.("messageBody"+it) = messageBodyGenerator()
                }
            }
        }
    }

    def "Check a list of #pos DTOs is correctly converted to a query"() {
        given: "the mapper"
        def mapper = new MessageQueryMapper()

        when: "get thw query"
        def actual = mapper.convertListToQuery(List.of(dtos as Message[]))

        then: "check is matches"
        actual.isSuccess()
        expected == actual.get()

        where:
        pos << (0..10).findAll()
        expected = randomQuery(pos)
        dtos = pos > 0 ? (1..pos).collect {
            new Message(expected.("username"+it), expected.("messageBody"+it))
        } : []
    }

    def "Check an invalid list of DTOs (>10) results in failure"() {
        given: "the mapper"
        def mapper = new MessageQueryMapper()

        expect: "conversion results in failure"
        mapper.convertListToQuery(List.of(dtos as Message[])).isFailure()

        where:
        count << [11, 50, 1000]
        dtos = (1..count).collect {
            new Message(usernameGenerator(), messageBodyGenerator())
        }
    }

    def "Check a query is converted correctly to a list of DTOs"() {
        given: "the mapper"
        def mapper = new MessageQueryMapper()

        when: "convert query"
        def dtos = mapper.convertQueryToList(query)

        then: "DTOs returned correctly"
        dtos.isSuccess()
        dtos.get().length() <= 10
        dtos.get().length() == count
        expected == dtos.get().asJava()

        where:
        count << (0..10).findAll()
        query = randomQuery(count)
        expected = count == 0 ? [] : (1..count).collect {
            new Message(query.("username"+it), query.("messageBody"+it))
        }
    }

}
