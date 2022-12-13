package uk.sky.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {

        /**
         * We have a main thread and a separate thread available to run the long running operation inside client method
         *
         *  with future.get() the main thread gets blocked until the thread managing the background operation has finished and future object is populated
         *  with future.get(timeout) the main thread does not get blocked. It waits certain time before throwing exception
         *
         *
         */
        SquareCalculator squareCalculator = new SquareCalculator();
        Future<Integer> future = squareCalculator.calculate(10);

        while(!future.isDone()) {
            System.out.println(future.isDone());
            System.out.println("Calculating...");
            Thread.sleep(3000);
        }

        System.out.println(future.get(0, TimeUnit.SECONDS)); // The main thread will


    }
}
