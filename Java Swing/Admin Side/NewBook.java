package frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;

import bookandreadermanagement.DataProcessing;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ScrollPaneConstants;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.Color;

public class NewBook extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField ISBNText;
	private JTextField ISBNCheck;
	private JTextField titleText;
	private JTextField bookNumText;

	private String ISBN;
	private String title;
	private String type;
	private String bookNumberString;
	private String desc;

	private int bookNumberInt;
	private JTextField titleCheck;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public NewBook() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1400, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("微软雅黑", Font.BOLD, 30)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("微软雅黑 Light", Font.PLAIN, 35)));

		JLabel newBookIn = new JLabel("\u767B\u8BB0\u65B0\u4E66");
		newBookIn.setBackground(new Color(220, 220, 220));
		newBookIn.setForeground(new Color(25, 25, 112));
		newBookIn.setHorizontalAlignment(SwingConstants.CENTER);
		newBookIn.setFont(new Font("微软雅黑", Font.BOLD, 76));
		newBookIn.setBounds(540, 5, 320, 85);
		contentPane.add(newBookIn);
		newBookIn.setOpaque(true);

		JLabel ISBNLabel = new JLabel("\u8BF7\u8F93\u5165ISBN:");
		ISBNLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		ISBNLabel.setBounds(250, 120, 300, 50);
		contentPane.add(ISBNLabel);

		ISBNText = new JTextField();
		ISBNText.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		ISBNText.setBounds(550, 120, 320, 50);
		contentPane.add(ISBNText);
		ISBNText.setColumns(10);

		ISBNCheck = new JTextField();
		ISBNCheck.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		ISBNCheck.setEditable(false);
		ISBNCheck.setBounds(900, 120, 250, 50);
		contentPane.add(ISBNCheck);
		ISBNCheck.setColumns(10);

		JLabel titleLabel = new JLabel("\u8BF7\u8F93\u5165\u56FE\u4E66\u6807\u9898:");
		titleLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		titleLabel.setBounds(250, 180, 300, 50);
		contentPane.add(titleLabel);

		titleText = new JTextField();
		titleText.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		titleText.setBounds(550, 180, 320, 50);
		contentPane.add(titleText);
		titleText.setColumns(10);

		titleCheck = new JTextField();
		titleCheck.setText("\u8BF7\u53BB\u6389\u4E66\u540D\u53F7");
		titleCheck.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		titleCheck.setEditable(false);
		titleCheck.setBounds(900, 180, 250, 50);
		contentPane.add(titleCheck);
		titleCheck.setColumns(10);

		JLabel typeChoiceLabel = new JLabel("\u8BF7\u9009\u62E9\u56FE\u4E66\u7C7B\u578B:");
		typeChoiceLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		typeChoiceLabel.setBounds(250, 240, 300, 50);
		contentPane.add(typeChoiceLabel);

		JComboBox<String> typeChoice = new JComboBox<String>();
		typeChoice.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		typeChoice.setBounds(550, 240, 320, 50);
		typeChoice.addItem("教材");
		typeChoice.addItem("文学");
		typeChoice.addItem("艺术");
		typeChoice.addItem("历史");
		typeChoice.addItem("哲学");
		typeChoice.addItem("数理科学和化学");
		contentPane.add(typeChoice);

		JLabel bookNumLabel = new JLabel("\u8BF7\u8F93\u5165\u56FE\u4E66\u6570\u91CF:");
		bookNumLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		bookNumLabel.setBounds(250, 300, 300, 50);
		contentPane.add(bookNumLabel);

		bookNumText = new JTextField();
		bookNumText.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		bookNumText.setBounds(550, 300, 320, 50);
		contentPane.add(bookNumText);
		bookNumText.setColumns(10);

		JLabel descriptionLabel = new JLabel("\u56FE\u4E66\u7B80\u4ECB(\u53EF\u7A7A):");
		descriptionLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		descriptionLabel.setBounds(250, 360, 900, 50);
		contentPane.add(descriptionLabel);

		JScrollPane descriptionPane = new JScrollPane();
		descriptionPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		descriptionPane.setBounds(250, 410, 900, 250);
		contentPane.add(descriptionPane);

		// textField是不能添加滚动条的，只能使用textArea
		JTextArea descriptionTextArea = new JTextArea();
		descriptionTextArea.setLineWrap(true);
		descriptionTextArea.setFont(new Font("微软雅黑 Light", Font.PLAIN, 25));
		descriptionTextArea.setColumns(20);
		descriptionTextArea.setRows(1);
		descriptionPane.setViewportView(descriptionTextArea);

		JButton register = new JButton("\u767B\u8BB0");
		register.setEnabled(false);
		register.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (register.isEnabled()) {
					if (titleText.getText().isBlank() && bookNumText.getText().isBlank()) {
						JOptionPane.showMessageDialog(null, "图书标题和图书数量不能为空!");
					} else if (titleText.getText().isBlank()) {
						JOptionPane.showMessageDialog(null, "图书标题不能为空!");
					} else if (bookNumText.getText().isBlank()) {
						JOptionPane.showMessageDialog(null, "图书数量不能为空!");
					} else {

						// 判断图书数量输入框中输入的是否全部都是数字,若不是则全部清空
						int allNum = 0;
						char[] allNumCheck = bookNumText.getText().toCharArray();
						for (int i = 0; i < bookNumText.getText().length(); i++) {
							if (allNumCheck[i] < '0' || allNumCheck[i] > '9')
								allNum = 1;
						}
						if (allNum == 1) {
							JOptionPane.showMessageDialog(null, "图书数量应为数字,请检查!");
							bookNumText.setText("");
						} else {
							ISBN = ISBNText.getText();
							title = titleText.getText();
							type = typeChoice.getSelectedItem().toString();
							bookNumberString = bookNumText.getText();
							desc = descriptionTextArea.getText();

							// 这个是用于输入具体图书号信息时计数的
							bookNumberInt = Integer.parseInt(bookNumText.getText());

							// 执行相关的数据库操作
							DataProcessing.newBooksIn(ISBN, title, type, bookNumberString, desc);

							SpecificNewBook page = new SpecificNewBook(ISBN, bookNumberInt);

							page.setUndecorated(true);
							page.setLocationRelativeTo(null);
							page.setVisible(true);

							ISBNText.setText("");
							titleText.setText("");
							bookNumText.setText("");
							descriptionTextArea.setText("");
							// 不要关闭页面，把东西全部变空，然后等待新的书的输入
						}
					}
				}
			}
		});
		register.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		register.setBounds(350, 670, 200, 60);
		contentPane.add(register);

		JButton cancel = new JButton("\u53D6\u6D88");
		cancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		cancel.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		cancel.setBounds(850, 670, 200, 60);
		contentPane.add(cancel);

		// 写在bookIn按钮下面才能检测到的
		// 用focusLost更好一点,不会每按一次键就检测。
		ISBNText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				// 搜索此书是否已经在馆的
				// 如果此书已在馆

				if (DataProcessing.searchBookISBN(ISBNText.getText())) {
					register.setEnabled(false);
					ISBNCheck.setText("此书已在馆");
				} else if (ISBNText.getText().length() != 13) {
					register.setEnabled(false);
					ISBNCheck.setText("ISBN应为13位");
				} else {
					// 对每一位进行检查,正确情况下应该都是数字
					int allnumber = 0;
					char[] verify = ISBNText.getText().toCharArray();
					for (int i = 0; i < 13; i++) {
						if (verify[i] < '0' || verify[i] > '9')
							allnumber = 1;
					}
					if (allnumber == 0) {
						ISBNCheck.setText("");
						register.setEnabled(true);
					} else {
						register.setEnabled(false);
						ISBNCheck.setText("存在非法字符");
					}
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				ISBNCheck.setText("");
			}
		});

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground1400750.png"));
		} catch (IOException e) {
			System.out.println("NewBook:背景图片载入失败");
		}

		JLabel inputBckground = new JLabel("");
		inputBckground.setBackground(new Color(220, 220, 220));
		inputBckground.setBounds(245, 115, 910, 550);
		contentPane.add(inputBckground);
		inputBckground.setOpaque(true);

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setBounds(0, -30, 1500, 800);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);

	}
}
