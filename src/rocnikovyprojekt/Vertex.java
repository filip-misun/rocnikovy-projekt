/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocnikovyprojekt;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Dodo
 */
public class Vertex {
    Class<? extends Automaton> data;
    List<Edge> edges;
    
    public Vertex(Class<? extends Automaton> d){
        data = d;
        edges = new LinkedList<>();
    }
    
    public Class<? extends Automaton> getData(){
        return data;
    }
    
    public void addEdge(Vertex to, Conversion conv){
        edges.add(new Edge(this, to, conv));
    }
    
    public List<Edge> getOutcommingEdges(){
        return edges;
    }
}
