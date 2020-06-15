package ed.carisu.messageboard.saescqrstx

import spock.lang.Specification

class GeneratorSpecification extends Specification {
    static generator = { String alphabet, int n ->
        new Random().with {
            n > 0 ? (1..n).collect { alphabet[nextInt(alphabet.length())] }.join() : ""
        }
    }

    static usernameGenerator(int len = new Random().nextInt(10)+1) {
        return generator.curry((('A'..'Z') + ('a'..'z')).join(), len)()
    }

    static messageBodyGenerator(int len = new Random().nextInt(1001)) {
        return generator.curry((' '..'~').join(), len)()
    }
}
