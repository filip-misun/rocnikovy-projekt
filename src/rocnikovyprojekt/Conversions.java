/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */ 
package rocnikovyprojekt;

import com.google.common.reflect.ClassPath;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dodo
 */
public class Conversions {
    Graph graph;
    
    public Conversions(){
        System.out.println("Initializing Conversions...");
        ArrayList<Class<? extends Conversion>> list = new ArrayList<>();
        try{
            ClassPath classpath = ClassPath.from(Thread.currentThread().getContextClassLoader());
            /*Naplnenie list-u triedami implementujucimi Conversion*/
            for (ClassPath.ClassInfo classInfo : classpath.getTopLevelClassesRecursive("rocnikovyprojekt")) {
                Class<?> c = Class.forName(classInfo.getName());
                if (Conversion.class.isAssignableFrom(c) && !c.equals(Conversion.class)) {
                    System.out.println("Found " + classInfo.getName());
                    list.add((Class<? extends Conversion>) c);
                }
            }
            /*Naplnenie grafu*/
            graph = new Graph();
            for (Class<? extends Conversion> c : list) {
                Conversion conv = c.newInstance();
                graph.addEdge(conv);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Initialization complete.");
    }
    /**
     * Metoda prevedie automat afrom na ekvivalentny automat typu toClass.
     * @param afrom
     * @param toClass
     * @return 
     */
    public Automaton convert(Automaton afrom, Class<? extends Automaton> toClass){
        System.out.println("Convert start...");
        /*Najde v grafe najkarsiu cestu pre prevod.*/
        List<Conversion> path =
                graph.getShortestPath(afrom.getClass(), toClass);
        /*Vykona postupne potrebne konverzie.*/
        for(Conversion conv : path){
            System.out.println("Converting from " + conv.getFrom().getSimpleName() + 
                    " to " + conv.getTo().getSimpleName() + ".");
            afrom = conv.convert(afrom);
        }
        System.out.println("Conversion complete.");
        return afrom;
    }
    
    void print(){
        System.out.println("Conversions:");
        graph.print();
    }
}
