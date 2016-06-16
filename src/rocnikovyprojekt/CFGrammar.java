package rocnikovyprojekt;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
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
    Map<Object, Set<Word>> rulesMap = null;
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
    
    /**
     * Returns equivalent grammar without rules X -> epsilon. 
     * @return 
     */
    public CFGrammar epsilonFree(){
        Set<Object> nullable = new HashSet<>();
        boolean changed = true;
        while(changed){
            changed = false;
            for(Rule r : rules){
                boolean add = true;
                for(Object ch : r.word.getSymbols()){
                    if(!nullable.contains(ch)){
                        add = false;
                        break;
                    }
                }
                if(add){
                    changed = changed || nullable.add(r.nonterminal);
                }
            }
        }
        Set<Rule> newRules = new HashSet<>();
        for(Rule r : rules){
            Word w = r.word;
            if(w.isEmpty()){
                continue;
            }
            ArrayList<Boolean> omit = new ArrayList<>();
            int count = 0;
            for(int i = 0; i < w.length(); i++){
                if(nullable.contains(w.symbolAt(i))){
                    omit.add(true);
                    count++;
                } else {
                    omit.add(false);
                }
            }
            for(int mask = 0; mask < (1 << count); mask++){
                int m = mask;
                ArrayList<Object> newWord = new ArrayList<>();
                for(int i = 0; i < w.length(); i++){
                    if(omit.get(i)){
                        if(m % 2 == 0){
                            newWord.add(w.symbolAt(i));
                        }
                        m = m >> 1;
                    } else {
                        newWord.add(w.symbolAt(i));
                    }
                }
                newRules.add(new Rule(r.nonterminal, new Word(newWord)));
            }
        }
        /* We remove epsilon-rules. */
        for (Iterator<Rule> it = newRules.iterator(); it.hasNext();) {
            Rule r = it.next();
            if(r.word.isEmpty()){
                it.remove();
            }
        }
        /* For equivalence with formrer grammar, we add rule
         * startSymbol -> epsilon, if necesarry. */
        if(nullable.contains(startSymbol)){
            newRules.add(new Rule(startSymbol, Word.EMPTYWORD));
        }
        return new CFGrammar(nonterminals, terminals, newRules, startSymbol);
    }
    
    /**
     * Returns equivalent grammar in Chomsky normal form.
     * The right sides of the rules of the returned grammar consist of
     * two nonterminals, one terminal or epsilon.
     * @return 
     */
    public CFGrammar chomsky(){
        Set<Object> N = new HashSet<>(nonterminals);
        Set<Rule> newRules = new HashSet<>();
        /* For each terminal a, we create nonterminal (-1,a).
         * For these nonterminals, we create rules (-1,a) -> a. */
        
        
        for(Rule r : rules){
            /* Each occurence of a we replace with (-1,a). */
            ArrayList<Object> symbols = r.word.getSymbols();
            for(int i = 0; i < symbols.size(); i++){
                if(terminals.contains(symbols.get(i))){
                    symbols.set(i, new Tuple(-1, symbols.get(i)));
                }
            }
            /* To too short rules we add one nonterminal. */
            if(symbols.size() == 1){
                symbols.add(new Tuple(-1, Word.EMPTYWORD));
            }
            newRules.add(new Rule(r.nonterminal, new Word(symbols)));
        }
        /* We number all long rules. */
        ArrayList<Rule> longRules = new ArrayList<>();
        for(Rule r : newRules){
            if(r.word.length() > 2){
                longRules.add(r);
            }
        }
        /* Each long rule we divide into several smaller rules. */
        for(int i = 0; i < longRules.size(); i++){
            Word w = longRules.get(i).word;
            Object prev = longRules.get(i).nonterminal;
            for(int j = 0; j < w.length() - 2; j++){
                newRules.add(new Rule(prev,
                        new Word(Arrays.asList(w.symbolAt(j), new Tuple(i,j)))));
                prev = new Tuple(i,j);
                N.add(prev);
            }
            Object[] end = {w.symbolAt(w.length() - 2), w.symbolAt(w.length() - 1)}; 
            newRules.add(new Rule(prev, new Word(end)));
            newRules.remove(longRules.get(i));
        }
        /* We add new nonterminals (-1,a) to the set of nonterminals and 
           we add new rules from this nonterminals. */
        for(Object ch : terminals){
            Object nonterm = new Tuple(-1, ch);
            N.add(nonterm);
            newRules.add(new Rule(new Tuple(-1, ch), new Word(ch)));
        }
        /* Same for (-1,epsilon) */
        Object nonterm = new Tuple(-1, Word.EMPTYWORD);
        N.add(nonterm);
        newRules.add(new Rule(new Tuple(-1, Word.EMPTYWORD), Word.EMPTYWORD));
        return new CFGrammar(N, terminals, newRules, startSymbol);
    }
    
    /**
     * Returns equivalent grammar in strict Chomsky normal form.
     * The rules of returned grammar are subset of N x (NN u T).
     * First it converts given grammar to chomsky normal form and then
     * it removes epsilon-rules and chain rules.
     * @return 
     */
    public CFGrammar strictChomsky(){
        CFGrammar g = this.chomsky();
        g = g.epsilonFree();
        g = g.removeChainRules();
        return g;
    }
    
    /**
     * This method decides, if given rule is a chain rule.
     * @param r
     * @return 
     */
    private boolean isChainRule(Rule r){
        return r.word.length() == 1 && nonterminals.contains(r.word.symbolAt(0));
    }
    
    /**
     * Returns equivalent grammar without chainrules, i. e. X -> Y.
     * @return 
     */
    public CFGrammar removeChainRules(){
        /* We represent nonterminals as verticies of a graph. Oriented edges
         * represent chain rules. */
        HashMap<Object, ArrayList<Object>> adj = new HashMap<>();
        for(Rule r : rules){
            if(isChainRule(r)){
                ArrayList<Object> a = adj.remove(r.nonterminal);
                if(a == null){
                    a = new ArrayList<>();
                }
                a.add(r.word.symbolAt(0));
                adj.put(r.nonterminal, a);
            }
        }
        /* For each nonterminal n, we determine set of nonterminals
         * which are form n reachable. */
        HashMap<Object, Set<Object>> reachable = new HashMap<>();
        for(Object n : nonterminals){
            Set<Object> visited = new HashSet<>();
            LinkedList<Object> queue = new LinkedList<>();
            queue.addLast(n);
            while(!queue.isEmpty()){
                Object current = queue.removeFirst();
                visited.add(current);
                for(Object neighb : adj.getOrDefault(current, new ArrayList<>())){
                    if(!visited.contains(neighb)){
                        queue.addLast(neighb);
                    }
                }
            }
            reachable.put(n, visited);
        }
        Set<Rule> newRules = new HashSet<>();
        for(Rule r : rules){
            if(isChainRule(r)){
                for(Rule s : rules){
                    if(reachable.get(r.word.symbolAt(0)).contains(s.nonterminal)
                            && !isChainRule(s)){
                        newRules.add(new Rule(r.nonterminal, s.word));
                    }
                }
            } else {
                newRules.add(r);
            }
        }
        return new CFGrammar(nonterminals, terminals, newRules, startSymbol);
    }
    
    /**
     * This method decide if given word belongs to the language generated by
     * this grammar.
     * It converts grammar to strict Chomsky normal form and then uses
     * CYK algorithm.
     * @param w
     * @return 
     */
    public boolean accepts(Word w){
        CFGrammar g = this.strictChomsky();
        if(w.isEmpty()){
            return g.rules.contains(new Rule(g.startSymbol, Word.EMPTYWORD));
        }
        int len = w.length();
        ArrayList<ArrayList<Set<Object>>> N = new ArrayList<>();
        for(int i = 0; i < w.length(); i++){
            N.add(new ArrayList<>());
            for(int j = 0; j < w.length(); j++){
                N.get(i).add(new HashSet<>());
            }
        }
        for(Rule r : g.rules){
            if(r.word.length() == 1){
                for(int i = 0; i < w.length(); i++){
                    if(w.symbolAt(i).equals(r.word.symbolAt(0))){
                        N.get(i).get(i).add(r.nonterminal);
                    }
                }
            }
        }
        for(int diff = 1; diff < w.length(); diff++){
            for(int i = 0; i + diff < w.length(); i++){
                for(int k = i; k < i + diff; k++){
                    for(Rule r : g.rules){
                        if(r.word.length() != 2){
                            continue;
                        }
                        if(N.get(i).get(k).contains(r.word.symbolAt(0)) &&
                                N.get(k+1).get(i+diff).contains(r.word.symbolAt(1))){
                            N.get(i).get(i + diff).add(r.nonterminal);
                        }
                    }
                }
            }
        }
        return N.get(0).get(w.length()-1).contains(g.startSymbol);
    }
    
    /* Groups the rules by nonterminals. */
    private void groupRules(){
        rulesMap = new HashMap<>();
        for(Rule r : rules){
            Set<Word> val = rulesMap.remove(r.nonterminal);
            if(val == null){
                val = new HashSet<>();
            }
            val.add(r.word);
            rulesMap.put(r.nonterminal, val);
        }
    }
    
    /**
     * Prints text representation of this Grammar to specified PrintStream.
     * @param out 
     */
    public void print(PrintStream out){
        groupRules();
        out.println(Sets.toString(nonterminals));
        out.println(Sets.toString(terminals));
        out.println(startSymbol);
        for(Object n : nonterminals){
            out.print(n + " -> ");
            boolean space = false;
            for(Word w : rulesMap.getOrDefault(n, new HashSet<>())){
                if(space) out.print(" | ");
                out.print(w);
                space = true;
            }
            out.println();
        }
    }
    
    /**
     * Class representing one rule in context-free grammar.
     * It consist of nonterminal on the right side and word of nonterminals
     * and terminals on the left side.
     */
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
            return this.nonterminal.equals(r.nonterminal) && this.word.equals(r.word);
        }
        
        @Override
        public int hashCode(){
            return Objects.hash(nonterminal, word);
        }
        
        @Override
        public String toString(){
            return nonterminal + " -> " + word;
        }
    }
    
}
