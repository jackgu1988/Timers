/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.com.koilakos;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.jdesktop.swingx.JXTaskPane;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 * Class NewTimer
 * 
 * Creates a timer for a selected worker and performs all actions.
 * 
 * @author jack gurulian
 * 
 */
public class NewTimer extends JXTaskPane {

	/**
	 * ID
	 */
	private static final long serialVersionUID = 1L;
	/** Start/Stop/Continue button */
	private JButton btn1 = new JButton();
	/** New car button */
	private JButton btn2 = new JButton();
	/** True if the counter is running */
	private boolean counting = false;
	/** Number of cars */
	private int cars = 0;
	/** When job started */
	private Date startTime;
	/** When job finished */
	private Date stopTime;
	/** Label that works as the titles for the table columns */
	private JLabel outLbl = new JLabel();
	/**
	 * A container with a more free layout to hold the various interface buttons
	 */
	private JPanel container = new JPanel();
	/** Table to present the metrics */
	private JTable table;
	/** ArrayList to hold the data (time started, finished etc) */
	private ArrayList<Object[]> list = new ArrayList<Object[]>();
	/** ArrayList to hold the start times */
	private ArrayList<String> startTimes = new ArrayList<String>();
	/** ArrayList to hold the stop times */
	private ArrayList<String> stopTimes = new ArrayList<String>();
	/** ArrayList to hold the car labels */
	private ArrayList<String> labels = new ArrayList<String>();

	/** Icon to be presented when a counter is running */
	private ImageIcon loading = new ImageIcon(
			Timers.class.getResource("/icons/loader.gif"));
	/** Label to hold the running icon */
	private JLabel load = new JLabel(loading);

	/** REGEX to validate time format */
	private static final String TIME24HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";
	/** Pattern for time format */
	private Pattern pattern;
	/** Matcher for time format */
	private Matcher matcher;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            the name of the JXTaskPane
	 * @param frame
	 *            the frame it lives in
	 */
	public NewTimer(String name, final JFrame frame) {
		this.setSize(450, 25);
		this.setLocation(0, 37);
		this.setTitle(name);
		this.setScrollOnExpand(true);
		this.setVisible(true);
		this.setSpecial(true);
		outLbl.setText("<html><b>Car &emsp;&emsp;&emsp;&emsp;&emsp;Start time&emsp;&emsp;Finish time &emsp; Total time</b></html>");

		pattern = Pattern.compile(TIME24HOURS_PATTERN);

		final DefaultTableModel model = new DefaultTableModel();
		table = new JTable(model) {

			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int rowIndex, int colIndex) {
				if (colIndex == 3)
					return false; // Disallow the editing of column 3 (total
									// time)
				return true;
			}
		};

		table.getModel().addTableModelListener(new TableModelListener() {

			public void tableChanged(TableModelEvent e) {

				int column = e.getColumn();
				int row = e.getFirstRow();

				TableModel model = table.getModel();
				String newValue = "";

				if (column == 1 || column == 2) {

					newValue = (String) model.getValueAt(row, column);

					if (validate(newValue) == false) {
						JOptionPane
								.showMessageDialog(
										frame,
										"Invalid time input.\nIt should look like HH:MM:SS.",
										"Error", JOptionPane.ERROR_MESSAGE);
						if (column == 1)
							model.setValueAt(startTimes.get(row), row, column);
						else if (column == 2)
							model.setValueAt(stopTimes.get(row), row, column);
					} else {
						if (column == 1) {
							startTimes.set(row, newValue);
							list.get(list.size() - 1)[1] = startTimes
									.get(startTimes.size() - 1);
						} else if (column == 2) {
							stopTimes.set(row, newValue);
							list.get(list.size() - 1)[2] = stopTimes
									.get(stopTimes.size() - 1);
						}
						// TODO update time spent
					}
				} else if (column == 0) {
					newValue = (String) model.getValueAt(row, column);
					labels.set(row, newValue);
					list.get(list.size() - 1)[0] = labels.get(labels.size() - 1);
				}
			}
		});

		model.addColumn("Car");
		model.addColumn("Start time");
		model.addColumn("Finish time");
		model.addColumn("Total time");

		this.add(outLbl);
		this.add(table);
		btn1.addMouseListener(new MouseAdapter() {

			@SuppressWarnings("deprecation")
			@Override
			public void mouseClicked(MouseEvent e) {
				counting = !counting;
				if (counting) {
					load.setVisible(true);
					btn2.setEnabled(false);
					if (!btn1.getLabel().equals("Continue")) {
						cars++;
						labels.add("" + cars);
						startTimes.add(getTime());
						stopTimes.add("");
						list.add(new Object[] { labels.get(labels.size() - 1),
								startTimes.get(startTimes.size() - 1), "", "" });
						model.addRow(list.get(list.size() - 1));
						btn1.setText("Stop");
					} else {
						btn1.setText("Stop");
					}
				} else {
					stopTimes.set(stopTimes.size() - 1, getTime());
					list.get(list.size() - 1)[2] = stopTimes.get(stopTimes
							.size() - 1);
					list.get(list.size() - 1)[3] = totalTime();
					model.removeRow(model.getRowCount() - 1);
					model.addRow(list.get(list.size() - 1));
					btn2.setEnabled(true);
					btn1.setText("Continue");
					load.setVisible(false);
				}

			}
		});
		btn1.setText("Start");

		btn2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				counting = true;
				cars++;
				labels.add("" + cars);
				startTimes.add(getTime());
				stopTimes.add("");
				list.add(new Object[] { labels.get(labels.size() - 1),
						startTimes.get(startTimes.size() - 1), "", "" });
				model.addRow(list.get(list.size() - 1));
				btn2.setEnabled(false);
				btn1.setText("Stop");
				load.setVisible(true);
			}
		});
		btn2.setText("New car");
		btn2.setEnabled(false);

		load.setVisible(false);

		container.add(btn1);
		container.add(btn2);
		container.add(load);
		container.setLayout(new FlowLayout());
		this.add(container);
	}

	/**
	 * Returns current time
	 * 
	 * @return current time
	 */
	private String getTime() {
		Calendar cal = Calendar.getInstance();
		if (counting)
			startTime = cal.getTime();
		else
			stopTime = cal.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(cal.getTime());
	}

	/**
	 * Calculates the time that was needed for the job to finish
	 * 
	 * @return how much time the job required
	 */
	private String totalTime() {
		long difference = stopTime.getTime() - startTime.getTime();
		int days = (int) (difference / (1000 * 60 * 60 * 24));
		int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
		int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours))
				/ (1000 * 60);
		int sec = (int) (difference - (1000 * 60 * 60 * 24 * days)
				- (1000 * 60 * 60 * hours) - (1000 * 60 * min)) / 1000;

		return hours + "h" + min + "m" + sec + "s";
	}

	/**
	 * Validate time in 24 hours format with regular expression
	 * 
	 * @param time
	 *            time address for validation
	 * @return true valid time format, false invalid time format
	 */
	private boolean validate(final String time) {

		matcher = pattern.matcher(time);
		return matcher.matches();

	}

}
