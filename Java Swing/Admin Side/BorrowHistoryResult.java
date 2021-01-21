package frame;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
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

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import bookandreadermanagement.DataProcessing;
import javax.swing.JScrollPane;
import java.awt.Color;

public class BorrowHistoryResult extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTable searchResultTable;
	private DefaultTableModel model;

	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private JButton cancelButton;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public BorrowHistoryResult(String cardid) {

		String driverName = "com.mysql.cj.jdbc.Driver"; // �������ݿ�������
		String url = "jdbc:mysql://localhost:3306/librarydb?serverTimezone=GMT%2B8&useSSL=false"; // �������ݿ��URL(�ǵü���ʱ��������ᵼ��SQLException)
		String user = "root"; // ���ݿ��û�
		String password = "1999Lyh#";
		try {
			Class.forName(driverName); // �������ݿ�������
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e1) {
			System.out.println("���ݿ����ʧ��");
		} catch (ClassNotFoundException e) {
			System.out.println("���ݿ����ʧ��");
		}

		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1400, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel borrowHistoryLabel = new JLabel("\u5386\u53F2\u501F\u9605\u67E5\u8BE2\u7ED3\u679C");
		borrowHistoryLabel.setBackground(new Color(220, 220, 220));
		borrowHistoryLabel.setFont(new Font("΢���ź�", Font.BOLD, 76));
		borrowHistoryLabel.setForeground(new Color(25, 25, 112));
		borrowHistoryLabel.setBounds(380, 5, 640, 85);
		contentPane.add(borrowHistoryLabel);
		borrowHistoryLabel.setOpaque(true);

		JLabel readerNameLabel = new JLabel("New label");
		readerNameLabel.setBackground(new Color(245, 245, 245));
		readerNameLabel.setFont(new Font("΢���ź� Light", Font.PLAIN, 40));
		readerNameLabel.setBounds(30, 105, 1340, 55);
		contentPane.add(readerNameLabel);
		readerNameLabel.setOpaque(true);
		readerNameLabel.setText("����:" + DataProcessing.cardIDToName(cardid));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 160, 1340, 500);
		contentPane.add(scrollPane);

		searchResultTable = new JTable();
		searchResultTable.setEnabled(false);
		searchResultTable.getTableHeader().setFont(new Font("΢���ź�", Font.PLAIN, 25));
		searchResultTable.setFont(new Font("΢���ź� Light", Font.PLAIN, 40));
		scrollPane.setViewportView(searchResultTable);
		searchResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		searchResultTable.setRowHeight(60);

		model = new DefaultTableModel() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		String[] columnNames = { "ͼ�����", "����ʱ��" };
		model.addColumn(columnNames[0]);
		model.addColumn(columnNames[1]);

		String sql = "SELECT * FROM return_info WHERE card_id ='" + cardid + "'";
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sql);

			int rowNum = getResultRowNum(resultSet);
			String[][] result = new String[rowNum][2];

			resultSet.first();

			int n = 0;
			do {
				result[n][0] = DataProcessing.bookIDToName(resultSet.getString("book_id"));
				result[n][1] = resultSet.getString("return_date");
				n++;
			} while (resultSet.next());

			model.setDataVector(result, columnNames);
			searchResultTable.setModel(model);

			searchResultTable.getColumnModel().getColumn(0).setPreferredWidth(1200);
			searchResultTable.getColumnModel().getColumn(1).setPreferredWidth(1000);

			DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(JLabel.CENTER);
			searchResultTable.setDefaultRenderer(Object.class, renderer);
		} catch (SQLException e) {
			System.out.println("�޸����ݿ�ʧ��");
		}

		cancelButton = new JButton("\u53D6\u6D88");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		cancelButton.setFont(new Font("΢���ź�", Font.PLAIN, 50));
		cancelButton.setBounds(600, 670, 200, 60);
		contentPane.add(cancelButton);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground1400750.png"));
		} catch (IOException e) {
			System.out.println("BorrowHistoryResult:����ͼƬ����ʧ��");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setBackground(new Color(220, 220, 220));
		bckground.setBounds(0, -30, 1500, 800);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}

	// ��ѯ�����������
	// ���ڽ���
	private int getResultRowNum(ResultSet resultSet) {
		int rowNum = 0;
		try {
			while (resultSet.next())
				rowNum++;
		} catch (SQLException e) {
			System.out.println("BorrowHistoryResult:���в�ѯ�����������ʱ����");
		}
		return rowNum;
	}
}
