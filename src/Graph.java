import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
public class Graph {
    private final int V ;
    private int E ;
    private ArrayList<Edge>[] adj;
    public ArrayList<Edge> edges;
    public boolean directed;



    /**
    * Initializes an empty graph with V vertices and 0 edges.
    * param V the number of vertices
    **/
    public Graph(int V, boolean directed) {
        if(V <= 0 ) throw new IllegalArgumentException("Number of Vertices must be positive ");
        this.V = V ;
        this.E = 0;
        this.edges = new ArrayList<Edge>();
        adj = (ArrayList<Edge>[]) new ArrayList[V];
        for ( int v = 0 ; v < V ; v++){
            adj[v] = new ArrayList<Edge>();
        }
    }
    public void addEdge(Edge edge) {
        int v = edge.either();
        int w = edge.other(v);
        validateVertex(v);
        validateVertex(w);
        adj[v].add(edge);
        if (!directed) adj[w].add(edge);
        edges.add(edge);
        E++;
    }
    public int V(){
        return V ;
    }
    public int E(){
        return E ;
    }
    public void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

    public Iterable<Edge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }
    public int degree(int v) {
        return adj[v].size();
    }
    public int maxDegree() {
        int max_deg = 0;
        for (int i = 0; i < V; i++) {
            if (degree(i) > max_deg) {
                max_deg = degree(i);
            }
        }
        return max_deg;
    }
    public Iterable<Edge> edges() {
        return edges;
    }

}