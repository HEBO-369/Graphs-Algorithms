package Benchmark;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class CSVExporter {
    private FileWriter fileWriter;
    private PrintWriter printWriter;

    public CSVExporter(String fileName, int runs) {
        try {
            fileWriter = new FileWriter(fileName);
            printWriter = new PrintWriter(fileWriter);

            StringBuilder header = new StringBuilder("Graph_Size");
            for (int i = 1; i <= runs; i++) {
                header.append(",Run_").append(i).append("(ms)");
            }
            header.append(",Mean(ms),Median(ms),StdDev(ms)");

            printWriter.println(header.toString());
        } catch (IOException e) {
            System.err.println("Error initializing CSV file: " + e.getMessage());
        }
    }

    public void addResult(int size, long[] times, double mean, double median, double stdDev) {
        if (printWriter != null) {
            StringBuilder row = new StringBuilder();
            row.append(size);

            for (long t : times) {
                row.append(String.format(",%.4f", t / 1_000_000.0));
            }
            row.append(String.format(",%.4f,%.4f,%.4f", mean, median, stdDev));

            printWriter.println(row.toString());
            printWriter.flush();
        }
    }

    public void close() {
        try {
            if (printWriter != null) printWriter.close();
            if (fileWriter != null) fileWriter.close();
        } catch (IOException e) {
            System.err.println("Error closing CSV file: " + e.getMessage());
        }
    }
}