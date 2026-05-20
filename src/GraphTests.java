import Data.Edge;
import Data.Graph;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class GraphTests {

    @Test
    public void testMST() {
        Graph G = new Graph(3, false);
        G.addEdge(0, 1, 10);
        G.addEdge(1, 2, 20);
        G.addEdge(0, 2, 5);

        List<Edge> primMST = G.PrimMST();
        int primWeight = primMST.stream().mapToInt(Edge::weight).sum();
        assertEquals(15, primWeight);

        List<Edge> kruskalMST = G.kruskalMST();
        int kruskalWeight = kruskalMST.stream().mapToInt(Edge::weight).sum();
        assertEquals(15, kruskalWeight);
    }

    @Test
    public void testDijkstra() {
        Graph G = new Graph(3, true);
        G.addDirectedEdge(0, 1, 2);
        G.addDirectedEdge(1, 2, 3);
        G.addDirectedEdge(0, 2, 10);

        int[] distTo = G.dijkstra(0);

        assertEquals(2, distTo[1]);
        assertEquals(5, distTo[2]);
    }

    @Test
    public void testDAGShortestPath() {
        Graph G = new Graph(3, true);
        G.addDirectedEdge(0, 1, 5);
        G.addDirectedEdge(1, 2, 5);
        G.addDirectedEdge(0, 2, 20);

        int[] distTo = G.dagShortestPath(0);

        assertEquals(10, distTo[2]);
    }

    @Test
    public void testDAGCycleDetection() {
        Graph G = new Graph(3, true);
        G.addDirectedEdge(0, 1, 1);
        G.addDirectedEdge(1, 2, 1);
        G.addDirectedEdge(2, 0, 1);

        try {
            G.dagShortestPath(0);
            fail("Expected IllegalArgumentException for cyclic graph");
        } catch (IllegalArgumentException e) {
            // Test passed successfully (Exception caught)
        }
    }
}