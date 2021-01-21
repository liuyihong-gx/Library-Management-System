package frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;

import bookandreadermanagement.DataProcessing;

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
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BooksDelete extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField deleteISBNText;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public BooksDelete() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("΢���ź�", Font.BOLD, 30)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("΢���ź� Light", Font.PLAIN, 35)));

		JLabel bookDeleteLabel = new JLabel("\u5220\u9664\u8BE5ISBN\u4E0B\u6240\u6709\u56FE\u4E66");
		bookDeleteLabel.setForeground(new Color(25, 25, 112));
		bookDeleteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bookDeleteLabel.setFont(new Font("΢���ź�", Font.BOLD, 60));
		bookDeleteLabel.setBounds(80, 5, 640, 70);
		contentPane.add(bookDeleteLabel);

		JLabel deleteISBNLabel = new JLabel("\u8981\u5220\u9664\u4E66\u7684ISBN:");
		deleteISBNLabel.setBackground(new Color(220, 220, 220));
		deleteISBNLabel.setFont(new Font("΢���ź� Light", Font.PLAIN, 50));
		deleteISBNLabel.setBounds(25, 190, 380, 60);
		contentPane.add(deleteISBNLabel);
		deleteISBNLabel.setOpaque(true);

		deleteISBNText = new JTextField();
		deleteISBNText.setFont(new Font("΢���ź� Light", Font.PLAIN, 50));
		deleteISBNText.setBounds(405, 190, 370, 60);
		contentPane.add(deleteISBNText);
		deleteISBNText.setColumns(10);
		deleteISBNText.setBorder(null);

		JButton deleteButton = new JButton("\u5220\u9664");
		deleteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!DataProcessing.searchBookISBN(deleteISBNText.getText())) {
					JOptionPane.showMessageDialog(null, "��ISBN�Ŷ�Ӧ���鲻����!");
					deleteISBNText.setText("");
					return;
				} else {
					int n = JOptionPane.showConfirmDialog(null,
							"ȷ��ɾ��ͼ�顶" + DataProcessing.ISBNToName(deleteISBNText.getText()) + "���������Ϣ��?", "����Աȷ��",
							JOptionPane.YES_NO_OPTION);
					if (n == 0) {
						// �Ȱ�title��ֵ�нӹ���,Ȼ���ٽ���ɾ������.�������һ����ɾ��ʱ,��Ϣ���޷�ʹ��ISBNToName������ʾ����
						String title = DataProcessing.ISBNToName(deleteISBNText.getText());

						DataProcessing.deleteBooks(deleteISBNText.getText());
						dispose();
						JOptionPane.showMessageDialog(null, "ͼ�顶" + title + "��������Ϣ�ѳɹ�ɾ��!");
					}
					return;
				}
			}
		});
		deleteButton.setFont(new Font("΢���ź�", Font.PLAIN, 50));
		deleteButton.setBounds(140, 340, 200, 60);
		contentPane.add(deleteButton);

		JButton cancelButton = new JButton("\u53D6\u6D88");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		cancelButton.setFont(new Font("΢���ź�", Font.PLAIN, 50));
		cancelButton.setBounds(460, 340, 200, 60);
		contentPane.add(cancelButton);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground800500.png"));
		} catch (IOException e) {
			System.out.println("BooksDelete:����ͼƬ����ʧ��");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setBounds(0, -30, 810, 550);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);

	}
}
