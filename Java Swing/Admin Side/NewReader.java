package frame;

import java.awt.Color;
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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;

import bookandreadermanagement.DataProcessing;

public class NewReader extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField cardIDText;
	private JTextField nameText;

	private String cardID;
	private String name;
	private String phone;
	private String status;

	private JTextField phoneText;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public NewReader() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1400, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("微软雅黑", Font.BOLD, 30)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("微软雅黑 Light", Font.PLAIN, 35)));

		JLabel newReaderIn = new JLabel("\u767B\u8BB0\u65B0\u8BFB\u8005");
		newReaderIn.setBackground(new Color(220, 220, 220));
		newReaderIn.setForeground(new Color(25, 25, 112));
		newReaderIn.setHorizontalAlignment(SwingConstants.CENTER);
		newReaderIn.setFont(new Font("微软雅黑", Font.BOLD, 76));
		newReaderIn.setBounds(500, 5, 400, 85);
		contentPane.add(newReaderIn);
		newReaderIn.setOpaque(true);

		JLabel cardIDLabel = new JLabel("\u56FE\u4E66\u8BC1\u53F7:");
		cardIDLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		cardIDLabel.setBounds(350, 140, 190, 50);
		contentPane.add(cardIDLabel);

		cardIDText = new JTextField();
		cardIDText.setHorizontalAlignment(SwingConstants.CENTER);
		cardIDText.setEditable(false);
		cardIDText.setText(DataProcessing.getCardID());
		cardIDText.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		cardIDText.setBounds(540, 140, 320, 50);
		contentPane.add(cardIDText);
		cardIDText.setColumns(10);

		JLabel nameLabel = new JLabel("\u8BFB\u8005\u59D3\u540D:");
		nameLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		nameLabel.setBounds(350, 240, 190, 50);
		contentPane.add(nameLabel);

		nameText = new JTextField();
		nameText.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		nameText.setBounds(540, 240, 320, 50);
		contentPane.add(nameText);
		nameText.setColumns(10);

		JLabel phoneLabel = new JLabel("\u7535\u8BDD\u53F7\u7801:");
		phoneLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		phoneLabel.setBounds(350, 340, 190, 50);
		contentPane.add(phoneLabel);

		JComboBox<String> statusChoice = new JComboBox<String>();
		statusChoice.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		statusChoice.setBounds(540, 440, 320, 50);
		statusChoice.addItem("学生");
		statusChoice.addItem("教师");

		phoneText = new JTextField();
		phoneText.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		phoneText.setBounds(540, 340, 320, 50);
		contentPane.add(phoneText);
		phoneText.setColumns(10);

		JLabel statusLabel = new JLabel("\u8BFB\u8005\u8EAB\u4EFD:");
		statusLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		statusLabel.setBounds(350, 440, 190, 50);
		contentPane.add(statusLabel);
		contentPane.add(statusChoice);

		JButton newReader = new JButton("\u767B\u8BB0");
		newReader.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (nameText.getText().isBlank() || phoneText.getText().isBlank())
					JOptionPane.showMessageDialog(null, "请确保每一项信息填写完整!");

				// 判断电话位数是否正确
				else if (phoneText.getText().length() != 11)
					JOptionPane.showMessageDialog(null, "手机号位数不正确,请检查!");
				else {
					// 判断到电话位数正确之后,判断电话是否都为数字
					int allNum = 0;

					char[] checkPhoneNum = phoneText.getText().toCharArray();
					for (int i = 0; i < 11; i++) {
						if (checkPhoneNum[i] < '0' || checkPhoneNum[i] > '9')
							allNum = 1;
					}

					if (allNum == 1)
						JOptionPane.showMessageDialog(null, "手机号码有不合法字符,请检查!");
					else {
						cardID = cardIDText.getText();
						name = nameText.getText();
						phone = phoneText.getText();
						status = statusChoice.getSelectedItem().toString();

						// 执行相关的数据库操作
						DataProcessing.newReaderIn(cardID, name, phone, status);

						cardIDText.setText(DataProcessing.getCardID());
						nameText.setText("");
						phoneText.setText("");

						JOptionPane.showMessageDialog(null, name + "(" + status + ")已成功登记!借书证号为:" + cardID);
						// 不要关闭页面，把东西全部变空，然后等待新读者登记
					}
				}
			}
		});
		newReader.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		newReader.setBounds(420, 580, 200, 60);
		contentPane.add(newReader);

		JButton cancel = new JButton("\u53D6\u6D88");
		cancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		cancel.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		cancel.setBounds(780, 580, 200, 60);
		contentPane.add(cancel);

		JLabel inputBckground = new JLabel("");
		inputBckground.setBackground(new Color(220, 220, 220));
		inputBckground.setBounds(345, 135, 710, 360);
		contentPane.add(inputBckground);
		inputBckground.setOpaque(true);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground1400750.png"));
		} catch (IOException e) {
			System.out.println("NewReader:背景图片载入失败");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setBounds(0, -30, 1500, 800);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}
}
