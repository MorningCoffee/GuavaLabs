package com.morningcoffee.guava;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.fail;

/**
 * Exploring EventBus type
 * Date: 3/28/13
 *
 * @author osklyarenko
 */
public class EventBusTest {

    private EventBus bus = new EventBus("EventBus");

    @Test
    public void testSimpleEvent() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        bus.register(new Listener(latch));
        bus.post(new Event("event"));
        if (!latch.await(100, TimeUnit.MILLISECONDS)) {
            fail();
        }
    }

    static class Event {
        private final String name;

        Event(String name) {
            this.name = name;
        }
    }

    static class Listener {
        final CountDownLatch latch;

        Listener(CountDownLatch latch) {
            this.latch = latch;
        }
        @Subscribe
        public void listen(Event e) {

            latch.countDown();
        }

    }
}
