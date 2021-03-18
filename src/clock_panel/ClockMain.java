/*
 * Creates a Frame using Java Swing of multiple tabs consisting of Digital Clock, Analog Clock both synchronized with the system clock, 
 * also tabs with Calendar, StopWatch and a Countdown Timer. 
 * This is the Main class file.
 * @author 04xRaynal
 */
package clock_panel;
import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class ClockMain extends JFrame{
	
	private static final long serialVersionUID = 1L;
	
	public ClockMain() {
		Image icon = Toolkit.getDefaultToolkit().getImage("clock-icon.png").getScaledInstance(60, 60, Image.SCALE_SMOOTH);
		setTitle("Clock Application");
		setIconImage(icon);
		setLayout(null);
		setSize(330, 475);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				//Frame exit when close button is pressed
		
		/*
		 * Swing Utilities runs the piece of code on the AWT thread. Which lets you modify the GUI from other threads.
		 * 
		 * Because GUI updates must be done in the event dispatch thread. 
		 * If you're operating in a different thread, doing the update in invokeLater yanks it out of your thread and into the event thread.
		 * Which in turn makes the components load properly and doesn't behave weird when its updated.
		 */
		SwingUtilities.invokeLater(new Runnable() {					//Using Swing Utilities for better processing
			
			@Override
			public void run() {
				createAndRunGUI();
			}
		});
	}
	
	
	public void createAndRunGUI() {
		JTabbedPane tabbedPane = new JTabbedPane();					//TabbedPane for multiple panes as tabs
		tabbedPane.setBounds(20, 20, 272, 395);
		
		JPanel panel1 = new JPanel(null);
		panel1.setBackground(Color.WHITE);						//only panel1 will have white background color
		JPanel panel2 = new JPanel(null);
		JPanel panel3 = new JPanel(null);
		JPanel panel4 = new JPanel(null);
		
		tabbedPane.add("Clock", panel1);
		tabbedPane.add("Calendar", panel2);
		tabbedPane.add("StopWatch", panel3);
		tabbedPane.add("Timer", panel4);

		
		DigitalClock digitalClock = new DigitalClock();
		digitalClock.setBounds(32, 0, 260, 400);
		panel1.add(digitalClock);
		AnalogClock analogClock = new AnalogClock();
		analogClock.setBounds(32, 60, 220, 220);
		panel1.add(analogClock);
		
		CalendarPanel calendarPanel = new CalendarPanel();
		calendarPanel.setBounds(10, 10, 300, 380);
		panel2.add(calendarPanel);
		
		StopWatch stopWatch = new StopWatch();
		stopWatch.setBounds(10, 10, 300, 360);
		panel3.add(stopWatch);
		
		TimerPanel timerPanel = new TimerPanel();
		timerPanel.setBounds(10, 10, 300, 360);
		panel4.add(timerPanel);
		
		add(tabbedPane);
	}
	
	
	public static void main(String[] args) {
		new ClockMain();
	}

}
