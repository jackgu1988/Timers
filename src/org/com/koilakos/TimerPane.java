package org.com.koilakos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.Timer;

/**
 * 
 * @author jack gurulian
 * 
 */
public class TimerPane extends JLabel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public TimerPane() {
		super("" + new Date());
		Timer t = new Timer(1000, this);
		t.start();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		setText((new Date()).toString());
	}

}
