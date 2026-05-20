import Benchmark.Benchmark;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting CSE224 Graph Algorithms Benchmarking Suite...");

        // Trigger the evaluation benchmarks

        int[] sizes = {100, 500, 1000, 5000, 10000};
        int numberOfRuns = 5;

        Benchmark.runAllBenchmarks(sizes, numberOfRuns);
    }
}