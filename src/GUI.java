import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * @author 17-571-167
 *
 * GUI to give inputs for the cruise-control simulator and display the result as a Label and a history graph
 */
public class GUI extends JFrame implements Subject {

	private final JPanel bottomPanel;
	private final JTextField goalInput;

	private double goalSpeed;
	LinkedList<Observer> observers = new LinkedList<>();
	LinkedList<ObserverPanel> observersPanels = new LinkedList<>();

	public GUI() {
		setTitle("Process Control      Serie 1      17-571-167");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(500,200));
		setPreferredSize(new Dimension(1150,250));
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(new Point((int) (screen.getWidth()-getPreferredSize().width)/2, (int) (screen.getHeight()-getPreferredSize().height)/2));
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

		JLabel goalText = new JLabel("Goal speed : ");
		goalInput = new JTextField("33.333");
		goalInput.setPreferredSize(new Dimension(100,20));
		goalInput.addActionListener(e -> {										// Speed input for the Simulator, on [Enter] key press
			try {
				setGoalSpeed(Double.parseDouble(goalInput.getText()));
				notifyObservers();												// Updating the goal speed
				goalInput.setBackground(Color.WHITE);
			} catch (NumberFormatException e1) {
				goalInput.setBackground(Color.red);
				e1.printStackTrace();
			}
		});
		JLabel units = new JLabel("m/s");
		add(goalText);
		add(goalInput);

		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		topPanel.add(goalText);
		topPanel.add(goalInput);
		topPanel.add(units);



		JLabel currentText = new JLabel("Current speed : ");

		bottomPanel = new JPanel();
		bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		bottomPanel.add(currentText);
		bottomPanel.setBackground(Color.white);

		mainPanel.add(topPanel);
		mainPanel.add(bottomPanel);
		setContentPane(mainPanel);
		pack();
		setVisible(true);
	}

	/**
	 * Sets the current speed Label for one CruiseController Observer
	 * @param o
	 * @param speed
	 */
	public void setLabel(Observer o, double speed) {
		ObserverPanel panel = observersPanels.get(observers.indexOf(o));
		String text = Double.toString(speed);
		int rounding = 4,
			dotIndex = text.lastIndexOf('.');
		text = text.substring(0, Math.min(dotIndex + rounding, text.length()));
		panel.speedLabel.setText(text);
		panel.speedLabel.setSize(panel.speedLabel.getPreferredSize());
		panel.speedLabel.repaint();
	}

	/**
	 * Pushes a new speed in the speed graph and updates it for one CruiseController Observer
	 * @param o
	 * @param speed
	 */
	public void putInGraph(Observer o, double speed) {
		ObserverPanel panel = observersPanels.get(observers.indexOf(o));
		panel.speedPlot.push((int) Math.round(speed));
		panel.speedPlot.repaint();
	}

	/**
	 * Gets the state of the GUI subject, i.e. the goal speed given by the user
	 * @return goalSpeed
	 */
	public double getGoalSpeed() {
		return goalSpeed;
	}

	/**
	 * Sets the state of the GUI subject, i.e. the goal speed given by the user
	 * @param goalSpeed
	 */
	public void setGoalSpeed(double goalSpeed) {
		this.goalSpeed = goalSpeed;
	}

	/**
	 * Attach a new Observer, i.e. a new AbstractCruiseController, and create its ObserverPanel
	 * @param o
	 */
	public void attach(Observer o) {
		observers.add(o);
		ObserverPanel observerPanel = new ObserverPanel(o.getClass().getName() + " : ");
		observersPanels.add(observerPanel);
		bottomPanel.add(observerPanel);
	}

	/**
	 * Detach an observer and remove its ObserverPanel from the GUI
	 * @param o
	 */
	public void detach(Observer o) {
		int i = observers.indexOf(o);
		observers.remove(i);
		observersPanels.remove(i);
	}

	/**
	 * Notify all the observers that are observing this as a Subject
	 */
	public void notifyObservers() {
		for (Observer o : observers)
			o.update();
	}
}
