package rocnikovyprojekt;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class Word {
	
	public final static Word EMPTYWORD = new Word(){
            @Override
            public String toString(){
                return "epsilon";
            }
        };
	private List<Object> symbols = new ArrayList<>();
	
	public Word() {
		super();
	}
	
	public Word(Collection<Object> symbols) {
		this.symbols = new ArrayList<>(symbols);
	}
        
        public Word(Object[] array){
            symbols.addAll(Arrays.asList(array));
        }
        
        public Word(String s){
            for(int i = 0; i < s.length(); i++){
                symbols.add((new Character(s.charAt(i))).toString());
            }
        }
        
        public Word(Word word){
            this.symbols = new ArrayList<>(word.symbols);
        }
        
        public Word(Object ch){
            this.symbols.add(ch);
        }
	
	public Object symbolAt(int index) {
		return symbols.get(index);
	}
	
	public Word append(Object symbol) {
            Word w = new Word();
            w.symbols = new ArrayList<>(symbols);
            w.symbols.add(symbol);
            return w;
	}
        
        public Word append(Word word){
            Word w = new Word();
            w.symbols = new ArrayList<>(symbols);
            w.symbols.addAll(word.symbols);
            return w;
        }
	
	public int length() {
		return symbols.size();
	}
        
        public boolean isEmpty(){
            return symbols.isEmpty();
        }
        
        public Object last(){
            return symbols.get(symbols.size() - 1);
        }
        
        public Word pop(){
            Word w = new Word(symbols);
            w.symbols.remove(w.symbols.size() - 1);
            return w;
        }
        
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
