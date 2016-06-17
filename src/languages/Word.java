package languages;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Class representing words in formal languages.
 * A word can conaints of any objects as symbols. This class is build persistent,
 * so all methods called on a word does not modify it, but return new word.
 */
public class Word {
	
    /**
     * Static field for empty word -- epsilon.
     */
	public final static Word EPSILON = new Word(){
            @Override
            public String toString(){
                return "epsilon";
            }
        };
        /** List of symbols in word */
	private List<Object> symbols = new ArrayList<>();
	
        /**
         * Initializes empty word.
         */
	public Word() {
		super();
	}
	
        /**
         * Initializes new word consisting of symbols in given Collection.
         */
	public Word(Collection<Object> symbols) {
		this.symbols = new ArrayList<>(symbols);
	}
        
        /**
         * Initializes new word consisting of symbols in given array of Objects.
         * @param array 
         */
        public Word(Object[] array){
            symbols.addAll(Arrays.asList(array));
        }
        
        /**
         * Initializes new word consisting of characters in given string. 
         */
        public Word(String s){
            for(int i = 0; i < s.length(); i++){
                symbols.add((new Character(s.charAt(i))).toString());
            }
        }
        
        /**
         * Initializes word as a copy of another word.
         */
        public Word(Word word){
            this.symbols = new ArrayList<>(word.symbols);
        }
        
        /**
         * Initializes word consisting of one symbol.
         * @param ch 
         */
        public Word(Object ch){
            this.symbols.add(ch);
        }
	
        /**
         * Returns symbol in word at given position. 
         */
	public Object symbolAt(int index) {
		return symbols.get(index);
	}
	
        /**
         * Append given symbol to the end of this word and returns modified
         * word.
         */
	public Word append(Object symbol) {
            Word w = new Word();
            w.symbols = new ArrayList<>(symbols);
            w.symbols.add(symbol);
            return w;
	}
        
        /**
         * Append given word to the end of this word and returns modified
         * word.
         */
        public Word append(Word word){
            Word w = new Word();
            w.symbols = new ArrayList<>(symbols);
            w.symbols.addAll(word.symbols);
            return w;
        }
	
        /**
         * Returns length of this word.
         */
	public int length() {
		return symbols.size();
	}
        
        /**
         * Returns whether this word is empty.
         */
        public boolean isEmpty(){
            return symbols.isEmpty();
        }
        
        /**
         * Returns last symbol of this word.
         */
        public Object last(){
            return symbols.get(symbols.size() - 1);
        }
        
        /**
         * Returns word without last symbol.
         */
        public Word pop(){
            if(this.length() == 0){
                return this;
            }
            Word w = new Word(symbols);
            w.symbols.remove(w.symbols.size() - 1);
            return w;
        }
        
        /**
         * Returns ArrayList of symbols in this word.
         */
        public ArrayList<Object> getSymbols(){
            return new ArrayList<>(symbols);
        }
        
        /**
         * Returns word with replaced character at position index with Word w.
         * Note that this method does not change this word.
         * @param index index of character to be replaced
         * @param w word which the character is replaced with 
         * @return word with replaced character at position index with Word w
         */
        public Word replace(int index, Word w){
            List<Object> newWord = new ArrayList<>(w.length() + this.length());
            newWord.addAll(symbols.subList(0, index));
            newWord.addAll(w.symbols);
            newWord.addAll(symbols.subList(index + 1, symbols.size()));
            return new Word(newWord);
        }
        
        /**
         * Returns reverse of this word.
         */
        public Word reverse(){
            return new Word(Lists.reverse(symbols));
        }
        
        @Override
        public String toString(){
            if(symbols.isEmpty()){
                return "epsilon";
            }
            StringBuilder str = new StringBuilder();
            boolean space = false;
            for(Object s : symbols){
                if(space){
                    str.append(" ");
                }
                str.append(s);
                space = true;
            }
            return str.toString();
        }
        
        @Override
        public boolean equals(Object o){
            if(o instanceof Word){
                return symbols.equals(((Word) o).symbols);
            } else {
                return false;
            }
        }
        
        @Override
        public int hashCode(){
            return symbols.hashCode();
        }
}
