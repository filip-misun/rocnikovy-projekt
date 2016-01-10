/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocnikovyprojekt;

/**
 *
 * @author Dodo
 */
public class Edge {
    Vertex from;
    Vertex to;
    Conversion conv;
    
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
