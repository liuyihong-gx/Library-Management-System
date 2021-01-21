package frame;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;

import bookandreadermanagement.DataProcessing;
import java.awt.Color;

public class ReadersInfoChange extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField cardIDText;
	private JTextField cardIDCheck;
	private JLabel itemChangeLabel;

	ButtonGroup group = new ButtonGroup();
	private JTextField nameChangeText;
	private JTextField phoneChangeText;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private JTextField originalName;
	private JTextField originalPhone;
	private JTextField originalStatus;

	public ReadersInfoChange() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1400, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("΢���ź�", Font.BOLD, 30)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("΢���ź� Light", Font.PLAIN, 35)));

		JLabel readersInfoChangeLabel = new JLabel("\u4FEE\u6539\u8BFB\u8005\u4FE1\u606F");
		readersInfoChangeLabel.setBackground(new Color(220, 220, 220));
		readersInfoChangeLabel.setForeground(new Color(25, 25, 112));
		readersInfoChangeLabel.setFont(new Font("΢���ź�", Font.BOLD, 60));
		readersInfoChangeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		readersInfoChangeLabel.setBounds(500, 5, 400, 65);
		contentPane.add(readersInfoChangeLabel);
		readersInfoChangeLabel.setOpaque(true);

		JLabel cardIDLabel = new JLabel("\u8BFB\u8005\u501F\u4E66\u8BC1\u53F7\uFF1A");
		cardIDLabel.setFont(new Font("΢���ź� Light", Font.PLAIN, 40));
		cardIDLabel.setBounds(410, 130, 280, 50);
		contentPane.add(cardIDLabel);

		cardIDText = new JTextField();
		cardIDText.setHorizontalAlignment(SwingConstants.CENTER);
		cardIDText.setFont(new Font("΢���ź�", Font.PLAIN, 45));
		cardIDText.setBounds(690, 130, 300, 50);
		contentPane.add(cardIDText);
		cardIDText.setColumns(10);

		cardIDCheck = new JTextField();
		cardIDCheck.setHorizontalAlignment(SwingConstants.CENTER);
		cardIDCheck.setEditable(false);
		cardIDCheck.setFont(new Font("΢���ź�", Font.PLAIN, 40));
		cardIDCheck.setBounds(1010, 130, 280, 50);
		contentPane.add(cardIDCheck);
		cardIDCheck.setColumns(10);

		itemChangeLabel = new JLabel("\u8BF7\u9009\u62E9\u9700\u8981\u4FEE\u6539\u7684\u6570\u636E\u9879");
		itemChangeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		itemChangeLabel.setFont(new Font("΢���ź� Light", Font.PLAIN, 25));
		itemChangeLabel.setBounds(550, 235, 300, 30);
		contentPane.add(itemChangeLabel);

		JRadioButton nameChangeButton = new JRadioButton("\u4FEE\u6539\u59D3\u540D");
		nameChangeButton.setBackground(new Color(220, 220, 220));
		nameChangeButton.setFont(new Font("΢���ź� Light", Font.PLAIN, 30));
		nameChangeButton.setBounds(430, 270, 150, 40);
		group.add(nameChangeButton);
		contentPane.add(nameChangeButton);

		JRadioButton phoneChangeButton = new JRadioButton("\u4FEE\u6539\u7535\u8BDD");
		phoneChangeButton.setBackground(new Color(220, 220, 220));
		phoneChangeButton.setFont(new Font("΢���ź� Light", Font.PLAIN, 30));
		phoneChangeButton.setBounds(625, 270, 150, 40);
		group.add(phoneChangeButton);
		contentPane.add(phoneChangeButton);

		JRadioButton statusChangeButton = new JRadioButton("\u4FEE\u6539\u8EAB\u4EFD");
		statusChangeButton.setBackground(new Color(220, 220, 220));
		statusChangeButton.setFont(new Font("΢���ź� Light", Font.PLAIN, 30));
		statusChangeButton.setBounds(820, 270, 150, 40);
		group.add(statusChangeButton);
		contentPane.add(statusChangeButton);

		JLabel nameChangeLabel = new JLabel("\u4FEE\u6539\u59D3\u540D\u4E3A:");
		nameChangeLabel.setFont(new Font("΢���ź� Light", Font.PLAIN, 40));
		nameChangeLabel.setBounds(410, 330, 280, 50);
		contentPane.add(nameChangeLabel);

		nameChangeText = new JTextField();
		nameChangeText.setFont(new Font("΢���ź�", Font.PLAIN, 40));
		nameChangeText.setEditable(false);
		nameChangeText.setBounds(690, 330, 300, 50);
		contentPane.add(nameChangeText);
		nameChangeText.setColumns(10);

		JLabel phoneChangeLabel = new JLabel("\u4FEE\u6539\u7535\u8BDD\u4E3A:");
		phoneChangeLabel.setFont(new Font("΢���ź� Light", Font.PLAIN, 40));
		phoneChangeLabel.setBounds(410, 415, 280, 50);
		contentPane.add(phoneChangeLabel);

		phoneChangeText = new JTextField();
		phoneChangeText.setFont(new Font("΢���ź�", Font.PLAIN, 40));
		phoneChangeText.setEditable(false);
		phoneChangeText.setBounds(690, 415, 300, 50);
		contentPane.add(phoneChangeText);
		phoneChangeText.setColumns(10);

		JLabel statusChangeLabel = new JLabel("\u4FEE\u6539\u8EAB\u4EFD\u4E3A:");
		statusChangeLabel.setFont(new Font("΢���ź� Light", Font.PLAIN, 40));
		statusChangeLabel.setBounds(410, 500, 280, 50);
		contentPane.add(statusChangeLabel);

		JComboBox<String> statusChangeCombo = new JComboBox<String>();
		statusChangeCombo.setFont(new Font("΢���ź�", Font.PLAIN, 40));
		statusChangeCombo.setEnabled(false);
		statusChangeCombo.setBounds(690, 500, 300, 50);
		statusChangeCombo.addItem("ѧ��");
		statusChangeCombo.addItem("��ʦ");
		contentPane.add(statusChangeCombo);

		JButton changeButton = new JButton("\u4FEE\u6539");
		changeButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (changeButton.isEnabled()) {
					if (nameChangeButton.isSelected()) {
						if (nameChangeText.getText().isBlank())
							JOptionPane.showMessageDialog(null, "��������Ϊ��!");
						else if (nameChangeText.getText().equals(originalName.getText()))
							JOptionPane.showMessageDialog(null, "��������ԭ������ͬ,δ�����޸�");
						else {

							DataProcessing.updateReaderName(cardIDText.getText(), nameChangeText.getText());
							JOptionPane.showMessageDialog(null, "���������Ѹ��ĳɹ�!");
							nameChangeText.setText("");
							cardIDText.setText("");
						}
					} else if (phoneChangeButton.isSelected()) {
						// ���һ���ֻ�����λ���Ƿ���ȷ�Լ�ÿһλ�Ƿ�������
						if (phoneChangeText.getText().isBlank())
							JOptionPane.showMessageDialog(null, "�ֻ����벻��Ϊ��!");
						// �жϵ绰λ���Ƿ���ȷ
						else if (phoneChangeText.getText().length() != 11)
							JOptionPane.showMessageDialog(null, "�ֻ���λ������ȷ,����!");
						else {
							// �жϵ��绰λ����ȷ֮��,�жϵ绰�Ƿ�Ϊ����
							int allNum = 0;

							char[] checkPhoneNum = phoneChangeText.getText().toCharArray();
							for (int i = 0; i < 11; i++) {
								if (checkPhoneNum[i] < '0' || checkPhoneNum[i] > '9')
									allNum = 1;
							}

							if (allNum == 1)
								JOptionPane.showMessageDialog(null, "�ֻ������в��Ϸ��ַ�,����!");
							else if (phoneChangeText.getText().equals(originalPhone.getText()))
								JOptionPane.showMessageDialog(null, "���ֻ�������ԭ�ֻ�������ͬ,δ�����޸�");
							else {
								int n = JOptionPane.showConfirmDialog(null,
										"�ö�������Ϊ:" + DataProcessing.cardIDToName(cardIDText.getText()) + ",���������޸���ȷ��",
										"����Աȷ��", JOptionPane.YES_NO_OPTION);
								if (n == 0) {
									DataProcessing.updateReaderPhoneNum(cardIDText.getText(),
											phoneChangeText.getText());
									JOptionPane.showMessageDialog(null, "�����ֻ������Ѹ��ĳɹ�!");
								}
							}
						}
					} else if (statusChangeButton.isSelected()) {
						if (statusChangeCombo.getSelectedItem().toString().equals(originalStatus.getText()))
							JOptionPane.showMessageDialog(null, "�������ԭ�����ͬ,δ�����޸�");
						else if (statusChangeCombo.getSelectedItem().toString().equals("ѧ��")
								&& DataProcessing.howManyBooksBorrowed(cardIDText.getText()) > 3) {
							int needToReturnNum = DataProcessing.howManyBooksBorrowed(cardIDText.getText()) - 3;
							JOptionPane.showMessageDialog(null,
									"�ö������ڽ��ĵ��鼮�Ѿ�����ѧ���ɽ�����\n" + "����߹黹����" + needToReturnNum + "��ͼ����ٽ�������޸�");
						} else {
							int n = JOptionPane.showConfirmDialog(null,
									"�ö�������Ϊ:" + DataProcessing.cardIDToName(cardIDText.getText()) + ",���������޸���ȷ��",
									"����Աȷ��", JOptionPane.YES_NO_OPTION);
							if (n == 0) {
								DataProcessing.updateReaderStatus(cardIDText.getText(),
										statusChangeCombo.getSelectedItem().toString());
								JOptionPane.showMessageDialog(null, "��������Ѹ��ĳɹ�!");
							}
						}
					}
				}
			}
		});

		JLabel originalInfoLabel = new JLabel("\u539F\u4FE1\u606F");
		originalInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		originalInfoLabel.setFont(new Font("΢���ź� Light", Font.PLAIN, 30));
		originalInfoLabel.setBounds(1010, 270, 280, 40);
		contentPane.add(originalInfoLabel);

		originalName = new JTextField();
		originalName.setHorizontalAlignment(SwingConstants.CENTER);
		originalName.setEditable(false);
		originalName.setFont(new Font("΢���ź� Light", Font.PLAIN, 36));
		originalName.setBounds(1010, 330, 280, 50);
		contentPane.add(originalName);
		originalName.setColumns(10);

		originalPhone = new JTextField();
		originalPhone.setHorizontalAlignment(SwingConstants.CENTER);
		originalPhone.setEditable(false);
		originalPhone.setFont(new Font("΢���ź� Light", Font.PLAIN, 36));
		originalPhone.setBounds(1010, 415, 280, 50);
		contentPane.add(originalPhone);
		originalPhone.setColumns(10);

		originalStatus = new JTextField();
		originalStatus.setHorizontalAlignment(SwingConstants.CENTER);
		originalStatus.setFont(new Font("΢���ź� Light", Font.PLAIN, 40));
		originalStatus.setEditable(false);
		originalStatus.setBounds(1010, 500, 280, 50);
		contentPane.add(originalStatus);
		originalStatus.setColumns(10);

		changeButton.setEnabled(false);
		changeButton.setFont(new Font("΢���ź�", Font.PLAIN, 50));
		changeButton.setBounds(410, 630, 200, 60);
		contentPane.add(changeButton);

		JButton cancelButton = new JButton("\u53D6\u6D88");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		cancelButton.setFont(new Font("΢���ź�", Font.PLAIN, 50));
		cancelButton.setBounds(790, 630, 200, 60);
		contentPane.add(cancelButton);

		// ���е��޸�������ʼ��ȫ��,ֻ��ѡ�еĲŻָ�����

		// ѡ�����޸�����
		nameChangeButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (nameChangeButton.isSelected() && DataProcessing.searchCardID(cardIDText.getText())) {
					nameChangeText.setEditable(true);
					phoneChangeText.setText("");
					originalName.setText(DataProcessing.cardIDToName(cardIDText.getText()));
					originalPhone.setText("");
					originalStatus.setText("");
				} else
					nameChangeText.setEditable(false);
			}
		});

		// ѡ�����޸ĵ绰
		phoneChangeButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (phoneChangeButton.isSelected() && DataProcessing.searchCardID(cardIDText.getText())) {
					nameChangeText.setText("");
					phoneChangeText.setEditable(true);
					originalName.setText("");
					originalPhone.setText(DataProcessing.cardIDToPhone(cardIDText.getText()));
					originalStatus.setText("");
				} else
					phoneChangeText.setEditable(false);
			}
		});

		// ѡ�����޸����
		statusChangeButton.addChangeListener(new ChangeListener() {

			public void stateChanged(ChangeEvent e) {
				if (statusChangeButton.isSelected() && DataProcessing.searchCardID(cardIDText.getText())) {
					statusChangeCombo.setEnabled(true);
					nameChangeText.setText("");
					phoneChangeText.setText("");
					originalName.setText("");
					originalPhone.setText("");
					originalStatus.setText(DataProcessing.cardIDToStatus(cardIDText.getText()));
				} else
					statusChangeCombo.setEnabled(false);
			}
		});

		// д�����,��ť֮��,�������ܶ԰�ťִ�в���
		cardIDText.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				// ���������Ƿ��Ѿ��ڹݵ�
				// �������δ�ڹ�
				if (!DataProcessing.searchCardID(cardIDText.getText())) {
					changeButton.setEnabled(false);
					cardIDCheck.setText("�˶��߲�����!");
				} else {
					cardIDCheck.setText("");
					changeButton.setEnabled(true);
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				cardIDCheck.setText("");
			}
		});

		JLabel checkBckground = new JLabel("");
		checkBckground.setBackground(new Color(220, 220, 220));
		checkBckground.setBounds(405, 120, 590, 70);
		contentPane.add(checkBckground);
		checkBckground.setOpaque(true);

		JLabel inputBckground = new JLabel("");
		inputBckground.setBackground(new Color(220, 220, 220));
		inputBckground.setBounds(405, 230, 590, 330);
		contentPane.add(inputBckground);
		inputBckground.setOpaque(true);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground1400750.png"));
		} catch (IOException e) {
			System.out.println("ReadersInfoChange:����ͼƬ����ʧ��");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setBounds(0, -30, 1500, 800);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}
}
