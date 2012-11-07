package org.com.koilakos;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jdesktop.swingx.JXTaskPaneContainer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Toolkit;
import javax.swing.JScrollPane;

/**
 * 
 * @author jack gurulian
 * 
 */
public class Timers {

	private JFrame frmTimer;
	private JXTaskPaneContainer cont = new JXTaskPaneContainer();

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Timers window = new Timers();
					window.frmTimer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Timers() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmTimer = new JFrame();
		frmTimer.setResizable(false);
		frmTimer.setIconImage(Toolkit.getDefaultToolkit().getImage(
				Timers.class.getResource("/icons/Car Repair Blue 2.png")));
		frmTimer.setTitle("Timer");
		frmTimer.setBounds(100, 100, 450, 500);
		frmTimer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTimer.getContentPane().setLayout(null);

		TimerPane timer = new TimerPane();
		timer.setSize(313, 25);
		timer.setLocation(0, 0);
		frmTimer.getContentPane().add(timer);

		JButton btnNewTimer = new JButton("New Timer");
		btnNewTimer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Object[] list = { "Koilakos", "Koilakos 2", "Koilakos 3" };
				String s = (String) JOptionPane.showInputDialog(frmTimer,
						"Mechanic:", "New Timer", JOptionPane.PLAIN_MESSAGE,
						null, list, null);
				if (s != null)
					cont.add(new NewTimer(s, frmTimer));
			}
		});
		btnNewTimer.setBounds(331, 0, 117, 25);
		frmTimer.getContentPane().add(btnNewTimer);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 37, 448, 430);
		frmTimer.getContentPane().add(scrollPane);
		scrollPane.setViewportView(cont);

		cont.setBackground(Color.DARK_GRAY);

	}
}
