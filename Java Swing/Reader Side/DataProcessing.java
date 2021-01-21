package readerfunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

public class DataProcessing {

	private static boolean connectToDB = false;
	private static Connection connection;
	private static Statement statement;
	private static PreparedStatement preparedStatement;
	private static ResultSet resultSet;
	private static ResultSet borrowAllResultSet;

	public static boolean connectResult = false; // 连接结果，只有成功连接之后才会开启主页面

	public static void connectToDB(String driverName, String url, String user, String password) throws SQLException {
		try {
			Class.forName(driverName); // 加载数据库驱动类
			connection = DriverManager.getConnection(url, user, password);
			connectToDB = true;
		}

		catch (SQLSyntaxErrorException e1) {
			System.out.println("DataProcessing:数据库操作语法错误");
		} catch (ClassNotFoundException e2) {
			System.out.println("DataProcessing:Java连接MySQL所依赖的jar包加载失败.建议:将jar包加入classpath");
		} catch (SQLException e3) {
			System.out.println("DataProcessing:数据库连接失败");
		}
		connectResult = connectToDB;
	}

	// -----查询类-----
	// 查询图书(ISBN)是否记录过,若已经记录在册则返回true,没记录过就返回false
	public static boolean searchBookISBN(String ISBN) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM books WHERE ISBN ='" + ISBN + "'";
			resultSet = statement.executeQuery(sql);

