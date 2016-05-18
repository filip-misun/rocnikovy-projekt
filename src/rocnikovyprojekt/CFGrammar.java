/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rocnikovyprojekt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jozef Rajnik
 */
public class CFGrammar {
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
    
    public class Rule {
        Object nonterminal;
        Word word;
        
        public Rule(Object n, Word w){
            nonterminal = n;
            word = w;
        }
    }
    
}
