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
public interface Automaton {
}

/*Zopar tried na ilustraciu fungovania:*/

class A1 implements Automaton{
    int a;
    int b;
    
    public A1(int a_, int b_){
        a = a_;
        b = b_;
    }
}

class A2 implements Automaton{
    int x;
    
    public A2(int x_){
        x = x_;
    }
}

class A3 implements Automaton{
    String s;
    
    public A3(String s_){
        s = s_;
    }
}

class A4 implements Automaton{
    String s;
    int a;
    
    public A4(String s_, int a_){
        s = s_;
        a = a_;
    }
}