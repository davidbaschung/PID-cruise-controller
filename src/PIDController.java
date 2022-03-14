import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author 17-571-167
 *
 * PID controller based on the abstract cruise controller model.
 * This version correct physics disturbances and uses a standard acceleration window, that is used in the others cruise
 * controllers of this package.
 */
public class PIDController extends AbstractCruiseController {

	// Timer variables
	private static final double cP = 100, cI = 19, cD = 34;																// PID values
	private static ErrorsAcceleration<Double> errorsAcceleration = new ErrorsAcceleration<>(0.1d);   		// Acceleration errors are stored for the integral and the derivative

	public PIDController(CarSimulator carSimulator, GUI gui) {
		super(carSimulator, gui);
	}


	/**
	 * computes the acceleration required for the simulator, using a PID controller
	 * @return
	 */
	@Override
	public double computeAcceleration() {
		double currentSpeed = carSimulator.getSpeed();
		double error = goalSpeed - currentSpeed, errorAcceleration = error;												// Acceleration error, for PID

		if (errorsAcceleration.size() > errorsAcceleration.optimalSeconds/ DT) {										// Acceleration errors are stored in a FIFO queue
			errorsAcceleration.pop();																					// Too old errors are removed from the queue
		}
		errorsAcceleration.add(errorAcceleration);

		double	reaction = ( cP*errorAcceleration + cI*errorsAcceleration.integrate(DT) + cD*errorsAcceleration.derivate(DT) ) / (cP+cI+cD), // Computes the reaction to accelerate the car, based on the PID model
				eaPhysicsCompensation = - ( - Math.signum(carSimulator.getSpeed()) * 0.001d * Math.pow(carSimulator.getSpeed(), 2) - 9.81d * Math.sin(Math.atan(carSimulator.currentSlope / 100.0d)) ); // Physics disadvantages must be taken into account to have the right maximal acceleration or deceleration
		WINDOW_ACCEL += WINDOW_DELTA * ((reaction>WINDOW_ACCEL+eaPhysicsCompensation || reaction<-WINDOW_ACCEL+eaPhysicsCompensation)?1:-1); // The acceleration window grows if there's a required acceleration, otherwise it shrinks
		if (reaction>WINDOW_ACCEL+eaPhysicsCompensation) return WINDOW_ACCEL+eaPhysicsCompensation;						// If acceleration too big, accelerate at max of the window
		if (reaction<-WINDOW_ACCEL+eaPhysicsCompensation) return -WINDOW_ACCEL+eaPhysicsCompensation;					// If deceleration too low, decelerate at max of the window (max for positive or negative as min=0)
		return reaction+eaPhysicsCompensation;																		    // If acceleration OK, use the value given by the PID.
	}

	/**
	 * Acceleration errors are stored for the integral and the derivative.
	 * the integral computes the weighted sum of each previous acceleration
	 * the derivative gives the slope from firsts elements to the last ones
	 * @param <T>
	 */
	private static class ErrorsAcceleration<T extends Double> extends LinkedList<T> {
		double optimalSeconds;

		public ErrorsAcceleration(double optimalSeconds) {
			this.optimalSeconds = optimalSeconds;
		}

		/**
		 * Sums all the errors stored in the linkedList
		 * @param dt
		 * @return
		 */
		double integrate(double dt) {
			double sum=0;
			Iterator<T> iterator = iterator();
			while (iterator.hasNext()) {
				sum += iterator.next().doubleValue() * dt;																// sums the acceleration errors, weighted by delta-time
			}
			return sum;
		}

		/**
		 * Simple derivative for the smallest possible delta-time (and so delta-element) from the last element
		 * @param dt
		 * @return
		 */
		double derivate(double dt) {
			if (size()>= 3) {
				double avgOlds = get(size()-1 -1).doubleValue(),														// for more precise "first" or "last" values, we calculate the average of the nearest 3 elements
						avgLasts = getLast().doubleValue();																// in the improved cruise control, making the average on only one element was better
				return (avgLasts - avgOlds) / optimalSeconds;													   		// calculates the slope
			}
			return 0;
		}
	}
}
