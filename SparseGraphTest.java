package hw8;


public class SparseGraphTest extends GraphTest {

  @Override
  protected Graph<String, String> createGraph() {
    return new SparseGraph<>();
  }
}
