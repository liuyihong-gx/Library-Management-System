package frame;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
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

public class ReaderSearchResult extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTable searchResultTable;
	private DefaultTableModel model;

	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private JButton cancelButton;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	private JLabel generalInfoLabel;

	public ReaderSearchResult(String cardID) {

		String driverName = "com.mysql.cj.jdbc.Driver"; // 加载数据库驱动类
		String url = "jdbc:mysql://localhost:3306/librarydb?serverTimezone=GMT%2B8&useSSL=false"; // 声明数据库的URL(记得加入时区，否则会导致SQLException)
		String user = "root"; // 数据库用户
		String password = "1999Lyh#";
		try {
			Class.forName(driverName); // 加载数据库驱动类
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			System.out.println("ReaderSearchResult:数据库连接失败");
		} catch (ClassNotFoundException e) {
			System.out.println("ReaderSearchResult:Java连接MySQL所依赖的jar包加载失败.建议:将jar包加入classpath");
		}

		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1400, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		generalInfoLabel = new JLabel("\u57FA\u672C\u4FE1\u606F\u67E5\u8BE2\u7ED3\u679C");
		generalInfoLabel.setBackground(new Color(220, 220, 220));
		generalInfoLabel.setForeground(new Color(25, 25, 112));
		generalInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		generalInfoLabel.setFont(new Font("微软雅黑", Font.BOLD, 76));
		generalInfoLabel.setBounds(380, 5, 640, 85);
		contentPane.add(generalInfoLabel);
		generalInfoLabel.setOpaque(true);

		searchResultTable = new JTable();
		searchResultTable.setEnabled(false);
		searchResultTable.setBounds(20, 260, 1360, 200);
		searchResultTable.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		searchResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		contentPane.add(searchResultTable);
		searchResultTable.setRowHeight(100);

		model = new DefaultTableModel() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		String[] columnNames = { "借书证号", "姓名", "手机号", "身份", "可借书数", "已借书数" };
		model.addColumn(columnNames[0]);
		model.addColumn(columnNames[1]);
		model.addColumn(columnNames[2]);
		model.addColumn(columnNames[3]);
		model.addColumn(columnNames[4]);
		model.addColumn(columnNames[5]);

		String[][] result = new String[2][6];

		String sql = "SELECT * FROM readers WHERE card_id ='" + cardID + "'";
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sql);

			result[0][0] = "借书证号";
			result[0][1] = "姓名";
			result[0][2] = "电话号码";
			result[0][3] = "身份";
			result[0][4] = "可借书数";
			result[0][5] = "已借书数";

			int n = 1;
			while (resultSet.next()) {
				result[n][0] = resultSet.getString("card_id");
				result[n][1] = resultSet.getString("reader_name");
				result[n][2] = resultSet.getString("phone_num");
				result[n][3] = resultSet.getString("reader_status");
				result[n][4] = resultSet.getString("can_borrow");
				result[n][5] = resultSet.getString("already_borrow");
			}
			model.setDataVector(result, columnNames);
			searchResultTable.setModel(model);

			cancelButton = new JButton("\u53D6\u6D88");
			cancelButton.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					dispose();
				}
			});
			cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 50));
			cancelButton.setBounds(600, 580, 200, 60);
			contentPane.add(cancelButton);

			searchResultTable.getColumnModel().getColumn(0).setPreferredWidth(1200);
			searchResultTable.getColumnModel().getColumn(1).setPreferredWidth(1000);
			searchResultTable.getColumnModel().getColumn(2).setPreferredWidth(1300);
			searchResultTable.getColumnModel().getColumn(3).setPreferredWidth(600);
			searchResultTable.getColumnModel().getColumn(4).setPreferredWidth(800);
			searchResultTable.getColumnModel().getColumn(5).setPreferredWidth(800);

			DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(JLabel.CENTER);
			searchResultTable.setDefaultRenderer(Object.class, renderer);

		} catch (SQLException e) {
			System.out.println("ReaderSearchResult:查询读者表失败");
		}

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground1400750.png"));
		} catch (IOException e) {
			System.out.println("ReaderSearchResult:背景图片载入失败");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setBounds(0, -30, 1500, 800);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);

	}
}
