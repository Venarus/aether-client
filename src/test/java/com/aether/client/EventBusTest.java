package com.aether.client;

import com.aether.client.events.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventBusTest {
    private EventBus eventBus;

    public static class TestEvent extends Event {
        public final String message;
        public TestEvent(String message) { this.message = message; }
    }

    public static class CancellableTestEvent extends CancellableEvent {
        public final int value;
        public CancellableTestEvent(int value) { this.value = value; }
    }

    @BeforeEach
    void setUp() {
        eventBus = new EventBus();
    }

    @Test
    void testBasicEventDispatch() {
        List<String> received = new ArrayList<>();
        Object listener = new Object() {
            @Subscribe
            public void onTest(TestEvent event) {
                received.add(event.message);
            }
        };

        eventBus.register(listener);
        eventBus.post(new TestEvent("Hello Aether"));
        assertEquals(1, received.size());
        assertEquals("Hello Aether", received.get(0));

        eventBus.unregister(listener);
        eventBus.post(new TestEvent("Second Message"));
        assertEquals(1, received.size(), "Unregistered listener should not receive events");
    }

    @Test
    void testPriorityOrdering() {
        List<String> order = new ArrayList<>();
        Object listener = new Object() {
            @Subscribe(priority = EventPriority.LOWEST)
            public void onLowest(TestEvent event) { order.add("LOWEST"); }

            @Subscribe(priority = EventPriority.HIGHEST)
            public void onHighest(TestEvent event) { order.add("HIGHEST"); }

            @Subscribe(priority = EventPriority.NORMAL)
            public void onNormal(TestEvent event) { order.add("NORMAL"); }
        };

        eventBus.register(listener);
        eventBus.post(new TestEvent("priority-test"));

        assertEquals(List.of("HIGHEST", "NORMAL", "LOWEST"), order);
    }

    @Test
    void testEventCancellation() {
        List<Integer> received = new ArrayList<>();
        Object listener = new Object() {
            @Subscribe(priority = EventPriority.HIGHEST)
            public void onCancel(CancellableTestEvent event) {
                event.cancel();
            }

            @Subscribe(priority = EventPriority.NORMAL)
            public void onNormal(CancellableTestEvent event) {
                received.add(event.value);
            }

            @Subscribe(priority = EventPriority.LOWEST, receiveCancelled = true)
            public void onReceiveCancelled(CancellableTestEvent event) {
                received.add(event.value * 2);
            }
        };

        eventBus.register(listener);
        eventBus.post(new CancellableTestEvent(42));

        assertEquals(1, received.size());
        assertEquals(84, received.get(0), "Only handler with receiveCancelled=true should run after cancel");
    }
}
