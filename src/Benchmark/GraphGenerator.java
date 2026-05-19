package Benchmark;

import java.util.Random;

import Data.Graph;

public class GraphGenerator {
    // Fixed seed
    private static final int SEED = 369;

    // Sparse Graph Undirected E ~ 5V
    public static Graph generateSparseGraph(int V) {
        Graph G = new Graph(V, false);
        Random random = new Random(SEED);
        int targetEdges = 5 * V;

        // Ensure the graph is connected 7ne3mel linear spanning path
        for (int i = 1; i < V; i++) {
            int weight = random.nextInt(1000) + 1;
            G.addEdge(i - 1, i, weight);
        }

        // 7T remaining random edges
        while (G.E() < targetEdges) {
            int u = random.nextInt(V);
            int v = random.nextInt(V);
            if (u != v) {
                int weight = random.nextInt(1000) + 1;
                G.addEdge(u, v, weight);
            }
        }
        return G;
    }

    // Dense Graph Undirected E ~ 25%
    public static Graph generateDenseGraph(int V) {
        Graph G = new Graph(V, false);
        Random random = new Random(SEED);
        long maxEdges = (long) V * (V - 1) / 2;
        long targetEdges = maxEdges / 4;   // 25%

        // Ensure the graph is connected
        for (int i = 1; i < V; i++) {
            int weight = random.nextInt(1000) + 1;
            G.addEdge(i - 1, i, weight);
        }

        // Add the remaining edges
        while (G.E() < targetEdges) {
            int u = random.nextInt(V);
            int v = random.nextInt(V);
            if (u != v) {
                int weight = random.nextInt(1000) + 1;
                G.addEdge(u, v, weight);
            }
        }
        return G;
    }

    // Complete Graph Undirected E = V*(V-1)/2
    public static Graph generateCompleteGraph(int V) {
        Graph G = new Graph(V, false);
        Random random = new Random(SEED);

        // Connect every vertex to every other vertex
        for (int i = 0; i < V; i++) {
            for (int j = i + 1; j < V; j++) {
                int weight = random.nextInt(1000) + 1;
                G.addEdge(i, j, weight);
            }
        }
        return G;
    }

    // Directed Acyclic Graph Directed E ~ 5V
    public static Graph generateDAG(int V) {
        Graph G = new Graph(V, true);
        Random random = new Random(SEED);
        int targetEdges = 5 * V;

        // Ensure connectivity with paths (always u < v)
        for (int i = 1; i < V - 1; i++) {
            int weight = random.nextInt(1000) + 1;
            G.addDirectedEdge(0, i, weight);
        }

        // Add random directed edges ensuring u < v
        while (G.E() < targetEdges) {
            int u = random.nextInt(V);
            int v = random.nextInt(V);
            if (u < v) {
                int weight = random.nextInt(1000) + 1;
                G.addDirectedEdge(u, v, weight);
            }
        }
        return G;
    }
}