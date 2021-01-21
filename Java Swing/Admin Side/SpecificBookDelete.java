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
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;

import bookandreadermanagement.DataProcessing;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.Color;

public class SpecificBookDelete extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField deleteBookIDText;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public SpecificBookDelete() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("微软雅黑", Font.BOLD, 30)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("微软雅黑 Light", Font.PLAIN, 35)));

		JLabel bookDeleteLabel = new JLabel("\u5220\u9664\u5355\u672C\u56FE\u4E66");
		bookDeleteLabel.setForeground(new Color(25, 25, 112));
		bookDeleteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bookDeleteLabel.setFont(new Font("微软雅黑", Font.BOLD, 60));
		bookDeleteLabel.setBounds(120, 10, 600, 70);
		contentPane.add(bookDeleteLabel);

		JLabel deleteBookIDLabel = new JLabel("\u8981\u5220\u9664\u4E66\u7684\u56FE\u4E66\u53F7:");
		deleteBookIDLabel.setBackground(new Color(220, 220, 220));
		deleteBookIDLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 50));
		deleteBookIDLabel.setBounds(25, 190, 420, 60);
		contentPane.add(deleteBookIDLabel);
		deleteBookIDLabel.setOpaque(true);

		deleteBookIDText = new JTextField();
		deleteBookIDText.setHorizontalAlignment(SwingConstants.CENTER);
		deleteBookIDText.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		deleteBookIDText.setBounds(445, 190, 330, 60);
		contentPane.add(deleteBookIDText);
		deleteBookIDText.setColumns(10);
		deleteBookIDText.setBorder(null);

		JButton deleteButton = new JButton("\u5220\u9664");
		deleteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!DataProcessing.searchBookID(deleteBookIDText.getText())) {
					JOptionPane.showMessageDialog(null, "此图书号对应的书不存在!");
					deleteBookIDText.setText("");
				} else {
					int lastNum = DataProcessing.howManyLast(deleteBookIDText.getText());

					if (lastNum > 1) {
						int n = JOptionPane.showConfirmDialog(null, "确认删除图书号[" + deleteBookIDText.getText() + "]这本书吗?",
								"管理员确认", JOptionPane.YES_NO_OPTION);
						if (n == 0) {
							DataProcessing.deleteSpecificBook(deleteBookIDText.getText());
							dispose();
							JOptionPane.showMessageDialog(null, "该本图书信息已成功删除!");
						}
					} else {
						int n = JOptionPane.showConfirmDialog(null,
								"图书号[" + deleteBookIDText.getText() + "]为图书《"
										+ DataProcessing.bookIDToName(deleteBookIDText.getText()) + "》最后一本,确认删除吗?",
								"管理员确认", JOptionPane.YES_NO_OPTION);
						if (n == 0) {
							dispose();
							JOptionPane.showMessageDialog(null, "图书《"
									+ DataProcessing.ISBNToName(DataProcessing.bookIDToISBN(deleteBookIDText.getText()))
									+ "》所有信息已成功删除!");

							// 先做这一步,要不然最后一本图书在图书表被删除之后,就无法使用这个方法将图书号转换成ISBN了
							String ISBN = DataProcessing.bookIDToISBN(deleteBookIDText.getText());

							DataProcessing.deleteSpecificBook(deleteBookIDText.getText());
							DataProcessing.deleteBooks(ISBN);
						}
					}
				}
			}
		});
		deleteButton.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		deleteButton.setBounds(140, 340, 200, 60);
		contentPane.add(deleteButton);

		JButton cancelButton = new JButton("\u53D6\u6D88");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		cancelButton.setBounds(460, 340, 200, 60);
		contentPane.add(cancelButton);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground800500.png"));
		} catch (IOException e) {
			System.out.println("SpecificBookDelete:背景图片载入失败");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setBounds(0, -30, 810, 550);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}
}
