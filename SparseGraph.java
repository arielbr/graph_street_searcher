package hw8;

import exceptions.InsertionException;
import exceptions.PositionException;
import exceptions.RemovalException;
import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of a directed graph using incidence lists
 * for sparse graphs where most things aren't connected.
 *
 * @param <V> Vertex element type.
 * @param <E> Edge element type.
 */
public class SparseGraph<V, E> implements Graph<V, E> {

  private List<Vertex<V>> vertices;
  private List<Edge<E>> edges;

  /**
   * Constructor for instantiating a graph.
   */
  public SparseGraph() {
    this.vertices = new ArrayList<>();
    this.edges = new ArrayList<>();
  }

  // Checks vertex belongs to this graph
  private void checkOwner(VertexNode<V> toTest) {
    if (toTest.owner != this) {
      throw new PositionException();
    }
  }

  // Checks edge belongs to this graph
  private void checkOwner(EdgeNode<E> toTest) {
    if (toTest.owner != this) {
      throw new PositionException();
    }
  }

  // Converts the vertex back to a VertexNode to use internally
  private VertexNode<V> convert(Vertex<V> v) throws PositionException {
    try {
      VertexNode<V> gv = (VertexNode<V>) v;
      this.checkOwner(gv);
      return gv;
    } catch (ClassCastException ex) {
      throw new PositionException();
    }
  }

  // Converts and edge back to a EdgeNode to use internally
  private EdgeNode<E> convert(Edge<E> e) throws PositionException {
    try {
      EdgeNode<E> ge = (EdgeNode<E>) e;
      this.checkOwner(ge);
      return ge;
    } catch (ClassCastException ex) {
      throw new PositionException();
    }
  }

  // todo changes start below
  @Override
  public Vertex<V> insert(V v) {
    VertexNode<V> vert = new VertexNode<>(v, this);
    vertices.add(vert);
    return vert;
  }

  @Override
  public Edge<E> insert(Vertex<V> from, Vertex<V> to, E e)
      throws PositionException, InsertionException {
    if (!vertices.contains(from) || !vertices.contains(to)) {
      throw new PositionException();
    }
    // check self-loop
    if (from == to) {
      throw new InsertionException();
    }
    checkDuplicateEdge(from, to, e);
    Edge<E> newEdge = new EdgeNode<E>(convert(from), convert(to), e, this);
    edges.add(newEdge);
    convert(from).outgoing.add(newEdge);
    convert(to).incoming.add(newEdge);
    return newEdge;
  }

  /**
   * Helper function to check if the given edge is a duplicate of another.
   * @param from starting vertex of the edge.
   * @param to ending vertex of the edge.
   * @param e data of edge.
   */
  public void checkDuplicateEdge(Vertex<V> from, Vertex<V> to, E e) {
    Iterable<Edge<E>> out = outgoing(from);
    for (Edge<E> ed : out) {
      if (to(ed) == to) {
        throw new InsertionException();
      }
    }
    /*
    out = outgoing(to);
    for (Edge<E> ed : out) {
      if (to(ed) == from) {
        throw new InsertionException();
      }
    }
     */
  }

  @Override
  public V remove(Vertex<V> v) throws PositionException,
      RemovalException {
    if (!vertices.contains(v)) {
      throw new PositionException();
    }
    if (convert(v).incoming.size() != 0 || convert(v).outgoing.size() != 0) {
      throw new RemovalException();
    }
    vertices.remove(v);
    return convert(v).data;
  }

  @Override
  public E remove(Edge<E> e) throws PositionException {
    if (!edges.contains(e)) {
      throw new PositionException();
    }
    edges.remove(e);
    VertexNode<V> from = convert(e).from;
    from.outgoing.remove(e);
    VertexNode<V> to = convert(e).to;
    to.incoming.remove(e);
    return convert(e).data;
  }

  @Override
  public Iterable<Vertex<V>> vertices() {
    return vertices;
  }

  @Override
  public Iterable<Edge<E>> edges() {
    return edges;
  }

