package Algor;

import Data.Edge;
import Data.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstPaths {
    private int[] edgeTo ;
    private int[] distTo ;
    private boolean[] marked ;
    private Queue<Integer> queue ;
    private int s ;

    public BreadthFirstPaths(Graph G , int s){
        marked = new boolean[G.V()];
        edgeTo = new int[G.V()];
        distTo = new int[G.V()];
        this.s = s;
        queue = new LinkedList<>();
        bfs(G , s );
    }
    private void bfs(Graph G , int s){
        queue.add(s);
        marked[s] = true;
        distTo[s] = 0;
        while(!queue.isEmpty()){
            int v = queue.poll();
            for (Edge w : G.adj(v)){
                if(!marked[w.other(v)]){
                    queue.add(w.other(v));
                    edgeTo[w.other(v)] = v;
                    distTo[w.other(v)] = distTo[v] + 1;
                    marked[w.other(v)] = true;
                }
            }
        }
    }
    public int distTo(int v) {
        return distTo[v];
    }
    public boolean hasPathTo(int v) {
        return marked[v];
    }
    public ArrayList<Integer> pathTo(int v) {
        ArrayList<Integer> path = new ArrayList<Integer>();
        if(!hasPathTo(v)){ return null ;}
        else{
            for (int x = v ; distTo[x] != 0 ; x = edgeTo[x]){
                path.add(x);
            }
            path.add(s);
            Collections.reverse(path);
            return path;
        }
    }


}
