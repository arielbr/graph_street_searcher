package hw8;

/**
 * This profiler can be used for simple benchmarking of Java applications.
 *
 * <p> To use the application:
 * <code>
 * SimpleProfiler.reset();
 * SimpleProfiler.start();
 * // Run the application with benchmark data.
 * SimpleProfiler.stop();
 * SimpleProfiler.getStatistics("Description of experiment");
 * </code>
 * </p>
 */
public class SimpleProfiler {

  private static final int NOT_RUNNING = -1;
  private static final long KB = 1024;

  private static final Runtime RUNTIME = Runtime.getRuntime();
  private static boolean isRunning; // defaults to false.

  // Time (in milliseconds)
  private static long netTime;
  private static long startTime = NOT_RUNNING;

  // Memory used (in bytes, printed as kilobytes)
  private static long netMemory;
  private static long startMemory = NOT_RUNNING;

  /**
   * Return total time elapsed (in milliseconds).
   *
   * @return total time elapsed between <code>start</code>
   *         and <code>stop</code>.
   */
  public static long getNetTime() {
    return netTime;
  }

  /**
   * Return net heap memory footprint in kilobytes.
   *
   * @return net memory consumption between <code>start</code>
   *         and <code>stop</code>.
   */
  public static long getNetMemory() {
    return netMemory / KB;
  }

  /**
   * Start collecting statistics for JVM runtime / heap usage.
   *
   * @throws RuntimeException If already running when started.
   */
  public static void start() {
    if (isRunning) {
      throw new RuntimeException("the profiler is already running");
    }

    isRunning = true;
    startTime = getCurrentTime();
    startMemory = getCurrentMemory();
  }

  /**
   * Stop collecting statistics for JVM runtime / heap usage.
   *
   * @throws RuntimeException If not running when stopped.
   */
  public static void stop() {
    if (!isRunning) {
      throw new RuntimeException("the profiler was not running");
    }

    netTime = getCurrentTime() - startTime;
    netMemory = getCurrentMemory() - startMemory;
    isRunning = false;
  }

  /**
   * Reset statistics.
   */
  public static void reset() {
    if (isRunning) {
      startMemory = getCurrentMemory();
      startTime = getCurrentTime();
    }
    netTime = 0;
    netMemory = 0;
  }

  /**
   * Get string of statistics.
   *
   * @param description of the process being profiled.
   * @return String containing time in milliseconds and heap usage in kilobytes.
   */
  public static String getStatistics(String description) {
    return String.format("%s in %d ms using %d kb memory.",
      description, getNetTime(), getNetMemory());
  }

  // Get current memory used.
  private static long getCurrentMemory() {
    return RUNTIME.totalMemory() - RUNTIME.freeMemory();
  }

  // Get current time.
  private static long getCurrentTime() {
    return System.currentTimeMillis();
  }
}