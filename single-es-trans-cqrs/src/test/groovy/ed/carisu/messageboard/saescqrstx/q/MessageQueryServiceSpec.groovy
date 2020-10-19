package ed.carisu.messageboard.saescqrstx.q

import ed.carisu.messageboard.saescqrstx.GeneratorSpecification
import ed.carisu.messageboard.saescqrstx.db.MessageBoardEvent
import ed.carisu.messageboard.saescqrstx.db.MessageBoardQuery
import ed.carisu.messageboard.saescqrstx.db.MessageBoardQueryRepository
import io.vavr.collection.List
import io.vavr.control.Try

import java.sql.SQLException
import java.time.Instant

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

    def "Check query returns correct data"() {
        given: "the service"
        def repository = Stub(MessageBoardQueryRepository) {
            getOne(_ as UUID) >> query
        }
        def mapper = Mock(MessageQueryMapper)
        def service = new MessageQueryService(repository, mapper)

        when: "query"
        def dtos = service.query()

        then: "correct dtos returned"
        dtos.isSuccess()
        expected == dtos.get().asJava()
        1 * mapper.convertQueryToList(query) >> Try.success(List.ofAll(expected))

        where:
        count << (0..10).findAll()
        query = randomQuery(count)
        expected = count == 0 ? [] : (1..count).collect {
            new Message(query.("username"+it), query.("messageBody"+it))
        }
    }

    def "Check repository throwing SQL expection results in failure"() {
        given: "the service"
        def repository = Stub(MessageBoardQueryRepository) {
            _(_) >> { throw new SQLException() }
        }
        def mapper = Mock(MessageQueryMapper) {
            0 * _
        }
        def service = new MessageQueryService(repository, mapper)

        expect: "query to be a failure"
        service.query().isFailure()
    }

    def "Check applying new events adds correctly to the existing query"() {
        given: "the service"
        def repository = Mock(MessageBoardQueryRepository)
        def mapper = Mock(MessageQueryMapper)
        def service = new MessageQueryService(repository, mapper)

        when: "update query with event"
        def test = service.updateQuery(event)

        then: "query updated, passing correct values to repository"
        test.isSuccess()
        1 * repository.getOne(_ as UUID) >> oldQuery
        1 * repository.saveAndFlush(newQuery) >> newQuery
        1 * mapper.convertQueryToList(oldQuery) >> Try.success(List.empty())
        1 * mapper.convertListToQuery(_) >> Try.success(newQuery)

        where:
        count << (0..10).findAll()
        oldQuery = randomQuery(count)
        event = new MessageBoardEvent(usernameGenerator(), messageBodyGenerator(), Instant.now())
        newQuery = new MessageBoardQuery().tap {
            q -> (0..count).take(10).each {
                q.("username"+(it+1)) = (it == 0 ? event.username : oldQuery.("username"+it))
                q.("messageBody"+(it+1)) = (it == 0 ? event.messageBody : oldQuery.("messageBody"+it))
            }
        }
    }

    def "Check applying event with SQL excpetion results in failure"() {
        given: "The service"
        def repository = Stub(MessageBoardQueryRepository) {
            _(_) >> { throw new SQLException() }
        }
        def mapper = Mock(MessageQueryMapper) {
            0 * _
        }
        def service = new MessageQueryService(repository, mapper)

        expect: "failure on exception"
        service.updateQuery(new MessageBoardEvent(usernameGenerator(), messageBodyGenerator(), Instant.now())).isFailure()
    }
}
