package MST;

import Data.Edge;
import Data.Graph;

import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;


public class LazyPrimMST {
    private Queue<Edge> mst;
    private boolean[] marked;
    private PriorityQueue<Edge> pq;


    public LazyPrimMST(Graph G) {
        this.marked = new boolean[G.V()];
        this.mst = new LinkedList<Edge>();
        this.pq = new PriorityQueue<Edge>();
        visit(G, 0);
        while (!pq.isEmpty()) {
            Edge e = pq.poll();
            int v = e.either();
            int w = e.other(v);
            if (marked[v] && marked[w]) continue;
            mst.add(e);
            if (!marked[v]) visit(G, v);
            if (!marked[w]) visit(G, w);

        }

    }

    private void visit(Graph G, int v) {
        marked[v] = true;
        for (Edge e : G.adj(v)) {
            if (!marked[e.other(v)]) {
                pq.add(e);
            }

        }
    }

    public Iterable<Edge> PrimMST() {
        return mst;
    }

}
