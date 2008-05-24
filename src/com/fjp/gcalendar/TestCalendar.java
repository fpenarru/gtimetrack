package com.fjp.gcalendar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.TreeSet;

import javax.swing.JOptionPane;

import com.fjp.gcalendar.gui.DlgChooseCalendar;
import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

public class TestCalendar {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			CalendarService myService = new CalendarService("gTimeTrack");

			Properties p = new Properties();
			File f = new File("user.ini");
			if (!f.exists())
			{
				String user = JOptionPane.showInputDialog("Fist time execution. Enter username for google calendar:");
				String passw = JOptionPane.showInputDialog("Enter password for google calendar:");
				if (f.createNewFile())
				{
					Properties prop = new Properties();
					prop.setProperty("user", user);
					prop.setProperty("password", passw);
					FileOutputStream out = new FileOutputStream(f);
					prop.store(out, "gTimeTrack");
					out.close();
				}
				else
				{
					System.err.println("Cannot create user.ini file " + f.getAbsolutePath());
				}
			}
			p.load(new FileInputStream("user.ini"));
			String user = p.getProperty("user");
			String password = p.getProperty("password");

			myService.setUserCredentials(user, password);

			DlgChooseCalendar myDlg = new DlgChooseCalendar(null);
			// Send the request and print the response
			URL feedUrlCal = new URL(
					"http://www.google.com/calendar/feeds/default/allcalendars/full");
			CalendarFeed resultFeed = myService.getFeed(feedUrlCal,
					CalendarFeed.class);
			System.out.println("Your calendars:");
			System.out.println();
			myDlg.setCalendarService(myService);
			myDlg.setCalendarEntries(resultFeed.getEntries());
			myDlg.setModal(true);
			myDlg.setVisible(true);

			Date desde, hasta;

			URL feedUrl;
			// IVER
			// feedUrl = new
			CalendarEntry selectedEntry = myDlg.getSelectedCalendar();
			String strFeed = selectedEntry.getLinks().get(0).getHref();

			System.out.println(strFeed);
			feedUrl = new URL(strFeed);

			desde = myDlg.getFromDate();
			hasta = myDlg.getToDate();
			// JCalendar calendar = new JCalendar();
			// JDialog dlg = new JDialog();
			// dlg.getContentPane().add(calendar);
			// dlg.setModal(true);
			// dlg.setVisible(true);
			// desde = calendar.getSelectedDate();
			// dlg.setVisible(true);
			// hasta = calendar.getSelectedDate();
			showWorkTime(desde, hasta, feedUrl);


		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (AuthenticationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param desde
	 * @param hasta
	 * @throws MalformedURLException
	 * @throws AuthenticationException
	 * @throws IOException
	 * @throws ServiceException
	 */
	private static void showWorkTime(Date desde, Date hasta, URL feedUrl)
			throws MalformedURLException, AuthenticationException, IOException,
			ServiceException {

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		CalendarQuery myQuery = new CalendarQuery(feedUrl);
		DateTime fromG = new DateTime(desde);
		DateTime toG = new DateTime(hasta);
		myQuery.setMinimumStartTime(fromG);
		myQuery.setMaximumStartTime(toG);

		CalendarService myService = new CalendarService("TestGCalendar");
		myService.setUserCredentials("fpenarru@gmail.com", "aquilina");

		// Send the request and receive the response:
		myQuery.setMaxResults(200);

		CalendarEventFeed resultFeed = myService.query(myQuery,
				CalendarEventFeed.class);

		System.out.println("Total: " + resultFeed.getTotalResults());
		System.out.println("Total: " + resultFeed.getEntries().size());

		List list = resultFeed.getEntries();
		double sumaHoras = 0;

		// Ordenamos por fecha
		TreeSet<MyEntry> ordered = new TreeSet<MyEntry>();
		for (int i = 0; i < list.size(); i++) {
			CalendarEventEntry ev = (CalendarEventEntry) list.get(i);
			List<When> times = ev.getTimes();
			for (int j = 0; j < times.size(); j++) {
				DateTime from = times.get(j).getStartTime();
				DateTime to = times.get(j).getEndTime();
				MyEntry myEntry = new MyEntry(ev, from, to);
				ordered.add(myEntry);
			}
		}
		Iterator<MyEntry> ordIterator = ordered.iterator();
		while (ordIterator.hasNext()) {
			MyEntry entry = ordIterator.next();
			CalendarEventEntry ev = entry.getEntry();
			System.out.print(ev.getTitle().getPlainText() + "; ");
			DateTime from = entry.getStartTime();
			DateTime to = entry.getStopTime();

			Date t;
			Date d;
			long dif = 0;
			try {
				if (from.isDateOnly()) {
					dif = 8 * 60 * 60 * 1000;
				} else {
					d = df.parse(from.toUiString());
					t = df.parse(to.toUiString());
					dif = t.getTime() - d.getTime();
					if (dif >= 24 * 60 * 60 * 1000)
						dif = 8 * 60 * 60 * 1000;
				}
				long seconds = (dif / 1000);
				int hours = (int) seconds / 3600;
				int minutes = (int) ((seconds / 60) - (hours * 60));
				double workT = hours + minutes / 60.0;
				System.out.println(from.toUiString() + "; " + to.toUiString()
						+ "; " + workT);
				sumaHoras += workT;

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		System.out.println("Total de horas: " + sumaHoras
				+ ". Precio estimado: " + (sumaHoras * 37) + " euros");
	}
}
