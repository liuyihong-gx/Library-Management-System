package frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;

import bookandreadermanagement.DataProcessing;

import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.SwingConstants;
import java.awt.Color;

public class BookPositionChange extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField originalBookIDText;
	private JTextField originalBookIDCheck;
	private JTextField newFloorText;
	private JTextField newShelfText;
	private JTextField newShelfFloorText;
	private JTextField newShelfOrderText;

	private int newFloor;
	private int newShelf;
	private int newShelfFloor;
	private int newShelfOrder;

	private String originalBookID;
	private String newBookID;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public BookPositionChange() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1400, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("Œ¢»Ì—≈∫⁄", Font.BOLD, 30)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 35)));

		JLabel positionChangeLabel = new JLabel("\u56FE\u4E66\u4F4D\u7F6E\u53D8\u52A8");
		positionChangeLabel.setBackground(new Color(220, 220, 220));
		positionChangeLabel.setForeground(new Color(25, 25, 112));
		positionChangeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		positionChangeLabel.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.BOLD, 76));
		positionChangeLabel.setBounds(470, 5, 460, 85);
		contentPane.add(positionChangeLabel);
		positionChangeLabel.setOpaque(true);

		JLabel originalBookIDLabel = new JLabel("\u539F\u56FE\u4E66\u53F7:");
		originalBookIDLabel.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 40));
		originalBookIDLabel.setBounds(525, 120, 200, 50);
		contentPane.add(originalBookIDLabel);

		originalBookIDText = new JTextField();
		originalBookIDText.setHorizontalAlignment(SwingConstants.CENTER);
		originalBookIDText.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 40));
		originalBookIDText.setBounds(725, 120, 150, 50);
		contentPane.add(originalBookIDText);
		originalBookIDText.setColumns(10);

		originalBookIDCheck = new JTextField();
		originalBookIDCheck.setHorizontalAlignment(SwingConstants.CENTER);
		originalBookIDCheck.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 40));
		originalBookIDCheck.setEditable(false);
		originalBookIDCheck.setBounds(525, 180, 350, 50);
		contentPane.add(originalBookIDCheck);
		originalBookIDCheck.setColumns(10);

		JLabel newFloorLabel = new JLabel("\u65B0\u697C\u5C42\u53F7:");
		newFloorLabel.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 40));
		newFloorLabel.setBounds(525, 290, 200, 50);
		contentPane.add(newFloorLabel);

		newFloorText = new JTextField();
		newFloorText.setHorizontalAlignment(SwingConstants.CENTER);
		newFloorText.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 40));
		newFloorText.setBounds(725, 290, 150, 50);
		contentPane.add(newFloorText);
		newFloorText.setColumns(10);

		JLabel newShelfLabel = new JLabel("\u65B0\u4E66\u67B6\u53F7:");
		newShelfLabel.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 40));
		newShelfLabel.setBounds(525, 380, 200, 50);
		contentPane.add(newShelfLabel);

		newShelfText = new JTextField();
		newShelfText.setHorizontalAlignment(SwingConstants.CENTER);
		newShelfText.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 40));
		newShelfText.setBounds(725, 380, 150, 50);
		contentPane.add(newShelfText);
		newShelfText.setColumns(10);

		JLabel newShelfFloorLabel = new JLabel("\u65B0\u4E66\u67B6\u5C42:");
		newShelfFloorLabel.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 40));
		newShelfFloorLabel.setBounds(525, 470, 200, 50);
		contentPane.add(newShelfFloorLabel);

		newShelfFloorText = new JTextField();
		newShelfFloorText.setHorizontalAlignment(SwingConstants.CENTER);
		newShelfFloorText.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 40));
		newShelfFloorText.setBounds(725, 470, 150, 50);
		contentPane.add(newShelfFloorText);
		newShelfFloorText.setColumns(10);

		JLabel newShelfOrderLabel = new JLabel("\u6446\u653E\u987A\u5E8F:");
		newShelfOrderLabel.setFont(new Font("Œ¢»Ì—≈∫⁄ Light", Font.PLAIN, 40));
		newShelfOrderLabel.setBounds(525, 560, 200, 50);
		contentPane.add(newShelfOrderLabel);

		newShelfOrderText = new JTextField();
		newShelfOrderText.setHorizontalAlignment(SwingConstants.CENTER);
		newShelfOrderText.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 40));
		newShelfOrderText.setBounds(725, 560, 150, 50);
		contentPane.add(newShelfOrderText);
		newShelfOrderText.setColumns(10);

		JButton changeButton = new JButton("\u4FEE\u6539");
		changeButton.setEnabled(false);
		changeButton.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 50));
		changeButton.setBounds(470, 670, 200, 60);
		contentPane.add(changeButton);

		JButton cancelButton = new JButton("\u53D6\u6D88");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		cancelButton.setFont(new Font("Œ¢»Ì—≈∫⁄", Font.PLAIN, 50));
		cancelButton.setBounds(730, 670, 200, 60);
		contentPane.add(cancelButton);

		originalBookIDText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				originalBookID = originalBookIDText.getText();
				// ∏√Œª÷√…œ≤ª¥Ê‘⁄ È
				if (!DataProcessing.searchBookID(originalBookID)) {
					originalBookIDCheck.setText("∏√Œª÷√≤ª¥Ê‘⁄ È!");
					changeButton.setEnabled(false);
				} else {
					originalBookIDCheck.setText("");
					changeButton.setEnabled(true);
				}

			}

			@Override
			public void focusGained(FocusEvent e) {
				originalBookIDCheck.setText("");
			}
		});

		changeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (changeButton.isEnabled()) {
					if (newFloorText.getText().isBlank() || newShelfText.getText().isBlank()
							|| newShelfFloorText.getText().isBlank() || newShelfOrderText.getText().isBlank())
						JOptionPane.showMessageDialog(null, "«Î»∑±£√ø“ªœÓŒª÷√–≈œ¢ÃÓ–¥ÕÍ»´!");
					else {
						// œ»ªªÀ„≥…Õº È∫≈

						newFloor = Integer.parseInt(newFloorText.getText());
						newShelf = Integer.parseInt(newShelfText.getText());
						newShelfFloor = Integer.parseInt(newShelfFloorText.getText());
						newShelfOrder = Integer.parseInt(newShelfOrderText.getText());

						newBookID = DataProcessing.bookID(newFloor, newShelf, newShelfFloor, newShelfOrder);

						// »ª∫Û≈–∂œ–¬Œª÷√ «∑Ò”– È
						if (DataProcessing.searchBookID(newBookID)) {
							JOptionPane.showMessageDialog(null, "∏√Œª÷√“—”–Õº È!");
							newFloorText.setText("");
							newShelfText.setText("");
							newShelfFloorText.setText("");
							newShelfOrderText.setText("");
							return;
						} else {
							DataProcessing.updateBookID(originalBookID, newBookID);
							dispose();
							JOptionPane.showMessageDialog(null, "∏√Õº ÈŒª÷√“—∏¸–¬!");
						}
					}
				}
			}
		});

		JLabel originalBckground = new JLabel("");
		originalBckground.setBackground(new Color(220, 220, 220));
		originalBckground.setBounds(520, 115, 360, 120);
		contentPane.add(originalBckground);
		originalBckground.setOpaque(true);

		JLabel newPosBckground = new JLabel("");
		newPosBckground.setBackground(new Color(220, 220, 220));
		newPosBckground.setBounds(520, 285, 360, 330);
		contentPane.add(newPosBckground);
		newPosBckground.setOpaque(true);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground1400750.png"));
		} catch (IOException e) {
			System.out.println("BookPositionChange:±≥æ∞Õº∆¨‘ÿ»Î ß∞‹");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setBounds(0, -30, 1500, 800);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}
}
