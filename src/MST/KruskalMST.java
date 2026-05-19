package MST;

import Algor.*;
import Data.Edge;
import Data.Graph;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
public class KruskalMST
{
    private Queue<Edge> mst = new LinkedList<Edge>();
    public KruskalMST(Graph G)
    {
        PriorityQueue<Edge> pq = new PriorityQueue<Edge>();
        for (Edge e : G.edges())
            pq.add(e);
        UF uf = new UF(G.V());
        while (!pq.isEmpty() && mst.size() < G.V()-1)
        {
            Edge e = pq.poll();
            int v = e.either(), w = e.other(v);
            if (!uf.connected(v, w))
            {
                uf.union(v, w);
                mst.add(e);
            }
        }
    }

    public Iterable<Edge> KruskalMST()
    { return mst; }
}