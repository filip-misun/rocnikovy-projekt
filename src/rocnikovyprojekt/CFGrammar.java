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
    Set<Object> nonterminals = new HashSet<>();
    Set<Object> terminals = new HashSet<>();
    Set<Rule> rules;
    Object startSymbol;
    
    public CFGrammar(Set<Object> N, Set<Object> T, Set<Rule> P, Object s){
        nonterminals = N;
        terminals = T;
        rules = P;
        startSymbol = s;
    }
    
    /**
     * Reads and initializes grammar from Scanner.
     * The input format is as following:
     * - first line contains nonterminals (space-separated)
     * - second line contains terminals (space-separated)
     * - third line contains start nonterminal
     * - each next lines contains rules.
     * @param s Scanner which is read from
     */
    public CFGrammar(Scanner s){
        String line = s.nextLine();
        while(line.startsWith("//")){
            line = s.nextLine();
        }
        nonterminals.addAll(Arrays.asList(line.split(" ")));
        line = s.nextLine();
        terminals.addAll(Arrays.asList(line.split(" ")));
        line = s.nextLine();
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
                Word w = Word.EMPTYWORD;
                if(!word.equals("epsilon")){
                    w = new Word(Arrays.asList(word.split(" ")));
                }
                rules.add(new Rule(nonterm, w));
            }
        }
    }
    
    /**
     * Returns equivalent grammar which does not contain rules X -> X.
     * @return 
     */
    public CFGrammar removeXtoX(){
        Set<Rule> newRules = new HashSet<>();
        for(Rule r : rules){
            if(!(r.word.length() == 1 && r.word.symbolAt(0).equals(r.nonterminal))){
                newRules.add(r);
            }
        }
        return new CFGrammar(nonterminals, terminals, newRules, startSymbol);
    }
    
    /**
     * Creates equivalent grammar, which contains only reachable nonterminals.
     * @return 
     */
    public CFGrammar removeUnreachable(){
        Set<Object> reachable = new HashSet<>();
        reachable.add(startSymbol);
        boolean changed = true;
        while(changed){
            changed = false;
            for(Rule r : rules){
                if(reachable.contains(r.nonterminal)){
                    for(Object ch : r.word.getSymbols()){
                        if(nonterminals.contains(ch)){
                            changed = changed || reachable.add(ch);
                        }
                    }
                }
            }
        }
        System.out.println("Reachable: " + reachable);
        Set<Rule> newRules = new HashSet<>();
        for(Rule r : rules){
            boolean add = reachable.contains(r.nonterminal);
            for(Object ch : r.word.getSymbols()){
                if(!add){
                    continue;
                }
                add = add && (!nonterminals.contains(ch) || reachable.contains(ch));
            }
            if(add){
                newRules.add(r);
            }
        }
        return new CFGrammar(reachable, terminals, newRules, startSymbol);
    }
    
    /**
     * Returns equivalent grammar which does not contain nonterminals which no
     * terminal word can be reached from.
     * @return 
     */
    public CFGrammar removeNonterminating(){
        Set<Object> terminating = new HashSet<>();
        boolean changed = true;
        while(changed){
            changed = false;
            for(Rule r : rules){
                boolean add = true;
                for(Object ch : r.word.getSymbols()){
                    if(!terminating.contains(ch) && !terminals.contains(ch)){
                        add = false;
                        break;
                    }
                }
                if(add){
                    changed = changed || terminating.add(r.nonterminal);
                }
            }
        }
        System.out.println("Terminating: " + terminating);
        Set<Rule> newRules = new HashSet<>();
        for(Rule r : rules){
            boolean add = terminating.contains(r.nonterminal);
            for(Object ch : r.word.getSymbols()){
                if(!add || (!terminals.contains(ch) && !terminating.contains(ch))){
                    add = false;
                    break;
                }
            }
            if(add){
                newRules.add(r);
            }
        }
        return new CFGrammar(terminating, terminals, newRules, startSymbol);
    }
    
    /**
     * Returns equivalent grammar in reduced form.
     * @return 
     */
    public CFGrammar reduce(){
        CFGrammar g = this.removeXtoX();
        g = g.removeNonterminating();
        return g.removeUnreachable();
    }
    
    public void print(PrintStream out){
        Sets.println(nonterminals);
        Sets.println(terminals);
        out.println(startSymbol);
        for(Rule r : rules){
            out.println(r.nonterminal + " -> " + r.word);
        }
    }
    
    public static class Rule {
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
