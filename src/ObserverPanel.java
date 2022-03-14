import javax.swing.*;
import java.awt.*;

/**
 * @author 17-571-167
 *
 * Each Observer managing a variable number (e.g. speed) output can display its results here.
 * This class offers an opprtunity to display the current variable number and log past variable numbers in a graph.
 */
public class ObserverPanel extends JPanel {

	final JLabel algoLabel;														// Algorithm name Label
	final JLabel speedLabel;													// Current speed Label
	final SpeedPlot speedPlot;													// Previous speeds Graph

	public ObserverPanel(String observerName) {
		super();
		setLayout(new FlowLayout(FlowLayout.LEFT));
		setPreferredSize(new Dimension(510,148));

		algoLabel = new JLabel(observerName);
		speedLabel = new JLabel();
		speedPlot = new SpeedPlot();
		speedLabel.setFont(new Font("Arial",Font.BOLD,30));
		add(speedPlot);
		add(algoLabel);
		add(speedLabel);
		add(new JLabel("m/s"));
	}
}
