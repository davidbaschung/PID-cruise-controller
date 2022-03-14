/**
 * @author 17-571-167
 *
 * Interface for the subject in an observer pattern
 */
public interface Subject {
	void attach(Observer o);
	void detach(Observer o);
	void notifyObservers();

}
