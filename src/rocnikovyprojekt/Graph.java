/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocnikovyprojekt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author Dodo
 */
public class Graph {
    private Map<Class<? extends Automaton>, Vertex> map;
    
    public Graph(){
        map = new HashMap<>();
    }
    
    public void addEdge(Conversion conv){
        Class<? extends Automaton> afrom = conv.getFrom();
        Class<? extends Automaton> ato = conv.getTo();
        if(!map.containsKey(afrom)){
            map.put(afrom, new Vertex(afrom));
        }
        if(!map.containsKey(ato)){
            map.put(ato, new Vertex(ato));
        }
        map.get(afrom).addEdge(map.get(ato), conv);
    }
    
    public List<Conversion> getShortestPath(Class<? extends Automaton> afrom, Class<? extends Automaton> ato){
        Vertex from = map.get(afrom);
        Vertex to = map.get(ato);
        Map<Vertex, Edge> prev = new HashMap<>();
        prev.put(from, null);
        LinkedList<Vertex> queue = new LinkedList<>();
        queue.add(from);
        while(!queue.isEmpty()){
            Vertex v = queue.pollFirst();
            for(Edge e : v.getOutcommingEdges()){
                if(!prev.containsKey(e.getTo())){
                    prev.put(e.getTo(), e);
                    queue.addLast(e.getTo());
                    if(e.getTo().equals(to)){
                        break;
                    }
                }
            }
        }
        LinkedList<Conversion> path = new LinkedList<>();
        while(!from.equals(to)){
            Edge e = prev.get(to);
            path.addFirst(e.getConversion());
            to = e.getFrom();
        }
        return path;
    }
    
    void print(){
        for(Entry<Class<? extends Automaton>, Vertex> entry : map.entrySet()){
            Vertex v = entry.getValue();
            System.out.print(entry.getKey().getName() + ": ");
            for(Edge e : v.getOutcommingEdges()){
                System.out.print(e.conv.getTo().getName() + ",");
            }
            System.out.println();
        }
    }
}
