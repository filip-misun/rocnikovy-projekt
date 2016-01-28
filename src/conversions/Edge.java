package conversions;

public class Edge {
    private Vertex from;
    private Vertex to;
    private Conversion conv;
    
    public Edge(Vertex f, Vertex t, Conversion c){
        from = f;
        to = t;
        conv = c;
    }
    
    public Vertex getFrom(){
        return from;
    }
    
    public Vertex getTo(){
        return to;
    }
    
    public Conversion getConversion(){
        return conv;
    }
}
