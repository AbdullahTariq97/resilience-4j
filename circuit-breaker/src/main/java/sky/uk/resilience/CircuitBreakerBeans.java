package sky.uk.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CircuitBreakerBeans {

    @Bean
    public CircuitBreakerConfig circuitBreakerConfig(){
        return CircuitBreakerConfig.custom()
                .failureRateThreshold(20)
                .waitDurationInOpenState(Duration.ofMillis(10000))
                .permittedNumberOfCallsInHalfOpenState(1)
                .minimumNumberOfCalls(5)
                .slidingWindowType(io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(5)
                .build();
    }

    @Bean
    public CircuitBreakerRegistry CircuitBreakerRegistry(CircuitBreakerConfig circuitBreakerConfig){
        return CircuitBreakerRegistry.of(circuitBreakerConfig);
    }

    @Bean
    public CircuitBreaker circuitBreaker(CircuitBreakerRegistry circuitBreakerRegistry){
        return  circuitBreakerRegistry.circuitBreaker("breaker-1");
    }

//    https://resilience4j.readme.io/docs/micrometer
    @Bean
    public MeterRegistry meterRegistry(){
        return new SimpleMeterRegistry();
    }
}
