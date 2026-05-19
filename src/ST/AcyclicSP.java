package ST;

import Data.Edge;
import Data.Graph;

import java.util.Stack;

public class AcyclicSP {
    private int[] edgeTo;
    private int[] distTo;
    private boolean[] marked;
    private Stack<Integer> topological;
    private boolean[] onStack;

    public AcyclicSP(Graph G, int s) {
        edgeTo = new int[G.V()];
        distTo = new int[G.V()];
        marked = new boolean[G.V()];
        topological = new Stack<>();
        onStack = new boolean[G.V()];

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Integer.MAX_VALUE;
        distTo[s] = 0;
        int[] topological = Topological(G);
        for (int v : topological)
            for (Edge e : G.adj(v))
                relax(e, v);
    }

    private void relax(Edge e, int v) {
        int w = e.other(v);
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = v;
        }
    }

    private int[] Topological(Graph G) {
        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) {
                dfs(G, v);
            }
        }
        int[] result = new int[G.V()];
        for (int i = 0; i < G.V(); i++) {
            result[i] = topological.pop();
        }
        return result;
    }

    private void dfs(Graph G, int v) {
        marked[v] = true;
        onStack[v] = true;
        for (Edge w : G.adj(v)) {
            if (onStack[w.other(v)]) {
                throw new IllegalArgumentException("Data.Graph contains a cycle");
            }
            if (!marked[w.other(v)]) {
                dfs(G, w.other(v));
            }
        }
        onStack[v] = false;
        topological.push(v);
    }

    public int[] distTo() {
        return distTo;
    }

}