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

	public static boolean connectResult = false; // ���ӽ����ֻ�гɹ�����֮��ŻῪ����ҳ��

	public static void connectToDB(String driverName, String url, String user, String password) throws SQLException {
		try {
			Class.forName(driverName); // �������ݿ�������
			connection = DriverManager.getConnection(url, user, password);
			connectToDB = true;
		}

		catch (SQLSyntaxErrorException e) {
			System.out.println("DataProcessing:���ݿ�����﷨����");
		} catch (ClassNotFoundException e) {
			System.out.println("DataProcessing:Java����MySQL��������jar������ʧ��.����:��jar������classpath");
		} catch (SQLException e) {
			System.out.println("DataProcessing:���ݿ�����ʧ��");
		}
		connectResult = connectToDB;
	}

	// -----������-----
	// ���ֻ��ͼ����Ĳ��빤��
	public static boolean newBooksIn(String ISBN, String title, String type, String num, String desc) {
		String sql = "INSERT INTO books (ISBN,book_title,book_type,book_num,book_desc,borrowed_num) VALUES('" + ISBN
				+ "','" + title + "','" + type + "','" + num + "','" + desc + "'," + 0 + ")";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println(
					"DataProcessing:newBooksIn(String ISBN, String title, String type, String num, String desc)�������޸�ͼ�����ʧ��");
		}
		return true;
	}

	// ����ͼ���
	public static String bookID(int floorNum, int shelfNum, int shelfFloorNum, int specificOrder) {
		int bookID;
		bookID = floorNum * 100000 + shelfNum * 1000 + shelfFloorNum * 100 + specificOrder;

		String bookIDTranslate;
		bookIDTranslate = Integer.toString(bookID);

		return bookIDTranslate;
	}

	// ���ֻ������ͼ��Ĳ��빤��
	// ��ֱ����specificNewBook����д��Ȼ�󿴿��Ĳ��ֿ���Ǩ�ƹ���
	public static void specificNewBookIn(String ISBN, String bookID) {
		String sql = "INSERT INTO book (ISBN,book_id,in_or_out,reserved) VALUES('" + ISBN + "','" + bookID + "'," + 0
				+ "," + 0 + ")";
		// 0-�ڹ�,
		// 1-���
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:specificNewBookIn(String ISBN, String bookID)�������޸�ͼ���ʧ��");
		}
	}

	// ������������¶�����Ϣ�Ĳ���
	public static void newReaderIn(String cardID, String name, String phone, String status) {
		String sql_student = "INSERT INTO readers (card_id,reader_name,phone_num,reader_status,can_borrow,already_borrow) VALUES('"
				+ cardID + "','" + name + "','" + phone + "','" + status + "','" + 3 + "'," + 0 + ")";
		String sql_teacher = "INSERT INTO readers (card_id,reader_name,phone_num,reader_status,can_borrow,already_borrow) VALUES('"
				+ cardID + "','" + name + "','" + phone + "','" + status + "','" + 5 + "'," + 0 + ")";
		try {
			if (status.equals("ѧ��")) {
				preparedStatement = connection.prepareStatement(sql_student);
				preparedStatement.executeUpdate();
			} else if (status.equals("��ʦ")) {
				preparedStatement = connection.prepareStatement(sql_teacher);
				preparedStatement.executeUpdate();
			}

		} catch (SQLException e) {
			System.out.println("DataProcessing:specificNewBookIn(String ISBN, String bookID)�������޸Ķ��߱�ʧ��");
		}
	}

	// -----��ѯ��-----
	// ----��ѯbooks��----
	// ��ѯͼ��(ISBN)�Ƿ��¼��,���Ѿ���¼�ڲ��򷵻�true,û��¼���ͷ���false
	public static boolean searchBookISBN(String ISBN) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM books WHERE ISBN ='" + ISBN + "'";
			resultSet = statement.executeQuery(sql);

			if (resultSet.next())
				// �Ѿ������ڹݵ����
				return true;
		} catch (SQLException e) {
			System.out.println("DataProcessing:searchBookISBN(String ISBN)�����в�ѯͼ�����ʧ��");
		}
		return false;
	}

	// ��ѯͼ������Ƿ����(ģ����ѯ)
	public static boolean searchBookTitle(String title) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM books WHERE book_title like '%" + title + "%'";
			resultSet = statement.executeQuery(sql);

			if (resultSet.next())
				// �Ѿ������ڹݵ����
				return true;
		} catch (SQLException e) {
			System.out.println("DataProcessing:searchBookTitle(String title)�����в�ѯͼ�����ʧ��");
		}
		return false;
	}

	// ��ѯͼ�������Ƿ����(����о����Ǻܱ�Ҫ,ͼ������Ӧ�ö��е�)
	public static boolean searchBookType(String type) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM books WHERE book_type ='" + type + "'";
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				// �Ѿ������ڹݵ����
				return true;
		} catch (SQLException e) {
			System.out.println("DataProcessing:searchBookType(String type)�����в�ѯͼ�����ʧ��");
		}
		return false;
	}

	// ��ѯ�Ȿ�黹ʣ����
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
			System.out.println("DataProcessing:howManyLast(String bookID)�����в�ѯͼ���ʧ��");
		}
		return lastNum;
	}

	// ----��ѯbook��----
	// ��ѯͼ����Ƿ��¼��(��λ���Ƿ��Ѿ�����ͼ��),���Ѿ���ͼ���򷵻�true,û�оͷ���false
	public static boolean searchBookID(String bookID) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT book_id FROM book WHERE book_id ='" + bookID + "'";
			resultSet = statement.executeQuery(sql);

			if (resultSet.next())
				// �Ѿ������ڸ�λ���ϵ����
				return true;
			else
				return false;
		} catch (SQLException e) {
			System.out.println("DataProcessing:searchBookID(String bookID)�����в�ѯͼ���ʧ��");
		}
		return false;
	}

	// ��ѯ����֤��(cardID)�Ƿ��¼��,���Ѿ���¼�ڲ��򷵻�true,û��¼���ͷ���false
	public static boolean searchCardID(String cardID) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM readers WHERE card_id ='" + cardID + "'";
			resultSet = statement.executeQuery(sql);

			if (resultSet.next())
				// ����֤�ѵǼ����
				return true;
		} catch (SQLException e) {
			System.out.println("DataProcessing:searchCardID(String cardID)�����в�ѯ���߱�ʧ��");
		}
		return false;
	}

	// ��ѯ���������Ӧ�Ľ���֤��(����Ҫ���ֶ�������,ֱ�ӷ���һ�����ʵ�,�������µǼǵĶ���)
	public static String getCardID() {
		Date currentDate = new Date();

		Calendar period = Calendar.getInstance();
		period.setTime(currentDate);
		SimpleDateFormat ft = new SimpleDateFormat("yyMMdd");
		int draftCardID = Integer.parseInt(ft.format(period.getTime())) * 10000 + 1;

		// ����ID�Ѿ���ʹ��ʱ
		while (searchCardID(Integer.toString(draftCardID))) {
			draftCardID++;
		}

		return Integer.toString(draftCardID);
	}

	// �жϴ˶�����û�н��Ȿ��
	public static boolean isMatch(String bookID, String cardID) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM borrow_info WHERE card_id ='" + cardID + "' AND book_id ='" + bookID + "'";
			resultSet = statement.executeQuery(sql);

			if (resultSet.next())
				return true;
		} catch (SQLException e) {
			System.out.println("DataProcessing:isMatch(String bookID, String cardID)�����в�ѯ������Ϣ��ʧ��");
		}
		return false;
	}

	// ��ѯ������û�й��ڵ�
	public static int isOverdue(String bookID, String cardID) {
		// ���ҵ�����ʱ�䣬��һ��String�н�
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
			System.out.println("DataProcessing:isOverdue(String bookID, String cardID)�����в�ѯ������Ϣ��ʧ��");
		}

		// ������ʱ��ת��ΪDate����,����Ӧ��ʱ��dueDate
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		Date borrowDate;
		try {
			borrowDate = ft.parse(borrowDay);
			Calendar period = Calendar.getInstance();
			period.setTime(borrowDate);
			period.add(Calendar.DAY_OF_MONTH, 14); // ����ǿ��Կ��·���ݽ��м����
			dueDate = period.getTime();
		} catch (ParseException e) {
			System.out.println("DataProcessing:isOverdue(String bookID, String cardID)�����м���Ӧ��ʱ��ʱ��ʽת��ʧ��");
		}

		// ��ȡ��ǰʱ��
		Date currentDate = new Date();

		// �ж�dueDate��currentDate���Ⱥ�˳��
		// ���û����
		if (currentDate.before(dueDate))
			return 0;
		else {
			// ���㳬ʱ����������һ��Ͳ�����
			int daysover; // �нӳ�ʱ������
			long current = currentDate.getTime();
			long due = dueDate.getTime();

			daysover = (int) ((current - due) / (1000 * 60 * 60 * 24));
			return daysover;
		}
	}

	// ��ѯ�ö���(����ɾ���Ķ���)��û����û��
	public static boolean isStillKeeping(String cardID) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM borrow_info WHERE card_id ='" + cardID + "'";
			resultSet = statement.executeQuery(sql);

			if (resultSet.next())
				return true;
		} catch (SQLException e) {
			System.out.println("DataProcessing:isStillKeeping(String cardID)�����н�����Ϣ��ʧ��");
		}
		return false;
	}

	// ��ѯ����ʦ�����,׼������Ϊ��ѧ������ݵ�����û�н賬��3����
	public static int howManyBooksBorrowed(String cardID) {
		int borrowedNum = 0;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT already_borrow FROM readers WHERE card_id ='" + cardID + "'";
			resultSet = statement.executeQuery(sql);

			resultSet.next();
			borrowedNum = Integer.parseInt(resultSet.getString("already_borrow"));
		} catch (SQLException e) {
			System.out.println("DataProcessing:howManyBooksBorrowed(String cardID)�����в�ѯ���߱�ʧ��");
		}
		return borrowedNum;
	}

	// -----������-----
	// ����ͼ���������Ϣ��
	public static void updateBooksTitle(String ISBN, String title) {
		String sql = "UPDATE books SET book_title=? WHERE ISBN ='" + ISBN + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, title);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:updateBooksTitle(String ISBN, String title)�������޸�ͼ�����ʧ��");
		}
	}

	// ����ͼ����������Ϣ��
	public static void updateBooksType(String ISBN, String type) {
		String sql = "UPDATE books SET book_type=? WHERE ISBN ='" + ISBN + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, type);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:updateBooksType(String ISBN, String type)�������޸��޸�ͼ�����ʧ��");
		}
	}

	// ����ͼ���౾����Ϣ��(ͬʱ�޸�ͼ����Ϣ)(����Ѱһ��ͼ���,����û����ͬ��ͼ��ŵ�ʱ����ܽ��в���)
	public static void updateBooksNum(String ISBN, String num) {
		int originalNum;
		String sql1 = "SELECT book_num FROM books WHERE ISBN ='" + ISBN + "'";

		// Ȼ����next()�ж��Ƿ����,����getStringȡ����ʼ����,���int֮���ٺͱ��int��num��ӣ�Ȼ���ٱ��String����update
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sql1);

			// �ó�ԭʼbook_num,ת����int,����originalNum��
			if (resultSet.next()) {
				originalNum = Integer.parseInt(resultSet.getString("book_num"));
				num = Integer.toString((originalNum + Integer.parseInt(num)));
			}

		} catch (SQLException e1) {
			System.out.println("DataProcessing:updateBooksNum(String ISBN, String num)�����в�ѯͼ�����ʧ��");
		}

		String sql2 = "UPDATE books SET book_num=? WHERE ISBN ='" + ISBN + "'";
		try {
			preparedStatement = connection.prepareStatement(sql2);
			preparedStatement.setString(1, num);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("DataProcessing:updateBooksNum(String ISBN, String num)�������޸�ͼ�����ʧ��");
		}
	}

	// ����ͼ��������Ϣ��
	public static void updateBooksDesc(String ISBN, String desc) {
		String sql = "UPDATE books SET book_desc=? WHERE ISBN ='" + ISBN + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, desc);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:updateBooksDesc(String ISBN, String desc)�������޸�ͼ�����ʧ��");
		}
	}

	// ����ͼ��λ����Ϣ��(������ͼ��λ�ñ䶯��)
	public static void updateBookID(String originalbookID, String newbookID) {
		String sql = "UPDATE book SET book_id=? WHERE book_id ='" + originalbookID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, newbookID);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:updateBookID(String originalbookID, String newbookID)�������޸�ͼ���ʧ��");
		}
	}

	// ��׼��Ҫɾ���Ķ��������ͼ�����Ϊ�ڹ�״̬
	// ���ڽ�����Ϣ���и���cardID�ҵ��˶��������ͼ��
	// Ȼ��һ��һ���ظ���ͼ�����״̬
	// �������֮���ٸ���cardIDɾ��������Ϣ���д˶��ߵĽ�����Ϣ -> deleteBorrowInfo(String deleteCardID)
	// ��ʱ���ø��·�����Ϣ����,���߶��Ѿ���ɾ����,������֮ǰ����ʷ���Ļ��Ǳ�����
	// ֮����ɾ�����߱��еĶ�����Ϣ -> deleteReader(String deleteCardID)
	public static void returnAllBooksForDeletedReader(String cardID) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			// ���ڽ�����Ϣ���и���cardID�ҵ��˶��������ͼ��
			// Ȼ��һ��һ���ظ���ͼ�����״̬
			String sql = "SELECT book_id FROM borrow_info WHERE card_id ='" + cardID + "'";
			resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				String bookToBeReturned = resultSet.getString("book_id");
				// ��ʱreservedλ�Ѿ�Ϊ0,�����ٽ����޸���
				String returnsql = "UPDATE book SET in_or_out=0 WHERE book_id ='" + bookToBeReturned + "'";
				try {
					preparedStatement = connection.prepareStatement(returnsql);
					preparedStatement.executeUpdate();
				} catch (SQLException e) {
					System.out.println("DataProcessing:returnAllBooksForDeletedReader(String cardID)�������޸�ͼ���ʧ��");
				}
			}
		} catch (SQLException e) {
			System.out.println("DataProcessing:returnAllBooksForDeletedReader(String cardID)�����в�ѯ������Ϣ��ʧ��");
		}
	}

	// ���¶���������Ϣ��
	public static void updateReaderName(String cardID, String newReaderName) {
		String sql = "UPDATE readers SET reader_name=? WHERE card_id ='" + cardID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, newReaderName);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:updateReaderName(String cardID, String newReaderName)�������޸Ķ��߱�ʧ��");
		}
	}

	// ���¶��ߵ绰��Ϣ��
	public static void updateReaderPhoneNum(String cardID, String newPhonenum) {
		String sql = "UPDATE readers SET phone_num=? WHERE card_id ='" + cardID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, newPhonenum);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:updateReaderPhoneNum(String cardID, String newPhonenum)�������޸Ķ��߱�ʧ��");
		}
	}

	// ���¶��������Ϣ��
	public static void updateReaderStatus(String cardID, String newStatus) {
		String sql1 = "UPDATE readers SET reader_status=? WHERE card_id ='" + cardID + "'";
		String sql2 = "UPDATE readers SET can_borrow=? WHERE card_id ='" + cardID + "'";
		try {
			// �޸����
			preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.setString(1, newStatus);
			preparedStatement.executeUpdate();

			// �޸Ŀɽ�����
			if (newStatus.equals("��ʦ")) {
				preparedStatement = connection.prepareStatement(sql2);
				preparedStatement.setString(1, "5");
			} else if (newStatus.equals("ѧ��")) {
				preparedStatement = connection.prepareStatement(sql2);
				preparedStatement.setString(1, "3");
			}
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:updateReaderStatus(String cardID, String newStatus)�������޸Ķ��߱�ʧ��");
		}
	}

	// -----ɾ����-----
	// ɾ��ͬISBN����ͼ����Ϣ
	public static void deleteBooks(String deleteISBN) {
		String sql1 = "DELETE FROM book where ISBN = '" + deleteISBN + "'";
		String sql2 = "DELETE FROM books where ISBN = '" + deleteISBN + "'";

		try {
			preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.executeUpdate();

			preparedStatement = connection.prepareStatement(sql2);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:deleteBooks(String deleteISBN)�������޸�ͼ������ͼ���ʧ��");
		}
	}

	// ɾ������ĳһ��ͼ��,������ͼ�������Ϣ(UPDATE)
	public static void deleteSpecificBook(String deleteBookID) {
		String sql1 = "DELETE FROM book where book_id = '" + deleteBookID + "'";
		String sql2 = "UPDATE books SET book_num = book_num - 1 where ISBN = '" + bookIDToISBN(deleteBookID) + "'";

		try {
			preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.executeUpdate();

			preparedStatement = connection.prepareStatement(sql2);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("DataProcessing:deleteSpecificBook(String deleteBookID)�������޸�ͼ������ͼ���ʧ��");
		}
	}

	// ɾ��������Ϣ
	public static void deleteReader(String deleteCardID) {
		String sql = "DELETE FROM readers where card_id = '" + deleteCardID + "'";

		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			System.out.println("DataProcessing:deleteReader(String deleteCardID)�������޸Ķ��߱�ʧ��");
		}
	}

	// ɾ��������Ϣ���ж�����Ϣ
	public static void deleteBorrowInfo(String deleteCardID) {
		String sql = "DELETE FROM borrow_info where card_id = '" + deleteCardID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:deleteBorrowInfo(String deleteCardID)�������޸Ľ�����Ϣ��ʧ��");
		}
	}

	// ����ͼ�鷵������
	public static void returnThisOverdueBook(String bookID, String cardID) {
		// ִ�з�������
		// ������Ϣ��insertһ���¼�¼
		// ͼ���update��ͼ���Ƿ��ڹ�״̬in_or_outΪ0
		// �޸Ķ��߱���ѽ�����
		// ɾ��������Ϣ���¼

		// ��ȡ��ǰʱ��
		Date currentDate = new Date();

		// ��ʽת��
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

		// ������Ϣ��insertһ���¼�¼
		String returnTime = ft.format(currentDate);
		String sql = "INSERT INTO return_info (card_id,book_id,return_date) VALUES ('" + cardID + "','" + bookID + "','"
				+ returnTime + "')";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:returnThisOverdueBook(String bookID, String cardID)�������޸ķ�����Ϣ��ʧ��");
		}

		// ͼ���update��ͼ���Ƿ��ڹ�״̬in_or_outΪ0
		String sql1 = "UPDATE book SET in_or_out = 0 WHERE book_id ='" + bookID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:returnThisOverdueBook(String bookID, String cardID)�������޸�ͼ���ʧ��");
		}

		// ���߱��ѽ�������1
		String sql2 = "UPDATE readers SET already_borrow = already_borrow - 1 WHERE card_id ='" + cardID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql2);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:returnThisOverdueBook(String bookID, String cardID)�������޸Ķ��߱�ʧ��");
		}

		// ɾ��������Ϣ���¼
		String sql3 = "DELETE FROM borrow_info WHERE card_id ='" + cardID + "' AND book_id ='" + bookID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql3);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:returnThisOverdueBook(String bookID, String cardID)�������޸Ľ�����Ϣ��ʧ��");
		}
	}

	// -----ת����-----
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
			System.out.println("DataProcessing:bookIDToName(String bookID)�����в�ѯͼ���ʧ��");
		}
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql1 = "SELECT book_title FROM books WHERE ISBN ='" + ISBN + "'";
			resultSet = statement.executeQuery(sql1);

			while (resultSet.next())
				BookName = resultSet.getString("book_title");
		} catch (SQLException e) {
			System.out.println("DataProcessing:bookIDToName(String bookID)�����в�ѯͼ�����ʧ��");
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
			System.out.println("DataProcessing:bookIDToISBN(String bookID)�����в�ѯͼ���ʧ��");
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
			System.out.println("DataProcessing:ISBNToName(String ISBN)�����в�ѯͼ�����ʧ��");
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
			System.out.println("DataProcessing:cardIDToName(String cardID)�����в�ѯ���߱�ʧ��");
		}
		return ReaderName;
	}

	// ����֤��ת���ߵ绰����
	public static String cardIDToPhone(String cardID) {
		String ReaderPhone = null;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT phone_num FROM readers WHERE card_id ='" + cardID + "'";
			resultSet = statement.executeQuery(sql);

			while (resultSet.next())
				ReaderPhone = resultSet.getString("phone_num");
		} catch (SQLException e) {
			System.out.println("DataProcessing:cardIDToPhone(String cardID)�����в�ѯ���߱�ʧ��");
		}
		return ReaderPhone;
	}

	// ����֤��ת�������
	public static String cardIDToStatus(String cardID) {
		String ReaderStatus = null;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT reader_status FROM readers WHERE card_id ='" + cardID + "'";
			resultSet = statement.executeQuery(sql);

			while (resultSet.next())
				ReaderStatus = resultSet.getString("reader_status");
		} catch (SQLException e) {
			System.out.println("DataProcessing:cardIdToStatus(String cardID)�����в�ѯ���߱�ʧ��");
		}
		return ReaderStatus;
	}
}
