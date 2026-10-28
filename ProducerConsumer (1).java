import java.util.LinkedList;

class PC {
    private final LinkedList<Integer> buffer = new LinkedList<>();
    private final int capacity = 2;
    private int value = 0;

    public void produce() {
        while (true) {
            synchronized (this) {
                while (buffer.size() == capacity) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                buffer.add(value);
                System.out.println("Producer produced-" + value);
                value++;
                notify();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public void consume() {
        while (true) {
            synchronized (this) {
                while (buffer.isEmpty()) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }

                int item = buffer.removeFirst();
                System.out.println("Consumer consumed-" + item);
                notify();
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}

public class ProducerConsumer {
    public static void main(String[] args) {
        PC pc = new PC();

        Thread producer = new Thread(pc::produce);
        Thread consumer = new Thread(pc::consume);

        producer.start();
        consumer.start();

        try {
            producer.join();
            consumer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
