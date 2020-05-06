package hw8;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StreetSearcherExperiment {

  // Update this to any other data file for benchmarking experiments or testing.
  private static String getDataFile() {
    return "baltimore.streets.txt";
  }

  // Change the returned String to run StreetSearcher
  // with a different starting vertex
  private static String getStartName() {
    return "-76.6175,39.3296";
  }

  // Change the returned String to run StreetSearcher
  // with a different ending vertex
  private static String getEndName() {
    return "-76.6383,39.3206";
  }

  private static void profileStreetSearcher(
      StreetSearcher streetSearcher, File data, String startName, String endName
  ) {

    SimpleProfiler.reset();
    SimpleProfiler.start();

    try {
      streetSearcher.loadNetwork(data);
      streetSearcher.findShortestPath(startName, endName);
    } catch (FileNotFoundException e) {
      System.err.println("Could not find file " + data.getName());
      return;
    } catch (IllegalArgumentException e) {
      System.err.println("Invalid Endpoint: " + e.getMessage());
      return;
    }

    SimpleProfiler.stop();

    String description = String.format(
        "\nRan with %s from %s to %s", data.getName(), startName, endName
    );

    System.out.println(SimpleProfiler.getStatistics(description));
  }

  /**
   * Execution starts here.
   *
   * @param args command-line arguments not used here.
   */
  public static void main(String[] args) {
    StreetSearcher streetSearcher = new StreetSearcher();
    Path dataFile = Paths.get("res", "src", getDataFile());

    profileStreetSearcher(
        streetSearcher, dataFile.toFile(), getStartName(), getEndName()
    );
  }
}