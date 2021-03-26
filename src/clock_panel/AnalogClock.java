/*
 * Creates a Panel using Swing Components consisting of an Analog Clock synchronized with the system clock.
 * Displays the clock graphic along with the clock hands and reference points for hours, minutes and seconds. 
 * This is not the Main class file.
 * @author 04xRaynal
 * Reference: https://github.com/paoloboschini/analog-clock
 */
package clock_panel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.Calendar;

import javax.swing.JPanel;

class AnalogClock extends JPanel implements Runnable {
	
	private static final long serialVersionUID = 1L;

	Thread t = new Thread(this);
	
	//coordinates to paint the clock hands
	int xHandSec, yHandSec, xHandMin, yHandMin, xHandHour, yHandHour;
	
	//the size of the clock
	private final int HORIZONTAL_SIZE = 200;
	private final int VERTICAL_SIZE = 200;
	
	//length of the clock hands relative to the clock size
	private final int secondHandLength = HORIZONTAL_SIZE / 2 - 10;
	private final int minuteHandLength = HORIZONTAL_SIZE / 2 - 20;
	private final int hourHandLength = HORIZONTAL_SIZE / 2 - 45;
	
	//distance of the dots from the origin(center of the clock)
	private final int DISTANCE_DOT_FROM_ORIGIN = HORIZONTAL_SIZE / 2 - 10;
	
	//Diameter of dots
	private final int DIAMETER_BIG_DOT = 8;
	private final int DIAMETER_SMALL_DOT = 4;
	
	//Creating customized Color, grey
	private final static Color GREY_COLOR = new Color(160, 160, 160);
	
	
	public AnalogClock() {
		setSize(new Dimension(HORIZONTAL_SIZE, VERTICAL_SIZE));
		setLayout(null);
		t.start();
	}
	
	/*
	 * At each iteration we recalculate the coordinates of the clock hands,
	 * and repaint everything.
	 */
	@Override
	public void run() {
		while(true) {
			Calendar calendar = Calendar.getInstance();
			int second = calendar.get(Calendar.SECOND);
			int minute = calendar.get(Calendar.MINUTE);
			int hour = calendar.get(Calendar.HOUR);
			
			xHandSec = pointLocation(second, secondHandLength).x;
			yHandSec = pointLocation(second, secondHandLength).y;
			xHandMin = pointLocation(minute, minuteHandLength).x;
			yHandMin = pointLocation(minute, minuteHandLength).y;
			xHandHour = pointLocation(hour * 5 + relativeHour(minute), hourHandLength).x;
			yHandHour = pointLocation(hour * 5 + relativeHour(minute), hourHandLength).y;
			
//			paint(getGraphics());
			repaint();                  //instantly calls the paint method, faster and efficient than paint to repaint a graphic on each iteration
			
			try {
				Thread.sleep(1000);     //1 second interval, 1000ms = 1 sec
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}
	
	
   /*
    * Returns how much the hour hand should be ahead according to the minute value
    * 04:00 will return 0.
    * 04:12 will return 1, so we move the hour handle ahead by one dot.
    * @param min - The current minute.
    * @return the relative offset to add to the hour hand.
    */
	private int relativeHour(int min) {
		return min/12;
	}
	
	
	public void paint(Graphics g) {
		Graphics2D g2D = (Graphics2D) g;										//for a 2 dimensional shape
		g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2D.clearRect(0, 0, getWidth(), getHeight());						//clears the specified area by filling it with the background color of the current surface
		
		//draw the dots
		g2D.setColor(GREY_COLOR);
		for(int i = 0; i < 60; i++) {
			
			Point dotCoordinates = pointLocation(i, DISTANCE_DOT_FROM_ORIGIN);
			g2D.setColor((i <= Calendar.getInstance().get(Calendar.SECOND)) ? Color.WHITE : GREY_COLOR);			//if the second is passed, it fills dot with white color, otherwise grey
			
			if(i % 5 == 0) {
				//big dot
				g2D.setColor(GREY_COLOR);					//to override the above set Color, so that the big dots don't change color
				g2D.fillOval(dotCoordinates.x - (DIAMETER_BIG_DOT / 2), dotCoordinates.y - (DIAMETER_BIG_DOT / 2), DIAMETER_BIG_DOT, DIAMETER_BIG_DOT);
				
			}
			else {
				//small dot
				g2D.fillOval(dotCoordinates.x - (DIAMETER_SMALL_DOT / 2), dotCoordinates.y - (DIAMETER_SMALL_DOT / 2), DIAMETER_SMALL_DOT, DIAMETER_SMALL_DOT);
			}
		}
		
		//draw the clock hands
		g2D.setColor(GREY_COLOR);
		g2D.drawLine(HORIZONTAL_SIZE / 2, VERTICAL_SIZE / 2, xHandSec, yHandSec);
		g2D.setStroke(new BasicStroke(2));
		g2D.drawLine(HORIZONTAL_SIZE / 2, VERTICAL_SIZE / 2, xHandMin, yHandMin);
		g2D.setStroke(new BasicStroke(4));
		g2D.drawLine(HORIZONTAL_SIZE / 2, VERTICAL_SIZE / 2, xHandHour, yHandHour);
	}
	
	
	/*
	 * Converts second/ minute/ hour into x & y cordinates
	 * @param timeStep - The current time
	 * @param radius - the radius length
	 * @return - the coordinates point
	 */
	private Point pointLocation(int timeStep, int radius) {
		double t = 2* Math.PI * (timeStep - 15) / 60;
		int x = (int)(HORIZONTAL_SIZE / 2 + radius * Math.cos(t));
		int y = (int)(VERTICAL_SIZE / 2 + radius * Math.sin(t));
		
		return new Point(x,y);
	}

}
