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
import javax.swing.JComboBox;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import readerfunction.DataProcessing;

import javax.swing.event.ChangeEvent;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JTable;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.SystemColor;

public class BookSearch extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	ButtonGroup group = new ButtonGroup();
	private JTextField ISBNText;
	private JTextField titleText;

	private static JTable recommendTable;
	private static DefaultTableModel model;

	private static JTable reservedTable;
	private static DefaultTableModel model1;

	private static Connection connection;
	private static Statement statement;
	private static ResultSet resultSet;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public BookSearch(String cardID) {
		String driverName = "com.mysql.cj.jdbc.Driver"; // �������ݿ�������
		String url = "jdbc:mysql://localhost:3306/librarydb?serverTimezone=GMT%2B8&useSSL=false"; // �������ݿ��URL(�ǵü���ʱ��������ᵼ��SQLException)
		String user = "root"; // ���ݿ��û�
		String password = "1999Lyh#";
		try {
			Class.forName(driverName); // �������ݿ�������
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e1) {
			System.out.println("BookSearch:���ݿ�����ʧ��");
		} catch (ClassNotFoundException e2) {
			System.out.println("BookSearch:Java����MySQL��������jar������ʧ��.����:��jar������classpath");
		}

		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1400, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("΢���ź�", Font.BOLD, 30)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("΢���ź� Light", Font.PLAIN, 35)));

		JLabel bookSearchLabel = new JLabel("\u67E5\u8BE2\u56FE\u4E66");
		bookSearchLabel.setForeground(new Color(188, 143, 143));
		bookSearchLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bookSearchLabel.setFont(new Font("΢���ź�", Font.PLAIN, 50));
		bookSearchLabel.setBounds(845, 10, 220, 55);
		contentPane.add(bookSearchLabel);

		JLabel searchFeatureLabel = new JLabel(
				"\u8BF7\u9009\u62E9\u9700\u8981\u67E5\u8BE2\u7684\u56FE\u4E66\u7279\u5F81");
		searchFeatureLabel.setForeground(new Color(188, 143, 143));
		searchFeatureLabel.setHorizontalAlignment(SwingConstants.CENTER);
		searchFeatureLabel.setFont(new Font("΢���ź� Light", Font.PLAIN, 22));
		searchFeatureLabel.setBounds(820, 70, 270, 25);
		contentPane.add(searchFeatureLabel);

		JRadioButton ISBNButton = new JRadioButton("ISBN");
		ISBNButton.setBackground(new Color(245, 245, 245));
		ISBNButton.setHorizontalAlignment(SwingConstants.CENTER);
		ISBNButton.setFont(new Font("΢���ź�", Font.PLAIN, 28));
		ISBNButton.setBounds(735, 100, 120, 30);
		contentPane.add(ISBNButton);

		JRadioButton titleButton = new JRadioButton("\u56FE\u4E66\u6807\u9898");
		titleButton.setBackground(new Color(245, 245, 245));
		titleButton.setHorizontalAlignment(SwingConstants.CENTER);
		titleButton.setFont(new Font("΢���ź�", Font.PLAIN, 28));
		titleButton.setBounds(855, 100, 170, 30);
		contentPane.add(titleButton);

		JRadioButton typeButton = new JRadioButton("\u56FE\u4E66\u7C7B\u578B");
		typeButton.setBackground(new Color(245, 245, 245));
		typeButton.setHorizontalAlignment(SwingConstants.CENTER);
		typeButton.setFont(new Font("΢���ź�", Font.PLAIN, 28));
		typeButton.setBounds(1025, 100, 170, 30);
		contentPane.add(typeButton);

		group.add(ISBNButton);
		group.add(titleButton);
		group.add(typeButton);

		ISBNText = new JTextField();
		ISBNText.setHorizontalAlignment(SwingConstants.CENTER);
		ISBNText.setEditable(false);
		ISBNText.setFont(new Font("΢���ź�", Font.PLAIN, 28));
		ISBNText.setBounds(905, 150, 290, 30);
		contentPane.add(ISBNText);
		ISBNText.setColumns(10);

		JLabel ISBNTextLabel = new JLabel("\u8981\u67E5\u8BE2\u7684ISBN:");
		ISBNTextLabel.setBackground(new Color(245, 245, 245));
		ISBNTextLabel.setForeground(new Color(47, 79, 79));
		ISBNTextLabel.setFont(new Font("΢���ź� Light", Font.PLAIN, 25));
		ISBNTextLabel.setBounds(735, 150, 165, 30);
		contentPane.add(ISBNTextLabel);
		ISBNTextLabel.setOpaque(true);

		titleText = new JTextField();
		titleText.setEditable(false);
		titleText.setFont(new Font("΢���ź�", Font.PLAIN, 28));
		titleText.setBounds(905, 200, 290, 30);
		contentPane.add(titleText);
		titleText.setColumns(10);

		JLabel titleLabel = new JLabel("\u8981\u67E5\u8BE2\u7684\u6807\u9898:");
		titleLabel.setBackground(new Color(245, 245, 245));
		titleLabel.setForeground(new Color(47, 79, 79));
		titleLabel.setFont(new Font("΢���ź� Light", Font.PLAIN, 25));
		titleLabel.setBounds(735, 200, 165, 30);
		contentPane.add(titleLabel);
		titleLabel.setOpaque(true);

		JComboBox<String> typeCombo = new JComboBox<String>();
		typeCombo.setFont(new Font("΢���ź�", Font.PLAIN, 25));
		typeCombo.setEnabled(false);
		typeCombo.setBounds(905, 250, 290, 30);
		typeCombo.addItem("�̲�");
		typeCombo.addItem("��ѧ");
		typeCombo.addItem("����");
		typeCombo.addItem("��ʷ");
		typeCombo.addItem("��ѧ");
		typeCombo.addItem("�����ѧ�ͻ�ѧ");
		contentPane.add(typeCombo);

		JLabel typeLabel = new JLabel("\u8981\u67E5\u8BE2\u7684\u7C7B\u578B:");
		typeLabel.setBackground(new Color(245, 245, 245));
		typeLabel.setForeground(new Color(47, 79, 79));
		typeLabel.setFont(new Font("΢���ź� Light", Font.PLAIN, 25));
		typeLabel.setBounds(735, 250, 165, 30);
		contentPane.add(typeLabel);
		typeLabel.setOpaque(true);

		JButton searchButton = new JButton("\u67E5\u8BE2");
		searchButton.setFont(new Font("΢���ź�", Font.PLAIN, 36));
		searchButton.setBounds(855, 295, 200, 45);
		contentPane.add(searchButton);

		JButton cancelButton = new JButton("\u8FD4\u56DE");
		cancelButton.setForeground(new Color(107, 142, 35));
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// ���֮��(�����������)
				DataProcessing.dropReserveTable(cardID);
				dispose();
			}
		});

		cancelButton.setFont(new Font("΢���ź�", Font.BOLD, 35));
		cancelButton.setBounds(1270, 5, 120, 50);
		contentPane.add(cancelButton);

		JLabel recommendLabel = new JLabel("\u56FE\u4E66\u63A8\u8350");
		recommendLabel.setHorizontalAlignment(SwingConstants.CENTER);
		recommendLabel.setForeground(new Color(188, 143, 143));
		recommendLabel.setFont(new Font("΢���ź�", Font.PLAIN, 40));
		recommendLabel.setBounds(20, 10, 160, 50);
		contentPane.add(recommendLabel);

		recommendTable = new JTable();
		recommendTable.setEnabled(false);
		recommendTable.setFont(new Font("΢���ź�", Font.PLAIN, 27));
		recommendTable.setBounds(20, 70, 680, 627);
		contentPane.add(recommendTable);
		recommendTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		recommendTable.setRowHeight(57);

		JLabel reservedLabel = new JLabel("\u5DF2\u9009\u56FE\u4E66");
		reservedLabel.setBackground(SystemColor.text);
		reservedLabel.setForeground(new Color(188, 143, 143));
		reservedLabel.setFont(new Font("΢���ź�", Font.PLAIN, 30));
		reservedLabel.setBounds(730, 372, 130, 32);
		contentPane.add(reservedLabel);
		reservedLabel.setOpaque(true);

		// �����ֻ����ʾ���ѡ����������������е�����ת�����û��Ѻõ����ݽ�����ʾ
		// ��ʵ�ʲ������Ƕ��������Ǹ�����в���
		reservedTable = new JTable();
		reservedTable.setFont(new Font("΢���ź�", Font.PLAIN, 20));
		reservedTable.setShowGrid(true);
		reservedTable.setBounds(730, 404, 660, 220);
		contentPane.add(reservedTable);
		reservedTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		JLabel readerNameLabel = new JLabel("     \u8BFB\u8005:" + DataProcessing.cardIDToName(cardID));
		readerNameLabel.setBackground(SystemColor.text);
		readerNameLabel.setForeground(new Color(47, 79, 79));
		readerNameLabel.setFont(new Font("΢���ź�", Font.PLAIN, 24));
		readerNameLabel.setBounds(860, 372, 530, 32);
		contentPane.add(readerNameLabel);
		readerNameLabel.setOpaque(true);

		// recommendTable�õ�
		model = new DefaultTableModel() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// reservedTable�õ�
		model1 = new DefaultTableModel() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		ISBNButton.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (ISBNButton.isSelected()) {
					ISBNText.setEditable(true);
					titleText.setText("");
				} else
					ISBNText.setEditable(false);

			}
		});

		// �����Ƽ����ˢ��
		recommendTableRefresh();

		// ������ʱԤ�����ˢ��
		reservedTableRefresh(cardID);

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
					if (ISBNText.getText().isBlank()) {
						JOptionPane.showMessageDialog(null, "ISBN����Ϊ��!");
						ISBNText.setText("");
					} else if (!DataProcessing.searchBookISBN(ISBNText.getText())) {
						JOptionPane.showMessageDialog(null, "�Ҳ���ISBNΪ" + ISBNText.getText() + "��ͼ��!");
						ISBNText.setText("");
					} else {
						// int����Ϊ1�ʹ��������String��ISBN
						SearchResult page = new SearchResult(cardID, ISBNText.getText(), 1);
						page.setLocationRelativeTo(null);
						page.setUndecorated(true);
						page.setVisible(true);
					}
				} else if (titleButton.isSelected()) {
					if (titleText.getText().isBlank()) {
						JOptionPane.showMessageDialog(null, "ͼ����ⲻ��Ϊ��!");
						titleText.setText("");
					} else if (!DataProcessing.searchBookTitle(titleText.getText())) {
						JOptionPane.showMessageDialog(null, "�Ҳ�������Ϊ" + titleText.getText() + "��ͼ��!");
						titleText.setText("");
					} else {
						// int����Ϊ2�ʹ��������String��title
						SearchResult page = new SearchResult(cardID, titleText.getText(), 2);
						page.setLocationRelativeTo(null);
						page.setUndecorated(true);
						page.setVisible(true);
					}
				} else if (typeButton.isSelected()) {
					if (!DataProcessing.searchBookType(typeCombo.getSelectedItem().toString())) {
						JOptionPane.showMessageDialog(null, "�Ҳ�������Ϊ" + typeCombo.getSelectedItem() + "��ͼ��!");
					} else {
						// int����Ϊ3�ʹ��������String��type
						// �ⶫ�����û����Ű�������Ҫ����
						SearchResult page = new SearchResult(cardID, typeCombo.getSelectedItem().toString(), 3);
						page.setLocationRelativeTo(null);
						page.setUndecorated(true);
						page.setVisible(true);
					}
				}
			}
		});

		JButton borrowButton = new JButton("\u501F\u9605");
		borrowButton.setFont(new Font("΢���ź�", Font.PLAIN, 32));
		borrowButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (DataProcessing.borrowAllBooks(cardID) == 1)
					dispose();
			}
		});
		borrowButton.setBounds(730, 645, 180, 52);
		contentPane.add(borrowButton);

		JButton deleteOneButton = new JButton("\u5220\u9664\u9009\u4E2D");
		deleteOneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// ѡ��һ����
				int currentRow = reservedTable.getSelectedRow();
				// δѡ
				if (currentRow == -1) {
					JOptionPane.showMessageDialog(null, "δѡ���κ���!");
				} else {
					Object id = model1.getValueAt(currentRow, 0);
					if (id == null)
						JOptionPane.showMessageDialog(null, "δѡ���κ���!");
					else {
						DataProcessing.cancelOneReserved(cardID, currentRow);
						reservedTableRefresh(cardID);
					}
				}
			}
		});

		deleteOneButton.setFont(new Font("΢���ź�", Font.PLAIN, 32));
		deleteOneButton.setBounds(970, 645, 180, 52);
		contentPane.add(deleteOneButton);

		JButton deleteAllButton = new JButton("\u5220\u9664\u5168\u90E8");
		deleteAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (reservedTableRefresh(cardID) == 0)
					JOptionPane.showMessageDialog(null, "δԤ���κ���!");
				else {
					DataProcessing.cancelAllReserved(cardID);
					reservedTableRefresh(cardID);
				}
			}
		});
		deleteAllButton.setFont(new Font("΢���ź�", Font.PLAIN, 32));
		deleteAllButton.setBounds(1210, 645, 180, 52);
		contentPane.add(deleteAllButton);

		JLabel searchBackGround = new JLabel("");
		searchBackGround.setBackground(new Color(245, 245, 245));
		searchBackGround.setBounds(730, 70, 470, 275);
		contentPane.add(searchBackGround);
		searchBackGround.setOpaque(true);

		BufferedImage background = null;
		try {
			background = ImageIO.read(new File("./img/pagebackground1400750.png"));
		} catch (IOException e) {
			System.out.println("BookSearch:����ͼƬ����ʧ��");
		}

		JLabel bckground = new JLabel(new ImageIcon(background));
		bckground.setText("");
		bckground.setBounds(0, -30, 1500, 800);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}

	public static void recommendTableRefresh() {
		String[] columnNames = { "ISBN", "ͼ�����", "ͼ������", "�����Ĵ���" };
		model.addColumn(columnNames[0]);
		model.addColumn(columnNames[1]);
		model.addColumn(columnNames[2]);
		model.addColumn(columnNames[3]);

		// �����СҪ�Ǻܴ���ô��(��һ��limit����)
		String[][] result = new String[11][4];

		// borrowed_num+0������ȷ����
		String sql = "SELECT ISBN, book_title, book_type, borrowed_num FROM books ORDER BY (borrowed_num+0) DESC LIMIT 10";
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sql);

			result[0][0] = "ISBN";
			result[0][1] = "����";
			result[0][2] = "����";
			result[0][3] = "�������";

			int n = 1;
			while (resultSet.next()) {
				result[n][0] = resultSet.getString("ISBN");
				result[n][1] = resultSet.getString("book_title");
				result[n][2] = resultSet.getString("book_type");
				result[n][3] = resultSet.getString("borrowed_num");
				n++;
			}
			model.setDataVector(result, columnNames);
			recommendTable.setModel(model);

			recommendTable.getColumnModel().getColumn(0).setPreferredWidth(470);
			recommendTable.getColumnModel().getColumn(1).setPreferredWidth(570);
			recommendTable.getColumnModel().getColumn(2).setPreferredWidth(150);
			recommendTable.getColumnModel().getColumn(3).setPreferredWidth(240);

			DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(JLabel.CENTER);
			recommendTable.setDefaultRenderer(Object.class, renderer);

		} catch (SQLException e) {
			System.out.println("BookSearch:�ڽ����Ƽ����ˢ��ʱ��ѯͼ��������");
		}
	}

	// cardID��������ʾ����������,���濴���ܲ��ܶ��ߵĽ��Ŀ����ܲ�������
	public static int reservedTableRefresh(String cardID) {

		int hasElem = 0;

		String[] columnNames = { "ͼ�����", "ͼ��λ��" };
		model1.addColumn(columnNames[0]);
		model1.addColumn(columnNames[1]);

		// ������ܸĳ�ʹ�û��ܽ���ٱ�����������
		// ��Ϊ������ܽ�һ��,�Ѿ�Ԥ��������,��ôˢ��֮�󽫹���һ��ֻ��һ�еı���������������,�����
		// ����ֻ�ܶ��Ƿ�ѡ���˿��н����ж�
		// ȷ������֮��,������С�ǲ����
		// ��ע����ʱԤ�����С�ǻ���
		String[][] result = new String[DataProcessing.cardIDToCanBorrow(cardID)][2];

		// borrowed_num+0������ȷ����
		String sql = "SELECT bookID FROM temp" + cardID;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sql);

			if (DataProcessing.cardIDToStatus(cardID).equals("��ʦ")) {
				reservedTable.setRowHeight(44);

				int n = 0;
				while (resultSet.next()) {
					hasElem = 1;
					result[n][0] = DataProcessing.bookIDToName(resultSet.getString("bookID"));
					result[n][1] = DataProcessing.bookIDToPosInfo(resultSet.getString("bookID"));
					n++;
				}
			} else {
				reservedTable.setRowHeight(73);

				int n = 0;
				while (resultSet.next()) {
					hasElem = 1;
					result[n][0] = DataProcessing.bookIDToName(resultSet.getString("bookID"));
					result[n][1] = DataProcessing.bookIDToPosInfo(resultSet.getString("bookID"));
					n++;
				}
			}

			model1.setDataVector(result, columnNames);
			reservedTable.setModel(model1);

			reservedTable.getColumnModel().getColumn(0).setPreferredWidth(200);
			reservedTable.getColumnModel().getColumn(1).setPreferredWidth(200);

			DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(JLabel.CENTER);
			reservedTable.setDefaultRenderer(Object.class, renderer);

		} catch (SQLException e) {
			System.out.println("BookSearch:�ڽ�����ʱԤ�����ˢ��ʱ��ѯ��ʱԤ�������");
		}

		return hasElem;
	}
}
