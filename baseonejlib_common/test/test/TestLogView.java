package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JMenu;

import com.baseoneonline.java.swing.logview.LogView;

public class TestLogView {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					TestLogView window = new TestLogView();
					window.frame.setTitle("Test: " + LogView.class.getName());
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TestLogView() {
		initialize();

		// $hide>>$
		startGeneratingMessages();
		// $hide<<$
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final LogView logView = new LogView();
		frame.getContentPane().add(logView, BorderLayout.CENTER);

	}

	private void startGeneratingMessages() {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {

				// Pick random level
				Level level = randomFrom(LogView.LOG_LEVELS);
				String text = randomFrom(loremIpSum);

				Logger.getLogger(getClass().getName()).log(level, text);

			}
		}, 0, 300);
	}

	private static <T> T randomFrom(T[] arr) {
		return arr[(int) (Math.floor(Math.random() * arr.length))];
	}

	private static String[] loremIpSum = {
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
			"Donec pellentesque vehicula mi, sit amet tincidunt est dignissim quis.",
			"Nullam pellentesque hendrerit metus, sed consectetur lorem ultrices ac.",
			"Fusce dictum mattis sapien, quis congue orci malesuada nec.",
			"Ut non dui in magna egestas luctus.",
			"Aliquam sodales eros a tortor gravida volutpat.",
			"Vestibulum vitae elit congue odio luctus egestas.",
			"Nunc vel libero ut nisi egestas rhoncus.",
			"Curabitur sollicitudin arcu ut velit vulputate egestas.",
			"Sed id quam felis, ac dictum est.",
			"Morbi non diam magna, nec fringilla felis.",
			"Nam laoreet lectus vitae turpis viverra in convallis ipsum vestibulum.",
			"Nam id justo ac quam viverra tincidunt at a quam.",
			"Ut id nulla nec ante imperdiet mollis.",
			"Etiam ut mi dolor, a placerat urna.",
			"Nam sit amet diam id lorem ultrices pretium nec nec sapien.",
			"Maecenas eget lorem et tellus laoreet pretium eget sit amet est.",
			"Sed in nulla vel purus porta ultricies non quis purus.",
			"Donec ut dui non massa convallis pharetra non nec sem.",
			"Donec id ipsum sit amet urna tempus condimentum.",
			"Sed blandit felis vitae massa bibendum eu tincidunt sapien fringilla.",
			"Phasellus posuere vehicula lorem, sit amet congue elit hendrerit rutrum.",
			"Phasellus lacinia dictum turpis, ut elementum urna tempus vitae.",
			"Morbi id neque in orci lacinia mollis egestas sit amet nisi.",
			"Donec egestas augue rutrum dui facilisis adipiscing.",
			"Nulla consectetur velit augue, nec dapibus lacus.",
			"Vivamus eu nisl sed nulla placerat egestas." };
	private JMenu mnLevel;
}
