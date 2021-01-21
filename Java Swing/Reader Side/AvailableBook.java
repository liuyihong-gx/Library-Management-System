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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import readerfunction.DataProcessing;
import javax.swing.UIManager;
import javax.swing.JLabel;

public class AvailableBook extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTable availableBookTable;
	private DefaultTableModel model;

	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public AvailableBook(String cardID, String ISBN) {

		String driverName = "com.mysql.cj.jdbc.Driver"; // 加载数据库驱动类
		String url = "jdbc:mysql://localhost:3306/librarydb?serverTimezone=GMT%2B8&useSSL=false"; // 声明数据库的URL(记得加入时区，否则会导致SQLException)
		String user = "root"; // 数据库用户
		String password = "1999Lyh#";
		try {
			Class.forName(driverName); // 加载数据库驱动类
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e1) {
			System.out.println("AvailableBook:数据库连接失败");
		} catch (ClassNotFoundException e) {
			System.out.println("AvailableBook:Java连接MySQL所依赖的jar包加载失败.建议:将jar包加入classpath");
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
		scrollPane.setBounds(15, 10, 1370, 660);
		contentPane.add(scrollPane);

		availableBookTable = new JTable();
		availableBookTable.getTableHeader().setFont(new Font("微软雅黑", Font.PLAIN, 30));
		availableBookTable.setFont(new Font("微软雅黑", Font.PLAIN, 60));
		scrollPane.setViewportView(availableBookTable);
		availableBookTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
		availableBookTable.setRowHeight(100);

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
		renderer.setHorizontalAlignment(JLabel.CENTER);
		availableBookTable.setDefaultRenderer(Object.class, renderer);

		JButton reserveThisButton = new JButton("\u9884\u5B9A");
		reserveThisButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 选任一本书
				int currentRow = availableBookTable.getSelectedRow();
				// 未选
				if (currentRow == -1) {
					JOptionPane.showMessageDialog(null, "未选中任何书!");
				} else if (DataProcessing.canReserveMore(cardID)) {
					Object id = model.getValueAt(currentRow, 0);
					// 先reserve这本书,然后再进行相关的操作
					DataProcessing.reserveThisBook((String) id, cardID);
					JOptionPane.showMessageDialog(null, "图书号为:" + (String) id + "的图书预定成功!");
					BookSearch.reservedTableRefresh(cardID);
					dispose();
				}
			}
		});
		reserveThisButton.setFont(new Font("微软雅黑", Font.PLAIN, 32));
		reserveThisButton.setBounds(420, 688, 180, 52);
		contentPane.add(reserveThisButton);

		model = new DefaultTableModel() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		String[] columnNames = { "图书号", "图书位置" };
		model.addColumn(columnNames[0]);
		model.addColumn(columnNames[1]);

		String sql = "SELECT book_id FROM book WHERE ISBN ='" + ISBN + "' AND in_or_out = 0 AND reserved = 0";
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sql);

			int rowNum = getResultRowNum(resultSet);
			String[][] result = new String[rowNum][2];

			resultSet.first();

			int n = 0;
			do {
				String bookID = resultSet.getString("book_id");
				result[n][0] = bookID;
				result[n][1] = DataProcessing.bookIDToPosInfo(bookID);
				n++;
			} while (resultSet.next());
			model.setDataVector(result, columnNames);
			availableBookTable.setModel(model);

			availableBookTable.getColumnModel().getColumn(0).setPreferredWidth(700);
			availableBookTable.getColumnModel().getColumn(1).setPreferredWidth(800);

		} catch (SQLException e) {
			System.out.println("AvailableBook:通过图书表查询在馆图书时出错");
		}

		JButton cancelButton = new JButton("\u53D6\u6D88");
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		cancelButton.setFont(new Font("微软雅黑", Font.PLAIN, 32));
		cancelButton.setBounds(800, 688, 180, 52);
		contentPane.add(cancelButton);

		BufferedImage background = null;
		try {
			background = ImageIO.read(new File("./img/pagebackground1400750.png"));
		} catch (IOException e) {
			System.out.println("AvailableBook:背景图片载入失败");
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
			System.out.println("AvailableBook:进行在馆可选图书表行数计算时出错");
		}
		return rowNum;
	}
}
