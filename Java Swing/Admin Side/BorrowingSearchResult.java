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
import javax.swing.SwingConstants;
import java.awt.Color;

public class BorrowingSearchResult extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTable searchResultTable;
	private DefaultTableModel model;

	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private JButton cancelButton;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public BorrowingSearchResult(String cardID) {

		String driverName = "com.mysql.cj.jdbc.Driver"; // 加载数据库驱动类
		String url = "jdbc:mysql://localhost:3306/librarydb?serverTimezone=GMT%2B8&useSSL=false"; // 声明数据库的URL(记得加入时区，否则会导致SQLException)
		String user = "root"; // 数据库用户
		String password = "1999Lyh#";
		try {
			Class.forName(driverName); // 加载数据库驱动类
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e1) {
			System.out.println("数据库加载失败");
		} catch (ClassNotFoundException e) {
			System.out.println("数据库加载失败");
		}

		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1400, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel borrowingInfoLabel = new JLabel("\u6B63\u5728\u501F\u9605\u67E5\u8BE2\u7ED3\u679C");
		borrowingInfoLabel.setBackground(new Color(220, 220, 220));
		borrowingInfoLabel.setForeground(new Color(25, 25, 112));
		borrowingInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		borrowingInfoLabel.setFont(new Font("微软雅黑", Font.BOLD, 76));
		borrowingInfoLabel.setBounds(380, 5, 640, 85);
		contentPane.add(borrowingInfoLabel);
		borrowingInfoLabel.setOpaque(true);

		JLabel readerNameLabel = new JLabel("");
		readerNameLabel.setBackground(new Color(245, 245, 245));
		readerNameLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		readerNameLabel.setBounds(30, 105, 1340, 55);
		contentPane.add(readerNameLabel);
		readerNameLabel.setOpaque(true);
		readerNameLabel.setText("读者:" + DataProcessing.cardIDToName(cardID));

		searchResultTable = new JTable();
		searchResultTable.setEnabled(false);
		searchResultTable.setBounds(30, 160, 1340, 480);
		searchResultTable.setFont(new Font("微软雅黑 Light", Font.PLAIN, 45));
		searchResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		contentPane.add(searchResultTable);

		model = new DefaultTableModel() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		String[] columnNames = { "图书标题", "借阅时间", "续借次数" };
		model.addColumn(columnNames[0]);
		model.addColumn(columnNames[1]);
		model.addColumn(columnNames[2]);

		String sql = "SELECT * FROM borrow_info WHERE card_id ='" + cardID + "'";
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sql);

			if (DataProcessing.cardIDToStatus(cardID).equals("教师")) {
				String[][] result = new String[6][3];

				result[0][0] = "图书标题";
				result[0][1] = "借阅时间";
				result[0][2] = "续借次数";
				// 每一次.next(),就转换一下书名

				int n = 1;
				while (resultSet.next()) {
					result[n][0] = DataProcessing.bookIDToName(resultSet.getString("book_id"));
					result[n][1] = resultSet.getString("borrow_date");
					result[n][2] = resultSet.getString("renew_num");
					n++;

					model.setDataVector(result, columnNames);

					searchResultTable.setRowHeight(80);
				}
			} else {
				String[][] result = new String[4][3];

				result[0][0] = "图书标题";
				result[0][1] = "借阅时间";
				result[0][2] = "续借次数";
				// 每一次.next(),就转换一下书名

				int n = 1;
				while (resultSet.next()) {
					result[n][0] = DataProcessing.bookIDToName(resultSet.getString("book_id"));
					result[n][1] = resultSet.getString("borrow_date");
					result[n][2] = resultSet.getString("renew_num");
					n++;

					model.setDataVector(result, columnNames);

					searchResultTable.setRowHeight(120);
				}
			}
			searchResultTable.setModel(model);

			searchResultTable.getColumnModel().getColumn(0).setPreferredWidth(200);
			searchResultTable.getColumnModel().getColumn(1).setPreferredWidth(100);
			searchResultTable.getColumnModel().getColumn(2).setPreferredWidth(100);

			DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(JLabel.CENTER);
			searchResultTable.setDefaultRenderer(Object.class, renderer);
		} catch (SQLException e) {
			System.out.println("修改数据库失败");
		}

		cancelButton = new JButton("\u53D6\u6D88");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		cancelButton.setBounds(600, 650, 200, 60);
		contentPane.add(cancelButton);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground1400750.png"));
		} catch (IOException e) {
			System.out.println("BorrowingSearchResult:背景图片载入失败");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setBounds(0, -30, 1500, 800);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}
}
