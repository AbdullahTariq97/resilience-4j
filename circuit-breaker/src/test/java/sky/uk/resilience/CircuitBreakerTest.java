package sky.uk.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.mockito.Mockito.*;

public class CircuitBreakerTest {

    private Service service = mock(Service.class);

    @Test
    public void whenCircuitBreakerIsUsed_thenItWorksAsExpected() {

        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                // Percentage of failures to start short-circuit
                .failureRateThreshold(20)
                .minimumNumberOfCalls(5)
                // Min number of call attempts
                .build();

        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        CircuitBreaker circuitBreaker = registry.circuitBreaker("my");
        Function<Integer, Integer> decorated = CircuitBreaker.decorateFunction(circuitBreaker, service::process);

        when(service.process(anyInt())).thenThrow(new RuntimeException());

        for (int i = 0; i < 10; i++) {
            try {
                decorated.apply(i);
            } catch (Exception ignore) {
            }
        }

        verify(service, times(5)).process(any(Integer.class));
    }
}
