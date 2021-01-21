package frame;

import bookandreadermanagement.*;

import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainPage extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String driverName = "com.mysql.cj.jdbc.Driver"; // º”‘ÿ ˝æ›ø‚«˝∂Ø¿‡
					String url = "jdbc:mysql://localhost:3306/librarydb?serverTimezone=GMT%2B8&useSSL=false"; // …˘√˜ ˝æ›ø‚µƒURL(º«µ√º”»Î ±«¯£¨∑Ò‘Úª·µº÷¬SQLException)
					String user = "root"; //  ˝æ›ø‚”√ªß
					String password = "1999Lyh#";
					DataProcessing.connectToDB(driverName, url, user, password);

					if (DataProcessing.connectResult) {
						MainPage page = new MainPage();
						page.setLocationRelativeTo(null);
						page.setUndecorated(true);
						page.setResizable(false);
						page.setVisible(true);
					}
				} catch (SQLException e) {
					System.out.println("MainPage: ˝æ›ø‚¡¨Ω” ß∞‹");
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainPage() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 800, 60);
		contentPane.add(menuBar);

		JMenu bookInfo = new JMenu("\u56FE\u4E66\u4FE1\u606F\u7BA1\u7406");
		bookInfo.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 40));
		menuBar.add(bookInfo);

		JMenuItem newBookIn = new JMenuItem("\u767B\u8BB0\u65B0\u4E66");
		newBookIn.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 36));
		newBookIn.addActionListener(new ActionListener() {

			//  π”√mouseclickedº‡Ã˝Œﬁ–ß£¨JMenuItem≤ª≥‘≥£πÊ Û±Í ¬º˛
			public void actionPerformed(ActionEvent e) {
				NewBook page = new NewBook();
				page.setLocationRelativeTo(null);
				page.setUndecorated(true);
				page.setVisible(true);
				// ’‚Ãı“≤ «±ÿ–Îµƒ
			}
		});
		bookInfo.add(newBookIn);

		JMenu changeBookInfo = new JMenu("\u66F4\u6539\u56FE\u4E66\u4FE1\u606F");
		changeBookInfo.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 36));
		bookInfo.add(changeBookInfo);

		JMenuItem changeBooksInfo = new JMenuItem("\u66F4\u6539\u56FE\u4E66\u57FA\u672C\u4FE1\u606F");
		changeBooksInfo.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 36));
		changeBooksInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BooksInfoChange page = new BooksInfoChange();
				page.setLocationRelativeTo(null);
				page.setUndecorated(true);
				page.setVisible(true);
			}
		});
		changeBookInfo.add(changeBooksInfo);

		JMenuItem changeBookPos = new JMenuItem("\u56FE\u4E66\u4F4D\u7F6E\u53D8\u52A8");
		changeBookPos.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 36));
		changeBookPos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BookPositionChange page = new BookPositionChange();
				page.setLocationRelativeTo(null);
				page.setUndecorated(true);
				page.setVisible(true);
			}
		});
		changeBookInfo.add(changeBookPos);

		JMenu deleteBook = new JMenu("\u5220\u9664\u56FE\u4E66");
		deleteBook.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 36));
		bookInfo.add(deleteBook);

		JMenuItem deleteAll = new JMenuItem("\u8BE5ISBN\u4E0B\u6240\u6709\u56FE\u4E66");
		deleteAll.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 36));
		deleteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BooksDelete page = new BooksDelete();
				page.setLocationRelativeTo(null);
				page.setUndecorated(true);
				page.setVisible(true);
			}
		});
		deleteBook.add(deleteAll);

		JMenuItem deleteOne = new JMenuItem("\u5355\u672C\u56FE\u4E66");
		deleteOne.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 36));
		deleteOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SpecificBookDelete page = new SpecificBookDelete();
				page.setLocationRelativeTo(null);
				page.setUndecorated(true);
				page.setVisible(true);
			}
		});
		deleteBook.add(deleteOne);

		JMenuItem searchBook = new JMenuItem("\u67E5\u8BE2\u56FE\u4E66");
		searchBook.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 36));
		searchBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BookSearch page = new BookSearch();
				page.setLocationRelativeTo(null);
				page.setUndecorated(true);
				page.setVisible(true);
			}
		});
		bookInfo.add(searchBook);

		JMenuItem overdueBook = new JMenuItem("\u903E\u671F\u56FE\u4E66\u8FD4\u8FD8");
		overdueBook.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 36));
		overdueBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OverdueBookReturn page = new OverdueBookReturn();
				page.setLocationRelativeTo(null);
				page.setUndecorated(true);
				page.setVisible(true);
			}
		});
		bookInfo.add(overdueBook);

		JMenu readerInfo = new JMenu("\u7528\u6237\u4FE1\u606F\u7BA1\u7406");
		readerInfo.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 40));
		menuBar.add(readerInfo);

		JMenuItem newReader = new JMenuItem("\u767B\u8BB0\u65B0\u8BFB\u8005");
		newReader.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 36));
		newReader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewReader page = new NewReader();
				page.setLocationRelativeTo(null);
				page.setUndecorated(true);
				page.setVisible(true);
			}
		});
		readerInfo.add(newReader);

		JMenuItem changeReaderInfo = new JMenuItem("\u4FEE\u6539\u8BFB\u8005\u4FE1\u606F");
		changeReaderInfo.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 36));
		changeReaderInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReadersInfoChange page = new ReadersInfoChange();
				page.setLocationRelativeTo(null);
				page.setUndecorated(true);
				page.setVisible(true);
			}
		});
		readerInfo.add(changeReaderInfo);

		JMenu readerBasicInfo = new JMenu("\u8BFB\u8005\u4FE1\u606F\u67E5\u8BE2");
		readerBasicInfo.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 36));
		readerInfo.add(readerBasicInfo);

		JMenuItem generalInfo = new JMenuItem("\u8BFB\u8005\u57FA\u672C\u4FE1\u606F");
		generalInfo.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 36));
		generalInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReaderSearch page = new ReaderSearch();
				page.setLocationRelativeTo(null);
				page.setUndecorated(true);
				page.setVisible(true);
			}
		});
		readerBasicInfo.add(generalInfo);

		JMenuItem borrowingInfo = new JMenuItem("\u6B63\u5728\u501F\u9605\u4FE1\u606F");
		borrowingInfo.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 36));
		borrowingInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BorrowingInfoSearch page = new BorrowingInfoSearch();
				page.setLocationRelativeTo(null);
				page.setUndecorated(true);
				page.setVisible(true);
			}
		});
		readerBasicInfo.add(borrowingInfo);

		JMenuItem borrowHistory = new JMenuItem("\u5386\u53F2\u501F\u9605\u4FE1\u606F");
		borrowHistory.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 36));
		borrowHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BorrowHistorySearch page = new BorrowHistorySearch();
				page.setLocationRelativeTo(null);
				page.setUndecorated(true);
				page.setVisible(true);
			}
		});
		readerBasicInfo.add(borrowHistory);

		JMenuItem deleteReader = new JMenuItem("\u5220\u9664\u8BFB\u8005");
		deleteReader.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 36));
		deleteReader.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReaderDelete page = new ReaderDelete();
				page.setLocationRelativeTo(null);
				page.setUndecorated(true);
				page.setVisible(true);
			}
		});
		readerInfo.add(deleteReader);

		JButton quitButton = new JButton("\u9000\u51FA\u7CFB\u7EDF");
		quitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		quitButton.setBounds(570, 440, 220, 50);
		contentPane.add(quitButton);
		quitButton.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.BOLD, 35));
		quitButton.setForeground(new Color(107, 142, 35));

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground800500.png"));
		} catch (IOException e) {
			System.out.println("MainPage:±≥æ∞Õº∆¨‘ÿ»Î ß∞‹");
		}

		JLabel background = new JLabel(new ImageIcon(img));
		background.setBounds(0, -30, 810, 550);
		contentPane.add(background);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}
}
