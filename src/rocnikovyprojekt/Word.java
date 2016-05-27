package rocnikovyprojekt;

import java.util.ArrayList;
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
	
	public Word(List<Object> symbols) {
		this.symbols = symbols;
	}
        
        public Word(String s){
            for(int i = 0; i < s.length(); i++){
                symbols.add((new Character(s.charAt(i))).toString());
            }
        }
	
	public Object symbolAt(int index) {
		return symbols.get(index);
	}
	
	public void append(Object symbol) {
		symbols.add(symbol);
	}
	
	public int length() {
		return symbols.size();
	}
        
        public boolean isEmpty(){
            return symbols.isEmpty();
        }
        
        public Object pop(){
            return symbols.remove(symbols.size() - 1);
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
}
