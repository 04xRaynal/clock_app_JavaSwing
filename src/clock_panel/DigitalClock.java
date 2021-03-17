/*
 * Creates a Panel using Swing Components consisting of Digital Clock synchronized with the system clock.
 * Displays the Full Date, Current Time and TimeZone with Offset. 
 * This is not the Main class file.
 * @author 04xRaynal
 */
package clock_panel;
import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

class DigitalClock extends JPanel implements Runnable{
	
	private static final long serialVersionUID = 1L;
	
	Thread thread = new Thread(this);
	String timeString = "", dateString = "", timeZoneString = "";
	JButton button;
	JLabel label1, label2;
	
	public DigitalClock() {
		
		setBackground(Color.WHITE);
		setLayout(null);
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
		
	}

	
	public void createAndShowGUI() {
		label1 = new JLabel();
		label1.setBounds(0,10,260,50);
		label1.setFont(new Font("Arial", Font.PLAIN, 17));
		
		button = new JButton();
		button.setBounds(20,280,160,30);
		button.setEnabled(false);
		button.setFont(new Font("Arial", Font.BOLD, 18));
		
		label2 = new JLabel();
		label2.setBounds(12, 320, 450, 50);
		label2.setFont(new Font("Arial", Font.PLAIN, 15));
		
		add(label1); add(button); add(label2);
		thread.start();
	}
	
	
	@Override
	public void run() {
		while(true) {
			Calendar calendar = Calendar.getInstance();
			
			//For Date
			DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");		//formats date as Tuesday, March 16, 2021
			LocalDate localDate = LocalDate.now();
			dateString = dtFormat.format(localDate);
			label1.setText(dateString);
			
			//For TimeZone with OffsetId
			TimeZone timeZone = calendar.getTimeZone();
			String offsetId = timeZone.toZoneId().getRules().getStandardOffset(Instant.now()).getId();		//
			/*
			 * offsetId returns the offset of the given timeZone, 
			 * ZoneId converts TimeZone to ZoneIdand, 
			 * getRules allows calculations to be performed on the ZoneId, 
			 * gets the StardardOffset of the given Instant or LocalDateTime
			 * Instant.now() for getting the instantaneous point of time of the day (As Offset changes for different seasons in some regions)
			 */
			timeZoneString = timeZone.getDisplayName() + " " + offsetId;
			label2.setText(timeZoneString);
			
			//For Time
			SimpleDateFormat timeFormatter = new SimpleDateFormat("hh:mm:ss a");        //Formats the date as 10:45:15 PM
			Date time = calendar.getTime();
			timeString = timeFormatter.format(time);
			button.setText(timeString);
			
			
			try {
			 Thread.sleep(1000); //interval in milliseconds, 1000 ms = 1 second, this helps refresh the thread every second
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}