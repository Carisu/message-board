package ed.carisu.messageboard.sqestcqrs.q;

import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Queue;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class QueueProcessor<V> {
    private final Map<Thread, Queue<V>> queues = HashMap.empty();

    @EventListener
    public void addToQueue(V value) {

    }
}
