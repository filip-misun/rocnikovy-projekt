/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tests;

import rocnikovyprojekt.*;
import conversions.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.zip.DataFormatException;

/**
 *
 * @author Dodo
 */
public class CFtests {
    public static void test1() throws DataFormatException, FileNotFoundException{
        Scanner s = new Scanner(new File("pda2.txt"));
        PushdownAutomaton a = new PushdownAutomaton(s);
        //System.out.println(a.getDelta().get("0", Word.EMPTYWORD, "c"));
        CFGrammar G = (CFGrammar) Conversions.convert(a, CFGrammar.class);
        G.print(System.out);
    }
    
    public static void test2() throws FileNotFoundException{
        CFGrammar g = new CFGrammar(new Scanner(new File("g1.txt")));
        //g.print(System.out);
        CFGrammar g1 = g.removeNonterminating();
        System.out.println("Afrer removing nonterminating:");
        g1.print(System.out);
        System.out.println("Afrer removing unreachable:");
        g1.removeUnreachable().print(System.out);
        System.out.println("Reduced:");
        g.reduce().print(System.out);
        
    }
    
    public static void test3() throws FileNotFoundException{
        CFGrammar g = new CFGrammar(new Scanner(new File("g2.txt")));
        //g.print(System.out);
        System.out.println("Chomsky form:");
        CFGrammar g1 = g.chomsky();
        g1.print(System.out);
        System.out.println("Strict Chomsky form:");
        CFGrammar g2 = g.strictChomsky();
        g2.print(System.out);
    }
    
    public static void test4() throws FileNotFoundException{
        CFGrammar g = new CFGrammar(new Scanner(new File("g3.txt")));
        //g.print(System.out);
        System.out.println("Epsilon-free grammar:");
        g = g.epsilonFree();
        g.print(System.out);
    }
    
    public static void test5() throws FileNotFoundException{
        CFGrammar g = new CFGrammar(new Scanner(new File("g4.txt")));
        //g.print(System.out);
        System.out.println("Grammar without chain rules:");
        g = g.removeChainRules();
        g.print(System.out);
    }
    
    public static void test6() throws FileNotFoundException{
        CFGrammar g = new CFGrammar(new Scanner(new File("cfg5.txt")));
        assert g.accepts(new Word("aabb")) == true; //true
        assert g.accepts(new Word("aabbb")) == false;
        assert g.accepts(new Word(Word.EMPTYWORD)) == true; //true
        assert g.accepts(new Word("ababab")) == false;
        assert g.accepts(new Word("aaaaaaaaaabbbbbbbbbb")) == true; //true
        System.out.println("Test finished.");
    }
    
    public static void test7() throws FileNotFoundException{
        CFGrammar g = new CFGrammar(new Scanner(new File("cfg1.txt")));
        g.strictChomsky().print(System.out);
        System.out.println(g.accepts(new Word("((123+22)*2)"))); //true
        System.out.println(g.accepts(new Word("(1+1)"))); //true
        System.out.println(g.accepts(new Word(Word.EMPTYWORD)));
        System.out.println(g.accepts(new Word("1221201223221312"))); //true
        System.out.println(g.accepts(new Word("(12+(05*62))")));
        System.out.println(g.accepts(new Word("((23+(10*10))*(1+(2+3302)))"))); //true
        System.out.println("Test finished.");
    }
}
