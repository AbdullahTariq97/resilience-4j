package sky.uk.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpClient;
import java.util.function.Supplier;

@RestController
public class AccountService {

    private static final HttpClient httpClient = HttpClient.newHttpClient();

    @Autowired
    private CircuitBreaker circuitBreaker1;

    @GetMapping("/account-data")
    public String getAccountDate() {

       Supplier<String> httpResponseSupplier = CircuitBreaker.decorateSupplier(circuitBreaker1, callDownstream());

        System.out.println("Outside the supplier");

        return httpResponseSupplier.get();
    }

    private Supplier<String> callDownstream() {
        return () -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Tried to poll downstream");
            throw new RuntimeException("Downstream failed");
        };
    }
}
