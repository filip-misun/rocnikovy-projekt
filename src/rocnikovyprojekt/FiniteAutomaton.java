package rocnikovyprojekt;

public class FiniteAutomaton implements FiniteDescription {
	
	public static class Configuration {
		private Object state;
		private int position;
		
		public Configuration() {}
		
		public Configuration(Object state, int position) {
			this.state = state;
			this.position = position;
		}
		
		public Object getState() {
			return state;
		}
		
		public void setState(Object state) {
			this.state = state;
		}
		
		public int getPosition() {
			return position;
		}
		
		public void setPosition(int position) {
			this.position = position;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Configuration)) return false;
			Configuration conf = (Configuration) obj;
			return this.state.equals(conf.getState()) &&
					this.position == conf.getPosition();
		}
	}
	
}
