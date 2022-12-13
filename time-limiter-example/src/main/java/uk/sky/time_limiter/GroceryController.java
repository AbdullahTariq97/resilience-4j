package uk.sky.time_limiter;

import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@RestController
public class GroceryController {

    @GetMapping("/grocery")
    public ResponseEntity<String> getGroceryList() throws Exception {

        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(1)).build();

        TimeLimiterRegistry timeLimiterRegistry = TimeLimiterRegistry.of(timeLimiterConfig);

        TimeLimiter timeLimiter = timeLimiterRegistry.timeLimiter("custom-time-limiter");

        String downstreamResponse = timeLimiter.executeFutureSupplier(() -> CompletableFuture.supplyAsync(() -> {
            try {
                /**
                 * If the downstream operation takes longer than timeout duration on time limiter than TimeoutException is thrown by the time limiter decorating supplier
                 * If the downstream operation takes less than timeout duration on time limiter, no exception gets thrown
                 */
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Something done after downstream call");
            return "Apples";
        }));

        return ResponseEntity.ok(downstreamResponse);
    }
}

