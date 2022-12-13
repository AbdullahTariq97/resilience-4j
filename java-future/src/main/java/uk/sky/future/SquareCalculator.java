package uk.sky.future;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SquareCalculator {

    private ExecutorService executorService = Executors.newSingleThreadExecutor(); // This gives us the 2nd Thread that handled long running task in method

    public Future<Integer> calculate(int input) {
        /**
         * The Future object is a place holder for long running operation. It encapsulates value returned by the operation
         * When the long running operation is not finished the value inside it is empty
         * After the long running operation has finished the value inside is populated
         */
        Future<Integer> integerFuture = executorService.submit(() -> {
            Thread.sleep(3000);
            return input * input;
        });

        return integerFuture;
    }
}
