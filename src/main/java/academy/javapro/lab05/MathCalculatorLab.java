package academy.javapro.lab05;

import java.util.Scanner;
import java.util.Map;
import java.util.LinkedHashMap;

public class MathCalculatorLab {


    static class LRUCache<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;
        public LRUCache(int capacity) {
            super(capacity, 0.75f, true);
            this.capacity = capacity;
        }
        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;
        }
    }

    // Simple class to hold calculation data (PROVIDED - DO NOT MODIFY)
    static class CalculatorBase {
        int n;
        long result;

        public CalculatorBase(int n) {
            this.n = n;
            this.result = 0;
        }
    }

    // TODO 1: Implement this method
    // Creates a Runnable that calculates the nth Fibonacci number
    // Fibonacci sequence: 0, 1, 1, 2, 3, 5, 8, 13, 21, 34...
    // Formula: fib(n) = fib(n-1) + fib(n-2)
    // Base cases: fib(0) = 0, fib(1) = 1
    //
    // The Runnable should:
    // 1. Print: "Thread-X computing: fib(n)"
    // 2. Calculate Fibonacci iteratively using two variables (prev, curr)
    // 3. Include Thread.sleep(5) in the loop to simulate work
    // 4. Store result in calc.result
    // 5. Print: "Thread-X completed: fibonacci(n) = result"
    //
    // Example: fibonacci(8) = 21
    public static Runnable fibonacciCalculator(CalculatorBase calc) {
        // TODO: Return a Runnable (use lambda or anonymous class)

        Function<Integer, Integer> fib = (n) -> {
            if (n == 0) return 0;
            if (n == 1) return 1;
            return fib(n-1) + fib(n-2);
        };
        
        return new Runnable() {
            public void run {
                calc.result = fib(calc.n);
            }
        }; // Replace this
    }

    // TODO 2: Implement this method
    // Creates a Runnable that calculates sum of squares from 1 to n
    // Formula: 1² + 2² + 3² + ... + n²
    //
    // The Runnable should:
    // 1. Print: "Thread-X computing: 1² + 2² + 3² + ... + n²"
    // 2. Loop from 1 to n, adding i*i to result
    // 3. Include Thread.sleep(5) in the loop to simulate work
    // 4. Store result in calc.result
    // 5. Print: "Thread-X completed: sumOfSquares(n) = result"
    //
    // Example: sumOfSquares(5) = 1 + 4 + 9 + 16 + 25 = 55
    public static Runnable sumOfSquaresCalculator(CalculatorBase calc) {
        // TODO: Return a Runnable (use lambda or anonymous class)
        return null; // Replace this
    }

    // Main method (PROVIDED - DO NOT MODIFY)
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Threaded Math Calculator ===");
            System.out.println("1. Single calculation demo");
            System.out.println("2. Multiple concurrent calculations");
            System.out.println("3. Exit");
            System.out.print("\nEnter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    runSingleDemo(scanner);
                    break;
                case 2:
                    runMultipleDemo(scanner);
                    break;
                case 3:
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }

        scanner.close();
    }

    // Helper method for single calculation (PROVIDED - DO NOT MODIFY)
    private static void runSingleDemo(Scanner scanner) {
        System.out.println("\n=== Single Calculation Demo ===");
        System.out.println("1. Fibonacci");
        System.out.println("2. Sum of Squares");
        System.out.print("\nChoose calculator: ");

        int calcChoice = scanner.nextInt();
        System.out.print("Enter a number (1-20): ");
        int n = scanner.nextInt();

        if (n < 1 || n > 20) {
            System.out.println("Number must be between 1 and 20");
            return;
        }

        CalculatorBase calc = new CalculatorBase(n);
        Runnable task = null;
        String calcName = "";

        if (calcChoice == 1) {
            calcName = "Fibonacci";
            task = fibonacciCalculator(calc);
        } else if (calcChoice == 2) {
            calcName = "Sum of Squares";
            task = sumOfSquaresCalculator(calc);
        } else {
            System.out.println("Invalid calculator choice");
            return;
        }

        System.out.println("\nCreating " + calcName + " calculator thread...");
        long startTime = System.currentTimeMillis();

        Thread thread = new Thread(task);
        System.out.println(thread.getName() + " starting calculation for " + n);
        thread.start();

        try {
            System.out.println("\nMain thread waiting for calculation to complete...");
            thread.join();
            System.out.println(thread.getName() + " has finished execution");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("\nResult: " + calcName.toLowerCase() + "(" + n + ") = " + calc.result);
        System.out.println("Execution time: " + (endTime - startTime) + "ms");
    }

    // Helper method for multiple calculations (PROVIDED - DO NOT MODIFY)
    private static void runMultipleDemo(Scanner scanner) {
        System.out.println("\n=== Multiple Concurrent Calculations ===");
        System.out.print("Enter a number (1-20): ");
        int n = scanner.nextInt();

        if (n < 1 || n > 20) {
            System.out.println("Number must be between 1 and 20");
            return;
        }

        System.out.println("\nCreating all calculator threads...");

        CalculatorBase fibCalc = new CalculatorBase(n);
        CalculatorBase sumCalc = new CalculatorBase(n);

        System.out.println("Creating thread for Fibonacci...");
        Runnable fibTask = fibonacciCalculator(fibCalc);
        Thread fibThread = new Thread(fibTask);

        System.out.println("Creating thread for Sum of Squares...");
        Runnable sumTask = sumOfSquaresCalculator(sumCalc);
        Thread sumThread = new Thread(sumTask);

        System.out.println("\nStarting both threads concurrently...\n");
        long startTime = System.currentTimeMillis();

        fibThread.start();
        sumThread.start();

        try {
            System.out.println("\nMain thread waiting for all calculations to complete...");
            fibThread.join();
            System.out.println(fibThread.getName() + " has finished execution");

            sumThread.join();
            System.out.println(sumThread.getName() + " has finished execution");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();

        System.out.println("\n=== Results ===");
        System.out.println("Fibonacci(" + n + ") = " + fibCalc.result);
        System.out.println("Sum of Squares(" + n + ") = " + sumCalc.result);
        System.out.println("\nTotal execution time: " + (endTime - startTime) + "ms");
        System.out.println("All calculations completed successfully!");
    }
}
