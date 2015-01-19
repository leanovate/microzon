package de.leanovate.dose.billing.connector;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class ServiceFailover {
    private static final Random RANDOM = new Random(System.currentTimeMillis());

    public static <E, T> T retry(final List<E> endpoints, final Requester<E, T> requester) throws IOException {

        final int size = endpoints.size();

        if (size == 0) {
            throw new RuntimeException("No active endpoints found");
        }

        final int offset = RANDOM.nextInt(size);
        IOException lastException = null;

        for (int idx = 0; idx < size; idx++) {
            final E endpoint = endpoints.get((idx + offset) % size);

            try {
                return requester.perform(endpoint);
            } catch (IOException e) {
                lastException = e;
            }
        }
        throw lastException;
    }

    @FunctionalInterface
    public interface Requester<E, T> {
        T perform(E endpoing) throws IOException;
    }
}
