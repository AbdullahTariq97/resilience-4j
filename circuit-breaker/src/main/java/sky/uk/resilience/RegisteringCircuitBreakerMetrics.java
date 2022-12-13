package sky.uk.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.micrometer.tagged.TaggedCircuitBreakerMetrics;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
// Use this technique if you want some method to run using beans on startup of application
// You can also put this logic in the no args constructor of this class that will be used by spring to instantiate its bean. But the depend beans have to be instantiated manually

@Component
public class RegisteringCircuitBreakerMetrics {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    // here we add the circuit breaker registry to the meter registry that contains all prometheus metrics
    @PostConstruct
    public void registerCircuitBreakerMetrics(){
        TaggedCircuitBreakerMetrics
                .ofCircuitBreakerRegistry(circuitBreakerRegistry)
                .bindTo(meterRegistry);
    }
}
