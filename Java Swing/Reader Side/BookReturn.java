package frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;

import readerfunction.DataProcessing;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import javax.swing.SwingConstants;

public class BookReturn extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField bookIDText;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	/**
	 * Create the frame.
	 */
	public BookReturn(String cardID) {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("微软雅黑", Font.BOLD, 30)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("微软雅黑 Light", Font.PLAIN, 35)));

		JLabel returnLabel = new JLabel("\u8FD4\u8FD8\u56FE\u4E66");
		returnLabel.setHorizontalAlignment(SwingConstants.CENTER);
		returnLabel.setForeground(new Color(25, 25, 112));
		returnLabel.setFont(new Font("微软雅黑", Font.BOLD, 76));
		returnLabel.setBounds(130, 5, 540, 85);
		contentPane.add(returnLabel);

		JLabel bookIDLabel = new JLabel("\u56FE\u4E66\u53F7:");
		bookIDLabel.setBackground(new Color(220, 220, 220));
		bookIDLabel.setFont(new Font("微软雅黑", Font.PLAIN, 68));
		bookIDLabel.setBounds(140, 200, 220, 80);
		contentPane.add(bookIDLabel);
		bookIDLabel.setOpaque(true);

		bookIDText = new JTextField();
		bookIDText.setFont(new Font("微软雅黑", Font.PLAIN, 70));
		bookIDText.setBounds(360, 200, 300, 80);
		contentPane.add(bookIDText);
		bookIDText.setColumns(10);

		JButton returnButton = new JButton("\u8FD4\u8FD8");
		returnButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!DataProcessing.searchBookID(bookIDText.getText())) {
					JOptionPane.showMessageDialog(null, "图书号不存在");
					bookIDText.setText("");
				}
				// 若都输入对了
				else {
					// 看看这个人有没有借这本书
					if (!DataProcessing.isMatch(bookIDText.getText(), cardID)) {
						JOptionPane.showMessageDialog(null, "您未借阅此书");
						bookIDText.setText("");
					} else {
						// 执行返还操作
						int i = DataProcessing.returnThisBook(bookIDText.getText(), cardID);
						if (i == 0) {
							JOptionPane.showMessageDialog(null, "图书号为" + bookIDText.getText() + "的图书退还成功");
							bookIDText.setText("");
						} else {
							String fine = new java.text.DecimalFormat("0.0").format(0.2 * i);
							// 每天罚款0.2元
							{
								JOptionPane.showMessageDialog(null, "图书号为" + bookIDText.getText() + "的图书已过期" + i
										+ "天, 需要缴纳罚款" + fine + "元。\n\n" + "请至管理员处缴纳罚款并还书。");
								bookIDText.setText("");
							}
						}
					}

				}
			}
		});
		returnButton.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		returnButton.setBounds(140, 360, 200, 60);
		contentPane.add(returnButton);

		JButton cancelButton = new JButton("\u53D6\u6D88");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});

		cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		cancelButton.setBounds(460, 360, 200, 60);
		contentPane.add(cancelButton);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground800500.png"));
		} catch (IOException e) {
			System.out.println("BookReturn:背景图片载入失败");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setBounds(0, -30, 810, 550);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}
}
