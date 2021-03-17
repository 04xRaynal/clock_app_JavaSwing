/*
 * Creates a Panel using Swing Components consisting of a CountDown Timer.
 * Displays the Time Counter, buttons to select hour, minutes, seconds as well as reset, start and pause buttons.
 * Set a time using the hour, minute and seconds buttons and the timer starts counting the set time down to zero. 
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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class TimerPanel extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	DecimalFormat formatter = new DecimalFormat("00");      //formats the decimal into string, single digit numbers get padded a left zero ie. int 6 gets formatted as 06, rest numbers as usual ie. 25 as 25, 999 as 999
	int hour, minutes, seconds;
	
	JLabel labelTime, h, min, sec;
	JComboBox<String> hourComboBox, minutesComboBox, secondsComboBox;
	JButton  reset, start, pause;
	
	Timer timer;                             //importing javax.swing.Timer class
	
	public TimerPanel() {
		hour = minutes = seconds = 0;
		
		setLayout(null);
		setSize(300, 400);
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}

	
	public void createAndShowGUI() {
		labelTime = new JLabel();             	//label that displays the time
		changeLabelTimer();						//initially displays 00:00:00
		labelTime.setFont(new Font("Arial", Font.PLAIN, 28));
		labelTime.setBounds(50, 20, 200, 80);
		add(labelTime);
		
		
		hourComboBox = new JComboBox<String>();                 //combobox to choose hour, taken only 0-24 hours here, can be extended 
		for(int i = 0; i <= 24; i++) {							//filling values
			hourComboBox.addItem(formatter.format(i));
		}
		hourComboBox.setBounds(50, 120, 50, 40);
		hourComboBox.addActionListener(this);
		hourComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
		((JLabel) hourComboBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);        //center aligning the combobox text, renderer is necessary for aligning the text in combobox
		add(hourComboBox);
		
		
		minutesComboBox = new JComboBox<String>();				//combobox to choose minutes
		for(int i = 0; i < 60; i++) {							//filling values
			minutesComboBox.addItem(formatter.format(i));
		}
		minutesComboBox.setBounds(100, 120, 50, 40);
		minutesComboBox.addActionListener(this);
		minutesComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
		((JLabel) minutesComboBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);      //center aligning the combobox text, renderer is necessary for aligning the text in combobox
		add(minutesComboBox);
		
		
		secondsComboBox = new JComboBox<String>();				//combobox to choose seconds
		for(int i = 0; i < 60; i++) {							//filling values
			secondsComboBox.addItem(formatter.format(i));
		}
		secondsComboBox.setBounds(150, 120, 50, 40);
		secondsComboBox.addActionListener(this);
		secondsComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
		((JLabel) secondsComboBox.getRenderer()).setHorizontalAlignment(JLabel.CENTER);			//center aligning the combobox text, renderer is necessary for aligning the text in combobox
		add(secondsComboBox);
		
		
		h = new JLabel("h");
		h.setBounds(70, 165, 20, 20);
		h.setFont(new Font("Arial", Font.ITALIC, 13));
		add(h);
		
		min = new JLabel("min");
		min.setBounds(115, 165, 40, 20);
		min.setFont(new Font("Arial", Font.ITALIC, 13));
		add(min);
		
		sec = new JLabel("sec");
		sec.setBounds(165, 165, 40, 20);
		sec.setFont(new Font("Arial", Font.ITALIC, 13));
		add(sec);
		
		
		Image resetImage = Toolkit.getDefaultToolkit().getImage("reset-icon.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH);        //Scaled Instance sets the image width and height
		reset = new JButton(new ImageIcon(resetImage));
		reset.setBounds(30, 220, resetImage.getWidth(getParent()), resetImage.getHeight(getParent()));									//width and height of the button taken from the image, hence no blank spaces
		reset.addActionListener(this);
		reset.setEnabled(false);					//disabled
		add(reset);
		
		
		Image startImage = Toolkit.getDefaultToolkit().getImage("play-icon.png").getScaledInstance(50, 50, Image.SCALE_SMOOTH);			//Scaled Instance sets the image width and height
		start = new JButton(new ImageIcon(startImage));
		start.setBounds(100, 220, startImage.getWidth(getParent()), startImage.getHeight(getParent()));									//width and height of the button taken from the image, hence no blank spaces
		start.addActionListener(this);
		add(start);
		
		
		Image pauseImage = Toolkit.getDefaultToolkit().getImage("pause-icon.jpg").getScaledInstance(50, 50, Image.SCALE_SMOOTH);		//Scaled Instance sets the image width and height
		pause = new JButton(new ImageIcon(pauseImage));
		pause.setBounds(170, 220, pauseImage.getWidth(getParent()), pauseImage.getHeight(getParent()));									//width and height of the button taken from the image, hence no blank spaces
		pause.addActionListener(this);
		pause.setEnabled(false);                 	 //disabled
		add(pause);
	}
	
	
	public void changeLabelTimer() {
//		labelTime.setForeground(Color.BLACK);				
		labelTime.setText(formatter.format(hour) + " : " + formatter.format(minutes) + " : " + formatter.format(seconds));
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == hourComboBox) {
			hour = Integer.parseInt(hourComboBox.getItemAt(hourComboBox.getSelectedIndex()));       //Selected Number from ComboBox which is a string gets parsed as an integer
			changeLabelTimer();
		}
		
		if(e.getSource() == minutesComboBox) {
			minutes = Integer.parseInt(minutesComboBox.getItemAt(minutesComboBox.getSelectedIndex()));		//Selected Number from ComboBox which is a string gets parsed as an integer
			changeLabelTimer();
		}
		
		if(e.getSource() == secondsComboBox) {
			seconds = Integer.parseInt(secondsComboBox.getItemAt(secondsComboBox.getSelectedIndex()));		//Selected Number from ComboBox which is a string gets parsed as an integer
			changeLabelTimer();
		}
		
		if(e.getSource() == start) {
			reset.setEnabled(true);  pause.setEnabled(true);					//enabling the disabled buttons
			start.setEnabled(false);										//disabling as to prevent running two timers for the same task
			hourComboBox.setEnabled(false);  minutesComboBox.setEnabled(false);  secondsComboBox.setEnabled(false);          //disabling combobox to prevent resetting the numbers, mid countdown
			timer = new Timer(1000, new ActionListener() {                  //timer to countdown the set time, 1000 ms (1 second) delay
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(hour > 0 || minutes > 0 || seconds > 0 ) {			//to stop when we reach 00:00:00
						
						if(seconds == 0) {									//if seconds is zero then seconds gets resetted to 59 and a value from minute is deducted (done to avoid seconds counter going negative)
							seconds = 59;
							if(minutes != 0) {								//if minutes is not zero, it is deducted by 1
								minutes--;
							}
							else {											//if minutes is zero, it checks whether the hour is zero, before resetting the minute counter and deducting hour by 1
								if(hour != 0) {								//if hour is not zero, minutes counter is resetted and hour is deducted by 1
									minutes = 59;
									hour--;
								}
							}
						}
						else {
							seconds--;										//second is deducted by 1
						}
						changeLabelTimer();
					}
					else {
						Toolkit.getDefaultToolkit().beep();					//for the beep noise when time label gets to 00:00:00
//						labelTime.setForeground(Color.RED);					
						timer.stop();										//timer is stopped
						start.setEnabled(true);								
						hourComboBox.setSelectedIndex(0);  minutesComboBox.setSelectedIndex(0);  secondsComboBox.setSelectedIndex(0);			//resetting comboboxes to their initial values
						hourComboBox.setEnabled(true);  minutesComboBox.setEnabled(true);  secondsComboBox.setEnabled(true);					//enabling comboboxes after timer is completed
					}
				}
				
			});

			timer.start();						//starting timer
		}
		
		if(e.getSource() == pause) {
			timer.stop();						//stopping timer
			pause.setEnabled(false);
			start.setEnabled(true);
		}
		
		if(e.getSource() == reset) {
			if(timer !=  null) {				//otherwise throws a runtime error if reset is clicked before start, as timer is null
				timer.stop();					//stopping timer
			}
			hour = minutes = seconds = 0;		//to reset the label to 00:00:00
			changeLabelTimer();					
			reset.setEnabled(false);  pause.setEnabled(false); 
			start.setEnabled(true);
			hourComboBox.setSelectedIndex(0);  minutesComboBox.setSelectedIndex(0);  secondsComboBox.setSelectedIndex(0);				//resetting comboboxes to their initial values
			hourComboBox.setEnabled(true);  minutesComboBox.setEnabled(true);  secondsComboBox.setEnabled(true);						//enabling comboboxes after timer is completed
		}
	}
	
}