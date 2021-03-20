/*
 * Creates a Panel using Swing Components consisting of a Calendar synchronized with the system date.
 * Displays the Current Full Date, and a button to select previous or next month as well as subsequent or previous years. 
 * This is not the Main class file.
 * @author 04xRaynal
 */
package clock_panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class CalendarPanel extends JPanel implements  ActionListener, ListSelectionListener{
	
	private static final long serialVersionUID = 1L;
	
	JLabel labelMonth, labelYearChange, selectedCellDate;
	JButton previousButton, nextButton;
	JTable tableCalendar;
	JComboBox<String> comboBoxYear;
	DefaultTableModel modelCalendar;
	int year, month, day, currentYear, currentMonth;
	JScrollPane scorllPaneCalendar;
	
	
	public CalendarPanel() {
		try {   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());   }
			catch (ClassNotFoundException e) {}
	        catch (InstantiationException e) {}
	        catch (IllegalAccessException e) {}
	        catch (UnsupportedLookAndFeelException e) {}        //Refines the look of the ui
		 
		setSize(250, 280);
		setLayout(null);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGUI();
			}
		});
	}
	
	
	public void createAndShowGUI() {
		labelMonth = new JLabel();
		labelYearChange = new JLabel("Change Year: ");
		selectedCellDate = new JLabel();
		comboBoxYear = new JComboBox<String>();
		previousButton = new JButton("<<");
		nextButton = new JButton(">>");
		modelCalendar = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int rowIndex, int colIndex) {				//makes table cells uneditable by user
				return false;
			}
		};
		tableCalendar = new JTable(modelCalendar);								//creating a table using modelCalendar as its data model
		scorllPaneCalendar = new JScrollPane(tableCalendar);					//ScrollPane is required to display the table
		
		
		previousButton.addActionListener(this);
		nextButton.addActionListener(this);
		comboBoxYear.addActionListener(this);

		
		add(labelMonth); add(labelYearChange); add(comboBoxYear);
		add(previousButton); add(nextButton); add(scorllPaneCalendar);
		add(selectedCellDate);
		
		selectedCellDate.setBounds(14, -4, 200, 30);
		selectedCellDate.setFont(new Font("Arial", Font.PLAIN, 13));
		selectedCellDate.setForeground(new Color(153,50,204));
		labelMonth.setBounds(108, 28, 100, 25);
		labelMonth.setFont(new Font("Arial", Font.PLAIN, 12));
		labelYearChange.setBounds(10, 328, 80, 20);
		comboBoxYear.setBounds(160, 330, 80, 20);
		previousButton.setBounds(10, 30, 50, 25);
		nextButton.setBounds(187, 30, 50, 25);
		scorllPaneCalendar.setBounds(5, 65, 240, 255);
		
		
		Calendar calendar = Calendar.getInstance();
		day = calendar.get(Calendar.DAY_OF_MONTH);
		month = calendar.get(Calendar.MONTH);
		year = calendar.get(Calendar.YEAR);
		currentMonth = month;
		currentYear = year;
		
		//adding column headers to the model of calendar
		String[] headers = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
		for(String h: headers)
			modelCalendar.addColumn(h);
		
				
		//No resize or reorder
		tableCalendar.getTableHeader().setResizingAllowed(false);
		tableCalendar.getTableHeader().setReorderingAllowed(false);
		
		//Single Cell Selection
