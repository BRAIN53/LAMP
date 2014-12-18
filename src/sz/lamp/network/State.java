package sz.lamp.network;

public class State {
	public int hue;
	public boolean on;
	public int bri;
	public int sat;
	public State(boolean on, int hue, int bri, int sat) {
		super();
		this.on = on;
		this.hue = hue;
		this.bri = bri;
		this.sat = sat;
	}
	
	
}
