package frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import readerfunction.DataProcessing;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class MainPage extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	/**
	 * Create the frame.
	 */
	public MainPage(String cardID) {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// 查询借阅按钮
		JButton searchNborrowButton = new JButton("\u67E5\u8BE2\u501F\u9605");
		searchNborrowButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 先建好表再构造page,要不然没办法在一开始对临时预定表进行刷新
				DataProcessing.createReserveTable(cardID);

				BookSearch page = new BookSearch(cardID);
				page.setLocationRelativeTo(null);
				page.setUndecorated(true);
				page.setResizable(false);
				page.setVisible(true);
			}
		});

		JLabel readerLabel = new JLabel("");
		readerLabel.setForeground(new Color(0, 0, 0));
		readerLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 34));
		readerLabel.setBounds(10, 10, 750, 40);
		contentPane.add(readerLabel);
		readerLabel.setText(
				"读者:" + DataProcessing.cardIDToName(cardID) + " [" + DataProcessing.cardIDToStatus(cardID) + "]");

		// 查询借阅按钮
		searchNborrowButton.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		searchNborrowButton.setBounds(250, 100, 300, 62);
		contentPane.add(searchNborrowButton);

		// 续借按钮
		JButton renewButton = new JButton("\u7EED\u501F");
		renewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BookRenew page = new BookRenew(cardID);
				page.setLocationRelativeTo(null);
				page.setUndecorated(true);
				page.setVisible(true);
			}
		});
		renewButton.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		renewButton.setBounds(250, 200, 300, 62);
		contentPane.add(renewButton);

		// 返还按钮
		JButton returnButton = new JButton("\u8FD4\u8FD8");
		returnButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				BookReturn page = new BookReturn(cardID);
				page.setLocationRelativeTo(null);
				page.setUndecorated(true);
				page.setVisible(true);
			}
		});
		returnButton.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		returnButton.setBounds(250, 300, 300, 62);
		contentPane.add(returnButton);

		JButton quitButton = new JButton("\u9000\u51FA");
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		quitButton.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		quitButton.setBounds(250, 400, 300, 62);
		contentPane.add(quitButton);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground800500.png"));
		} catch (IOException e) {
			System.out.println("MainPage:背景图片载入失败");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setText("");
		bckground.setBounds(0, -30, 810, 550);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}
}
