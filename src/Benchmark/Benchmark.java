package Benchmark;

import java.util.Arrays;
import java.util.Random;

import Data.Graph;

public class Benchmark {

    public static void runAllBenchmarks(int[] sizes, int RUNS) {
        System.out.println("Initializing 11 CSV Exporters...");

        CSVExporter mstSparsePrim = new CSVExporter("MST_Sparse_Prim.csv", RUNS);
        CSVExporter mstSparseKruskal = new CSVExporter("MST_Sparse_Kruskal.csv", RUNS);
        CSVExporter mstDensePrim = new CSVExporter("MST_Dense_Prim.csv", RUNS);
        CSVExporter mstDenseKruskal = new CSVExporter("MST_Dense_Kruskal.csv", RUNS);
        CSVExporter mstCompletePrim = new CSVExporter("MST_Complete_Prim.csv", RUNS);
        CSVExporter mstCompleteKruskal = new CSVExporter("MST_Complete_Kruskal.csv", RUNS);

        CSVExporter ssspSparseDijkstra = new CSVExporter("SSSP_Sparse_Dijkstra.csv", RUNS);
        CSVExporter ssspDenseDijkstra = new CSVExporter("SSSP_Dense_Dijkstra.csv", RUNS);
        CSVExporter ssspCompleteDijkstra = new CSVExporter("SSSP_Complete_Dijkstra.csv", RUNS);

        CSVExporter ssspDagDijkstra = new CSVExporter("SSSP_DAG_Dijkstra.csv", RUNS);
        CSVExporter ssspDagAcyclic = new CSVExporter("SSSP_DAG_AcyclicSP.csv", RUNS);

        for (int V : sizes) {
            System.out.println("\n==================================================");
            System.out.println(" Generating & Benchmarking Graph Topologies (V = " + V + ")");
            System.out.println("==================================================");

            Graph sparse = GraphGenerator.generateSparseGraph(V);
            Graph dense = GraphGenerator.generateDenseGraph(V);
            Graph complete = GraphGenerator.generateCompleteGraph(V);
            Graph dag = GraphGenerator.generateDAG(V);

            System.out.println("\n--- Benchmarking MST Construction ---");
            runMSTBenchmark("Sparse Graph", sparse, RUNS, V, mstSparsePrim, mstSparseKruskal);
            runMSTBenchmark("Dense Graph", dense, RUNS, V, mstDensePrim, mstDenseKruskal);
            runMSTBenchmark("Complete Graph", complete, RUNS, V, mstCompletePrim, mstCompleteKruskal);

            System.out.println("\n--- Benchmarking SSSP (Dijkstra) ---");
            runDijkstraBenchmark("Sparse Graph", sparse, RUNS, V, ssspSparseDijkstra);
            runDijkstraBenchmark("Dense Graph", dense, RUNS, V, ssspDenseDijkstra);
            runDijkstraBenchmark("Complete Graph", complete, RUNS, V, ssspCompleteDijkstra);

            System.out.println("\n--- Benchmarking SSSP on DAG ---");
            runDAGBenchmark(dag, RUNS, V, ssspDagDijkstra, ssspDagAcyclic);
        }

        mstSparsePrim.close();
        mstSparseKruskal.close();
        mstDensePrim.close();
        mstDenseKruskal.close();
        mstCompletePrim.close();
        mstCompleteKruskal.close();
        ssspSparseDijkstra.close();
        ssspDenseDijkstra.close();
        ssspCompleteDijkstra.close();
        ssspDagDijkstra.close();
        ssspDagAcyclic.close();

        System.out.println("\nAll benchmarks finished! 11 CSV files generated successfully.");
    }

    private static void runMSTBenchmark(String topology, Graph G, int RUNS, int V, CSVExporter primCsv, CSVExporter kruskalCsv) {
        long[] primTimes = new long[RUNS];
        long[] kruskalTimes = new long[RUNS];

        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            G.PrimMST();
            primTimes[i] = System.nanoTime() - start;

            start = System.nanoTime();
            G.kruskalMST();
            kruskalTimes[i] = System.nanoTime() - start;
        }

        saveAndPrintStats("MST", topology, "Prim", primTimes, V, primCsv);
        saveAndPrintStats("MST", topology, "Kruskal", kruskalTimes, V, kruskalCsv);
    }

    private static void runDijkstraBenchmark(String topology, Graph G, int RUNS, int V, CSVExporter csv) {
        long[] times = new long[RUNS];
        Random rand = new Random(42);

        for (int i = 0; i < RUNS; i++) {
            int source = rand.nextInt(G.V());
            long start = System.nanoTime();
            G.dijkstra(source);
            times[i] = System.nanoTime() - start;
        }
        saveAndPrintStats("SSSP_General", topology, "Dijkstra", times, V, csv);
    }

    private static void runDAGBenchmark(Graph G, int RUNS, int V, CSVExporter dijkstraCsv, CSVExporter dagCsv) {
        long[] dijkstraTimes = new long[RUNS];
        long[] dagTimes = new long[RUNS];
        Random rand = new Random(42);
        int source = 0;
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            G.dijkstra(source);
            dijkstraTimes[i] = System.nanoTime() - start;

            start = System.nanoTime();
            G.dagShortestPath(source);
            dagTimes[i] = System.nanoTime() - start;
        }

        double dijkstraMean = getMean(dijkstraTimes);
        double dagMean = getMean(dagTimes);
        double speedupMultiplier = dijkstraMean / dagMean;

        saveAndPrintStats("SSSP_DAG", "DAG", "Dijkstra", dijkstraTimes, V, dijkstraCsv);
        saveAndPrintStats("SSSP_DAG", "DAG", "DAG_ShortestPath", dagTimes, V, dagCsv);

        System.out.printf("[DAG Graph - V=%d] Speed-up: %.2fx\n", V, speedupMultiplier);
    }

    private static void saveAndPrintStats(String category, String topology, String algo, long[] times, int V, CSVExporter csv) {
        double meanMs = getMean(times) / 1_000_000.0;
        double medianMs = getMedian(times) / 1_000_000.0;
        double stdDevMs = getStdDev(times, getMean(times)) / 1_000_000.0;

        System.out.printf("[%s - %s (V=%d)] %s -> Mean: %.2f ms | Median: %.2f ms | StdDev: %.2f ms\n",
                category, topology, V, algo, meanMs, medianMs, stdDevMs);

        csv.addResult(V, times, meanMs, medianMs, stdDevMs);
    }

    private static double getMean(long[] times) {
        long sum = 0;
        for (long t : times) sum += t;
        return (double) sum / times.length;
    }

    private static double getMedian(long[] times) {
        long[] copy = times.clone();
        Arrays.sort(copy);
        return copy[copy.length / 2];
    }

    private static double getStdDev(long[] times, double mean) {
        double sum = 0;
        for (long t : times) sum += Math.pow(t - mean, 2);
        return Math.sqrt(sum / times.length);
    }
}