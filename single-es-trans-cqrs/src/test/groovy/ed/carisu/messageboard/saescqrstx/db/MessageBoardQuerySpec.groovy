package ed.carisu.messageboard.saescqrstx.db

import ed.carisu.messageboard.saescqrstx.GeneratorSpecification

class MessageBoardQuerySpec extends GeneratorSpecification {
    def "set at will set username and message body at that position"() {
        given: "The query"
        def query = new MessageBoardQuery()

        when: "set username and message bosy at position"
        query.("setAt"+pos+"Chain")(username, messageBody)

        then: "same username and message body"
        query.("username"+pos) == username
        query.("messageBody"+pos) == messageBody
        expected == (1..10).collect {
            new Tuple2(query.("username"+it), query.("messageBody"+it))
        }

        where:
        pos << (1..10).findAll()
        username = usernameGenerator()
        messageBody= messageBodyGenerator()
        expected = (1..10).collect {
            it == pos ? new Tuple2(username, messageBody) : new Tuple2(null, null)
        }
    }
}
