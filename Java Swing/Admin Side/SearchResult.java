package frame;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;

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

//只有在数据库中存在相应的图书(非空)的时候该页面才进行显示
//否则之后弹窗通知没有找到相应图书
//应该要传回来一个参数(可以是ISBN)这样才能在这个页面进行搜索
public class SearchResult extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTable searchResultTable;
	private DefaultTableModel model;

	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public SearchResult(String features, int i) {
		String driverName = "com.mysql.cj.jdbc.Driver"; // 加载数据库驱动类
		String url = "jdbc:mysql://localhost:3306/librarydb?serverTimezone=GMT%2B8&useSSL=false"; // 声明数据库的URL(记得加入时区，否则会导致SQLException)
		String user = "root"; // 数据库用户
		String password = "1999Lyh#";
		try {
			Class.forName(driverName); // 加载数据库驱动类
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e1) {
			System.out.println("SearchResult:数据库连接失败");
		} catch (ClassNotFoundException e) {
			System.out.println("SearchResult:Java连接MySQL所依赖的jar包加载失败.建议:将jar包加入classpath");
		}

		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1400, 750);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("微软雅黑", Font.BOLD, 30)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("微软雅黑 Light", Font.PLAIN, 35)));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 15, 1370, 660);
		contentPane.add(scrollPane);

		searchResultTable = new JTable();
		searchResultTable.getTableHeader().setFont(new Font("微软雅黑", Font.PLAIN, 25));
		searchResultTable.setFont(new Font("微软雅黑 Light", Font.PLAIN, 35));
		scrollPane.setViewportView(searchResultTable);
		searchResultTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		searchResultTable.setRowHeight(50);

		JButton cancelButton = new JButton("\u53D6\u6D88");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 32));
		cancelButton.setBounds(610, 688, 180, 52);
		contentPane.add(cancelButton);

		model = new DefaultTableModel() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		String[] columnNames = { "ISBN", "图书标题", "图书类型", "图书数量", "图书简介", "被借阅次数" };
		model.addColumn(columnNames[0]);
		model.addColumn(columnNames[1]);
		model.addColumn(columnNames[2]);
		model.addColumn(columnNames[3]);
		model.addColumn(columnNames[4]);
		model.addColumn(columnNames[5]);

		// 接下来进行int i值的判断
		if (i == 1) // 此时的feature是ISBN
		{
			String sql = "SELECT * FROM books WHERE ISBN ='" + features + "'";
			try {
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				resultSet = statement.executeQuery(sql);

				int rowNum = getResultRowNum(resultSet);
				String[][] result = new String[rowNum][6];

				resultSet.first();

				int n = 0;
				do {
					result[n][0] = resultSet.getString("ISBN");
					result[n][1] = resultSet.getString("book_title");
					result[n][2] = resultSet.getString("book_type");
					result[n][3] = resultSet.getString("book_num");
					result[n][4] = resultSet.getString("book_desc");
					result[n][5] = resultSet.getString("borrowed_num");
					n++;
				} while (resultSet.next());

				model.setDataVector(result, columnNames);
				searchResultTable.setModel(model);

				searchResultTable.getColumnModel().getColumn(0).setPreferredWidth(1600);
				searchResultTable.getColumnModel().getColumn(1).setPreferredWidth(1400);
				searchResultTable.getColumnModel().getColumn(2).setPreferredWidth(800);
				searchResultTable.getColumnModel().getColumn(3).setPreferredWidth(800);
				searchResultTable.getColumnModel().getColumn(4).setPreferredWidth(2000);
				searchResultTable.getColumnModel().getColumn(5).setPreferredWidth(1000);

				DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
				renderer.setHorizontalAlignment(JLabel.CENTER);
				searchResultTable.setDefaultRenderer(Object.class, renderer);
			} catch (SQLException e) {
				System.out.println("SearchResult:以ISBN为特征值查询图书类表时出错");
			}

		} else if (i == 2) // 此时的feature是图书标题
		{
			String sql = "SELECT * FROM books WHERE book_title like '%" + features + "%'";
			try {
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				resultSet = statement.executeQuery(sql);

				int rowNum = getResultRowNum(resultSet);
				String[][] result = new String[rowNum][6];

				resultSet.first();

				int n = 0;
				do {
					result[n][0] = resultSet.getString("ISBN");
					result[n][1] = resultSet.getString("book_title");
					result[n][2] = resultSet.getString("book_type");
					result[n][3] = resultSet.getString("book_num");
					result[n][4] = resultSet.getString("book_desc");
					result[n][5] = resultSet.getString("borrowed_num");
					n++;
				} while (resultSet.next());

				model.setDataVector(result, columnNames);
				searchResultTable.setModel(model);

				searchResultTable.getColumnModel().getColumn(0).setPreferredWidth(1600);
				searchResultTable.getColumnModel().getColumn(1).setPreferredWidth(1400);
				searchResultTable.getColumnModel().getColumn(2).setPreferredWidth(800);
				searchResultTable.getColumnModel().getColumn(3).setPreferredWidth(800);
				searchResultTable.getColumnModel().getColumn(4).setPreferredWidth(2000);
				searchResultTable.getColumnModel().getColumn(5).setPreferredWidth(1000);

				DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
				renderer.setHorizontalAlignment(JLabel.CENTER);
				searchResultTable.setDefaultRenderer(Object.class, renderer);
			} catch (SQLException e) {
				System.out.println("SearchResult:以图书标题为特征值查询图书类表时出错");
			}
		} else if (i == 3) // 此时的feature是图书类型
		{
			String sql = "SELECT * FROM books WHERE book_type ='" + features + "'";
			try {
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
				resultSet = statement.executeQuery(sql);

				int rowNum = getResultRowNum(resultSet);
				String[][] result = new String[rowNum][6];

				resultSet.first();

				int n = 0;
				do {
					result[n][0] = resultSet.getString("ISBN");
					result[n][1] = resultSet.getString("book_title");
					result[n][2] = resultSet.getString("book_type");
					result[n][3] = resultSet.getString("book_num");
					result[n][4] = resultSet.getString("book_desc");
					result[n][5] = resultSet.getString("borrowed_num");
					n++;
				} while (resultSet.next());

				model.setDataVector(result, columnNames);
				searchResultTable.setModel(model);

				searchResultTable.getColumnModel().getColumn(0).setPreferredWidth(1600);
				searchResultTable.getColumnModel().getColumn(1).setPreferredWidth(1400);
				searchResultTable.getColumnModel().getColumn(2).setPreferredWidth(800);
				searchResultTable.getColumnModel().getColumn(3).setPreferredWidth(800);
				searchResultTable.getColumnModel().getColumn(4).setPreferredWidth(2000);
				searchResultTable.getColumnModel().getColumn(5).setPreferredWidth(1000);

				DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
				renderer.setHorizontalAlignment(JLabel.CENTER);
				searchResultTable.setDefaultRenderer(Object.class, renderer);
			} catch (SQLException e) {
				System.out.println("SearchResult:以图书类型为特征值查询图书类表时出错");
			}
		}

		BufferedImage background = null;
		try {
			background = ImageIO.read(new File("./img/pagebackground1400750.png"));
		} catch (IOException e) {
			System.out.println("SearchResult:背景图片载入失败");
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
			System.out.println("SearchResult:进行查询结果行数计算时出错");
		}
		return rowNum;
	}
}
