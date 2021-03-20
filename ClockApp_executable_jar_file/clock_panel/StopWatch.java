/*
 * Creates a Panel using Swing Components consisting of a StopWatch synchronized with the System time.
 * Displays the timer counter, and buttons to start, pause and reset the watch.
 * Also has a button of Lap Timer for counting laps (a timed session), each lap is calculated and displayed on the panel.
 * This is not the Main class file.
 * @author 04xRaynal
 */
package clock_panel;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.time.Duration;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class StopWatch extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	
	JLabel label, timerLabel;
	JButton reset, start, pause, timer;
	JScrollPane scrollPane;
	DefaultListModel<String> listModel;
	JList<String> timerList;
	
	long hours, minutes, seconds, milliseconds;
	long lastTickTime, runningTime, pausedTime;
	int i = 1;
	long lapsedHours, lapsedMinutes, lapsedSeconds;
	
	Timer t;
	
	
	public StopWatch() {
		hours = minutes = seconds = milliseconds = 0;
		lastTickTime = runningTime = pausedTime = 0;
		lapsedHours = lapsedMinutes = lapsedSeconds = 0;
		
		setLayout(null);
		setSize(300, 460);
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
	
	public void createAndShowGUI() {
		label = new JLabel();
		changeLabel();
		label.setBounds(38, 5, 250, 50);
		label.setFont(new Font("Arial", Font.PLAIN, 24));
		add(label);
		
		
		Image resetImage = Toolkit.getDefaultToolkit().getImage("reset-icon.png").getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		reset = new JButton(new ImageIcon(resetImage));
		reset.addActionListener(this);
		reset.setBounds(20, 265, resetImage.getWidth(getParent()), resetImage.getHeight(getParent()));
		reset.setBorder(BorderFactory.createEmptyBorder());								//creates empty border
		reset.setContentAreaFilled(false);												//empty area around the image is not filled
		reset.setEnabled(false);
		add(reset);
		
		Image startImage = Toolkit.getDefaultToolkit().getImage("play-icon.png").getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		start = new JButton(new ImageIcon(startImage));
		start.addActionListener(this);
		start.setBounds(80, 265, startImage.getWidth(getParent()), startImage.getHeight(getParent()));
		start.setBorder(BorderFactory.createEmptyBorder());								//creates empty border
		start.setContentAreaFilled(false);												//empty area around the image is not filled
		add(start);
		
		Image pauseImage = Toolkit.getDefaultToolkit().getImage("pause-icon.jpg").getScaledInstance(40, 40, Image.SCALE_SMOOTH);
		pause = new JButton(new ImageIcon(pauseImage));
		pause.addActionListener(this);
		pause.setBounds(140, 265, pauseImage.getWidth(getParent()), pauseImage.getHeight(getParent()));
		pause.setBorder(BorderFactory.createEmptyBorder());								//creates empty border
		pause.setContentAreaFilled(false);												//empty area around the image is not filled
		pause.setEnabled(false);
		add(pause);
		
		Image timerImage = Toolkit.getDefaultToolkit().getImage("timer-icon.png").getScaledInstance(50, 60, Image.SCALE_SMOOTH);
		timer = new JButton(new ImageIcon(timerImage));
		timer.addActionListener(this);
		timer.setBounds(200, 290, timerImage.getWidth(getParent()), timerImage.getHeight(getParent()));
		timer.setBorder(BorderFactory.createEmptyBorder());								//creates empty border
		timer.setContentAreaFilled(false);												//empty area around the image is not filled
		add(timer);
		
		timerLabel = new JLabel("Lap Timer:");
		timerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
		timerLabel.setBounds(135, 320, 150, 30);
		add(timerLabel);
		
		listModel = new DefaultListModel<>();
		timerList = new JList<>(listModel);
		timerList.setFont(new Font("Arial", Font.ITALIC, 14));
		scrollPane = new JScrollPane(timerList);										//ScrollPane holds the list
		scrollPane.setBounds(5, 55, 238, 200);
		add(scrollPane);
	}
	
	
	public void update() {
		runningTime = System.currentTimeMillis() - lastTickTime + pausedTime;			//Calculates the time elapsed since the start button was clicked, pausedTime is initially zero, pauseTime gets a value when the pause button is clicked
		Duration duration = Duration.ofMillis(runningTime);								//Duration holds the amount of time
		
		hours = duration.toHours();									//gets the amount of hours from duration
		duration = duration.minusHours(hours);						//subtracts the hour value from duration
		minutes = duration.toMinutes();								//gets the amount of minutes from duration
		duration = duration.minusMinutes(minutes);					//subtracts the minutes value from duration
		milliseconds = duration.toMillis();							//remaining duration is converted into milliseconds
		seconds = milliseconds/1000;								//1 second = 1000 ms, amount of seconds is retrieved from milliseconds
		milliseconds -= (seconds * 1000);							//subtracts seconds from milliseconds, to get the actual value of milliseconds
	}
	
	
	public void reset() {
		try {
			Thread.sleep(1);
		}
		catch(InterruptedException e) {
			e.printStackTrace();
		}
		
		hours = minutes = seconds = milliseconds = 0;				//values are reseted back to 0
		lapsedHours = lapsedMinutes = lapsedSeconds = 0;
		
		changeLabel();
		listModel.removeAllElements();
		i = 1;
	}
	
	
	public void changeLabel() {
		DecimalFormat formatter = new DecimalFormat("00");		
		label.setText(formatter.format(hours) + " : " + formatter.format(minutes) + " : " + formatter.format(seconds) + " . " + formatter.format(milliseconds));
	}
	
	
	public void setLaps() {
		timerList.setFont(new Font("Arial", Font.ITALIC, 15));
		DecimalFormat formatter = new DecimalFormat("00");
		lapsedHours = Math.abs(hours - lapsedHours) ;
		lapsedMinutes = Math.abs(minutes - lapsedMinutes);
		lapsedSeconds = Math.abs(seconds - lapsedSeconds);
		
		listModel.addElement("Lap " + i++ + "                         " + formatter.format(hours) + " : " + formatter.format(minutes) + " : " + formatter.format(seconds));
		listModel.addElement("Elapsed: " + formatter.format(lapsedHours) + " : " + formatter.format(lapsedMinutes) + " : " + formatter.format(lapsedSeconds) );
		listModel.addElement("-------------------------------------------");
		lapsedHours = hours;
		lapsedMinutes = minutes;
		lapsedSeconds = seconds;
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == start) {
			start.setEnabled(false);
			reset.setEnabled(true);  pause.setEnabled(true);
			lastTickTime = System.currentTimeMillis();
			
			t = new Timer(1, new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					update();
					changeLabel();
				}
			});
			t.start();
		}
		
		if(e.getSource() == reset) {
			if(t != null) {					//if reset button is pressed to reset the laps without pressing the start button, it throws runtime error if null is not checked as Timer t doesn't start until start button is pressed
				t.stop();
			}
			reset();
			pausedTime = 0;
			reset.setEnabled(false); pause.setEnabled(false);
			start.setEnabled(true);
		}
		
		if(e.getSource() == pause) {
			t.stop();
			pausedTime = runningTime;					//stores the time value when the pause button is pressed
			pause.setEnabled(false);
			start.setEnabled(true); reset.setEnabled(true);
		}
		
		if(e.getSource() == timer) {
			setLaps();
			reset.setEnabled(true);
		}
	}

}
