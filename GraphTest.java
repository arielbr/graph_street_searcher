package hw8;

import exceptions.InsertionException;
import exceptions.PositionException;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public abstract class GraphTest {

  protected Graph<String, String> graph;

  @Before
  public void setupGraph() {
    this.graph = createGraph();
  }


  protected abstract Graph<String, String> createGraph();

  @Test
  public void testInsertVertex() {
    Vertex<String> v1 = graph.insert("v1");
    assertEquals(v1.get(), "v1");
  }

  @Test
  public void testInsertEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    assertEquals(e1.get(), "v1-v2");
    assertEquals(v1.get(), graph.from(e1).get());
    assertEquals(v2.get(), graph.to(e1).get());
  }

  @Test(expected = PositionException.class)
  public void testInsertEdgeThrowsPositionExceptionWhenfirstVertexIsNull() {
    Vertex<String> v = graph.insert("v");
    Edge<String> e = graph.insert(null, v, "e");
  }

  @Test(expected = PositionException.class)
  public void testInsertEdgeThrowsPositionExceptionWhenSecondVertexIsNull() {
    Vertex<String> v = graph.insert("v");
    Edge<String> e = graph.insert(v, null, "e");
  }

  @Test(expected = PositionException.class)
  public void testInsertEdgeThrowsPositionExceptionWhenFirstVertexIsInvalid() {
    Graph<String, String> otherGraph = createGraph();
    Vertex<String> v1 = otherGraph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e = graph.insert(v1, v2, "e");
  }

  @Test(expected = PositionException.class)
  public void testInsertEdgeThrowsPositionExceptionWhenSecondVertexIsInvalid() {
    Graph<String, String> otherGraph = createGraph();
    Vertex<String> v1 = otherGraph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e = graph.insert(v2, v1, "e");
  }

  @Test(expected = PositionException.class)
  public void testInsertEdgeThrowsPositionExceptionWhenFirstVertexWasRemoved() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    graph.remove(v1);
    graph.insert(v1, v2, "e");
  }

  @Test(expected = PositionException.class)
  public void testInsertEdgeThrowsPositionExceptionWhenSecondVertexWasRemoved() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    graph.remove(v2);
    graph.insert(v1, v2, "e");
  }

  @Test(expected = InsertionException.class)
  public void testInsertEdgeThrowsInsertionExceptionForSelfLoopEdge() {
    Vertex<String> v = graph.insert("v");
    graph.insert(v, v, "e");
  }

  @Test(expected = InsertionException.class)
  public void testInsertEdgeThrowsInsertionExceptionForMultipleParallelEdge() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    graph.insert(v1, v2, "v1-v2");
    graph.insert(v1, v2, "v1-v2");
  }

  @Test
  public void testInsertMultipleEdgeOppositeDirection() {
    Vertex<String> v1 = graph.insert("v1");
    Vertex<String> v2 = graph.insert("v2");
    Edge<String> e1 = graph.insert(v1, v2, "v1-v2");
    Edge<String> e2 = graph.insert(v2, v1, "v2-v1");
    assertEquals(e1.get(), "v1-v2");
    assertEquals(e2.get(), "v2-v1");
  }

  // TODO you must add many more tests here.
}
