package rocnikovyprojekt;

import java.util.List;

public class PushdownAutomaton {
	
	public static class Configuration {
		private Object state;
		private List<Object> stack;
		private int position;
		
		public Configuration() {}
		
		public Configuration(Object state, List<Object> stack, int position) {
			this.state = state;
			this.stack = stack;
			this.position = position;
		}
		
		public Object getState() {
			return this.state;
		}
		
		public void setState(Object state) {
			this.state = state;
		}
		
		public List<Object> getStack() {
			return this.stack;
		}
		
		public void setStack(List<Object> stack) {
			this.stack = stack;
		}
		
		public int getPosition() {
			return this.position;
		}
		
		public void setPosition(int position) {
			this.position = position;
		}
	}
	
}
