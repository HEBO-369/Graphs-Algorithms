package Data;

public class Edge implements Comparable<Edge> {
        private final int v, w;
    private final int weight;
    private final boolean directed;

    public Edge(int v, int w, int weight, boolean directed)
        {
            this.v = v;
            this.w = w;
            this.weight = weight;
            this.directed = directed;
        }
        public int either()
        { return v; }
        public int other(int vertex)
        {
            if (vertex == v) return w;
            else return v;
        }

    public int weight() {
        return weight;
    }
        public int compareTo(Edge that)
        {
            if (this.weight < that.weight) return -1;
            else if (this.weight > that.weight) return +1;
            else return 0;
        }

}