//		tableCalendar.setColumnSelectionAllowed(true);
//		tableCalendar.setRowSelectionAllowed(true);
//		tableCalendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableCalendar.setCellSelectionEnabled(true);      //works the same as when both row and column selection are enabled
		ListSelectionModel selectModel = tableCalendar.getSelectionModel();
		selectModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectModel.addListSelectionListener(this);
		
		
		/*  //tried using table model, but getting blank table.
		TableModel tableModel = tableCalendar.getModel();
		modelCalendar.addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent e) {
				int row = tableCalendar.getSelectedRow();
				int col = tableCalendar.getSelectedColumn();
				
				DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
				int date = Integer.parseInt(tableCalendar.getValueAt(row, col).toString());
				LocalDate localDate = LocalDate.of(currentYear, currentMonth+1, date );
				selectedCellDate.setText(dtFormat.format(localDate));
				
			}
		});
		*/
		
		
		//Set row/column count
		tableCalendar.setRowHeight(38);
		modelCalendar.setColumnCount(7);
		modelCalendar.setRowCount(6);
		
		//Populate comboBox
		for(int i = year- 100; i <= year + 100; i++) {
			comboBoxYear.addItem(String.valueOf(i));
		}
		
		
		//Refresh Calendar
		refreshCalendar(month, year);
	}


	
	public void refreshCalendar(int month, int year_var) {
		
		String[] months= {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};      //array of month names
		int numberOfDays, startOfMonth;        //To store the number of days in a month and the starting day of month
		
		previousButton.setEnabled(true);
		nextButton.setEnabled(true);
		
		if(month == 0 && year_var <= year - 10) {
			previousButton.setEnabled(false);
		}
		if(month == 11 && year_var >= year + 10) {
			nextButton.setEnabled(false);
		}
		
		labelMonth.setText(months[month]);				//gets the month string value from the above string array
		comboBoxYear.setSelectedItem(String.valueOf(year_var));
		
		
		//clear table
		for(int i = 0; i < 6; i++) {
			for(int j = 0; j < 7; j++) {
				modelCalendar.setValueAt(null, i, j);
			}
		}
		
		
		//Get first day of month and number of days
		Calendar cal = Calendar.getInstance();
		cal.set(year_var, month, 1);
		numberOfDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		startOfMonth = cal.get(Calendar.DAY_OF_WEEK);
		
		
		//Draw Calendar
		for(int i = 1; i <= numberOfDays; i++) {
			int row = (i + startOfMonth -2) / 7;
			int column = (i + startOfMonth -2) % 7;
			modelCalendar.setValueAt(i, row, column);
		}
		
		//Displaying JLabel text at the top with todays date
		DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
		LocalDate localDate = LocalDate.now();
		selectedCellDate.setText(dtFormat.format(localDate));
		
		
		//Apply renderers
		tableCalendar.setDefaultRenderer(tableCalendar.getColumnClass(0), new tableCalendarRenderer());
	}
	
	
	class tableCalendarRenderer extends DefaultTableCellRenderer{
		private static final long serialVersionUID = 1L;

		public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {
			super.getTableCellRendererComponent(table, value, selected, focused, row, column);
			
			DefaultTableCellRenderer  renderer = (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();
		    renderer.setHorizontalAlignment(JLabel.CENTER);               //Aligning the text of table header to center
		    
			setHorizontalAlignment(JLabel.CENTER);                     //aligning table row cells to center
		    
			//setting background color for table cells
			if(value != null) {
				if(column == 0 || (column == 6 && row % 2 != 0))			//Alternate 2nd Saturdays and All Sundays as Red Color
					setBackground(new Color(255, 220, 220));
				else
					setBackground(new Color(255, 255, 255));				//weekdays with white background color
				
				if(Integer.parseInt(value.toString()) == day && currentMonth == month && currentYear == year)		//current date with blue color
					setBackground(new Color(173, 216, 230));
			}
			else {
				setBackground(new Color(255, 255, 255));					//empty cells with white background color
			}
			
			
			setBorder(null);
			setForeground(Color.BLACK);
			return this;
		}
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == previousButton) {      //when previous button is clicked
			
			if(currentMonth == 0) {  //Back 1 year
				currentMonth = 11;
				currentYear -= 1;
			}
			else {               //Back 1 month
				currentMonth -= 1;
			}
			
			refreshCalendar(currentMonth, currentYear);
		}
		
		else if(e.getSource() == nextButton) {      //when next button is clicked
			
			if(currentMonth == 11) {      //Forward 1 year
				currentMonth = 0;
				currentYear += 1;
			}
			else {          //Forward 1 month
				currentMonth += 1;
			}
			refreshCalendar(currentMonth, currentYear);
		}
		
		else if(e.getSource() == comboBoxYear) {        //when value of combo box is changed
			
			if(comboBoxYear.getSelectedItem() != null) {
				String boxYear = comboBoxYear.getSelectedItem().toString();
				currentYear = Integer.parseInt(boxYear);
				refreshCalendar(currentMonth, currentYear);					//refreshing calendar with the year selected from the comboBox, month remains the same
			}
		}
		
	}

	
	//when a table cell is clicked, the top label displays the complete extended date
	@Override
	public void valueChanged(ListSelectionEvent e) {
		
		int[] row  =tableCalendar.getSelectedRows();
		int[] column = tableCalendar.getSelectedColumns();
		
		for(int i = 0; i < row.length; i++) {
			for(int j = 0; j < column.length; j++) {
				DateTimeFormatter dtFormat = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
				int date = Integer.parseInt(tableCalendar.getValueAt(row[i], column[j]).toString());
				LocalDate localDate = LocalDate.of(currentYear, currentMonth+1, date );				//currentMonth is added with a 1 because, LocalDate starts Month from zero and we have started month from 1
				selectedCellDate.setText(dtFormat.format(localDate));
			}
		}
	}

}
