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

public class BorrowHistorySearch extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	ButtonGroup group = new ButtonGroup();
	private JTextField cardIDText;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public BorrowHistorySearch() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("Œ¢»Ì—≈∫⁄", Font.BOLD, 30)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 35)));

		JLabel historyInfoLabel = new JLabel("\u5386\u53F2\u501F\u9605\u4FE1\u606F\u67E5\u8BE2");
		historyInfoLabel.setForeground(new Color(25, 25, 112));
		historyInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		historyInfoLabel.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.BOLD, 76));
		historyInfoLabel.setBounds(90, 5, 620, 85);
		contentPane.add(historyInfoLabel);

		JLabel cardIDTextLabel = new JLabel("\u501F\u4E66\u8BC1\u53F7:");
		cardIDTextLabel.setBackground(new Color(220, 220, 220));
		cardIDTextLabel.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 68));
		cardIDTextLabel.setBounds(30, 180, 300, 80);
		contentPane.add(cardIDTextLabel);
		cardIDTextLabel.setOpaque(true);

		cardIDText = new JTextField();
		cardIDText.setHorizontalAlignment(SwingConstants.CENTER);
		cardIDText.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 70));
		cardIDText.setBounds(330, 180, 440, 80);
		contentPane.add(cardIDText);
		cardIDText.setColumns(10);
		cardIDText.setBorder(null);

		JButton searchButton = new JButton("\u67E5\u8BE2");
		searchButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// »Áπ˚ΩË È÷§∫≈Œ¥º«¬º‘⁄≤·
				if (!DataProcessing.searchCardID(cardIDText.getText())) {
					JOptionPane.showMessageDialog(null, "’“≤ªµΩΩË È÷§∫≈Œ™" + cardIDText.getText() + "µƒ∂¡’ﬂ!");
					cardIDText.setText("");
				} else {
					BorrowHistoryResult page = new BorrowHistoryResult(cardIDText.getText());
					page.setLocationRelativeTo(null);
					page.setUndecorated(true);
					page.setVisible(true);
				}
			}
		});
		searchButton.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 50));
		searchButton.setBounds(140, 360, 200, 60);
		contentPane.add(searchButton);

		JButton cancelButton = new JButton("\u53D6\u6D88");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});

		cancelButton.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 50));
		cancelButton.setBounds(460, 360, 200, 60);
		contentPane.add(cancelButton);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground800500.png"));
		} catch (IOException e) {
			System.out.println("BorrowHistorySearch:±≥æ∞Õº∆¨‘ÿ»Î ß∞‹");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setBounds(0, -30, 810, 550);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}

}
