/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocnikovyprojekt;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Jozef Rajnik
 */
public class CFGrammar implements FiniteDescription{
    Set<Object> nonterminals = null;
    Set<Object> terminals = null;
    Set<Rule> rules;
    Object startSymbol;
    
    public CFGrammar(Set<Object> N, Set<Object> T, Set<Rule> P, Object s){
        nonterminals = N;
        terminals = T;
        rules = P;
        startSymbol = s;
    }
    
    public CFGrammar(Set<Rule> P, Object s){
        rules = P;
        startSymbol = s;
    }
    
    /**
     * Reads and initializes grammar from Scanner.
     * The input format is as following:
     * - first line contains start symbol
     * - each next lines contains rules.
     * @param s Scanner which is read from
     */
    public CFGrammar(Scanner s){
        String line = null;
        while(s.hasNext()){
            line = s.nextLine();
            if(line.length() < 2 || !line.substring(0,2).equals("//")){
                break;
            }
        }
        startSymbol = line;
        rules = new HashSet<>();
        while(s.hasNext()){
            line = s.nextLine();
            String[] args = line.split(" -> ");
            if(args.length < 2){
                continue;
            }
            Object nonterm = args[0];
            for(String word : args[1].split(" \\| ")){
                rules.add(new Rule(nonterm, new Word(Arrays.asList(word.split(" ")))));
            }
        }
    }
    
    public void print(PrintStream out){
        out.println(startSymbol);
        for(Rule r : rules){
            out.println(r.nonterminal + " -> " + r.word);
        }
    }
    
    public class Rule {
        Object nonterminal;
        Word word;
        
        public Rule(Object n, Word w){
            nonterminal = n;
            word = w;
        }
        
        @Override
        public boolean equals(Object o){
            if(!(o instanceof Rule)){
                return false;
            }
            Rule r = (Rule) o;
            return this.nonterminal == r.nonterminal && this.word == r.word;
        }
        
        @Override
        public int hashCode(){
            return Objects.hash(nonterminal, word);
        }
    }
    
}
