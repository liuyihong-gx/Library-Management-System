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
import javax.swing.ButtonGroup;
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
import javax.swing.SwingConstants;
import java.awt.Color;

public class BorrowingInfoSearch extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	ButtonGroup group = new ButtonGroup();
	private JTextField cardIDText;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public BorrowingInfoSearch() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("΢���ź�", Font.BOLD, 30)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("΢���ź� Light", Font.PLAIN, 35)));

		JLabel borrowingInfoLabel = new JLabel("\u6B63\u5728\u501F\u9605\u4FE1\u606F\u67E5\u8BE2");
		borrowingInfoLabel.setForeground(new Color(25, 25, 112));
		borrowingInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		borrowingInfoLabel.setFont(new Font("΢���ź�", Font.BOLD, 76));
		borrowingInfoLabel.setBounds(90, 5, 620, 85);
		contentPane.add(borrowingInfoLabel);

		JLabel cardIDTextLabel = new JLabel("\u501F\u4E66\u8BC1\u53F7:");
		cardIDTextLabel.setBackground(new Color(220, 220, 220));
		cardIDTextLabel.setFont(new Font("΢���ź�", Font.PLAIN, 68));
		cardIDTextLabel.setBounds(30, 180, 300, 80);
		contentPane.add(cardIDTextLabel);
		cardIDTextLabel.setOpaque(true);

		cardIDText = new JTextField();
		cardIDText.setHorizontalAlignment(SwingConstants.CENTER);
		cardIDText.setFont(new Font("΢���ź�", Font.PLAIN, 70));
		cardIDText.setBounds(330, 180, 440, 80);
		contentPane.add(cardIDText);
		cardIDText.setColumns(10);
		cardIDText.setBorder(null);

		JButton searchButton = new JButton("\u67E5\u8BE2");
		searchButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// �������֤��δ��¼�ڲ�
				if (!DataProcessing.searchCardID(cardIDText.getText())) {
					JOptionPane.showMessageDialog(null, "�Ҳ�������֤��Ϊ" + cardIDText.getText() + "�Ķ���!");
					cardIDText.setText("");
				} else {
					BorrowingSearchResult page = new BorrowingSearchResult(cardIDText.getText());
					page.setLocationRelativeTo(null);
					page.setUndecorated(true);
					page.setVisible(true);
				}
			}
		});
		searchButton.setFont(new Font("΢���ź�", Font.PLAIN, 50));
		searchButton.setBounds(140, 360, 200, 60);
		contentPane.add(searchButton);

		JButton cancelButton = new JButton("\u53D6\u6D88");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});

		cancelButton.setFont(new Font("΢���ź�", Font.PLAIN, 50));
		cancelButton.setBounds(460, 360, 200, 60);
		contentPane.add(cancelButton);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground800500.png"));
		} catch (IOException e) {
			System.out.println("BorrowingInfoSearch:����ͼƬ����ʧ��");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setBounds(0, -30, 810, 550);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);

	}

}
