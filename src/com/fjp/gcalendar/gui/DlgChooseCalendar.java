package com.fjp.gcalendar.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.google.gdata.data.calendar.CalendarEntry;

public class DlgChooseCalendar extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");  //  @jve:decl-index=0:
	
	private class myComparator implements Comparator<CalendarEntry> {

		public int compare(CalendarEntry o1, CalendarEntry o2) {
			String str1 = o1.getTitle().getPlainText();
			String str2 = o2.getTitle().getPlainText();
			return str1.compareTo(str2);
		}
		
	}
	
	private JPanel jContentPane = null;
	
	private JPanel jPanelCalendar = null;

	private JPanel jPanelTimeInterval = null;

	private JPanel jPanelButtons = null;

	private JButton jBtnExport = null;

	private JButton jBtnClose = null;

	private JLabel jLblChoose = null;

	private JComboBox jCboCalendar = null;

	private JLabel jLblFrom = null;

	private JLabel jLblTo = null;

	private JTextField jTxtFrom = null;

	private JTextField jTxtTo = null;

	private CalendarEntry[] entries;

	/**
	 * @param owner
	 */
	public DlgChooseCalendar(Frame owner) {
		super(owner);
		initialize();
		Calendar cal = Calendar.getInstance();
//		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		NumberFormat nF = NumberFormat.getInstance();
		nF.setMinimumIntegerDigits(2);
		nF.setMaximumFractionDigits(0);
		
		String strMonth = nF.format(cal.get(Calendar.MONTH) + 1);
		String strYear = ""+ cal.get(Calendar.YEAR);
		String strFrom = "01/" + strMonth + "/" + strYear;
		String lastDay = nF.format(cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		String strTo = lastDay + "/" + strMonth + "/" + strYear;
		jTxtFrom.setText(strFrom);
		jTxtTo.setText(strTo);
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setTitle("gTimeTrack");
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJPanelCalendar(), BorderLayout.NORTH);
			jContentPane.add(getJPanelTimeInterval(), BorderLayout.CENTER);
			jContentPane.add(getJPanelButtons(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPanelCalendar	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelCalendar() {
		if (jPanelCalendar == null) {
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.weightx = 1.0;
			jLblChoose = new JLabel();
			jLblChoose.setText("Choose Calendar:");
			jLblChoose.setPreferredSize(new Dimension(120, 16));
			jPanelCalendar = new JPanel();
			jPanelCalendar.setLayout(new GridBagLayout());
			jPanelCalendar.add(jLblChoose, new GridBagConstraints());
			jPanelCalendar.add(getJCboCalendar(), gridBagConstraints);
		}
		return jPanelCalendar;
	}

	/**
	 * This method initializes jPanelTimeInterval	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelTimeInterval() {
		if (jPanelTimeInterval == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints4.gridy = 1;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.gridx = 1;
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.fill = GridBagConstraints.VERTICAL;
			gridBagConstraints3.gridy = 0;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.gridwidth = 1;
			gridBagConstraints3.gridx = 1;
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 1;
			jLblTo = new JLabel();
			jLblTo.setText("To:");
			jLblTo.setHorizontalAlignment(SwingConstants.RIGHT);
			jLblTo.setPreferredSize(new Dimension(92, 16));
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.gridx = 0;
			gridBagConstraints1.gridy = 0;
			jLblFrom = new JLabel();
			jLblFrom.setText("From:");
			jLblFrom.setHorizontalTextPosition(SwingConstants.RIGHT);
			jLblFrom.setHorizontalAlignment(SwingConstants.RIGHT);
			jLblFrom.setPreferredSize(new Dimension(92, 16));
			jPanelTimeInterval = new JPanel();
			jPanelTimeInterval.setLayout(new GridBagLayout());
			jPanelTimeInterval.add(jLblFrom, gridBagConstraints1);
			jPanelTimeInterval.add(jLblTo, gridBagConstraints2);
			jPanelTimeInterval.add(getJTxtFrom(), gridBagConstraints3);
			jPanelTimeInterval.add(getJTxtTo(), gridBagConstraints4);
		}
		return jPanelTimeInterval;
	}

	/**
	 * This method initializes jPanelButtons	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanelButtons() {
		if (jPanelButtons == null) {
			jPanelButtons = new JPanel();
			jPanelButtons.setLayout(new GridBagLayout());
			jPanelButtons.add(getJBtnExport(), new GridBagConstraints());
			jPanelButtons.add(getJBtnClose(), new GridBagConstraints());
		}
		return jPanelButtons;
	}

	/**
	 * This method initializes jBtnExport	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJBtnExport() {
		if (jBtnExport == null) {
			jBtnExport = new JButton();
			jBtnExport.setText("Export");
		}
		return jBtnExport;
	}

	/**
	 * This method initializes jBtnClose	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJBtnClose() {
		if (jBtnClose == null) {
			jBtnClose = new JButton();
			jBtnClose.setText("Close");
			jBtnClose.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					dispose();
				}
			});
		}
		return jBtnClose;
	}

	/**
	 * This method initializes jCboCalendar	
	 * 	
	 * @return javax.swing.JComboBox	
	 */
	private JComboBox getJCboCalendar() {
		if (jCboCalendar == null) {
			jCboCalendar = new JComboBox();
			jCboCalendar.setPreferredSize(new Dimension(301, 25));
		}
		return jCboCalendar;
	}

	/**
	 * This method initializes jTxtFrom	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTxtFrom() {
		if (jTxtFrom == null) {
			jTxtFrom = new JTextField();
			jTxtFrom.setPreferredSize(new Dimension(94, 20));
		}
		return jTxtFrom;
	}

	/**
	 * This method initializes jTxtTo	
	 * 	
	 * @return javax.swing.JTextField	
	 */
	private JTextField getJTxtTo() {
		if (jTxtTo == null) {
			jTxtTo = new JTextField();
			jTxtTo.setPreferredSize(new Dimension(94, 20));
		}
		return jTxtTo;
	}

	public void setCalendarEntries(List<CalendarEntry> entries) {
		TreeSet tree = new TreeSet(new myComparator());
		tree.addAll(entries);
		this.entries = (CalendarEntry[]) tree.toArray(new CalendarEntry[0]);
		Iterator it = tree.iterator();
		while (it.hasNext())
		{
			CalendarEntry entry = (CalendarEntry) it.next();
			System.out.println("\t" + entry.getTitle().getPlainText());
			System.out.println("Feed: " + entry.getId());
			System.out.println("Feed2: " + entry.getHtmlLink());
			jCboCalendar.addItem(entry.getTitle().getPlainText());
//			jCboCalendar.add(entry);
		}
//		jCboCalendar.setModel(new DefaultComboBoxModel(tree.toArray(new CalendarEntry[0])));

		
	}

	public CalendarEntry getSelectedCalendar() {
		int selected = jCboCalendar.getSelectedIndex();
		
		return entries[selected];
	}

	public Date getFromDate() {
		try {
			return df.parse(jTxtFrom.getText());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Date getToDate() {
		try {
			return df.parse(jTxtTo.getText());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
