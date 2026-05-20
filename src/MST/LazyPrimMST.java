package MST;

import Data.Edge;
import Data.Graph;

import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;

public class LazyPrimMST {
    private Edge[] edgeTo;
    private int[] distTo;
    private boolean[] marked;
    private PriorityQueue<VertexWeight> pq;
    private Queue<Edge> mst;

    public LazyPrimMST(Graph G) {
        edgeTo = new Edge[G.V()];
        distTo = new int[G.V()];
        marked = new boolean[G.V()];
        mst = new LinkedList<>();
        pq = new PriorityQueue<>(G.V());

        for (int v = 0; v < G.V(); v++) {
            distTo[v] = Integer.MAX_VALUE;
        }

        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                prim(G, v);
            }
        }
    }

    private void prim(Graph G, int s) {
        distTo[s] = 0;
        pq.add(new VertexWeight(s, 0));
        while (!pq.isEmpty()) {
            VertexWeight current = pq.poll();
            int v = current.v;
            if (distTo[v] < current.weight) {
                continue;
            }
            marked[v] = true;
            if (edgeTo[v] != null) {
                mst.add(edgeTo[v]);
            }
            for (Edge e : G.adj(v)) {
                int w = e.other(v);
                if (marked[w]) continue;
                if (distTo[w] > e.weight()) {
                    distTo[w] = e.weight();
                    edgeTo[w] = e;
                    pq.add(new VertexWeight(w, distTo[w]));
                }
            }
        }
    }

    public Iterable<Edge> PrimMST() {
        return mst;
    }

    private static class VertexWeight implements Comparable<VertexWeight> {
        private int v;
        private int weight;

        public VertexWeight(int v, int weight) {
            this.v = v;
            this.weight = weight;
        }

        @Override
        public int compareTo(VertexWeight that) {
            return Integer.compare(this.weight, that.weight);
        }
    }
}