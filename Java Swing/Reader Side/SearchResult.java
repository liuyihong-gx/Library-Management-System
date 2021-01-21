package frame;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import readerfunction.DataProcessing;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//ֻ�������ݿ��д�����Ӧ��ͼ��(�ǿ�)��ʱ���ҳ��Ž�����ʾ
//����֮�󵯴�֪ͨû���ҵ���Ӧͼ��
//Ӧ��Ҫ������һ������(������ISBN)�������������ҳ���������
public class SearchResult extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTable searchResultTable;
	private DefaultTableModel model;

	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public SearchResult(String cardID, String features, int i) {
		String driverName = "com.mysql.cj.jdbc.Driver"; // �������ݿ�������
		String url = "jdbc:mysql://localhost:3306/librarydb?serverTimezone=GMT%2B8&useSSL=false"; // �������ݿ��URL(�ǵü���ʱ��������ᵼ��SQLException)
		String user = "root"; // ���ݿ��û�
		String password = "1999Lyh#";
		try {
			Class.forName(driverName); // �������ݿ�������
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e1) {
			System.out.println("SearchResult:���ݿ�����ʧ��");
		} catch (ClassNotFoundException e) {
			System.out.println("SearchResult:Java����MySQL��������jar������ʧ��.����:��jar������classpath");
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

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 15, 1370, 660);
		contentPane.add(scrollPane);

		searchResultTable = new JTable();
		searchResultTable.getTableHeader().setFont(new Font("΢���ź�", Font.PLAIN, 18));
		searchResultTable.setFont(new Font("΢���ź� Light", Font.PLAIN, 25));
		scrollPane.setViewportView(searchResultTable);
		searchResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		searchResultTable.setRowHeight(70);

		JButton cancelButton = new JButton("\u53D6\u6D88");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});

		JButton borrowThisButton = new JButton("\u60F3\u501F");
		borrowThisButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// ȡISBN��
				int currentRow = searchResultTable.getSelectedRow();
				// δѡ
				if (currentRow == -1) {
					JOptionPane.showMessageDialog(null, "δѡ���κ���!");
				} else {
					Object id = model.getValueAt(currentRow, 0);
					if (id == null) {
						JOptionPane.showMessageDialog(null, "δѡ���κ���!");
					} else {
						// �ȿ�����û���ڹ�ͼ��,��û�оͲ����������
						// ���û�����ڹ�
						if (!DataProcessing.searchSpecificBook((String) id))
							JOptionPane.showMessageDialog(null, "������ȫ���������Ԥ��!");
						else {
							AvailableBook page = new AvailableBook(cardID, (String) id);
							page.setLocationRelativeTo(null);
							page.setUndecorated(true);
							page.setVisible(true);
							dispose();
						}
					}
				}
			}
		});
		borrowThisButton.setFont(new Font("΢���ź�", Font.PLAIN, 32));
		borrowThisButton.setBounds(420, 688, 180, 52);
		contentPane.add(borrowThisButton);
		cancelButton.setFont(new Font("΢���ź�", Font.PLAIN, 32));
		cancelButton.setBounds(800, 688, 180, 52);
		contentPane.add(cancelButton);

		JLabel searchBackGround = new JLabel("");
		searchBackGround.setBackground(new Color(245, 245, 245));
		searchBackGround.setBounds(730, 70, 470, 275);
		contentPane.add(searchBackGround);
		searchBackGround.setOpaque(true);

		model = new DefaultTableModel() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		String[] columnNames = { "ISBN", "ͼ�����", "ͼ������", "ͼ����" };
		model.addColumn(columnNames[0]);
		model.addColumn(columnNames[1]);
		model.addColumn(columnNames[2]);
		model.addColumn(columnNames[3]);

		// ���Ƽ���ʱ�����String������ΪString[10][4]����,����ʾǰʮ��
		// �������ó�[10],����������10��,�ͻᵼ�³���.ֱ�Ӳ�Ҫ�ѱ�������scrollpane���������

		// ����������int iֵ���ж�
		if (i == 1) // ��ʱ��feature��ISBN
		{
			String sql = "SELECT * FROM books WHERE ISBN ='" + features + "'";
			try {
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				resultSet = statement.executeQuery(sql);

				int rowNum = getResultRowNum(resultSet);
				String[][] result = new String[rowNum][4];

				resultSet.first();

				int n = 0;
				do {
					result[n][0] = resultSet.getString("ISBN");
					result[n][1] = resultSet.getString("book_title");
					result[n][2] = resultSet.getString("book_type");
					result[n][3] = resultSet.getString("book_desc");
					n++;
				} while (resultSet.next());

				model.setDataVector(result, columnNames);
				searchResultTable.setModel(model);

				searchResultTable.getColumnModel().getColumn(0).setPreferredWidth(1600);
				searchResultTable.getColumnModel().getColumn(1).setPreferredWidth(1400);
				searchResultTable.getColumnModel().getColumn(2).setPreferredWidth(800);
				searchResultTable.getColumnModel().getColumn(3).setPreferredWidth(3800);

				DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
				renderer.setHorizontalAlignment(JLabel.CENTER);
				searchResultTable.setDefaultRenderer(Object.class, renderer);
			} catch (SQLException e) {
				System.out.println("SearchResult:��ISBNΪ����ֵ��ѯͼ�����ʱ����");
			}

		} else if (i == 2) // ��ʱ��feature��ͼ�����
		{
			String sql = "SELECT * FROM books WHERE book_title like '%" + features + "%'";
			try {
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				resultSet = statement.executeQuery(sql);

				int rowNum = getResultRowNum(resultSet);
				String[][] result = new String[rowNum][4];

				resultSet.first();

				int n = 0;
				do {
					result[n][0] = resultSet.getString("ISBN");
					result[n][1] = resultSet.getString("book_title");
					result[n][2] = resultSet.getString("book_type");
					result[n][3] = resultSet.getString("book_desc");
					n++;
				} while (resultSet.next());

				model.setDataVector(result, columnNames);
				searchResultTable.setModel(model);

				searchResultTable.getColumnModel().getColumn(0).setPreferredWidth(1600);
				searchResultTable.getColumnModel().getColumn(1).setPreferredWidth(1400);
				searchResultTable.getColumnModel().getColumn(2).setPreferredWidth(800);
				searchResultTable.getColumnModel().getColumn(3).setPreferredWidth(3800);

				DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
				renderer.setHorizontalAlignment(JLabel.CENTER);
				searchResultTable.setDefaultRenderer(Object.class, renderer);
			} catch (SQLException e) {
				System.out.println("SearchResult:��ͼ�����Ϊ����ֵ��ѯͼ�����ʱ����");
			}
		} else if (i == 3) // ��ʱ��feature��ͼ������
		{
			String sql = "SELECT * FROM books WHERE book_type ='" + features + "'";
			try {
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				resultSet = statement.executeQuery(sql);

				int rowNum = getResultRowNum(resultSet);
				String[][] result = new String[rowNum][4];

				resultSet.first();

				int n = 0;
				do {
					result[n][0] = resultSet.getString("ISBN");
					result[n][1] = resultSet.getString("book_title");
					result[n][2] = resultSet.getString("book_type");
					result[n][3] = resultSet.getString("book_desc");
					n++;
				} while (resultSet.next());

				model.setDataVector(result, columnNames);
				searchResultTable.setModel(model);

				searchResultTable.getColumnModel().getColumn(0).setPreferredWidth(1600);
				searchResultTable.getColumnModel().getColumn(1).setPreferredWidth(1400);
				searchResultTable.getColumnModel().getColumn(2).setPreferredWidth(800);
				searchResultTable.getColumnModel().getColumn(3).setPreferredWidth(3800);

				DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
				renderer.setHorizontalAlignment(JLabel.CENTER);
				searchResultTable.setDefaultRenderer(Object.class, renderer);
			} catch (SQLException e) {
				System.out.println("SearchResult:��ͼ������Ϊ����ֵ��ѯͼ�����ʱ����");
			}
		}

		BufferedImage background = null;
		try {
			background = ImageIO.read(new File("./img/pagebackground1400750.png"));
		} catch (IOException e) {
			System.out.println("SearchResult:����ͼƬ����ʧ��");
		}

		JLabel bckground = new JLabel(new ImageIcon(background));
		bckground.setText("");
		bckground.setBounds(0, -30, 1500, 800);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}

	private int getResultRowNum(ResultSet resultSet) {
		int rowNum = 0;
		try {
			while (resultSet.next())
				rowNum++;
		} catch (SQLException e) {
			System.out.println("SearchResult:���в�ѯ�����������ʱ����");
		}
		return rowNum;
	}
}
