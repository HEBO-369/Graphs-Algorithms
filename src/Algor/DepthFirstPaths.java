package Algor;

import Data.Edge;
import Data.Graph;

import java.util.ArrayList;
import java.util.Collections;

public class DepthFirstPaths {
    private boolean[] marked ;
    private int[] edgeTo ;
    private int s ;

    public DepthFirstPaths(Graph G, int s) {
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        this.s = s;
        dfs(G,s);
    }
    private void dfs(Graph G, int v) {
        marked[v] = true;
        for (Edge w : G.adj(v)) {
            if(!marked[w.other(v)]){
                edgeTo[w.other(v)] = v ;
                dfs(G,w.other(v));
            }
        }
    }
    public boolean hasPathTo(int v) {
        return marked[v];
    }
    public ArrayList<Integer> pathTo(int v) {
        ArrayList<Integer> path = new ArrayList<Integer>();
        if(!hasPathTo(v)){ return null ;}
        else{
            for (int x = v ; x!=s ; x =edgeTo[x]){
                path.add(x);
            }
            path.add(s);
            Collections.reverse(path);
            return path;
        }
    }
}
