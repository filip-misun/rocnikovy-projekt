package conversions;

import java.util.ArrayList;
import java.util.List;

import rocnikovyprojekt.FiniteDescription;

public class Vertex {
    private Class<? extends FiniteDescription> data;
    private List<Edge> edges = new ArrayList<>();
    
    public Vertex(Class<? extends FiniteDescription> d){
        data = d;
    }
    
    public Class<? extends FiniteDescription> getData(){
        return data;
    }
    
    public void addEdge(Vertex to, Conversion conv) {
    	edges.add(new Edge(this, to, conv));
    }
    
    public List<Edge> getOutcommingEdges(){
        return edges;
    }
}
