package frame;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JComboBox;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;

import bookandreadermanagement.DataProcessing;

import javax.swing.event.ChangeEvent;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.SwingConstants;
import java.awt.Color;

public class BookSearch extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	ButtonGroup group = new ButtonGroup();
	private JTextField ISBNText;
	private JTextField titleText;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public BookSearch() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("微软雅黑", Font.BOLD, 30)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("微软雅黑 Light", Font.PLAIN, 35)));

		JLabel bookSearchLabel = new JLabel("\u67E5\u8BE2\u56FE\u4E66");
		bookSearchLabel.setForeground(new Color(25, 25, 112));
		bookSearchLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bookSearchLabel.setFont(new Font("微软雅黑", Font.BOLD, 60));
		bookSearchLabel.setBounds(280, 5, 240, 70);
		contentPane.add(bookSearchLabel);

		JLabel searchFeatrueLabel = new JLabel(
				"\u8BF7\u9009\u62E9\u9700\u8981\u67E5\u8BE2\u7684\u56FE\u4E66\u7279\u5F81");
		searchFeatrueLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 25));
		searchFeatrueLabel.setBounds(250, 90, 300, 30);
		contentPane.add(searchFeatrueLabel);

		JRadioButton ISBNButton = new JRadioButton("ISBN");
		ISBNButton.setBackground(new Color(220, 220, 220));
		ISBNButton.setFont(new Font("微软雅黑 Light", Font.PLAIN, 30));
		ISBNButton.setBounds(190, 120, 100, 40);
		contentPane.add(ISBNButton);

		JRadioButton titleButton = new JRadioButton("\u56FE\u4E66\u6807\u9898");
		titleButton.setBackground(new Color(220, 220, 220));
		titleButton.setFont(new Font("微软雅黑 Light", Font.PLAIN, 30));
		titleButton.setBounds(300, 120, 150, 40);
		contentPane.add(titleButton);

		JRadioButton typeButton = new JRadioButton("\u56FE\u4E66\u7C7B\u578B");
		typeButton.setBackground(new Color(220, 220, 220));
		typeButton.setFont(new Font("微软雅黑 Light", Font.PLAIN, 30));
		typeButton.setBounds(460, 120, 150, 40);
		contentPane.add(typeButton);

		group.add(ISBNButton);
		group.add(titleButton);
		group.add(typeButton);

		JLabel ISBNTextLabel = new JLabel("ISBN:");
		ISBNTextLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		ISBNTextLabel.setBounds(150, 170, 100, 50);
		contentPane.add(ISBNTextLabel);

		ISBNText = new JTextField();
		ISBNText.setHorizontalAlignment(SwingConstants.CENTER);
		ISBNText.setEditable(false);
		ISBNText.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		ISBNText.setBounds(300, 170, 350, 50);
		contentPane.add(ISBNText);
		ISBNText.setColumns(10);

		JLabel titleLabel = new JLabel("\u6807\u9898:");
		titleLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		titleLabel.setBounds(150, 240, 100, 50);
		contentPane.add(titleLabel);

		titleText = new JTextField();
		titleText.setEditable(false);
		titleText.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		titleText.setBounds(300, 240, 350, 50);
		contentPane.add(titleText);
		titleText.setColumns(10);

		JLabel typeLabel = new JLabel("\u7C7B\u578B:");
		typeLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		typeLabel.setBounds(150, 310, 100, 50);
		contentPane.add(typeLabel);

		JComboBox<String> typeCombo = new JComboBox<String>();
		typeCombo.setFont(new Font("微软雅黑", Font.PLAIN, 36));
		typeCombo.setEnabled(false);
		typeCombo.setBounds(300, 310, 350, 50);
		typeCombo.addItem("教材");
		typeCombo.addItem("文学");
		typeCombo.addItem("艺术");
		typeCombo.addItem("历史");
		typeCombo.addItem("哲学");
		typeCombo.addItem("数理科学和化学");
		contentPane.add(typeCombo);

		JButton searchButton = new JButton("\u67E5\u8BE2");
		searchButton.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		searchButton.setBounds(150, 400, 200, 60);
		contentPane.add(searchButton);

		JButton cancelButton = new JButton("\u53D6\u6D88");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});

		cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		cancelButton.setBounds(450, 400, 200, 60);
		contentPane.add(cancelButton);

		ISBNButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (ISBNButton.isSelected()) {
					ISBNText.setEditable(true);
					titleText.setText("");
				} else
					ISBNText.setEditable(false);
			}
		});

		titleButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (titleButton.isSelected()) {
					titleText.setEditable(true);
					ISBNText.setText("");
				} else
					titleText.setEditable(false);
			}
		});

		typeButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (typeButton.isSelected()) {
					typeCombo.setEnabled(true);
					ISBNText.setText("");
					titleText.setText("");
				} else
					typeCombo.setEnabled(false);
			}
		});

		searchButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (ISBNButton.isSelected()) {
					if (ISBNText.getText().isBlank())
						JOptionPane.showMessageDialog(null, "ISBN不能为空!");
					else if (!DataProcessing.searchBookISBN(ISBNText.getText())) {
						JOptionPane.showMessageDialog(null, "找不到ISBN为" + ISBNText.getText() + "的图书!");
						ISBNText.setText("");
					} else {
						// 这东西不用花括号包起来就要报错
						// int参数为1就代表输入的String是ISBN
						SearchResult page = new SearchResult(ISBNText.getText(), 1);
						page.setLocationRelativeTo(null);
						page.setUndecorated(true);
						page.setVisible(true);
					}
				} else if (titleButton.isSelected()) {
					if (titleText.getText().isBlank())
						JOptionPane.showMessageDialog(null, "标题不能为空!");
					else if (!DataProcessing.searchBookTitle(titleText.getText())) {
						JOptionPane.showMessageDialog(null, "找不到标题为" + titleText.getText() + "的图书!");
						titleText.setText("");
					} else {
						// int参数为1就代表输入的String是title
						// 这东西不用花括号包起来就要报错
						SearchResult page = new SearchResult(titleText.getText(), 2);
						page.setLocationRelativeTo(null);
						page.setUndecorated(true);
						page.setVisible(true);
					}
				} else if (typeButton.isSelected()) {
					if (!DataProcessing.searchBookType(typeCombo.getSelectedItem().toString())) {
						JOptionPane.showMessageDialog(null, "找不到类型为" + typeCombo.getSelectedItem() + "的图书!");
					} else {
						// int参数为1就代表输入的String是title
						// 这东西不用花括号包起来就要报错
						SearchResult page = new SearchResult(typeCombo.getSelectedItem().toString(), 3);
						page.setLocationRelativeTo(null);
						page.setUndecorated(true);
						page.setVisible(true);
					}
				}
			}
		});

		JLabel inputBckground = new JLabel("");
		inputBckground.setBackground(new Color(220, 220, 220));
		inputBckground.setBounds(145, 85, 510, 280);
		contentPane.add(inputBckground);
		inputBckground.setOpaque(true);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground800500.png"));
		} catch (IOException e) {
			System.out.println("BookSearch:背景图片载入失败");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setBounds(0, -30, 810, 550);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}
}
