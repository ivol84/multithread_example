import org.apache.commons.lang3.time.StopWatch;

import java.util.*;

public class Application {
    private static final int NUMBER_OF_QUEUES  = 10;
    private static final int NUMBER_OF_PERSONS_PER_QUEUE = 100_000;

    /**
     * Супермаркет. Создать 10 очередей (100_000 человек в каждой). Создать 1 поток, который будет обслуживать людей,
     * по одному человеку из каждой очереди (печатать на экран “человек $username обслужен потоком $threadId”)
     * и затем записывать их имена в общий список. В конце должен получиться список размером 1000_000.
     */
    public static void main(String[] args) throws InterruptedException {
        // Initialise queues
        StopWatch stopwatch = new StopWatch();
        stopwatch.start();
        List<Queue<String>> queues = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_QUEUES; i++) {
            Queue<String> queue = new LinkedList<>();
            for (int j = 0; j < NUMBER_OF_PERSONS_PER_QUEUE; j++) {
                queue.add(String.format("Queue %d - Element %d", i + 1, j + 1));
            }
            queues.add(queue);
        }

        List<Thread> threads = new ArrayList<>();
        List<String> result = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_QUEUES; i++) {
            Thread thread = new Thread(new Consumer(result, queues));
            thread.start();
            threads.add(thread);
        }
        for (int i = 0; i < NUMBER_OF_QUEUES ; i++) {
            threads.get(i).join();
        }

        System.out.println(result);
        System.out.println(result.size());
        stopwatch.stop();
        System.out.println("Execution time:" + stopwatch.getTime());
    }

    static class Consumer implements Runnable {
        List<String> result;
        List<Queue<String>> queues;

        public Consumer(List<String> result, List<Queue<String>> queues) {
            this.result = result;
            this.queues = queues;
        }

        @Override
        public void run() {
            for (int i = 0; i < NUMBER_OF_QUEUES; i++) {
                String element = queues.get(i).poll();
                if (element != null) {
                    result.add(element);
                }
            }
        }
    }
}
