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

		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("΢���ź�", Font.BOLD, 30)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("΢���ź� Light", Font.PLAIN, 35)));

		JLabel returnLabel = new JLabel("\u903E\u671F\u56FE\u4E66\u8FD4\u8FD8");
		returnLabel.setHorizontalAlignment(SwingConstants.CENTER);
		returnLabel.setForeground(new Color(25, 25, 112));
		returnLabel.setFont(new Font("΢���ź�", Font.BOLD, 76));
		returnLabel.setBounds(130, 5, 540, 85);
		contentPane.add(returnLabel);

		JLabel cardIDLabel = new JLabel("\u501F\u4E66\u8BC1\u53F7:");
		cardIDLabel.setBackground(new Color(220, 220, 220));
		cardIDLabel.setFont(new Font("΢���ź�", Font.PLAIN, 65));
		cardIDLabel.setBounds(100, 140, 280, 80);
		contentPane.add(cardIDLabel);
		cardIDLabel.setOpaque(true);

		cardIDText = new JTextField();
		cardIDText.setHorizontalAlignment(SwingConstants.CENTER);
		cardIDText.setFont(new Font("΢���ź� Light", Font.PLAIN, 56));
		cardIDText.setBounds(380, 140, 320, 80);
		contentPane.add(cardIDText);
		cardIDText.setColumns(10);
		cardIDText.setBorder(null);

		JLabel bookIDLabel = new JLabel("\u56FE\u4E66\u53F7:");
		bookIDLabel.setBackground(new Color(220, 220, 220));
		bookIDLabel.setFont(new Font("΢���ź�", Font.PLAIN, 65));
		bookIDLabel.setBounds(100, 240, 280, 80);
		contentPane.add(bookIDLabel);
		bookIDLabel.setOpaque(true);

		bookIDText = new JTextField();
		bookIDText.setHorizontalAlignment(SwingConstants.CENTER);
		bookIDText.setFont(new Font("΢���ź� Light", Font.PLAIN, 56));
		bookIDText.setBounds(380, 240, 320, 80);
		contentPane.add(bookIDText);
		bookIDText.setColumns(10);
		bookIDText.setBorder(null);

		JButton returnButton = new JButton("\u8FD4\u8FD8");
		returnButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// ������֤�ź�ͼ���
				// ��û��
				if (!DataProcessing.searchCardID(cardIDText.getText())
						&& !DataProcessing.searchBookID(bookIDText.getText())) {
					JOptionPane.showMessageDialog(null, "����֤�ź�ͼ��Ŷ�������");
					bookIDText.setText("");
					cardIDText.setText("");
				}
				// ͼ��Ŵ���
				else if (!DataProcessing.searchCardID(cardIDText.getText())
						&& DataProcessing.searchBookID(bookIDText.getText())) {
					JOptionPane.showMessageDialog(null, "����֤�Ų�����");
					cardIDText.setText("");
				}
				// ����֤�Ŵ���
				else if (!DataProcessing.searchBookID(bookIDText.getText())
						&& DataProcessing.searchCardID(cardIDText.getText())) {
					JOptionPane.showMessageDialog(null, "ͼ��Ų�����");
					bookIDText.setText("");
				}
				// �����������
				else {
					// �����������û�н��Ȿ��
					if (!DataProcessing.isMatch(bookIDText.getText(), cardIDText.getText())) {
						JOptionPane.showMessageDialog(null, "�ö���δ���Ĵ���");
						bookIDText.setText("");
					} else {
						// ִ�з�������
						int i = DataProcessing.isOverdue(bookIDText.getText(), cardIDText.getText());

						if (i == 0) {
							JOptionPane.showMessageDialog(null, "ͼ���Ϊ" + bookIDText.getText() + "��ͼ��δ���ڣ����ڶ��߶˷���");
							bookIDText.setText("");
							cardIDText.setText("");
						} else {
							String fine = new java.text.DecimalFormat("0.0").format(0.2 * i);
							// ÿ�췣��0.2Ԫ
							{
								int n = JOptionPane.showConfirmDialog(null, "ͼ���Ϊ" + bookIDText.getText() + "��ͼ���ѹ���" + i
										+ "�죬��Ҫ���ɷ���" + fine + "Ԫ����ȷ�Ϸ���������л������", "����Աȷ��", JOptionPane.YES_NO_OPTION);
								if (n == 0) {
									// ���л������
									{
										DataProcessing.returnThisOverdueBook(bookIDText.getText(),
												cardIDText.getText());
										JOptionPane.showMessageDialog(null,
												"ͼ���Ϊ" + bookIDText.getText() + "������ͼ���ѳɹ�����");
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
		returnButton.setFont(new Font("΢���ź�", Font.PLAIN, 50));
		returnButton.setBounds(180, 370, 200, 60);
		contentPane.add(returnButton);

		JButton cancelButton = new JButton("\u53D6\u6D88");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});

		cancelButton.setFont(new Font("΢���ź�", Font.PLAIN, 50));
		cancelButton.setBounds(420, 370, 200, 60);
		contentPane.add(cancelButton);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground800500.png"));
		} catch (IOException e) {
			System.out.println("OverdueBookReturn:����ͼƬ����ʧ��");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setBounds(0, -30, 810, 550);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}

}
