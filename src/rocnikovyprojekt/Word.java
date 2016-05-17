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
}
