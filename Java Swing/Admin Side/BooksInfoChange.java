package frame;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import bookandreadermanagement.DataProcessing;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JTextField;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JRadioButton;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JButton;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.event.ChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;

public class BooksInfoChange extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField ISBNText;
	private JTextField ISBNCheck;
	private JLabel itemChangeLabel;

	ButtonGroup group = new ButtonGroup();
	private JTextField titleChangeText;
	private JTextField numChangeText;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public BooksInfoChange() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1400, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("微软雅黑", Font.BOLD, 30)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("微软雅黑 Light", Font.PLAIN, 35)));

		JLabel booksInfoChangeLabel = new JLabel("\u4FEE\u6539\u56FE\u4E66\u57FA\u672C\u4FE1\u606F");
		booksInfoChangeLabel.setBackground(new Color(220, 220, 220));
		booksInfoChangeLabel.setForeground(new Color(25, 25, 112));
		booksInfoChangeLabel.setFont(new Font("微软雅黑", Font.BOLD, 60));
		booksInfoChangeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		booksInfoChangeLabel.setBounds(450, 5, 500, 65);
		contentPane.add(booksInfoChangeLabel);
		booksInfoChangeLabel.setOpaque(true);

		JLabel ISBNLabel = new JLabel("\u56FE\u4E66ISBN\u53F7\uFF1A");
		ISBNLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		ISBNLabel.setBounds(400, 90, 250, 50);
		contentPane.add(ISBNLabel);

		ISBNText = new JTextField();
		ISBNText.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		ISBNText.setBounds(650, 90, 350, 50);
		contentPane.add(ISBNText);
		ISBNText.setColumns(10);

		ISBNCheck = new JTextField();
		ISBNCheck.setEditable(false);
		ISBNCheck.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		ISBNCheck.setBounds(1010, 90, 220, 50);
		contentPane.add(ISBNCheck);
		ISBNCheck.setColumns(10);

		itemChangeLabel = new JLabel("\u8BF7\u9009\u62E9\u9700\u8981\u4FEE\u6539\u7684\u6570\u636E\u9879");
		itemChangeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		itemChangeLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 25));
		itemChangeLabel.setBounds(550, 150, 300, 30);
		contentPane.add(itemChangeLabel);

		JRadioButton titleChangeButton = new JRadioButton("\u4FEE\u6539\u6807\u9898");
		titleChangeButton.setBackground(new Color(220, 220, 220));
		titleChangeButton.setFont(new Font("微软雅黑 Light", Font.PLAIN, 30));
		titleChangeButton.setBounds(400, 180, 150, 40);
		group.add(titleChangeButton);
		contentPane.add(titleChangeButton);

		JRadioButton typeChangeButton = new JRadioButton("\u4FEE\u6539\u7C7B\u578B");
		typeChangeButton.setBackground(new Color(220, 220, 220));
		typeChangeButton.setFont(new Font("微软雅黑 Light", Font.PLAIN, 30));
		typeChangeButton.setBounds(550, 180, 150, 40);
		group.add(typeChangeButton);
		contentPane.add(typeChangeButton);

		JRadioButton numChangeButton = new JRadioButton("\u589E\u6DFB\u56FE\u4E66");
		numChangeButton.setBackground(new Color(220, 220, 220));
		numChangeButton.setFont(new Font("微软雅黑 Light", Font.PLAIN, 30));
		numChangeButton.setBounds(700, 180, 150, 40);
		group.add(numChangeButton);
		contentPane.add(numChangeButton);

		JRadioButton descChangeButton = new JRadioButton("\u4FEE\u6539\u7B80\u4ECB");
		descChangeButton.setBackground(new Color(220, 220, 220));
		descChangeButton.setFont(new Font("微软雅黑 Light", Font.PLAIN, 30));
		descChangeButton.setBounds(850, 180, 150, 40);
		group.add(descChangeButton);
		contentPane.add(descChangeButton);

		JLabel titleChangeLabel = new JLabel("\u4FEE\u6539\u6807\u9898\u4E3A:");
		titleChangeLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		titleChangeLabel.setBounds(400, 240, 220, 50);
		contentPane.add(titleChangeLabel);

		titleChangeText = new JTextField();
		titleChangeText.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		titleChangeText.setEditable(false);
		titleChangeText.setBounds(650, 240, 350, 50);
		contentPane.add(titleChangeText);
		titleChangeText.setColumns(10);

		JLabel typeChangeLabel = new JLabel("\u4FEE\u6539\u7C7B\u578B\u4E3A:");
		typeChangeLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		typeChangeLabel.setBounds(400, 310, 220, 50);
		contentPane.add(typeChangeLabel);

		JComboBox<String> typeChangeCombo = new JComboBox<String>();
		typeChangeCombo.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		typeChangeCombo.setEnabled(false);
		typeChangeCombo.setBounds(650, 310, 350, 50);
		typeChangeCombo.addItem("教材");
		typeChangeCombo.addItem("文学");
		typeChangeCombo.addItem("艺术");
		typeChangeCombo.addItem("历史");
		typeChangeCombo.addItem("哲学");
		typeChangeCombo.addItem("数理科学和化学");
		contentPane.add(typeChangeCombo);

		JLabel numChangeLabel = new JLabel("\u65B0\u589E\u4E66\u672C\u6570:");
		numChangeLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		numChangeLabel.setBounds(400, 380, 220, 50);
		contentPane.add(numChangeLabel);

		numChangeText = new JTextField();
		numChangeText.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		numChangeText.setEditable(false);
		numChangeText.setBounds(650, 380, 350, 50);
		contentPane.add(numChangeText);
		numChangeText.setColumns(10);

		JLabel descChangeLabel = new JLabel("\u4FEE\u6539\u7B80\u4ECB\u4E3A:");
		descChangeLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		descChangeLabel.setBounds(400, 450, 220, 50);
		contentPane.add(descChangeLabel);

		JScrollPane descChangePane = new JScrollPane();
		descChangePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		descChangePane.setBounds(400, 500, 600, 150);
		contentPane.add(descChangePane);

		JTextArea descChangeArea = new JTextArea();
		descChangeArea.setFont(new Font("微软雅黑 Light", Font.PLAIN, 25));
		descChangeArea.setLineWrap(true);
		descChangeArea.setEditable(false);
		descChangeArea.setRows(5);
		descChangeArea.setColumns(20);
		descChangePane.setViewportView(descChangeArea);

		JButton changeButton = new JButton("\u4FEE\u6539");
		changeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (changeButton.isEnabled() && titleChangeButton.isSelected()) {
					if (titleChangeText.getText().isBlank())
						JOptionPane.showMessageDialog(null, "图书标题不能为空!");
					else {
						DataProcessing.updateBooksTitle(ISBNText.getText(), titleChangeText.getText());
						JOptionPane.showMessageDialog(null, "图书标题已更改成功!");
						titleChangeText.setText("");
						// ISBN保留,以便用户对同一本书进行另外的操作
					}
				} else if (changeButton.isEnabled() && typeChangeButton.isSelected()) {
					DataProcessing.updateBooksType(ISBNText.getText(), typeChangeCombo.getSelectedItem().toString());
					JOptionPane.showMessageDialog(null, "图书类型已更改成功!");
				} else if (changeButton.isEnabled() && numChangeButton.isSelected()) {
					if (numChangeText.getText().isBlank())
						JOptionPane.showMessageDialog(null, "新增图书数量不能为空!");
					else {
						// 图书类表的更改
						DataProcessing.updateBooksNum(ISBNText.getText(), numChangeText.getText());

						// 图书表的更改
						SpecificNewBook page = new SpecificNewBook(ISBNText.getText(),
								Integer.parseInt(numChangeText.getText()));
						page.setLocationRelativeTo(null);
						page.setVisible(true);

						numChangeText.setText("");
					}

				} else if (descChangeButton.isSelected()) {
					DataProcessing.updateBooksDesc(ISBNText.getText(), descChangeArea.getText());
					JOptionPane.showMessageDialog(null, "图书简介已更改成功!");
					descChangeArea.setText("");
				}
			}
		});
		changeButton.setEnabled(false);
		changeButton.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		changeButton.setBounds(400, 670, 200, 60);
		contentPane.add(changeButton);

		JButton cancelButton = new JButton("\u53D6\u6D88");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		cancelButton.setBounds(800, 670, 200, 60);
		contentPane.add(cancelButton);

		// 所有的修改输入框初始化全灰,只有选中的才恢复正常

		// 选择了图书标题
		titleChangeButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (titleChangeButton.isSelected()) {
					titleChangeText.setEditable(true);
					numChangeText.setText("");
					descChangeArea.setText("");
				} else
					titleChangeText.setEditable(false);
			}
		});

		// 选择了图书类型
		typeChangeButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (typeChangeButton.isSelected()) {
					typeChangeCombo.setEnabled(true);
					titleChangeText.setText("");
					numChangeText.setText("");
					descChangeArea.setText("");
				} else
					typeChangeCombo.setEnabled(false);
			}
		});

		// 选择了图书数量
		numChangeButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (numChangeButton.isSelected()) {
					numChangeText.setEditable(true);
					titleChangeText.setText("");
					descChangeArea.setText("");
				} else
					numChangeText.setEditable(false);
			}
		});

		// 选择了图书简介
		descChangeButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (descChangeButton.isSelected()) {
					descChangeArea.setEditable(true);
					titleChangeText.setText("");
					numChangeText.setText("");
				} else
					descChangeArea.setEditable(false);
			}
		});

		// 写在最后,按钮之后,这样才能对按钮执行操作
		ISBNText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				// 搜索此书是否已经在馆的
				// 如果此书未在馆
				if (!DataProcessing.searchBookISBN(ISBNText.getText())) {
					changeButton.setEnabled(false);
					ISBNCheck.setText("此书不存在!");
				} else {
					ISBNCheck.setText("");
					changeButton.setEnabled(true);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				ISBNCheck.setText("");
			}
		});

		JLabel ISBNBckground = new JLabel("");
		ISBNBckground.setBackground(new Color(220, 220, 220));
		ISBNBckground.setBounds(395, 85, 610, 60);
		contentPane.add(ISBNBckground);
		ISBNBckground.setOpaque(true);

		JLabel inputBckground = new JLabel("");
		inputBckground.setBackground(new Color(220, 220, 220));
		inputBckground.setBounds(395, 150, 610, 505);
		contentPane.add(inputBckground);
		inputBckground.setOpaque(true);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground1400750.png"));
		} catch (IOException e) {
			System.out.println("BooksInfoChange:背景图片载入失败");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setBounds(0, -30, 1500, 800);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);

	}
}
