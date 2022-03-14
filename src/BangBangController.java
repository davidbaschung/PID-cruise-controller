/**
 * @author 17-571-167
 *
 * Bang-Bang controller based on the abstract cruise controller model.
 * This version correct physics disturbances and uses a standard acceleration window, that is used in the others cruise
 * controllers of this package.
 */
public class BangBangController extends AbstractCruiseController {

	public BangBangController(CarSimulator carSimulator, GUI gui) {
		super(carSimulator, gui);
	}

	/**
	 * computes the acceleration required for the simulator, using a Bang-Bang controller
	 * @return
	 */
	public double computeAcceleration() {
		double vc = carSimulator.getSpeed();
		double e = goalSpeed - vc, R, ea = e / DT;
		if (ea>1) R = 100; else if (ea<-1) R = -100; else R = 0;													// The Bang-Bang uses immediate acceleration / deceleration
		double eaPhysicsCompensation = - ( - Math.signum(carSimulator.getSpeed()) * 0.001d * Math.pow(carSimulator.getSpeed(), 2) - 9.81d * Math.sin(Math.atan(carSimulator.currentSlope / 100.0d)) );
		WINDOW_ACCEL += WINDOW_DELTA * ((R>WINDOW_ACCEL+eaPhysicsCompensation || R<-WINDOW_ACCEL+eaPhysicsCompensation)?1:-1); // It also uses the window too, for smooth acceleration
		if (R>0) return WINDOW_ACCEL+eaPhysicsCompensation;
		if (R<0) return -WINDOW_ACCEL+eaPhysicsCompensation;
		return eaPhysicsCompensation;
	}
}
