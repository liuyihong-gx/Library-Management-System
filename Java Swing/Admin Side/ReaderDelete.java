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
import javax.swing.SwingConstants;
import java.awt.Color;

public class ReaderDelete extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField deleteCardIDText;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public ReaderDelete() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("΢���ź�", Font.BOLD, 30)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("΢���ź� Light", Font.PLAIN, 35)));

		JLabel readerDeleteLabel = new JLabel("\u5220\u9664\u8BFB\u8005");
		readerDeleteLabel.setForeground(new Color(25, 25, 112));
		readerDeleteLabel.setHorizontalAlignment(SwingConstants.CENTER);
		readerDeleteLabel.setFont(new Font("΢���ź�", Font.BOLD, 76));
		readerDeleteLabel.setBounds(200, 5, 400, 85);
		contentPane.add(readerDeleteLabel);

		JLabel deleteCardIDLabel = new JLabel("\u501F\u4E66\u8BC1\u53F7:");
		deleteCardIDLabel.setBackground(new Color(220, 220, 220));
		deleteCardIDLabel.setFont(new Font("΢���ź�", Font.PLAIN, 68));
		deleteCardIDLabel.setBounds(30, 180, 300, 80);
		contentPane.add(deleteCardIDLabel);
		deleteCardIDLabel.setOpaque(true);

		deleteCardIDText = new JTextField();
		deleteCardIDText.setHorizontalAlignment(SwingConstants.CENTER);
		deleteCardIDText.setFont(new Font("΢���ź�", Font.PLAIN, 70));
		deleteCardIDText.setBounds(330, 180, 440, 80);
		contentPane.add(deleteCardIDText);
		deleteCardIDText.setColumns(10);
		deleteCardIDText.setBorder(null);

		JButton deleteButton = new JButton("\u5220\u9664");
		deleteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!DataProcessing.searchCardID(deleteCardIDText.getText())) {
					JOptionPane.showMessageDialog(null, "�˽���֤�Ŷ�Ӧ�Ķ��߲�����!");
					deleteCardIDText.setText("");
					return;
				}
				// ����˶��߻�����,����һ����չʾ����
				// Ȼ��ѯ�ʹ���Ա
				// �Ƿ�Ҫ�黹��Щ��֮��ɾ���˶���
				// ����ȷ�����߽���ȫ������֮���ٽ���ɾ��
				else if (DataProcessing.isStillKeeping(deleteCardIDText.getText())) {
					int n = JOptionPane.showConfirmDialog(null,
							"����[" + DataProcessing.cardIDToName(deleteCardIDText.getText()) + "]����δ�黹ͼ��\n"
									+ "�Ƿ����������ͼ����Ϊ���ڹݡ�״̬?",
							"����Աȷ��", JOptionPane.YES_NO_OPTION);
					if (n == 0) {
						String deletedName = DataProcessing.cardIDToName(deleteCardIDText.getText());
						DataProcessing.returnAllBooksForDeletedReader(deleteCardIDText.getText());
						DataProcessing.deleteBorrowInfo(deleteCardIDText.getText());
						DataProcessing.deleteReader(deleteCardIDText.getText());
						dispose();
						JOptionPane.showMessageDialog(null, "����[" + deletedName + "]��Ϣ�ѳɹ�ɾ��!");
					}
				} else {
					int n = JOptionPane.showConfirmDialog(null,
							"ȷ��ɾ������[" + DataProcessing.cardIDToName(deleteCardIDText.getText()) + "]�������Ϣ��?", "����Աȷ��",
							JOptionPane.YES_NO_OPTION);
					if (n == 0) {
						String deletedName = DataProcessing.cardIDToName(deleteCardIDText.getText());
						DataProcessing.deleteReader(deleteCardIDText.getText());
						dispose();
						JOptionPane.showMessageDialog(null, "����[" + deletedName + "]��Ϣ�ѳɹ�ɾ��!");
					}
				}
			}
		});
		deleteButton.setFont(new Font("΢���ź�", Font.PLAIN, 50));
		deleteButton.setBounds(140, 360, 200, 60);
		contentPane.add(deleteButton);

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
			System.out.println("ReaderDelete:����ͼƬ����ʧ��");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setBounds(0, -30, 810, 550);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}

}
