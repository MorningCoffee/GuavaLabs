package com.morningcoffee.guava;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static junit.framework.Assert.assertTrue;
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
        bus.register(new SequentialListener(null));
        bus.post(new Event("event", 1));
    }

    @Test
    public void testSequentialSetOfEvents() throws InterruptedException {
        int N = 100;
        ArrayList<Integer> countList = new ArrayList<Integer>(N);

        bus.register(new SequentialListener(countList));

        for (int i = 0; i < N; i++) {
            bus.post(new Event(format("event_%d", i), i));
        }

        assertTrue(countList.size() == N);

        for (int i = 0; i < N; i++) {
            assertTrue(countList.get(i) == i);
        }
    }

    @Test
    public void testConcurrentSetOfEvents() throws Exception {
        int N = 1000000;
        CountDownLatch latch = new CountDownLatch(N);
        ArrayList<Integer> countList = new ArrayList<Integer>(N);


        bus.register(new ConcurrentListener(latch, countList));

        for (int i = 0; i < N; i++) {
            bus.post(new Event(format("event_%d", i), i));
        }

        if (!latch.await(100, TimeUnit.MILLISECONDS)) {
            fail();
        }

        assertTrue(countList.size() == N);

        for (int i = 0; i < N; i++) {
            assertTrue(countList.get(i) == i);
        }

    }

    static class Event {
        final String name;
        final int num;

        Event(String name, int num) {
            this.name = name;
            this.num = num;
        }
    }

    static class ConcurrentListener {
        final CountDownLatch latch;
        final ArrayList<Integer> eventCount;

        ConcurrentListener(CountDownLatch latch, ArrayList<Integer> eventCount) {
            this.latch = latch;
            this.eventCount = eventCount;
        }

        @SuppressWarnings("unused")
        @AllowConcurrentEvents
        @Subscribe
        public void listen(Event e) {
            if (eventCount != null) {
                eventCount.add(e.num);
            }

            latch.countDown();
        }
    }

    static class SequentialListener {
        final ArrayList<Integer> eventCount;

        SequentialListener(ArrayList<Integer> eventCount) {
            this.eventCount = eventCount;
        }

        @SuppressWarnings("unused")
        @Subscribe
        public void listen(Event e) {

            if (eventCount != null) {
                eventCount.add(e.num);
            }
        }

    }
}
