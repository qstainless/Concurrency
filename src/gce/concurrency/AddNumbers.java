package gce.concurrency;

/**
 * Add the elements of an array of integers using single or multiple threads.
 */
public class AddNumbers extends Thread {
    private final int[] arrayOfIntegers;
    private final int indexStart;
    private final int indexEnd;
    private long threadSum;

    /**
     * Object constructor.
     *
     * @param arrayOfIntegers Array of 200 million random numbers between 1 to 10.
     * @param indexStart      Starting index in arrayOfNumbers.
     * @param indexEnd        Ending index in arrayOfNumbers.
     */
    public AddNumbers(int[] arrayOfIntegers, int indexStart, int indexEnd) {
        this.arrayOfIntegers = arrayOfIntegers;
        this.indexStart = indexStart;
        this.indexEnd = Math.min(indexEnd, arrayOfIntegers.length);
        this.threadSum = 0;
    }

    /**
     * Called by {@link Concurrency#main(String[])} and by the {@link AddNumbers#run()} method to
     * perform the addition using a single thread and multiple threads, respectively,
     * respectively.
     *
     * @param arrayOfIntegers Array of random integers.
     * @param indexStart      Starting index of arrayOfNumbers.
     * @param indexEnd        Ending index of arrayOfNumbers.
     * @return The total sum of all integers in arrayOfIntegers.
     */
    public static long singleThreadSum(int[] arrayOfIntegers, int indexStart, int indexEnd) {
        long totalSum = 0;

        for (int i = indexStart; i < indexEnd; i++) {
            totalSum += arrayOfIntegers[i];
        }

        return totalSum;
    }

    /**
     * Performs the addition of the integers in {@code arrayOfIntegers} using
     * multiple threads. Each thread will add a number of elements equivalent
     * to the total array length divided by the maximum number of threads
     * defined.
     *
     * @param arrayOfIntegers Array of random integers.
     * @param numberOfThreads The number of threads to use for calculating the addition.
     * @return The total sum of all integers in arrayOfIntegers.
     */
    public static long multiThreadSum(int[] arrayOfIntegers, int numberOfThreads) throws InterruptedException {
        // Split the arrayOfIntegers across threads
        int threadIndexLength = (int) Math.ceil(arrayOfIntegers.length * 1.0 / numberOfThreads);

        AddNumbers[] additionThreadResults = new AddNumbers[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            int indexStart = i * threadIndexLength;
            int indexEnd = (i + 1) * threadIndexLength;
            additionThreadResults[i] = new AddNumbers(arrayOfIntegers, indexStart, indexEnd);
            additionThreadResults[i].start(); // Calls the run() method for each thread
        }

        // Wait for all threads to complete execution
        for (int i = 0; i < additionThreadResults.length; i++) {
            AddNumbers threadResult = additionThreadResults[i];
            threadResult.join();
        }

        return addThreadResults(additionThreadResults);
    }

    /**
     * Adds the individual results of all threads once they all complete
     * execution.
     *
     * @param additionThreadResults The addition results of each thread.
     * @return The total sum of all threads
     */
    public static long addThreadResults(AddNumbers[] additionThreadResults) {
        long totalThreadSum = 0;

        for (AddNumbers addNumbers : additionThreadResults) {
            totalThreadSum += addNumbers.getThreadSum();
        }

        return totalThreadSum;
    }

    /**
     * Getter that returns the {@code threadSum} of each thread when adding
     * using multiple threads.
     *
     * @return The threadSum value.
     */
    public long getThreadSum() {
        return this.threadSum;
    }

    /**
     * Run method called by the start() method for multi-thread operations,
     * each of which will call {@link AddNumbers#singleThreadSum(int[], int, int)}.
     */
    public void run() {
        threadSum = singleThreadSum(arrayOfIntegers, indexStart, indexEnd);
    }
}
