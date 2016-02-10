package rocnikovyprojekt;

import java.util.ArrayList;
import java.util.List;

public class Word {
	
	final static Object EMPTYWORD = new Object();
	private List<Object> symbols = new ArrayList<>();
	
	public Word() {
		super();
	}
	
	public Word(List<Object> symbols) {
		this.symbols = symbols;
	}
        
        public Word(String s){
            for(int i = 0; i < s.length(); i++){
                symbols.add(new Character(s.charAt(i)));
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
}
