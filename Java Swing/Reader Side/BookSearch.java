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
		String driverName = "com.mysql.cj.jdbc.Driver"; // 加载数据库驱动类
		String url = "jdbc:mysql://localhost:3306/librarydb?serverTimezone=GMT%2B8&useSSL=false"; // 声明数据库的URL(记得加入时区，否则会导致SQLException)
		String user = "root"; // 数据库用户
		String password = "1999Lyh#";
		try {
			Class.forName(driverName); // 加载数据库驱动类
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e1) {
			System.out.println("BookSearch:数据库连接失败");
		} catch (ClassNotFoundException e2) {
			System.out.println("BookSearch:Java连接MySQL所依赖的jar包加载失败.建议:将jar包加入classpath");
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

		JLabel bookSearchLabel = new JLabel("\u67E5\u8BE2\u56FE\u4E66");
		bookSearchLabel.setForeground(new Color(188, 143, 143));
		bookSearchLabel.setHorizontalAlignment(SwingConstants.CENTER);
		bookSearchLabel.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		bookSearchLabel.setBounds(845, 10, 220, 55);
		contentPane.add(bookSearchLabel);

		JLabel searchFeatureLabel = new JLabel(
				"\u8BF7\u9009\u62E9\u9700\u8981\u67E5\u8BE2\u7684\u56FE\u4E66\u7279\u5F81");
		searchFeatureLabel.setForeground(new Color(188, 143, 143));
		searchFeatureLabel.setHorizontalAlignment(SwingConstants.CENTER);
		searchFeatureLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 22));
		searchFeatureLabel.setBounds(820, 70, 270, 25);
		contentPane.add(searchFeatureLabel);

		JRadioButton ISBNButton = new JRadioButton("ISBN");
		ISBNButton.setBackground(new Color(245, 245, 245));
		ISBNButton.setHorizontalAlignment(SwingConstants.CENTER);
		ISBNButton.setFont(new Font("微软雅黑", Font.PLAIN, 28));
		ISBNButton.setBounds(735, 100, 120, 30);
		contentPane.add(ISBNButton);

		JRadioButton titleButton = new JRadioButton("\u56FE\u4E66\u6807\u9898");
		titleButton.setBackground(new Color(245, 245, 245));
		titleButton.setHorizontalAlignment(SwingConstants.CENTER);
		titleButton.setFont(new Font("微软雅黑", Font.PLAIN, 28));
		titleButton.setBounds(855, 100, 170, 30);
		contentPane.add(titleButton);

		JRadioButton typeButton = new JRadioButton("\u56FE\u4E66\u7C7B\u578B");
		typeButton.setBackground(new Color(245, 245, 245));
		typeButton.setHorizontalAlignment(SwingConstants.CENTER);
		typeButton.setFont(new Font("微软雅黑", Font.PLAIN, 28));
		typeButton.setBounds(1025, 100, 170, 30);
		contentPane.add(typeButton);

		group.add(ISBNButton);
		group.add(titleButton);
		group.add(typeButton);

		ISBNText = new JTextField();
		ISBNText.setHorizontalAlignment(SwingConstants.CENTER);
		ISBNText.setEditable(false);
		ISBNText.setFont(new Font("微软雅黑", Font.PLAIN, 28));
		ISBNText.setBounds(905, 150, 290, 30);
		contentPane.add(ISBNText);
		ISBNText.setColumns(10);

		JLabel ISBNTextLabel = new JLabel("\u8981\u67E5\u8BE2\u7684ISBN:");
		ISBNTextLabel.setBackground(new Color(245, 245, 245));
		ISBNTextLabel.setForeground(new Color(47, 79, 79));
		ISBNTextLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 25));
		ISBNTextLabel.setBounds(735, 150, 165, 30);
		contentPane.add(ISBNTextLabel);
		ISBNTextLabel.setOpaque(true);

		titleText = new JTextField();
		titleText.setEditable(false);
		titleText.setFont(new Font("微软雅黑", Font.PLAIN, 28));
		titleText.setBounds(905, 200, 290, 30);
		contentPane.add(titleText);
		titleText.setColumns(10);

		JLabel titleLabel = new JLabel("\u8981\u67E5\u8BE2\u7684\u6807\u9898:");
		titleLabel.setBackground(new Color(245, 245, 245));
		titleLabel.setForeground(new Color(47, 79, 79));
		titleLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 25));
		titleLabel.setBounds(735, 200, 165, 30);
		contentPane.add(titleLabel);
		titleLabel.setOpaque(true);

		JComboBox<String> typeCombo = new JComboBox<String>();
		typeCombo.setFont(new Font("微软雅黑", Font.PLAIN, 25));
		typeCombo.setEnabled(false);
		typeCombo.setBounds(905, 250, 290, 30);
		typeCombo.addItem("教材");
		typeCombo.addItem("文学");
		typeCombo.addItem("艺术");
		typeCombo.addItem("历史");
		typeCombo.addItem("哲学");
		typeCombo.addItem("数理科学和化学");
		contentPane.add(typeCombo);

		JLabel typeLabel = new JLabel("\u8981\u67E5\u8BE2\u7684\u7C7B\u578B:");
		typeLabel.setBackground(new Color(245, 245, 245));
		typeLabel.setForeground(new Color(47, 79, 79));
		typeLabel.setFont(new Font("微软雅黑 Light", Font.PLAIN, 25));
		typeLabel.setBounds(735, 250, 165, 30);
		contentPane.add(typeLabel);
		typeLabel.setOpaque(true);

		JButton searchButton = new JButton("\u67E5\u8BE2");
		searchButton.setFont(new Font("微软雅黑", Font.PLAIN, 36));
		searchButton.setBounds(855, 295, 200, 45);
		contentPane.add(searchButton);

		JButton cancelButton = new JButton("\u8FD4\u56DE");
		cancelButton.setForeground(new Color(107, 142, 35));
		cancelButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// 点击之后(不打算借书了)
				DataProcessing.dropReserveTable(cardID);
				dispose();
			}
		});

		cancelButton.setFont(new Font("微软雅黑", Font.BOLD, 35));
		cancelButton.setBounds(1270, 5, 120, 50);
		contentPane.add(cancelButton);

		JLabel recommendLabel = new JLabel("\u56FE\u4E66\u63A8\u8350");
		recommendLabel.setHorizontalAlignment(SwingConstants.CENTER);
		recommendLabel.setForeground(new Color(188, 143, 143));
		recommendLabel.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		recommendLabel.setBounds(20, 10, 160, 50);
		contentPane.add(recommendLabel);

		recommendTable = new JTable();
		recommendTable.setEnabled(false);
		recommendTable.setFont(new Font("微软雅黑", Font.PLAIN, 27));
		recommendTable.setBounds(20, 70, 680, 627);
		contentPane.add(recommendTable);
		recommendTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		recommendTable.setRowHeight(57);

		JLabel reservedLabel = new JLabel("\u5DF2\u9009\u56FE\u4E66");
		reservedLabel.setBackground(SystemColor.text);
		reservedLabel.setForeground(new Color(188, 143, 143));
		reservedLabel.setFont(new Font("微软雅黑", Font.PLAIN, 30));
		reservedLabel.setBounds(730, 372, 130, 32);
		contentPane.add(reservedLabel);
		reservedLabel.setOpaque(true);

		// 这个表只是显示而已————将真正表中的内容转换成用户友好的内容进行显示
		// 而实际操作还是对真正的那个表进行操作
		reservedTable = new JTable();
		reservedTable.setFont(new Font("微软雅黑", Font.PLAIN, 20));
		reservedTable.setShowGrid(true);
		reservedTable.setBounds(730, 404, 660, 220);
		contentPane.add(reservedTable);
		reservedTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		JLabel readerNameLabel = new JLabel("     \u8BFB\u8005:" + DataProcessing.cardIDToName(cardID));
		readerNameLabel.setBackground(SystemColor.text);
		readerNameLabel.setForeground(new Color(47, 79, 79));
		readerNameLabel.setFont(new Font("微软雅黑", Font.PLAIN, 24));
		readerNameLabel.setBounds(860, 372, 530, 32);
		contentPane.add(readerNameLabel);
		readerNameLabel.setOpaque(true);

		// recommendTable用的
		model = new DefaultTableModel() {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		// reservedTable用的
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

		// 进行推荐表的刷新
		recommendTableRefresh();

		// 进行临时预定表的刷新
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
						JOptionPane.showMessageDialog(null, "ISBN不能为空!");
						ISBNText.setText("");
					} else if (!DataProcessing.searchBookISBN(ISBNText.getText())) {
						JOptionPane.showMessageDialog(null, "找不到ISBN为" + ISBNText.getText() + "的图书!");
						ISBNText.setText("");
					} else {
						// int参数为1就代表输入的String是ISBN
						SearchResult page = new SearchResult(cardID, ISBNText.getText(), 1);
						page.setLocationRelativeTo(null);
						page.setUndecorated(true);
						page.setVisible(true);
					}
				} else if (titleButton.isSelected()) {
					if (titleText.getText().isBlank()) {
						JOptionPane.showMessageDialog(null, "图书标题不能为空!");
						titleText.setText("");
					} else if (!DataProcessing.searchBookTitle(titleText.getText())) {
						JOptionPane.showMessageDialog(null, "找不到标题为" + titleText.getText() + "的图书!");
						titleText.setText("");
					} else {
						// int参数为2就代表输入的String是title
						SearchResult page = new SearchResult(cardID, titleText.getText(), 2);
						page.setLocationRelativeTo(null);
						page.setUndecorated(true);
						page.setVisible(true);
					}
				} else if (typeButton.isSelected()) {
					if (!DataProcessing.searchBookType(typeCombo.getSelectedItem().toString())) {
						JOptionPane.showMessageDialog(null, "找不到类型为" + typeCombo.getSelectedItem() + "的图书!");
					} else {
						// int参数为3就代表输入的String是type
						// 这东西不用花括号包起来就要报错
						SearchResult page = new SearchResult(cardID, typeCombo.getSelectedItem().toString(), 3);
						page.setLocationRelativeTo(null);
						page.setUndecorated(true);
						page.setVisible(true);
					}
				}
			}
		});

		JButton borrowButton = new JButton("\u501F\u9605");
		borrowButton.setFont(new Font("微软雅黑", Font.PLAIN, 32));
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
				// 选任一本书
				int currentRow = reservedTable.getSelectedRow();
				// 未选
				if (currentRow == -1) {
					JOptionPane.showMessageDialog(null, "未选中任何书!");
				} else {
					Object id = model1.getValueAt(currentRow, 0);
					if (id == null)
						JOptionPane.showMessageDialog(null, "未选中任何书!");
					else {
						DataProcessing.cancelOneReserved(cardID, currentRow);
						reservedTableRefresh(cardID);
					}
				}
			}
		});

		deleteOneButton.setFont(new Font("微软雅黑", Font.PLAIN, 32));
		deleteOneButton.setBounds(970, 645, 180, 52);
		contentPane.add(deleteOneButton);

		JButton deleteAllButton = new JButton("\u5220\u9664\u5168\u90E8");
		deleteAllButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (reservedTableRefresh(cardID) == 0)
					JOptionPane.showMessageDialog(null, "未预定任何书!");
				else {
					DataProcessing.cancelAllReserved(cardID);
					reservedTableRefresh(cardID);
				}
			}
		});
		deleteAllButton.setFont(new Font("微软雅黑", Font.PLAIN, 32));
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
			System.out.println("BookSearch:背景图片载入失败");
		}

		JLabel bckground = new JLabel(new ImageIcon(background));
		bckground.setText("");
		bckground.setBounds(0, -30, 1500, 800);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}

	public static void recommendTableRefresh() {
		String[] columnNames = { "ISBN", "图书标题", "图书类型", "被借阅次数" };
		model.addColumn(columnNames[0]);
		model.addColumn(columnNames[1]);
		model.addColumn(columnNames[2]);
		model.addColumn(columnNames[3]);

		// 这个大小要是很大怎么办(有一个limit语句的)
		String[][] result = new String[11][4];

		// borrowed_num+0才能正确排序
		String sql = "SELECT ISBN, book_title, book_type, borrowed_num FROM books ORDER BY (borrowed_num+0) DESC LIMIT 10";
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sql);

			result[0][0] = "ISBN";
			result[0][1] = "标题";
			result[0][2] = "类型";
			result[0][3] = "被借次数";

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
			System.out.println("BookSearch:在进行推荐表的刷新时查询图书类表出错");
		}
	}

	// cardID是用来显示读者姓名的,后面看看能不能读者的借阅看看能不能用上
	public static int reservedTableRefresh(String cardID) {

		int hasElem = 0;

		String[] columnNames = { "图书标题", "图书位置" };
		model1.addColumn(columnNames[0]);
		model1.addColumn(columnNames[1]);

		// 这个不能改成使用还能借多少本书来构造表格
		// 因为如果还能借一本,已经预定了两本,那么刷新之后将构建一个只有一行的表格来存放两行内容,会出错
		// 所以只能对是否选中了空行进行判断
		// 确定读者之后,这个表大小是不变的
		// 但注意临时预定表大小是会变的
		String[][] result = new String[DataProcessing.cardIDToCanBorrow(cardID)][2];

		// borrowed_num+0才能正确排序
		String sql = "SELECT bookID FROM temp" + cardID;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sql);

			if (DataProcessing.cardIDToStatus(cardID).equals("教师")) {
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
			System.out.println("BookSearch:在进行临时预定表的刷新时查询临时预定表出错");
		}

		return hasElem;
	}
}
