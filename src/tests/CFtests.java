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
}
