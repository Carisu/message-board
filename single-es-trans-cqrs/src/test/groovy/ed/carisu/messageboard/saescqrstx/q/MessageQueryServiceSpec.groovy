package ed.carisu.messageboard.saescqrstx.q

import ed.carisu.messageboard.saescqrstx.GeneratorSpecification
import ed.carisu.messageboard.saescqrstx.db.MessageBoardQuery
import ed.carisu.messageboard.saescqrstx.db.MessageBoardQueryRepository
import io.vavr.collection.List
import io.vavr.control.Option

class MessageQueryServiceSpec extends GeneratorSpecification {
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

    def stubbedService() {
        def repository = Stub(MessageBoardQueryRepository)
        new MessageQueryService(repository)
    }

    def "Check DTO is obtained correctly from query"() {
        given: "the service"
        def service = stubbedService()

        when: "the conversion"
        def converted = service.getDtoForMessageQuery(query, pos)

        then: "conversion worked and created correct DTO"
        converted.isDefined()
        dto == converted.get()

        where:
        pos << (1..10).findAll()
        query = randomQuery(pos)
        dto = new MessageDto(query.("username"+pos), query.("messageBody"+pos))
    }

    def "Check DTO is not returned when pos <1 or >10"() {
        given: "the service"
        def service = stubbedService()

        expect: "the returned DTO is none"
        !service.getDtoForMessageQuery(randomQuery(), pos).isDefined()

        where:
        pos | _
        -1000 | _
        0 | _
        11 | _
        1000 | _
    }

    def "Check DTO ia not returned when the query is null at that position"() {
        given: "the service"
        def service = stubbedService()

        expect: "the DTO for entry after first specified is none"
        !service.getDtoForMessageQuery(query, pos+1).isDefined()

        where:
        pos << (0..9).findAll()
        query = randomQuery(pos)
    }

    def "Check DTO not injected into new query"() {
        given: "the service"
        def service = stubbedService()

        expect: "the conversion"
        service.appendDtoToQuery(Option.none(), dto, pos).isEmpty()

        where:
        pos << (1..10).findAll()
        dto = new MessageDto(usernameGenerator(), messageBodyGenerator())
    }

    def "Check DTO #pos injected correctly into existing query"() {
        given: "the service"
        def service = stubbedService()

        when: "the conversion"
              def converted = service.appendDtoToQuery(Option.some(query), dto, pos)

        then: "dto injected"
        converted.isDefined()
        expected == (1..10).collect {
            new MessageDto(converted.get().("username"+it), converted.get().("messageBody"+it))
        }

        where:
        pos << (1..10).findAll()
        query = randomQuery()
        dto = new MessageDto(usernameGenerator(), messageBodyGenerator())
        expected = (1..10).collect {
            it == pos ? dto : new MessageDto(query.("username"+it), query.("messageBody"+it))
        }
    }

    def "Check query is none when attempting to inject DTO at pos <1 or >10"() {
        given: "the service"
        def service = stubbedService()

        expect: "the query is none"
        !service.appendDtoToQuery(Option.some(randomQuery()), new MessageDto(usernameGenerator(), messageBodyGenerator()), pos).isDefined()

        where:
        pos << [-1000, 0, 11, 1000]
    }

    def "Check a list of #pos DTOs is correctly converted to a query"() {
        given: "the service"
        def service = stubbedService()

        when: "get thw query"
        def actual = service.convertListToQuery(List.of(dtos as MessageDto[]))

        then: "check is matches"
        actual.isSuccess()
        expected == actual.get()

        where:
        pos << (0..10).findAll()
        expected = randomQuery(pos)
        dtos = pos > 0 ? (1..pos).collect {
            new MessageDto(expected.("username"+it), expected.("messageBody"+it))
        } : []
    }

    def "Check an invalid list of DTOs (>10) results in failure"() {
        given: "the service"
        def service = stubbedService()

        expect: "conversion results in failure"
        service.convertListToQuery(List.of(dtos as MessageDto[])).isFailure()

        where:
        count << [11, 50, 1000]
        dtos = (1..count).collect {
            new MessageDto(usernameGenerator(), messageBodyGenerator())
        }
    }
}