			if (resultSet.next())
				// 已经有书在馆的情况
				return true;
		} catch (SQLException e) {
			System.out.println("DataProcessing:searchbookISBN(String ISBN)方法中查询图书类表失败");
		}
		return false;
	}

	// 查询图书标题是否存在(模糊查询)
	public static boolean searchBookTitle(String title) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM books WHERE book_title like '%" + title + "%'";
			resultSet = statement.executeQuery(sql);

			if (resultSet.next())
				// 已经有书在馆的情况
				return true;
		} catch (SQLException e) {
			System.out.println("DataProcessing:searchbookTitle(String title)方法中查询图书类表失败");
		}
		return false;
	}

	// 查询图书类型是否存在(这个感觉不是很必要,图书类型应该都有的)
	public static boolean searchBookType(String type) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM books WHERE book_type ='" + type + "'";
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				// 已经有书在馆的情况
				return true;
		} catch (SQLException e) {
			System.out.println("DataProcessing:searchbookType(String type)方法中查询图书类表失败");
		}
		return false;
	}

	// 查询有没有在馆的图书
	public static boolean searchSpecificBook(String ISBN) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM book WHERE ISBN ='" + ISBN + "' AND in_or_out = 0 AND reserved = 0";
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				// 已经有书在馆未被借出且未被预定
				return true;
		} catch (SQLException e) {
			System.out.println("DataProcessing:searchSpecificBook(String ISBN)方法中查询图书表失败");
		}
		return false;
	}

	// 查询具体图书号是否存在
	public static boolean searchBookID(String bookID) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT book_id FROM book WHERE book_id ='" + bookID + "'";
			resultSet = statement.executeQuery(sql);

			if (resultSet.next())
				// 已经有书在该位置上的情况
				return true;
			else
				return false;
		} catch (SQLException e) {
			System.out.println("DataProcessing:searchBookID(String bookID)方法中查询图书表失败");
		}
		return false;
	}

	// 判断此读者(这个借书证号)是否存在
	public static boolean searchCardID(String cardID) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM readers WHERE card_id ='" + cardID + "'";
			resultSet = statement.executeQuery(sql);

			if (resultSet.next())
				// 借阅证已登记情况
				return true;
		} catch (SQLException e) {
			System.out.println("DataProcessing:searchCardID(String cardID)方法中查询读者表失败");
		}
		return false;
	}

	// 还要判断一下这个人还能不能预定图书(根据已经借阅的书和已经预定的书)
	public static boolean canReserveMore(String cardID) {
		int canBorrow = 0;
		int bMore = 0;
		int rAlready = 0;

		try {
			// 搜寻借阅读者表
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT can_borrow, already_borrow FROM readers WHERE card_id ='" + cardID + "'";
			resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				canBorrow = Integer.parseInt(resultSet.getString("can_borrow"));
				int alreadyBorrow = Integer.parseInt(resultSet.getString("already_borrow"));
				bMore = canBorrow - alreadyBorrow;
			}

			// 搜寻临时预定表
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql1 = "SELECT * FROM temp" + cardID;
			resultSet = statement.executeQuery(sql1);

			while (resultSet.next()) {
				rAlready++;
			}

			// 不能再进行预定的3种情况:
			// [1]借有书,借的书已经到达上限;
			// [2]没借书,预定的书已达到上限;
			// [3]借有书,借的书没到达上限,但借的和已经预定的合起来就已经到达上限了;

			// 借的书已经到达上限
			if (bMore == 0)
				JOptionPane.showMessageDialog(null, "您所借图书数量已达上限!请先归还部分图书");

			// 没借书,预定的书已达到上限
			else if (bMore == canBorrow && rAlready == canBorrow)
				JOptionPane.showMessageDialog(null, "您所预定图书数量已达上限!请先删除部分预定图书");

			// 借有书,借的书没到达上限,但借的和已经预定的合起来就已经到达上限了
			// 有了第一个if (bMore == 0),此时bMore < canBorrow就不可能为0了
			else if (bMore < canBorrow && bMore - rAlready == 0)
				JOptionPane.showMessageDialog(null, "您所借图书与所预定图书的数量之和已达上限!请先归还部分图书或删除部分预定图书");

			else
				return true;
		} catch (SQLException e) {
			System.out.println("DataProcessing:canReserveMore(String cardID)方法中查询读者表或临时预定表失败");
		}
		return false;
	}

	// 判断此读者有没有借这本书
	public static boolean isMatch(String bookID, String cardID) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM borrow_info WHERE card_id ='" + cardID + "' AND book_id ='" + bookID + "'";
			resultSet = statement.executeQuery(sql);

			if (resultSet.next())
				return true;
		} catch (SQLException e) {
			System.out.println("DataProcessing:isMatch(String bookID, String cardID)方法中查询借阅信息表失败");
		}
		return false;
	}

	// -----预定借阅返还-----
	public static void reserveThisBook(String bookID, String cardID) {
		// 图书表update本图书被预定状态reserved为1
		// 临时表insert一条新记录

		// 图书表update本图书被预定状态reserved为1
		String sql = "UPDATE book SET reserved = 1 WHERE book_id ='" + bookID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:reserveThisBook(String bookID, String cardID)方法中修改图书表失败");
		}

		// 临时表insert一条新记录
		String sql1 = "INSERT INTO temp" + cardID + " (bookID) VALUES ('" + bookID + "')";
		try {
			preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:reserveThisBook(String bookID, String cardID)方法中修改临时借阅信息表失败");
		}

	}

	public static void borrowThisBook(String bookID, String cardID) {
		// 借阅信息表insert一条新记录
		// 图书表update本图书是否在馆状态in_or_out为1
		// 图书类表update被借阅次数borrowed_num加1
		// 读者表将已借书数加1

		Date currentDate = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

		String borrowTime = ft.format(currentDate);

		String ISBN = null;

		// 借阅信息表insert一条新记录
		String sql = "INSERT INTO borrow_info (card_id,book_id,borrow_date,renew_num) VALUES ('" + cardID + "','"
				+ bookID + "','" + borrowTime + "',0)";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:borrowThisBook(String bookID, String cardID)方法中修改借阅信息表失败");
		}

		// 图书表update本图书是否在馆状态in_or_out为1
		String sql1 = "UPDATE book SET in_or_out=1 WHERE book_id ='" + bookID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:borrowThisBook(String bookID, String cardID)方法中修改图书表失败");
		}

		// 图书类表update被借阅次数borrowed_num加1

		// ----先根据图书号找到ISBN号
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql2 = "SELECT ISBN FROM book WHERE book_id ='" + bookID + "'";
			resultSet = statement.executeQuery(sql2);

			while (resultSet.next())
				ISBN = resultSet.getString("ISBN");
		} catch (SQLException e) {
			System.out.println("DataProcessing:borrowThisBook(String bookID, String cardID)方法中查询图书表失败");
		}

		// ----再根据ISBN更新图书类表
		String sql3 = "UPDATE books SET borrowed_num = borrowed_num + 1 WHERE ISBN ='" + ISBN + "'";
		try {
			preparedStatement = connection.prepareStatement(sql3);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:borrowThisBook(String bookID, String cardID)方法中修改图书类表失败");
		}

		// 读者表将已借书数加1
		String sql4 = "UPDATE readers SET already_borrow = already_borrow + 1 WHERE card_id ='" + cardID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql4);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:borrowThisBook(String bookID, String cardID)方法中修改读者表失败");
		}
	}

	public static int borrowAllBooks(String cardID) {
		int isEmpty = 0;
		String bookID = null;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM temp" + cardID;
			borrowAllResultSet = statement.executeQuery(sql);

			// 执行借书操作
			while (borrowAllResultSet.next()) {
				bookID = borrowAllResultSet.getString("bookID");
				borrowThisBook(bookID, cardID);
				isEmpty = 1;
			}

			if (isEmpty == 1) {
				// 里面整合了修改图书表reserved位以及删除临时表的操作
				dropReserveTable(cardID);

				// 提示返还时间
				Date currentDate = new Date();

				Calendar period = Calendar.getInstance();
				period.setTime(currentDate);
				period.add(Calendar.DAY_OF_MONTH, 14); // 这个是可以跨月份年份进行计算的

				SimpleDateFormat ft = new SimpleDateFormat("yyyy年MM月dd日");
				String dueDate = ft.format(period.getTime());

				JOptionPane.showMessageDialog(null, "借书成功!请在" + dueDate + "之前归还");
			}

			else {
				JOptionPane.showMessageDialog(null, "未预定任何书!");
			}

		} catch (SQLException e) {
			System.out.println("DataProcessing:borrowAllBooks(String cardID)方法中查询临时表temp" + cardID + "失败");
		}
		return isEmpty;
	}

	// returnThisBook方法返回一个int类型的天数
	// 设置一个int函数，如果没超时就返回0，并进行相关修改
	// 超时了就返回超时天数，那边判断到不为0，就提示超时天数以及应交罚款，然后相关修改在管理员那边执行
	public static int returnThisBook(String bookID, String cardID) {
		// 先找到借阅时间，用一个String承接
		String borrowDay = null;
		Date dueDate = null;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT borrow_date FROM borrow_info WHERE card_id ='" + cardID + "' AND book_id ='" + bookID
					+ "'";
			resultSet = statement.executeQuery(sql);

			while (resultSet.next())
				borrowDay = resultSet.getString("borrow_date");
		} catch (SQLException e) {
			System.out.println("DataProcessing:returnThisBook(String bookID, String cardID)方法中查询借阅信息表失败");
		}

		// 将借阅时间转换为Date类型,计算应还时间dueDate
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		Date borrowDate;
		try {
			borrowDate = ft.parse(borrowDay);
			Calendar period = Calendar.getInstance();
			period.setTime(borrowDate);
			period.add(Calendar.DAY_OF_MONTH, 14); // 这个是可以跨月份年份进行计算的
			dueDate = period.getTime();
		} catch (ParseException e) {
			System.out.println("DataProcessing:returnThisBook(String bookID, String cardID)方法中计算应还时间时格式转换失败");
		}

		// 获取当前时间
		Date currentDate = new Date();

		// 判断dueDate和currentDate的先后顺序
		// 如果没过期
		if (currentDate.before(dueDate)) {
			// 执行返还操作
			// 返还信息表insert一条新记录
			// 图书表update本图书是否在馆状态in_or_out为0
			// 修改读者表的已借书数
			// 删除借阅信息表记录

			// 返还信息表insert一条新记录
			String returnTime = ft.format(currentDate);
			String sql = "INSERT INTO return_info (card_id,book_id,return_date) VALUES ('" + cardID + "','" + bookID
					+ "','" + returnTime + "')";
			try {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				System.out.println("DataProcessing:returnThisBook(String bookID, String cardID)方法中修改返还信息表失败");
			}

			// 图书表update本图书是否在馆状态in_or_out为0
			String sql1 = "UPDATE book SET in_or_out = 0 WHERE book_id ='" + bookID + "'";
			try {
				preparedStatement = connection.prepareStatement(sql1);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				System.out.println("DataProcessing:returnThisBook(String bookID, String cardID)方法中修改图书表失败");
			}

			// 读者表将已借书数减1
			String sql2 = "UPDATE readers SET already_borrow = already_borrow - 1 WHERE card_id ='" + cardID + "'";
			try {
				preparedStatement = connection.prepareStatement(sql2);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				System.out.println("DataProcessing:returnThisBook(String bookID, String cardID)方法中修改读者表失败");
			}

			// 删除借阅信息表记录
			String sql3 = "DELETE FROM borrow_info WHERE card_id ='" + cardID + "' AND book_id ='" + bookID + "'";
			try {
				preparedStatement = connection.prepareStatement(sql3);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				System.out.println("DataProcessing:returnThisBook(String bookID, String cardID)方法中修改借阅信息表失败");
			}

			return 0;
		} else {
			// 计算超时天数，不足一天就不管了
			int daysover; // 承接超时天数的
			long current = currentDate.getTime();
			long due = dueDate.getTime();

			daysover = (int) ((current - due) / (1000 * 60 * 60 * 24));
			return daysover;
		}
	}

	// 图书续借
	public static int renewThisBook(String bookID, String cardID) {
		// 先找到借阅时间，用一个String承接
		String borrowDay = null;
		int renewNum = 0;
		Date dueDate = null;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT borrow_date,renew_num FROM borrow_info WHERE card_id ='" + cardID + "' AND book_id ='"
					+ bookID + "'";
			resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				borrowDay = resultSet.getString("borrow_date");
				renewNum = Integer.parseInt(resultSet.getString("renew_num"));
			}
		} catch (SQLException e) {
			System.out.println("DataProcessing:renewThisBook(String bookID, String cardID)方法中查询借阅信息表失败");
		}

		// 将借阅时间转换为Date类型,计算应还时间dueDate
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat ft1 = new SimpleDateFormat("yyyy年MM月dd日");

		Date borrowDate;
		try {
			borrowDate = ft.parse(borrowDay);
			Calendar period = Calendar.getInstance();
			period.setTime(borrowDate);
			period.add(Calendar.DAY_OF_MONTH, 14); // 这个是可以跨月份年份进行计算的
			dueDate = period.getTime();
		} catch (ParseException e) {
			System.out.println("DataProcessing:renewThisBook(String bookID, String cardID)方法中计算应还时间时格式转换失败");
		}

		// 获取当前时间
		Date currentDate = new Date();

		// 判断dueDate和currentDate的先后顺序
		// 如果没过期
		if (currentDate.before(dueDate)) {
			// 先看看能不能续借,每次续借只能7天,最多续借两次
			if (renewNum == 2)
				JOptionPane.showMessageDialog(null,
						"图书号为" + bookID + "的图书续借次数已满,请在最后归还日期:" + ft1.format(dueDate) + "前归还");
			else {
				// 执行续借操作
				// renew_date加1
				// 更新借阅信息表的借阅时间就好了
				// 将借阅时间更新成currentDate,显示currentDate之后的14天
				// currentDate先加14天,让newDueDate承接结果之后再转换格式

				Calendar periodRenew = Calendar.getInstance();
				periodRenew.setTime(currentDate);
				periodRenew.add(Calendar.DAY_OF_MONTH, 7);
				Date newDueDate = periodRenew.getTime();

				String sql = "UPDATE borrow_info SET borrow_date = ? , renew_num = renew_num + 1 WHERE book_id = '"
						+ bookID + "' AND card_id ='" + cardID + "'";
				try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, ft.format(currentDate));
					preparedStatement.executeUpdate();
				} catch (SQLException e) {
					System.out
							.println("DataProcessing:renewThisBook(String bookID, String cardID)方法中修改借阅信息表(未过期的情况)失败");
				}
				JOptionPane.showMessageDialog(null, "图书号为" + bookID + "的图书续借成功!请于" + ft1.format(newDueDate) + "之前归还");
			}
			return 0;
		} else {
			// 计算超时天数，不足一天就不管了
			int daysover; // 承接超时天数的
			long current = currentDate.getTime();
			long due = dueDate.getTime();

			daysover = (int) ((current - due) / (1000 * 60 * 60 * 24));

			// 今日已过期,但过期天数还不足一天,也允许续借
			if (daysover == 0) {
				// 执行续借操作
				// 更新借阅信息表的借阅时间就好了
				// 将借阅时间更新成currentDate,显示currentDate之后的14天
				// currentDate先加14天,让newDueDate承接结果之后再转换格式
				Calendar periodRenew = Calendar.getInstance();
				periodRenew.setTime(currentDate);
				periodRenew.add(Calendar.DAY_OF_MONTH, 7);
				Date newDueDate = periodRenew.getTime();

				String sql = "UPDATE borrow_info SET borrow_date = ? WHERE book_id = '" + bookID + "' AND card_id ='"
						+ cardID + "'";
				try {
					preparedStatement = connection.prepareStatement(sql);
					preparedStatement.setString(1, ft.format(currentDate));
					preparedStatement.executeUpdate();
				} catch (SQLException e) {
					System.out.println(
							"DataProcessing:renewThisBook(String bookID, String cardID)方法中修改借阅信息表(过期少于一天的情况)失败");
				}

				JOptionPane.showMessageDialog(null, "续借成功!请于" + ft1.format(newDueDate) + "之前归还");

			}

			return daysover;
		}
	}

	// ---转换类---
	// 借书证号转读者姓名
	public static String cardIDToName(String cardID) {
		String ReaderName = null;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT reader_name FROM readers WHERE card_id ='" + cardID + "'";
			resultSet = statement.executeQuery(sql);

			while (resultSet.next())
				ReaderName = resultSet.getString("reader_name");
		} catch (SQLException e) {
			System.out.println("DataProcessing:cardIDToName(String cardID)方法中查询读者表失败");
		}
		return ReaderName;
	}

	// 借书证号转读者身份
	public static String cardIDToStatus(String cardID) {
		String ReaderStatus = null;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT reader_status FROM readers WHERE card_id ='" + cardID + "'";
			resultSet = statement.executeQuery(sql);

			while (resultSet.next())
				ReaderStatus = resultSet.getString("reader_status");
		} catch (SQLException e) {
			System.out.println("DataProcessing:cardIdToStatus(String cardID)方法中查询读者表失败");
		}
		return ReaderStatus;
	}

	// 图书号转图书标题
	public static String bookIDToName(String bookID) {
		String ISBN = null;
		String BookName = null;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT ISBN FROM book WHERE book_id ='" + bookID + "'";
			resultSet = statement.executeQuery(sql);

			while (resultSet.next())
				ISBN = resultSet.getString("ISBN");
		} catch (SQLException e) {
			System.out.println("DataProcessing:bookIDToName(String bookID)方法中查询图书表失败");
		}
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql1 = "SELECT book_title FROM books WHERE ISBN ='" + ISBN + "'";
			resultSet = statement.executeQuery(sql1);

			while (resultSet.next())
				BookName = resultSet.getString("book_title");
		} catch (SQLException e) {
			System.out.println("DataProcessing:bookIDToName(String bookID)方法中查询图书类表失败");
		}

		return BookName;
	}

	// 借书证号转可借书数
	public static int cardIDToCanBorrow(String cardID) {
		int CanBorrow = 0;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT can_borrow FROM readers WHERE card_id ='" + cardID + "'";
			resultSet = statement.executeQuery(sql);

			while (resultSet.next())
				CanBorrow = Integer.parseInt(resultSet.getString("can_borrow"));
		} catch (SQLException e) {
			System.out.println("DataProcessing:cardIDToCanBorrow(String cardID)方法中查询读者表失败");
		}
		return CanBorrow;
	}

	// 图书号转位置信息
	// 进行图书号到实际位置的换算
	// bookid = floorNum*100000+shelfNum*1000+shelfFloorNum*100+specificOrder;
	// 3楼12号书架4层第78本:312478
	public static String bookIDToPosInfo(String bookID) {
		int originalPos = Integer.parseInt(bookID);
		int floorNum = originalPos / 100000;
		int shelfNum = (originalPos % 100000) / 1000;
		int shelfFloorNum = (originalPos % 1000) / 100;
		int specificOrder = originalPos % 100;

		String PosInfo = Integer.toString(floorNum) + "楼" + Integer.toString(shelfNum) + "号书架"
				+ Integer.toString(shelfFloorNum) + "层序号" + Integer.toString(specificOrder);

		return PosInfo;
	}

	// ---预定表相关---
	// 创建临时预定表
	// 表名使用temp+借书证号(不能使用数字开头)
	public static void createReserveTable(String cardID) {
		String sql = "CREATE TABLE temp" + cardID + " ( bookID char(6) )";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:createReserveTable(String cardID)方法中创建临时预定表失败");
		}
	}

	// 根据临时表中指定的图书号,将图书表的reserved位恢复为0
	public static void reservedToZero(String bookID) {
		String sql = "UPDATE book SET reserved = 0 WHERE book_id ='" + bookID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:createReserveTable(String cardID)方法中修改图书表失败");
		}
	}

	// 删除临时预定表(供"取消"按钮使用)
	public static void dropReserveTable(String cardID) {
		// 先根据临时表中的图书号来对图书表的reserved位恢复为0,以便此书可被搜索到
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM temp" + cardID;
			resultSet = statement.executeQuery(sql);

			while (resultSet.next())
				reservedToZero(resultSet.getString("bookID"));

		} catch (SQLException e1) {
			System.out.println("DataProcessing:dropReserveTable(String cardID)方法中查询临时预定表或修改图书表失败");
		}

		// 然后再进行临时表删除操作
		String sql1 = "DROP TABLE temp" + cardID;
		try {
			preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.executeUpdate();
		} catch (SQLException e2) {
			System.out.println("DataProcessing:dropReserveTable(String cardID)方法中删除临时预定表失败");
		}
	}

	// 清空临时预定表(供"删除全部"按钮使用)
	public static void cancelAllReserved(String cardID) {
		String sql = "SELECT * FROM temp" + cardID;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				reservedToZero(resultSet.getString("bookID"));
			}
		} catch (SQLException e1) {
			System.out.println("DataProcessing:cancelAllReserved(String cardID)方法中查询临时预定表失败或修改图书表失败");
		}

		String sql1 = "DELETE FROM temp" + cardID + " WHERE bookID IS NOT NULL";
		try {
			preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:cancelAllReserved(String cardID)方法中删除临时表所有信息失败");
		}
	}

	// 删除选中的一本图书
	// 先在按钮处判断是否选对了(选上了而且没选空格————后面根据还能预定的书数分配行数应该就没有空格了)
	// 然后再进行图书表的修改以及临时表的删除
	public static void cancelOneReserved(String cardID, int pos) {
		int i = 0;
		String bookID = null;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM temp" + cardID;
			resultSet = statement.executeQuery(sql);

			while (i <= pos) {
				resultSet.next();
				i++;
			}
			bookID = resultSet.getString("bookID");

			reservedToZero(bookID);

		} catch (SQLException e1) {
			System.out.println("DataProcessing:cancelOneReserved(String cardID, int pos)方法中查询临时表失败或修改图书表失败");
		}

		deleteOneRecordInTemp(cardID, bookID);
	}

	// 删除操作整合成一个函数
	public static void deleteOneRecordInTemp(String cardID, String bookID) {
		String sql = "DELETE FROM temp" + cardID + " WHERE bookID = '" + bookID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:deleteOneLine(String cardID, String bookID)方法中修改临时表失败");
		}
	}
}
