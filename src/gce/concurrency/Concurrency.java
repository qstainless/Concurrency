package gce.concurrency;

import java.text.NumberFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * This Concurrency program attempts to implement an application that creates
 * an array of 200 million numbers between 1 and 10 and then computes the sum
 * of those numbers in parallel using multiple threads. It also computes the
 * sum of the numbers using a single thread and displays the sum and processing
 * times for both cases.
 * <p>
 * Course: CEN 3024C-27021 Software Development I
 * Instructor: Dr. Lisa Macon
 *
 * @author Guillermo Castaneda Echegaray
 * @version 1.8
 * @since 2020-03-12
 */
public class Concurrency {

    /**
     * The random number.
     */
    static final Random randomNumber = new Random();

    /**
     * The maximum value of the random numbers to generate.
     */
    static final int randomNumberMaxValue = 10;

    /**
     * The number of processors available in the system.
     */
    static final int numberOfProcessors = Runtime.getRuntime().availableProcessors();

    /**
     * The random number array length.
     */
    static final int randomNumberArrayLength = 200000000;

    /**
     * The maximum number of threads to use.
     */
    static final int maximumNumberOfThreads = 20;

    /**
     * The constant outputFormat for pretty output to the console.
     */
    static final String outputFormat = "| %-14s | %-13s | %-13s | %-13s |%n";

    /**
     * The constant startTime.
     */
    static long startTime;

    /**
     * The constant endTime.
     */
    static long endTime;

    /**
     * The constant totalSum. The total sum of integers in the randomNumberArray.
     */
    static long totalSum;

    /**
     * The application entry point.
     *
     * @param args the input arguments
     * @throws InterruptedException the interrupted exception
     */
    public static void main(String[] args) throws InterruptedException {

        // Create an array of 200 million random numbers
        int[] randomNumberArray = createRandomNumberArray(randomNumberArrayLength);

        System.out.println("200 million random numbers between 1 and 10 were added using \n" +
                "multiple and single threads in a system with " + numberOfProcessors + " processors.\n\n" +
                "These are the results:\n");

        outputHeaders();

        executeMultiThreadSum(randomNumberArray);

        executeSingleThreadSum(randomNumberArray);
    }

    /**
     * Creates an array of random integers of given values and length.
     *
     * @param arrayLength The length for the array of random numbers
     * @return The generated array of random numbers
     */
    public static int[] createRandomNumberArray(int arrayLength) {
        int[] randomNumberArray = new int[arrayLength];

        for (int i = 0; i < randomNumberArray.length; i++) {
            randomNumberArray[i] = randomNumber.nextInt(randomNumberMaxValue) + 1; // random numbers between 1 and 10
        }

        return randomNumberArray;
    }

    /**
     * Executes the sum of numbers in the {@code randomNumberArray} using
     * a single thread.
     *
     * @param randomNumberArray The array of random integers.
     */
    public static void executeSingleThreadSum(int[] randomNumberArray) {
        startTime = System.nanoTime();
        totalSum = AddNumbers.singleThreadSum(randomNumberArray, 0, randomNumberArrayLength);
        endTime = System.nanoTime();

        outputResults("Single", startTime, endTime, totalSum, 1);

        outputDivider();
    }

    /**
     * Executes the sum of numbers in the {@code randomNumberArray} using
     * multiple concurrent threads.
     *
     * @param randomNumberArray The array of random integers.
     * @throws InterruptedException the interrupted exception
     */
    public static void executeMultiThreadSum(int[] randomNumberArray) throws InterruptedException {
        for (int i = 2; i <= maximumNumberOfThreads; i++) {
            startTime = System.nanoTime();
            totalSum = AddNumbers.multiThreadSum(randomNumberArray, i);
            endTime = System.nanoTime();

            outputResults("Multiple", startTime, endTime, totalSum, i);
        }

        outputDivider();
    }

    /**
     * Outputs the results of each calculation as it is processed.
     *
     * @param type            The type of calculation made: multiple or single thread.
     * @param startTime       The start time of the calculation.
     * @param endTime         The end time of the calculation.
     * @param totalSum        The total sum of the numbers passed by the AddNumbers class.
     * @param numberOfThreads Number of threads used to calculate the sum.
     */
    public static void outputResults(String type, long startTime, long endTime, long totalSum, int numberOfThreads) {
        NumberFormat sumFormat = NumberFormat.getInstance();

        long executionTime = endTime - startTime;

        System.out.format(
                outputFormat,
                type + (numberOfThreads > 1 ? " (" + numberOfThreads + ")" : ""),
                sumFormat.format(totalSum),
                sumFormat.format(executionTime),
                sumFormat.format(TimeUnit.NANOSECONDS.toMillis(executionTime))
        );

    }

    /**
     * Outputs the table headers to the console
     */
    private static void outputHeaders() {
        outputDivider();
        System.out.format(outputFormat, "Threads Used", "Total Sum", "Nanoseconds", "Milliseconds");
        outputDivider();
    }

    /**
     * Outputs the table divider to the console
     */
    private static void outputDivider() {
        System.out.format("+----------------+---------------+---------------+---------------+%n");
    }
}
