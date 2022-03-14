/**
 * @author 17-571-167
 *
 * Abstract model for a Cruise Controller. Each Controller must implement the run method that creates a new thread.
 * There is also the possibility to create an acceleration window
 */
public abstract class AbstractCruiseController implements CruiseController, Observer {

	final CarSimulator carSimulator;
	final GUI gui;
	public double goalSpeed = 0;																						  // the inner goalSpeed can be updated, as this class is an observer
	long t0Graph, t0Label, t1;																							  // timers to adjust the frequency of refreshing for graphical elements

	public AbstractCruiseController(CarSimulator carSimulator, GUI gui) {
		this.carSimulator = carSimulator;
		this.gui = gui;
		gui.attach(this);																							  // Each Controller is an Observer, updating the goal speed
		this.run();
	}

	/**
	 * running process for any controller. It is launched on instanciation of the class
	 */
	private void run() {
		Thread t = new Thread(() -> {
			t0Graph = t0Label = System.nanoTime();
			while (true) {                                                                                                // Main loop to compute acceleration and labeling in GUI
				try {
					carSimulator.setAcceleration(computeAcceleration());
					t1 = System.nanoTime();
					if ((t1 - t0Graph) * Math.pow(10, -9) > 1.0d / 10) {
						gui.putInGraph(this, carSimulator.getSpeed());												  // updating the graph in the Observer panel
						t0Graph = t1;
					}
					if ((t1 - t0Label) * Math.pow(10, -9) > 1.0d / 2) {
						gui.setLabel(this, carSimulator.getSpeed());												  // updating the speed text label in the Observer panel
						t0Label = t1;
					}
					Thread.sleep((long) (1 / DT));																		  // interruption at 100 Hertz
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
	}

	/**
	 * Update, as an Observer of the observer pattern
	 */
	public void update() {
		goalSpeed = gui.getGoalSpeed();
	}

	static final double DT = 0.01d, MAX_ACCEL = 10, MIN_ACCEL = 1, MAX_WINDOW_TIME = 5;                                   // Acceleration window variables. An "Acceleration window" will allow to accelerate smoothly
	static double WINDOW_ACCEL = MIN_ACCEL, WINDOW_DELTA = (MAX_ACCEL - MIN_ACCEL) * DT / MAX_WINDOW_TIME;  			  // Acceleration window' leading variable.

	/***
	 * Factory method that each concrete cruise controller must implement
	 * @return
	 */
	public abstract double computeAcceleration();
}