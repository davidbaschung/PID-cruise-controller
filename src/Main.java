/**
 * @author 17-571-167
 *
 * Start Class for a Cruise-Control simulator using a PID and a Bang-Bang Controller
 */
public class Main {

	public static void main(String[] args) {
		CarSimulator myCar = new CarSimulator();										// new simulator threads
		Thread myThread = new Thread(myCar);
		myThread.start();
		CarSimulator myCar2 = new CarSimulator();
		Thread myThread2 = new Thread(myCar2);
		myThread2.start();

		GUI gui = new GUI();															// new GUI

		PIDController pidController = new PIDController(myCar, gui);					// new Controllers depending on the simulators, they will modify the GUI
		BangBangController bangBangController = new BangBangController(myCar2, gui);

	}

}