  @Override
  public Iterable<Edge<E>> outgoing(Vertex<V> v) throws PositionException {
    if (!vertices.contains(v)) {
      throw new PositionException();
    }
    return convert(v).outgoing;
  }

  @Override
  public Iterable<Edge<E>> incoming(Vertex<V> v) throws PositionException {
    if (!vertices.contains(v)) {
      throw new PositionException();
    }
    return convert(v).incoming;
  }

  @Override
  public Vertex<V> from(Edge<E> e) throws PositionException {
    if (!edges.contains(e)) {
      throw new PositionException();
    }
    return convert(e).from;
  }

  @Override
  public Vertex<V> to(Edge<E> e) throws PositionException {
    if (!edges.contains(e)) {
      throw new PositionException();
    }
    return convert(e).to;
  }

  @Override
  public void label(Vertex<V> v, Object l) throws PositionException {
    if (!vertices.contains(v)) {
      throw new PositionException();
    }
    convert(v).label = l;
  }

  @Override
  public void label(Edge<E> e, Object l) throws PositionException {
    if (!edges.contains(e)) {
      throw new PositionException();
    }
    convert(e).label = l;
  }

  @Override
  public Object label(Vertex<V> v) throws PositionException {
    if (!vertices.contains(v)) {
      throw new PositionException();
    }
    return convert(v).label;
  }

  @Override
  public Object label(Edge<E> e) throws PositionException {
    if (!edges.contains(e)) {
      throw new PositionException();
    }
    return convert(e).label;
  }

  @Override
  public void clearLabels() {
    for (Vertex<V> v : vertices) {
      convert(v).label = null;
    }
    for (Edge<E> e : edges) {
      convert(e).label = null;
    }
  }

  private String vertexString(Vertex<V> v) {
    return "\"" + v.get() + "\"";
  }

  private String verticesToString() {
    StringBuilder sb = new StringBuilder();
    for (Vertex<V> v : this.vertices) {
      sb.append("  ").append(vertexString(v)).append("\n");
    }
    return sb.toString();
  }

  private String edgeString(Edge<E> e) {
    return String.format("%s -> %s [label=\"%s\"]",
        this.vertexString(this.from(e)),
        this.vertexString(this.to(e)),
        e.get());
  }

  private String edgesToString() {
    String edgs = "";
    for (Edge<E> e : this.edges) {
      edgs += "  " + this.edgeString(e) + ";\n";
    }
    return edgs;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("digraph {\n")
        .append(this.verticesToString())
        .append(this.edgesToString())
        .append("}");
    return sb.toString();
  }

  // Class for a vertex of type V
  private final class VertexNode<V> implements Vertex<V> {
    V data;
    Graph<V, E> owner;
    List<Edge<E>> outgoing;
    List<Edge<E>> incoming;
    Double distance;
    Object label;
    final double maxDistance = 1e18;

    VertexNode(V v) {
      this.data = v;
      this.distance = maxDistance;
      this.outgoing = new ArrayList<>();
      this.incoming = new ArrayList<>();
      this.label = null;
    }

    VertexNode(V v, Graph<V, E> owner) {
      this.data = v;
      this.distance = maxDistance;
      this.outgoing = new ArrayList<>();
      this.incoming = new ArrayList<>();
      this.label = null;
      this.owner = owner;
    }

    @Override
    public V get() {
      return this.data;
    }

    @Override
    public void put(V v) {
      this.data = v;
    }
  }

  //Class for an edge of type E
  private final class EdgeNode<E> implements Edge<E> {
    E data;
    Graph<V, E> owner;
    VertexNode<V> from;
    VertexNode<V> to;
    Object label;

    // Constructor for a new edge
    EdgeNode(VertexNode<V> f, VertexNode<V> t, E e) {
      this.from = f;
      this.to = t;
      this.data = e;
      this.label = null;
    }

    EdgeNode(VertexNode<V> f, VertexNode<V> t, E e, Graph<V, E> owner) {
      this.from = f;
      this.to = t;
      this.data = e;
      this.label = null;
      this.owner = owner;
    }

    @Override
    public E get() {
      return this.data;
    }

    @Override
    public void put(E e) {
      this.data = e;
    }

  }
}
