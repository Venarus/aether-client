package com.aether.client.events;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * High-performance, thread-safe EventBus for Aether Client.
 * Listeners register methods annotated with @Subscribe.
 */
public class EventBus {
    private static final class ListenerInvocation implements Comparable<ListenerInvocation> {
        final Object target;
        final Method method;
        final EventPriority priority;
        final boolean receiveCancelled;

        ListenerInvocation(Object target, Method method, EventPriority priority, boolean receiveCancelled) {
            this.target = target;
            this.method = method;
            this.priority = priority;
            this.receiveCancelled = receiveCancelled;
            this.method.setAccessible(true);
        }

        void invoke(Event event) {
            try {
                if (event instanceof CancellableEvent && ((CancellableEvent) event).isCancelled() && !receiveCancelled) {
                    return;
                }
                method.invoke(target, event);
            } catch (Throwable t) {
                System.err.printf("[Aether EventBus] Error invoking %s#%s: %s%n",
                        target.getClass().getSimpleName(), method.getName(), t.getMessage());
            }
        }

        @Override
        public int compareTo(ListenerInvocation o) {
            // Lower ordinal means higher priority
            return Integer.compare(this.priority.ordinal(), o.priority.ordinal());
        }
    }

    private final Map<Class<? extends Event>, List<ListenerInvocation>> subscriberMap = new ConcurrentHashMap<>();
    private final Set<Object> registeredListeners = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public void register(Object listener) {
        if (listener == null || !registeredListeners.add(listener)) {
            return;
        }

        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Subscribe.class) || Modifier.isStatic(method.getModifiers())) {
                continue;
            }

            Class<?>[] params = method.getParameterTypes();
            if (params.length != 1 || !Event.class.isAssignableFrom(params[0])) {
                System.err.printf("[Aether EventBus] Ignored invalid subscriber method %s in %s%n",
                        method.getName(), listener.getClass().getName());
                continue;
            }

            @SuppressWarnings("unchecked")
            Class<? extends Event> eventType = (Class<? extends Event>) params[0];
            Subscribe sub = method.getAnnotation(Subscribe.class);
            ListenerInvocation invocation = new ListenerInvocation(listener, method, sub.priority(), sub.receiveCancelled());

            subscriberMap.compute(eventType, (key, list) -> {
                List<ListenerInvocation> updated = (list == null) ? new CopyOnWriteArrayList<>() : list;
                updated.add(invocation);
                Collections.sort(updated);
                return updated;
            });
        }
    }

    public void unregister(Object listener) {
        if (listener == null || !registeredListeners.remove(listener)) {
            return;
        }

        for (List<ListenerInvocation> invocations : subscriberMap.values()) {
            invocations.removeIf(inv -> inv.target == listener);
        }
    }

    public <T extends Event> T post(T event) {
        if (event == null) return null;

        List<ListenerInvocation> direct = subscriberMap.get(event.getClass());
        if (direct != null && !direct.isEmpty()) {
            for (ListenerInvocation inv : direct) {
                inv.invoke(event);
            }
        }

        // Also notify superclass handlers if any
        for (Map.Entry<Class<? extends Event>, List<ListenerInvocation>> entry : subscriberMap.entrySet()) {
            if (entry.getKey() != event.getClass() && entry.getKey().isAssignableFrom(event.getClass())) {
                for (ListenerInvocation inv : entry.getValue()) {
                    inv.invoke(event);
                }
            }
        }

        return event;
    }

    public boolean isRegistered(Object listener) {
        return registeredListeners.contains(listener);
    }

    public void clear() {
        subscriberMap.clear();
        registeredListeners.clear();
    }
}
