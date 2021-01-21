package frame;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;

import bookandreadermanagement.DataProcessing;
import java.awt.Color;
import javax.swing.SwingConstants;

public class OverdueBookReturn extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField cardIDText;
	private JTextField bookIDText;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public OverdueBookReturn() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("微软雅黑", Font.BOLD, 30)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("微软雅黑 Light", Font.PLAIN, 35)));

		JLabel returnLabel = new JLabel("\u903E\u671F\u56FE\u4E66\u8FD4\u8FD8");
		returnLabel.setHorizontalAlignment(SwingConstants.CENTER);
		returnLabel.setForeground(new Color(25, 25, 112));
		returnLabel.setFont(new Font("微软雅黑", Font.BOLD, 76));
		returnLabel.setBounds(130, 5, 540, 85);
		contentPane.add(returnLabel);

		JLabel cardIDLabel = new JLabel("\u501F\u4E66\u8BC1\u53F7:");
		cardIDLabel.setBackground(new Color(220, 220, 220));
		cardIDLabel.setFont(new Font("微软雅黑", Font.PLAIN, 65));
		cardIDLabel.setBounds(100, 140, 280, 80);
		contentPane.add(cardIDLabel);
		cardIDLabel.setOpaque(true);

		cardIDText = new JTextField();
		cardIDText.setHorizontalAlignment(SwingConstants.CENTER);
		cardIDText.setFont(new Font("微软雅黑 Light", Font.PLAIN, 56));
		cardIDText.setBounds(380, 140, 320, 80);
		contentPane.add(cardIDText);
		cardIDText.setColumns(10);
		cardIDText.setBorder(null);

		JLabel bookIDLabel = new JLabel("\u56FE\u4E66\u53F7:");
		bookIDLabel.setBackground(new Color(220, 220, 220));
		bookIDLabel.setFont(new Font("微软雅黑", Font.PLAIN, 65));
		bookIDLabel.setBounds(100, 240, 280, 80);
		contentPane.add(bookIDLabel);
		bookIDLabel.setOpaque(true);

		bookIDText = new JTextField();
		bookIDText.setHorizontalAlignment(SwingConstants.CENTER);
		bookIDText.setFont(new Font("微软雅黑 Light", Font.PLAIN, 56));
		bookIDText.setBounds(380, 240, 320, 80);
		contentPane.add(bookIDText);
		bookIDText.setColumns(10);
		bookIDText.setBorder(null);

		JButton returnButton = new JButton("\u8FD4\u8FD8");
		returnButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 检查借阅证号和图书号
				// 都没有
				if (!DataProcessing.searchCardID(cardIDText.getText())
						&& !DataProcessing.searchBookID(bookIDText.getText())) {
					JOptionPane.showMessageDialog(null, "借书证号和图书号都不存在");
					bookIDText.setText("");
					cardIDText.setText("");
				}
				// 图书号存在
				else if (!DataProcessing.searchCardID(cardIDText.getText())
						&& DataProcessing.searchBookID(bookIDText.getText())) {
					JOptionPane.showMessageDialog(null, "借书证号不存在");
					cardIDText.setText("");
				}
				// 借书证号存在
				else if (!DataProcessing.searchBookID(bookIDText.getText())
						&& DataProcessing.searchCardID(cardIDText.getText())) {
					JOptionPane.showMessageDialog(null, "图书号不存在");
					bookIDText.setText("");
				}
				// 若都输入对了
				else {
					// 看看这个人有没有借这本书
					if (!DataProcessing.isMatch(bookIDText.getText(), cardIDText.getText())) {
						JOptionPane.showMessageDialog(null, "该读者未借阅此书");
						bookIDText.setText("");
					} else {
						// 执行返还操作
						int i = DataProcessing.isOverdue(bookIDText.getText(), cardIDText.getText());

						if (i == 0) {
							JOptionPane.showMessageDialog(null, "图书号为" + bookIDText.getText() + "的图书未过期，请在读者端返还");
							bookIDText.setText("");
							cardIDText.setText("");
						} else {
							String fine = new java.text.DecimalFormat("0.0").format(0.2 * i);
							// 每天罚款0.2元
							{
								int n = JOptionPane.showConfirmDialog(null, "图书号为" + bookIDText.getText() + "的图书已过期" + i
										+ "天，需要缴纳罚款" + fine + "元。请确认罚款缴清后进行还书操作", "管理员确认", JOptionPane.YES_NO_OPTION);
								if (n == 0) {
									// 进行还书操作
									{
										DataProcessing.returnThisOverdueBook(bookIDText.getText(),
												cardIDText.getText());
										JOptionPane.showMessageDialog(null,
												"图书号为" + bookIDText.getText() + "的逾期图书已成功返还");
										bookIDText.setText("");
										cardIDText.setText("");
									}
								}
							}
						}
					}

				}
			}
		});
		returnButton.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		returnButton.setBounds(180, 370, 200, 60);
		contentPane.add(returnButton);

		JButton cancelButton = new JButton("\u53D6\u6D88");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});

		cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		cancelButton.setBounds(420, 370, 200, 60);
		contentPane.add(cancelButton);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground800500.png"));
		} catch (IOException e) {
			System.out.println("OverdueBookReturn:背景图片载入失败");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setBounds(0, -30, 810, 550);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}

}
