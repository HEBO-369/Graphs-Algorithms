class UF {
    private int[] parent;
    private int[] rank;

    public UF(int N) {
        parent = new int[N];
        rank = new int[N];
        for (int i = 0; i < N; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }
    public int find(int i) {
        if (parent[i] == i)
            return i;
        return parent[i] = find(parent[i]);
    }

    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    public void union(int p, int q) {
        int rootP = find(p);
        int rootY = find(q);
        if (rootP != rootY) {
            if (rank[rootP] < rank[rootY]) {
                parent[rootP] = rootY;
            } else if (rank[rootP] > rank[rootY]) {
                parent[rootY] = rootP;
            } else {
                parent[rootY] = rootP;
                rank[rootP]++;
            }
        }
    }
}