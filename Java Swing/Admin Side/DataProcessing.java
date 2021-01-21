package bookandreadermanagement;

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

public class DataProcessing {

	private static boolean connectToDB = false;
	private static Connection connection;
	private static Statement statement;
	private static PreparedStatement preparedStatement;
	private static ResultSet resultSet;

	public static boolean connectResult = false; // 连接结果，只有成功连接之后才会开启主页面

	public static void connectToDB(String driverName, String url, String user, String password) throws SQLException {
		try {
			Class.forName(driverName); // 加载数据库驱动类
			connection = DriverManager.getConnection(url, user, password);
			connectToDB = true;
		}

		catch (SQLSyntaxErrorException e) {
			System.out.println("DataProcessing:数据库操作语法错误");
		} catch (ClassNotFoundException e) {
			System.out.println("DataProcessing:Java连接MySQL所依赖的jar包加载失败.建议:将jar包加入classpath");
		} catch (SQLException e) {
			System.out.println("DataProcessing:数据库连接失败");
		}
		connectResult = connectToDB;
	}

	// -----增加类-----
	// 这个只做图书类的插入工作
	public static boolean newBooksIn(String ISBN, String title, String type, String num, String desc) {
		String sql = "INSERT INTO books (ISBN,book_title,book_type,book_num,book_desc,borrowed_num) VALUES('" + ISBN
				+ "','" + title + "','" + type + "','" + num + "','" + desc + "'," + 0 + ")";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(
					"DataProcessing:newBooksIn(String ISBN, String title, String type, String num, String desc)方法中修改图书类表失败");
		}
		return true;
	}

	// 计算图书号
	public static String bookID(int floorNum, int shelfNum, int shelfFloorNum, int specificOrder) {
		int bookID;
		bookID = floorNum * 100000 + shelfNum * 1000 + shelfFloorNum * 100 + specificOrder;

		String bookIDTranslate;
		bookIDTranslate = Integer.toString(bookID);

		return bookIDTranslate;
	}

	// 这个只做具体图书的插入工作
	// 先直接在specificNewBook类中写，然后看看哪部分可以迁移过来
	public static void specificNewBookIn(String ISBN, String bookID) {
		String sql = "INSERT INTO book (ISBN,book_id,in_or_out,reserved) VALUES('" + ISBN + "','" + bookID + "'," + 0
				+ "," + 0 + ")";
		// 0-在馆,
		// 1-借出
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:specificNewBookIn(String ISBN, String bookID)方法中修改图书表失败");
		}
	}

	// 这个是做插入新读者信息的操作
	public static void newReaderIn(String cardID, String name, String phone, String status) {
		String sql_student = "INSERT INTO readers (card_id,reader_name,phone_num,reader_status,can_borrow,already_borrow) VALUES('"
				+ cardID + "','" + name + "','" + phone + "','" + status + "','" + 3 + "'," + 0 + ")";
		String sql_teacher = "INSERT INTO readers (card_id,reader_name,phone_num,reader_status,can_borrow,already_borrow) VALUES('"
				+ cardID + "','" + name + "','" + phone + "','" + status + "','" + 5 + "'," + 0 + ")";
		try {
			if (status.equals("学生")) {
				preparedStatement = connection.prepareStatement(sql_student);
				preparedStatement.executeUpdate();
			} else if (status.equals("教师")) {
				preparedStatement = connection.prepareStatement(sql_teacher);
				preparedStatement.executeUpdate();
			}

		} catch (SQLException e) {
			System.out.println("DataProcessing:specificNewBookIn(String ISBN, String bookID)方法中修改读者表失败");
		}
	}

	// -----查询类-----
	// ----查询books表----
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
			System.out.println("DataProcessing:searchBookISBN(String ISBN)方法中查询图书类表失败");
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
			System.out.println("DataProcessing:searchBookTitle(String title)方法中查询图书类表失败");
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
			System.out.println("DataProcessing:searchBookType(String type)方法中查询图书类表失败");
		}
		return false;
	}

	// 查询这本书还剩多少
	public static int howManyLast(String bookID) {
		int lastNum = 0;
		String ISBN = null;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql1 = "SELECT ISBN FROM book WHERE book_id ='" + bookID + "'";
			resultSet = statement.executeQuery(sql1);

			if (resultSet.next())
				ISBN = resultSet.getString("ISBN");

			String sql2 = "SELECT book_id FROM book WHERE ISBN ='" + ISBN + "'";
			resultSet = statement.executeQuery(sql2);

			while (resultSet.next())
				lastNum++;
		} catch (SQLException e) {
			System.out.println("DataProcessing:howManyLast(String bookID)方法中查询图书表失败");
		}
		return lastNum;
	}

	// ----查询book表----
	// 查询图书号是否记录过(该位置是否已经存在图书),若已经有图书则返回true,没有就返回false
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

	// 查询借书证号(cardID)是否记录过,若已经记录在册则返回true,没记录过就返回false
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

	// 查询并返回相对应的借书证号(不需要再手动输入了,直接返回一个合适的,适用于新登记的读者)
	public static String getCardID() {
		Date currentDate = new Date();

		Calendar period = Calendar.getInstance();
		period.setTime(currentDate);
		SimpleDateFormat ft = new SimpleDateFormat("yyMMdd");
		int draftCardID = Integer.parseInt(ft.format(period.getTime())) * 10000 + 1;

		// 当该ID已经被使用时
		while (searchCardID(Integer.toString(draftCardID))) {
			draftCardID++;
		}

		return Integer.toString(draftCardID);
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

	// 查询该书有没有过期的
	public static int isOverdue(String bookID, String cardID) {
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
			System.out.println("DataProcessing:isOverdue(String bookID, String cardID)方法中查询借阅信息表失败");
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
			System.out.println("DataProcessing:isOverdue(String bookID, String cardID)方法中计算应还时间时格式转换失败");
		}

		// 获取当前时间
		Date currentDate = new Date();

		// 判断dueDate和currentDate的先后顺序
		// 如果没过期
		if (currentDate.before(dueDate))
			return 0;
		else {
			// 计算超时天数，不足一天就不管了
			int daysover; // 承接超时天数的
			long current = currentDate.getTime();
			long due = dueDate.getTime();

			daysover = (int) ((current - due) / (1000 * 60 * 60 * 24));
			return daysover;
		}
	}

	// 查询该读者(即将删除的读者)有没有书没还
	public static boolean isStillKeeping(String cardID) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM borrow_info WHERE card_id ='" + cardID + "'";
			resultSet = statement.executeQuery(sql);

			if (resultSet.next())
				return true;
		} catch (SQLException e) {
			System.out.println("DataProcessing:isStillKeeping(String cardID)方法中借阅信息表失败");
		}
		return false;
	}

	// 查询“教师”身份,准备更改为“学生”身份的人有没有借超过3本书
	public static int howManyBooksBorrowed(String cardID) {
		int borrowedNum = 0;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT already_borrow FROM readers WHERE card_id ='" + cardID + "'";
			resultSet = statement.executeQuery(sql);

			resultSet.next();
			borrowedNum = Integer.parseInt(resultSet.getString("already_borrow"));
		} catch (SQLException e) {
			System.out.println("DataProcessing:howManyBooksBorrowed(String cardID)方法中查询读者表失败");
		}
		return borrowedNum;
	}

	// -----更新类-----
	// 更新图书类标题信息的
	public static void updateBooksTitle(String ISBN, String title) {
		String sql = "UPDATE books SET book_title=? WHERE ISBN ='" + ISBN + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, title);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:updateBooksTitle(String ISBN, String title)方法中修改图书类表失败");
		}
	}

	// 更新图书类类型信息的
	public static void updateBooksType(String ISBN, String type) {
		String sql = "UPDATE books SET book_type=? WHERE ISBN ='" + ISBN + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, type);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:updateBooksType(String ISBN, String type)方法中修改修改图书类表失败");
		}
	}

	// 更新图书类本数信息的(同时修改图书信息)(在搜寻一下图书表,里面没有相同的图书号的时候才能进行插入)
	public static void updateBooksNum(String ISBN, String num) {
		int originalNum;
		String sql1 = "SELECT book_num FROM books WHERE ISBN ='" + ISBN + "'";

		// 然后用next()判断是否存在,再用getString取出初始数量,变成int之后，再和变成int的num相加，然后再变回String，再update
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sql1);

			// 拿出原始book_num,转换成int,存在originalNum中
			if (resultSet.next()) {
				originalNum = Integer.parseInt(resultSet.getString("book_num"));
				num = Integer.toString((originalNum + Integer.parseInt(num)));
			}

		} catch (SQLException e1) {
			System.out.println("DataProcessing:updateBooksNum(String ISBN, String num)方法中查询图书类表失败");
		}

		String sql2 = "UPDATE books SET book_num=? WHERE ISBN ='" + ISBN + "'";
		try {
			preparedStatement = connection.prepareStatement(sql2);
			preparedStatement.setString(1, num);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("DataProcessing:updateBooksNum(String ISBN, String num)方法中修改图书类表失败");
		}
	}

	// 更新图书类简介信息的
	public static void updateBooksDesc(String ISBN, String desc) {
		String sql = "UPDATE books SET book_desc=? WHERE ISBN ='" + ISBN + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, desc);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:updateBooksDesc(String ISBN, String desc)方法中修改图书类表失败");
		}
	}

	// 更新图书位置信息的(体现在图书位置变动上)
	public static void updateBookID(String originalbookID, String newbookID) {
		String sql = "UPDATE book SET book_id=? WHERE book_id ='" + originalbookID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, newbookID);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:updateBookID(String originalbookID, String newbookID)方法中修改图书表失败");
		}
	}

	// 把准备要删除的读者所借的图书更新为在馆状态
	// 先在借阅信息表中根据cardID找到此读者所借的图书
	// 然后一条一条地更新图书表中状态
	// 更新完毕之后再根据cardID删除借阅信息表中此读者的借阅信息 -> deleteBorrowInfo(String deleteCardID)
	// 此时不用更新返还信息表了,读者都已经被删除了,不过他之前的历史借阅还是保留了
	// 之后再删除读者表中的读者信息 -> deleteReader(String deleteCardID)
	public static void returnAllBooksForDeletedReader(String cardID) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			// 先在借阅信息表中根据cardID找到此读者所借的图书
			// 然后一条一条地更新图书表中状态
			String sql = "SELECT book_id FROM borrow_info WHERE card_id ='" + cardID + "'";
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String bookToBeReturned = resultSet.getString("book_id");
				// 此时reserved位已经为0,不用再进行修改了
				String returnsql = "UPDATE book SET in_or_out=0 WHERE book_id ='" + bookToBeReturned + "'";
				try {
					preparedStatement = connection.prepareStatement(returnsql);
					preparedStatement.executeUpdate();
				} catch (SQLException e) {
					System.out.println("DataProcessing:returnAllBooksForDeletedReader(String cardID)方法中修改图书表失败");
				}
			}
		} catch (SQLException e) {
			System.out.println("DataProcessing:returnAllBooksForDeletedReader(String cardID)方法中查询借阅信息表失败");
		}
	}

	// 更新读者姓名信息的
	public static void updateReaderName(String cardID, String newReaderName) {
		String sql = "UPDATE readers SET reader_name=? WHERE card_id ='" + cardID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, newReaderName);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:updateReaderName(String cardID, String newReaderName)方法中修改读者表失败");
		}
	}

	// 更新读者电话信息的
	public static void updateReaderPhoneNum(String cardID, String newPhonenum) {
		String sql = "UPDATE readers SET phone_num=? WHERE card_id ='" + cardID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, newPhonenum);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:updateReaderPhoneNum(String cardID, String newPhonenum)方法中修改读者表失败");
		}
	}

	// 更新读者身份信息的
	public static void updateReaderStatus(String cardID, String newStatus) {
		String sql1 = "UPDATE readers SET reader_status=? WHERE card_id ='" + cardID + "'";
		String sql2 = "UPDATE readers SET can_borrow=? WHERE card_id ='" + cardID + "'";
		try {
			// 修改身份
			preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.setString(1, newStatus);
			preparedStatement.executeUpdate();

			// 修改可借书数
			if (newStatus.equals("教师")) {
				preparedStatement = connection.prepareStatement(sql2);
				preparedStatement.setString(1, "5");
			} else if (newStatus.equals("学生")) {
				preparedStatement = connection.prepareStatement(sql2);
				preparedStatement.setString(1, "3");
			}
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:updateReaderStatus(String cardID, String newStatus)方法中修改读者表失败");
		}
	}

	// -----删除类-----
	// 删除同ISBN所有图书信息
	public static void deleteBooks(String deleteISBN) {
		String sql1 = "DELETE FROM book where ISBN = '" + deleteISBN + "'";
		String sql2 = "DELETE FROM books where ISBN = '" + deleteISBN + "'";

		try {
			preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.executeUpdate();

			preparedStatement = connection.prepareStatement(sql2);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:deleteBooks(String deleteISBN)方法中修改图书类表或图书表失败");
		}
	}

	// 删除具体某一本图书,并更新图书类表信息(UPDATE)
	public static void deleteSpecificBook(String deleteBookID) {
		String sql1 = "DELETE FROM book where book_id = '" + deleteBookID + "'";
		String sql2 = "UPDATE books SET book_num = book_num - 1 where ISBN = '" + bookIDToISBN(deleteBookID) + "'";

		try {
			preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.executeUpdate();

			preparedStatement = connection.prepareStatement(sql2);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("DataProcessing:deleteSpecificBook(String deleteBookID)方法中修改图书类表或图书表失败");
		}
	}

	// 删除读者信息
	public static void deleteReader(String deleteCardID) {
		String sql = "DELETE FROM readers where card_id = '" + deleteCardID + "'";

		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("DataProcessing:deleteReader(String deleteCardID)方法中修改读者表失败");
		}
	}

	// 删除借阅信息表中读者信息
	public static void deleteBorrowInfo(String deleteCardID) {
		String sql = "DELETE FROM borrow_info where card_id = '" + deleteCardID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:deleteBorrowInfo(String deleteCardID)方法中修改借阅信息表失败");
		}
	}

	// 逾期图书返还返还
	public static void returnThisOverdueBook(String bookID, String cardID) {
		// 执行返还操作
		// 返还信息表insert一条新记录
		// 图书表update本图书是否在馆状态in_or_out为0
		// 修改读者表的已借书数
		// 删除借阅信息表记录

		// 获取当前时间
		Date currentDate = new Date();

		// 格式转换
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

		// 返还信息表insert一条新记录
		String returnTime = ft.format(currentDate);
		String sql = "INSERT INTO return_info (card_id,book_id,return_date) VALUES ('" + cardID + "','" + bookID + "','"
				+ returnTime + "')";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:returnThisOverdueBook(String bookID, String cardID)方法中修改返还信息表失败");
		}

		// 图书表update本图书是否在馆状态in_or_out为0
		String sql1 = "UPDATE book SET in_or_out = 0 WHERE book_id ='" + bookID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:returnThisOverdueBook(String bookID, String cardID)方法中修改图书表失败");
		}

		// 读者表将已借书数减1
		String sql2 = "UPDATE readers SET already_borrow = already_borrow - 1 WHERE card_id ='" + cardID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql2);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:returnThisOverdueBook(String bookID, String cardID)方法中修改读者表失败");
		}

		// 删除借阅信息表记录
		String sql3 = "DELETE FROM borrow_info WHERE card_id ='" + cardID + "' AND book_id ='" + bookID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql3);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:returnThisOverdueBook(String bookID, String cardID)方法中修改借阅信息表失败");
		}
	}

	// -----转换类-----
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

	public static String bookIDToISBN(String bookID) {
		String ISBN = null;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT ISBN FROM book WHERE book_id ='" + bookID + "'";
			resultSet = statement.executeQuery(sql);

			while (resultSet.next())
				ISBN = resultSet.getString("ISBN");
		} catch (SQLException e) {
			System.out.println("DataProcessing:bookIDToISBN(String bookID)方法中查询图书表失败");
		}
		return ISBN;
	}

	public static String ISBNToName(String ISBN) {
		String BookName = null;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT book_title FROM books WHERE ISBN ='" + ISBN + "'";
			resultSet = statement.executeQuery(sql);

			while (resultSet.next())
				BookName = resultSet.getString("book_title");
		} catch (SQLException e) {
			System.out.println("DataProcessing:ISBNToName(String ISBN)方法中查询图书类表失败");
		}
		return BookName;
	}

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

	// 借书证号转读者电话号码
	public static String cardIDToPhone(String cardID) {
		String ReaderPhone = null;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT phone_num FROM readers WHERE card_id ='" + cardID + "'";
			resultSet = statement.executeQuery(sql);

			while (resultSet.next())
				ReaderPhone = resultSet.getString("phone_num");
		} catch (SQLException e) {
			System.out.println("DataProcessing:cardIDToPhone(String cardID)方法中查询读者表失败");
		}
		return ReaderPhone;
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
}
