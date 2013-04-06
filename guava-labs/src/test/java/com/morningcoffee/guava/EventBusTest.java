package com.morningcoffee.guava;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static java.lang.String.format;
import static junit.framework.Assert.*;

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
        List<Integer> countList = Collections.synchronizedList(new ArrayList<Integer>(N));


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

    @Test
    public void testConcurrentPost() throws Exception {

        final ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        int N = 1000;
        CountDownLatch latch = new CountDownLatch(N);
        ArrayList<Integer> counterList = new ArrayList<Integer>(N);
        bus.register(new ConcurrentListener(latch, counterList));

        for (int i = 0; i < N; i++) {
            final int finalI = i;
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    bus.post(new Event(format("event_%d", finalI), finalI));
                }
            });
        }

       if (!latch.await(10000, TimeUnit.MILLISECONDS)) {
           fail();
       }


        assertEquals(counterList.size(), N);
//
//        for (int i = 0; i < N; i++) {
//            assertTrue(counterList.get(i) == i);
//        }


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
        final List<Integer> eventCount;

        ConcurrentListener(CountDownLatch latch, List<Integer> eventCount) {
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
