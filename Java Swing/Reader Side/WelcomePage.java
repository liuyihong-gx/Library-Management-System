package frame;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;

import readerfunction.DataProcessing;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class WelcomePage extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private JTextField cardIDText;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String driverName = "com.mysql.cj.jdbc.Driver"; // �������ݿ�������
					String url = "jdbc:mysql://localhost:3306/librarydb?serverTimezone=GMT%2B8&useSSL=false"; // �������ݿ��URL(�ǵü���ʱ��������ᵼ��SQLException)
					String user = "root"; // ���ݿ��û�
					String password = "1999Lyh#";
					DataProcessing.connectToDB(driverName, url, user, password);

					if (DataProcessing.connectResult) {
						WelcomePage page = new WelcomePage();
						page.setLocationRelativeTo(null);
						page.setUndecorated(true);
						page.setVisible(true);
					}
				} catch (SQLException e) {
					System.out.println("WelcomePage:���ݿ�����ʧ��");
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WelcomePage() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("΢���ź�", Font.BOLD, 30)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("΢���ź� Light", Font.PLAIN, 35)));

		JLabel welcomeLabel = new JLabel("\u8BF7\u8F93\u5165\u501F\u4E66\u8BC1\u53F7");
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		welcomeLabel.setForeground(new Color(25, 25, 112));
		welcomeLabel.setFont(new Font("΢���ź�", Font.BOLD, 76));
		welcomeLabel.setBounds(130, 5, 540, 85);
		contentPane.add(welcomeLabel);

		cardIDText = new JTextField();
		cardIDText.setHorizontalAlignment(SwingConstants.CENTER);
		cardIDText.setFont(new Font("΢���ź�", Font.PLAIN, 60));
		cardIDText.setBounds(150, 220, 500, 62);
		contentPane.add(cardIDText);
		cardIDText.setColumns(10);

		JButton logOnButton = new JButton("\u786E\u8BA4");
		logOnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!DataProcessing.searchCardID(cardIDText.getText())) {
					JOptionPane.showMessageDialog(null, "�˽���֤������!");
					cardIDText.setText("");
				} else {
					MainPage page = new MainPage(cardIDText.getText());
					page.setLocationRelativeTo(null);
					page.setUndecorated(true);
					page.setVisible(true);

					// Ϊ�˰�ȫ���(��һ���û�ʹ��ʱ,�������֤������ʾ,�ǾͲ���ȫ��)
					// ���������������ǺŻ��ǻ��б�ð�õķ���,ʵ�������ʹ��ɨ��ǹɨ������������ֶ�����,�ڴ�ֻ����һ��ģ��
					cardIDText.setText("");
				}
			}
		});
		logOnButton.setFont(new Font("΢���ź�", Font.PLAIN, 50));
		logOnButton.setBounds(300, 310, 200, 60);
		contentPane.add(logOnButton);

		JButton quitButton = new JButton("\u9000\u51FA\u7CFB\u7EDF");
		quitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		quitButton.setForeground(new Color(107, 142, 35));
		quitButton.setFont(new Font("΢���ź�", Font.BOLD, 35));
		quitButton.setBounds(570, 440, 220, 50);
		contentPane.add(quitButton);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground800500.png"));
		} catch (IOException e) {
			System.out.println("WelcomePage:����ͼƬ����ʧ��");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setBounds(0, -30, 810, 550);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}
}
