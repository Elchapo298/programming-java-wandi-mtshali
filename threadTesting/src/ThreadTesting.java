public class ThreadTesting {
    static boolean step1 = false, step2 = false;

    public static void main(String[] args) {
        // Thread 1: Does step 1 and notifies
        Thread t1 = new Thread(() -> {
            synchronized (ThreadTesting.class) {
                System.out.println("Step 1 done");
                step1 = true;
                ThreadTesting.class.notifyAll();
            }
        });

        // Thread 2: Waits for step1, then does step 2
        Thread t2 = new Thread(() -> {
            synchronized (ThreadTesting.class) {
                try {
                    while (!step1) {
                        ThreadTesting.class.wait();
                    }
                    System.out.println("Step 2 done");
                    step2 = true;
                    ThreadTesting.class.notifyAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Thread 3: Waits for step2, then does step 3
        Thread t3 = new Thread(() -> {
            synchronized (ThreadTesting.class) {
                try {
                    while (!step2) {
                        ThreadTesting.class.wait();
                    }
                    System.out.println("Step 3 done");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Start threads in reverse order to demonstrate synchronization works
        t3.start();  // This thread will wait for step2
        t2.start();  // This thread will wait for step1  
        t1.start();  // This thread starts the chain
    }
}