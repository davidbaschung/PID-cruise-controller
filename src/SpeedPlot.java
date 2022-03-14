import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.LinkedList;
/**
 * @author 17-571-167
 *
 * Logs a variable number (e.g. speed) and its previous versions in a graph, in a FIFO queue model.
 */
public class SpeedPlot extends JPanel {

	private LinkedList<Integer> speeds = new LinkedList<>();

	public SpeedPlot() {
		super();
		setLayout(new FlowLayout());
		setPreferredSize(new Dimension(500,83));
		for (int i=0; i<500; i++)
			speeds.push(0);
	}

	/**
	 * push the new current number at the front of the graph and remove the oldest number
	 * @param speed
	 */
	public void push(int speed) {
		speeds.push(speed);
		speeds.removeLast();
	}

	/**
	 * draws a vertical line for each horizontal pixel-column. Each line links the previous number to the actual one.
	 * @param g
	 */
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D graph = (Graphics2D)g;
		graph.setStroke(new BasicStroke(2));
		for (int i=1; i<speeds.size(); i++) {
			graph.draw(new Line2D.Double(getWidth()-i, getHeight()-speeds.get(i-1), getWidth()-i, getHeight()-speeds.get(i)));
		}
		graph.setPaint(Color.BLUE);
	}
	@Override
	public void setBackground(Color bg) {
		super.setBackground(Color.white);
	}

	@Override
	public void setForeground(Color fg) {
		super.setForeground(new Color(0,150,0));
	}
}
