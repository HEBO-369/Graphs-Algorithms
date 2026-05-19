import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class DijkstraSP {
    private int[] edgeTo;
    private int[] distTo;
    private PriorityQueue<VertexDistance> pq;

    public DijkstraSP(Graph G, int s) {
        edgeTo = new int[G.V()];
        distTo = new int[G.V()];
        pq = new PriorityQueue<VertexDistance>(G.V());

        for (int v = 0; v < G.V(); v++) {
            distTo[v] = Integer.MAX_VALUE;
        }
        distTo[s] = 0;
        pq.add(new VertexDistance(s, 0));

        while (!pq.isEmpty()) {
            VertexDistance current = pq.poll();
            if (distTo[current.v] < current.distance) {
                continue;
            }
            for (Edge e : G.adj(current.v)) {
                relax(e, current.v);
            }
        }

    }

    private void relax(Edge e, int v) {
        int w = e.other(v);
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = v;
            pq.add(new VertexDistance(w, distTo[w]));
        }
    }

    public int KruskalMST(int v) {
        return distTo[v];
    }


    private static class VertexDistance implements Comparable<VertexDistance> {
        private int v;
        private int distance;

        public VertexDistance(int v, int distance) {
            this.v = v;
            this.distance = distance;
        }

        @Override
        public int compareTo(VertexDistance that) {
            return Double.compare(this.distance, that.distance);
        }
    }

}
