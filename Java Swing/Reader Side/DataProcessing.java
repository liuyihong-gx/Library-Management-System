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

	public static boolean connectResult = false; // ���ӽ����ֻ�гɹ�����֮��ŻῪ����ҳ��

	public static void connectToDB(String driverName, String url, String user, String password) throws SQLException {
		try {
			Class.forName(driverName); // �������ݿ�������
			connection = DriverManager.getConnection(url, user, password);
			connectToDB = true;
		}

		catch (SQLSyntaxErrorException e1) {
			System.out.println("DataProcessing:���ݿ�����﷨����");
		} catch (ClassNotFoundException e2) {
			System.out.println("DataProcessing:Java����MySQL��������jar������ʧ��.����:��jar������classpath");
		} catch (SQLException e3) {
			System.out.println("DataProcessing:���ݿ�����ʧ��");
		}
		connectResult = connectToDB;
	}

	// -----��ѯ��-----
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
			System.out.println("DataProcessing:searchbookISBN(String ISBN)�����в�ѯͼ�����ʧ��");
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
			System.out.println("DataProcessing:searchbookTitle(String title)�����в�ѯͼ�����ʧ��");
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
			System.out.println("DataProcessing:searchbookType(String type)�����в�ѯͼ�����ʧ��");
		}
		return false;
	}

	// ��ѯ��û���ڹݵ�ͼ��
	public static boolean searchSpecificBook(String ISBN) {
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM book WHERE ISBN ='" + ISBN + "' AND in_or_out = 0 AND reserved = 0";
			resultSet = statement.executeQuery(sql);
			if (resultSet.next())
				// �Ѿ������ڹ�δ�������δ��Ԥ��
				return true;
		} catch (SQLException e) {
			System.out.println("DataProcessing:searchSpecificBook(String ISBN)�����в�ѯͼ���ʧ��");
		}
		return false;
	}

	// ��ѯ����ͼ����Ƿ����
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

	// �жϴ˶���(�������֤��)�Ƿ����
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

	// ��Ҫ�ж�һ������˻��ܲ���Ԥ��ͼ��(�����Ѿ����ĵ�����Ѿ�Ԥ������)
	public static boolean canReserveMore(String cardID) {
		int canBorrow = 0;
		int bMore = 0;
		int rAlready = 0;

		try {
			// ��Ѱ���Ķ��߱�
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT can_borrow, already_borrow FROM readers WHERE card_id ='" + cardID + "'";
			resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				canBorrow = Integer.parseInt(resultSet.getString("can_borrow"));
				int alreadyBorrow = Integer.parseInt(resultSet.getString("already_borrow"));
				bMore = canBorrow - alreadyBorrow;
			}

			// ��Ѱ��ʱԤ����
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql1 = "SELECT * FROM temp" + cardID;
			resultSet = statement.executeQuery(sql1);

			while (resultSet.next()) {
				rAlready++;
			}

			// �����ٽ���Ԥ����3�����:
			// [1]������,������Ѿ���������;
			// [2]û����,Ԥ�������Ѵﵽ����;
			// [3]������,�����û��������,����ĺ��Ѿ�Ԥ���ĺ��������Ѿ�����������;

			// ������Ѿ���������
			if (bMore == 0)
				JOptionPane.showMessageDialog(null, "������ͼ�������Ѵ�����!���ȹ黹����ͼ��");

			// û����,Ԥ�������Ѵﵽ����
			else if (bMore == canBorrow && rAlready == canBorrow)
				JOptionPane.showMessageDialog(null, "����Ԥ��ͼ�������Ѵ�����!����ɾ������Ԥ��ͼ��");

			// ������,�����û��������,����ĺ��Ѿ�Ԥ���ĺ��������Ѿ�����������
			// ���˵�һ��if (bMore == 0),��ʱbMore < canBorrow�Ͳ�����Ϊ0��
			else if (bMore < canBorrow && bMore - rAlready == 0)
				JOptionPane.showMessageDialog(null, "������ͼ������Ԥ��ͼ�������֮���Ѵ�����!���ȹ黹����ͼ���ɾ������Ԥ��ͼ��");

			else
				return true;
		} catch (SQLException e) {
			System.out.println("DataProcessing:canReserveMore(String cardID)�����в�ѯ���߱����ʱԤ����ʧ��");
		}
		return false;
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

	// -----Ԥ�����ķ���-----
	public static void reserveThisBook(String bookID, String cardID) {
		// ͼ���update��ͼ�鱻Ԥ��״̬reservedΪ1
		// ��ʱ��insertһ���¼�¼

		// ͼ���update��ͼ�鱻Ԥ��״̬reservedΪ1
		String sql = "UPDATE book SET reserved = 1 WHERE book_id ='" + bookID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:reserveThisBook(String bookID, String cardID)�������޸�ͼ���ʧ��");
		}

		// ��ʱ��insertһ���¼�¼
		String sql1 = "INSERT INTO temp" + cardID + " (bookID) VALUES ('" + bookID + "')";
		try {
			preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:reserveThisBook(String bookID, String cardID)�������޸���ʱ������Ϣ��ʧ��");
		}

	}

	public static void borrowThisBook(String bookID, String cardID) {
		// ������Ϣ��insertһ���¼�¼
		// ͼ���update��ͼ���Ƿ��ڹ�״̬in_or_outΪ1
		// ͼ�����update�����Ĵ���borrowed_num��1
		// ���߱��ѽ�������1

		Date currentDate = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");

		String borrowTime = ft.format(currentDate);

		String ISBN = null;

		// ������Ϣ��insertһ���¼�¼
		String sql = "INSERT INTO borrow_info (card_id,book_id,borrow_date,renew_num) VALUES ('" + cardID + "','"
				+ bookID + "','" + borrowTime + "',0)";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:borrowThisBook(String bookID, String cardID)�������޸Ľ�����Ϣ��ʧ��");
		}

		// ͼ���update��ͼ���Ƿ��ڹ�״̬in_or_outΪ1
		String sql1 = "UPDATE book SET in_or_out=1 WHERE book_id ='" + bookID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:borrowThisBook(String bookID, String cardID)�������޸�ͼ���ʧ��");
		}

		// ͼ�����update�����Ĵ���borrowed_num��1

		// ----�ȸ���ͼ����ҵ�ISBN��
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql2 = "SELECT ISBN FROM book WHERE book_id ='" + bookID + "'";
			resultSet = statement.executeQuery(sql2);

			while (resultSet.next())
				ISBN = resultSet.getString("ISBN");
		} catch (SQLException e) {
			System.out.println("DataProcessing:borrowThisBook(String bookID, String cardID)�����в�ѯͼ���ʧ��");
		}

		// ----�ٸ���ISBN����ͼ�����
		String sql3 = "UPDATE books SET borrowed_num = borrowed_num + 1 WHERE ISBN ='" + ISBN + "'";
		try {
			preparedStatement = connection.prepareStatement(sql3);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:borrowThisBook(String bookID, String cardID)�������޸�ͼ�����ʧ��");
		}

		// ���߱��ѽ�������1
		String sql4 = "UPDATE readers SET already_borrow = already_borrow + 1 WHERE card_id ='" + cardID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql4);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:borrowThisBook(String bookID, String cardID)�������޸Ķ��߱�ʧ��");
		}
	}

	public static int borrowAllBooks(String cardID) {
		int isEmpty = 0;
		String bookID = null;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM temp" + cardID;
			borrowAllResultSet = statement.executeQuery(sql);

			// ִ�н������
			while (borrowAllResultSet.next()) {
				bookID = borrowAllResultSet.getString("bookID");
				borrowThisBook(bookID, cardID);
				isEmpty = 1;
			}

			if (isEmpty == 1) {
				// �����������޸�ͼ���reservedλ�Լ�ɾ����ʱ��Ĳ���
				dropReserveTable(cardID);

				// ��ʾ����ʱ��
				Date currentDate = new Date();

				Calendar period = Calendar.getInstance();
				period.setTime(currentDate);
				period.add(Calendar.DAY_OF_MONTH, 14); // ����ǿ��Կ��·���ݽ��м����

				SimpleDateFormat ft = new SimpleDateFormat("yyyy��MM��dd��");
				String dueDate = ft.format(period.getTime());

				JOptionPane.showMessageDialog(null, "����ɹ�!����" + dueDate + "֮ǰ�黹");
			}

			else {
				JOptionPane.showMessageDialog(null, "δԤ���κ���!");
			}

		} catch (SQLException e) {
			System.out.println("DataProcessing:borrowAllBooks(String cardID)�����в�ѯ��ʱ��temp" + cardID + "ʧ��");
		}
		return isEmpty;
	}

	// returnThisBook��������һ��int���͵�����
	// ����һ��int���������û��ʱ�ͷ���0������������޸�
	// ��ʱ�˾ͷ��س�ʱ�������Ǳ��жϵ���Ϊ0������ʾ��ʱ�����Լ�Ӧ�����Ȼ������޸��ڹ���Ա�Ǳ�ִ��
	public static int returnThisBook(String bookID, String cardID) {
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
			System.out.println("DataProcessing:returnThisBook(String bookID, String cardID)�����в�ѯ������Ϣ��ʧ��");
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
			System.out.println("DataProcessing:returnThisBook(String bookID, String cardID)�����м���Ӧ��ʱ��ʱ��ʽת��ʧ��");
		}

		// ��ȡ��ǰʱ��
		Date currentDate = new Date();

		// �ж�dueDate��currentDate���Ⱥ�˳��
		// ���û����
		if (currentDate.before(dueDate)) {
			// ִ�з�������
			// ������Ϣ��insertһ���¼�¼
			// ͼ���update��ͼ���Ƿ��ڹ�״̬in_or_outΪ0
			// �޸Ķ��߱���ѽ�����
			// ɾ��������Ϣ���¼

			// ������Ϣ��insertһ���¼�¼
			String returnTime = ft.format(currentDate);
			String sql = "INSERT INTO return_info (card_id,book_id,return_date) VALUES ('" + cardID + "','" + bookID
					+ "','" + returnTime + "')";
			try {
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				System.out.println("DataProcessing:returnThisBook(String bookID, String cardID)�������޸ķ�����Ϣ��ʧ��");
			}

			// ͼ���update��ͼ���Ƿ��ڹ�״̬in_or_outΪ0
			String sql1 = "UPDATE book SET in_or_out = 0 WHERE book_id ='" + bookID + "'";
			try {
				preparedStatement = connection.prepareStatement(sql1);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				System.out.println("DataProcessing:returnThisBook(String bookID, String cardID)�������޸�ͼ���ʧ��");
			}

			// ���߱��ѽ�������1
			String sql2 = "UPDATE readers SET already_borrow = already_borrow - 1 WHERE card_id ='" + cardID + "'";
			try {
				preparedStatement = connection.prepareStatement(sql2);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				System.out.println("DataProcessing:returnThisBook(String bookID, String cardID)�������޸Ķ��߱�ʧ��");
			}

			// ɾ��������Ϣ���¼
			String sql3 = "DELETE FROM borrow_info WHERE card_id ='" + cardID + "' AND book_id ='" + bookID + "'";
			try {
				preparedStatement = connection.prepareStatement(sql3);
				preparedStatement.executeUpdate();
			} catch (SQLException e) {
				System.out.println("DataProcessing:returnThisBook(String bookID, String cardID)�������޸Ľ�����Ϣ��ʧ��");
			}

			return 0;
		} else {
			// ���㳬ʱ����������һ��Ͳ�����
			int daysover; // �нӳ�ʱ������
			long current = currentDate.getTime();
			long due = dueDate.getTime();

			daysover = (int) ((current - due) / (1000 * 60 * 60 * 24));
			return daysover;
		}
	}

	// ͼ������
	public static int renewThisBook(String bookID, String cardID) {
		// ���ҵ�����ʱ�䣬��һ��String�н�
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
			System.out.println("DataProcessing:renewThisBook(String bookID, String cardID)�����в�ѯ������Ϣ��ʧ��");
		}

		// ������ʱ��ת��ΪDate����,����Ӧ��ʱ��dueDate
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat ft1 = new SimpleDateFormat("yyyy��MM��dd��");

		Date borrowDate;
		try {
			borrowDate = ft.parse(borrowDay);
			Calendar period = Calendar.getInstance();
			period.setTime(borrowDate);
			period.add(Calendar.DAY_OF_MONTH, 14); // ����ǿ��Կ��·���ݽ��м����
			dueDate = period.getTime();
		} catch (ParseException e) {
			System.out.println("DataProcessing:renewThisBook(String bookID, String cardID)�����м���Ӧ��ʱ��ʱ��ʽת��ʧ��");
		}

		// ��ȡ��ǰʱ��
		Date currentDate = new Date();

		// �ж�dueDate��currentDate���Ⱥ�˳��
		// ���û����
		if (currentDate.before(dueDate)) {
			// �ȿ����ܲ�������,ÿ������ֻ��7��,�����������
			if (renewNum == 2)
				JOptionPane.showMessageDialog(null,
						"ͼ���Ϊ" + bookID + "��ͼ�������������,�������黹����:" + ft1.format(dueDate) + "ǰ�黹");
			else {
				// ִ���������
				// renew_date��1
				// ���½�����Ϣ��Ľ���ʱ��ͺ���
				// ������ʱ����³�currentDate,��ʾcurrentDate֮���14��
				// currentDate�ȼ�14��,��newDueDate�нӽ��֮����ת����ʽ

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
							.println("DataProcessing:renewThisBook(String bookID, String cardID)�������޸Ľ�����Ϣ��(δ���ڵ����)ʧ��");
				}
				JOptionPane.showMessageDialog(null, "ͼ���Ϊ" + bookID + "��ͼ������ɹ�!����" + ft1.format(newDueDate) + "֮ǰ�黹");
			}
			return 0;
		} else {
			// ���㳬ʱ����������һ��Ͳ�����
			int daysover; // �нӳ�ʱ������
			long current = currentDate.getTime();
			long due = dueDate.getTime();

			daysover = (int) ((current - due) / (1000 * 60 * 60 * 24));

			// �����ѹ���,����������������һ��,Ҳ��������
			if (daysover == 0) {
				// ִ���������
				// ���½�����Ϣ��Ľ���ʱ��ͺ���
				// ������ʱ����³�currentDate,��ʾcurrentDate֮���14��
				// currentDate�ȼ�14��,��newDueDate�нӽ��֮����ת����ʽ
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
							"DataProcessing:renewThisBook(String bookID, String cardID)�������޸Ľ�����Ϣ��(��������һ������)ʧ��");
				}

				JOptionPane.showMessageDialog(null, "����ɹ�!����" + ft1.format(newDueDate) + "֮ǰ�黹");

			}

			return daysover;
		}
	}

	// ---ת����---
	// ����֤��ת��������
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

	// ͼ���תͼ�����
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

	// ����֤��ת�ɽ�����
	public static int cardIDToCanBorrow(String cardID) {
		int CanBorrow = 0;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT can_borrow FROM readers WHERE card_id ='" + cardID + "'";
			resultSet = statement.executeQuery(sql);

			while (resultSet.next())
				CanBorrow = Integer.parseInt(resultSet.getString("can_borrow"));
		} catch (SQLException e) {
			System.out.println("DataProcessing:cardIDToCanBorrow(String cardID)�����в�ѯ���߱�ʧ��");
		}
		return CanBorrow;
	}

	// ͼ���תλ����Ϣ
	// ����ͼ��ŵ�ʵ��λ�õĻ���
	// bookid = floorNum*100000+shelfNum*1000+shelfFloorNum*100+specificOrder;
	// 3¥12�����4���78��:312478
	public static String bookIDToPosInfo(String bookID) {
		int originalPos = Integer.parseInt(bookID);
		int floorNum = originalPos / 100000;
		int shelfNum = (originalPos % 100000) / 1000;
		int shelfFloorNum = (originalPos % 1000) / 100;
		int specificOrder = originalPos % 100;

		String PosInfo = Integer.toString(floorNum) + "¥" + Integer.toString(shelfNum) + "�����"
				+ Integer.toString(shelfFloorNum) + "�����" + Integer.toString(specificOrder);

		return PosInfo;
	}

	// ---Ԥ�������---
	// ������ʱԤ����
	// ����ʹ��temp+����֤��(����ʹ�����ֿ�ͷ)
	public static void createReserveTable(String cardID) {
		String sql = "CREATE TABLE temp" + cardID + " ( bookID char(6) )";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:createReserveTable(String cardID)�����д�����ʱԤ����ʧ��");
		}
	}

	// ������ʱ����ָ����ͼ���,��ͼ����reservedλ�ָ�Ϊ0
	public static void reservedToZero(String bookID) {
		String sql = "UPDATE book SET reserved = 0 WHERE book_id ='" + bookID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:createReserveTable(String cardID)�������޸�ͼ���ʧ��");
		}
	}

	// ɾ����ʱԤ����(��"ȡ��"��ťʹ��)
	public static void dropReserveTable(String cardID) {
		// �ȸ�����ʱ���е�ͼ�������ͼ����reservedλ�ָ�Ϊ0,�Ա����ɱ�������
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM temp" + cardID;
			resultSet = statement.executeQuery(sql);

			while (resultSet.next())
				reservedToZero(resultSet.getString("bookID"));

		} catch (SQLException e1) {
			System.out.println("DataProcessing:dropReserveTable(String cardID)�����в�ѯ��ʱԤ������޸�ͼ���ʧ��");
		}

		// Ȼ���ٽ�����ʱ��ɾ������
		String sql1 = "DROP TABLE temp" + cardID;
		try {
			preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.executeUpdate();
		} catch (SQLException e2) {
			System.out.println("DataProcessing:dropReserveTable(String cardID)������ɾ����ʱԤ����ʧ��");
		}
	}

	// �����ʱԤ����(��"ɾ��ȫ��"��ťʹ��)
	public static void cancelAllReserved(String cardID) {
		String sql = "SELECT * FROM temp" + cardID;
		try {
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			resultSet = statement.executeQuery(sql);

			while (resultSet.next()) {
				reservedToZero(resultSet.getString("bookID"));
			}
		} catch (SQLException e1) {
			System.out.println("DataProcessing:cancelAllReserved(String cardID)�����в�ѯ��ʱԤ����ʧ�ܻ��޸�ͼ���ʧ��");
		}

		String sql1 = "DELETE FROM temp" + cardID + " WHERE bookID IS NOT NULL";
		try {
			preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:cancelAllReserved(String cardID)������ɾ����ʱ��������Ϣʧ��");
		}
	}

	// ɾ��ѡ�е�һ��ͼ��
	// ���ڰ�ť���ж��Ƿ�ѡ����(ѡ���˶���ûѡ�ո񡪡�����������ݻ���Ԥ����������������Ӧ�þ�û�пո���)
	// Ȼ���ٽ���ͼ�����޸��Լ���ʱ���ɾ��
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
			System.out.println("DataProcessing:cancelOneReserved(String cardID, int pos)�����в�ѯ��ʱ��ʧ�ܻ��޸�ͼ���ʧ��");
		}

		deleteOneRecordInTemp(cardID, bookID);
	}

	// ɾ���������ϳ�һ������
	public static void deleteOneRecordInTemp(String cardID, String bookID) {
		String sql = "DELETE FROM temp" + cardID + " WHERE bookID = '" + bookID + "'";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			System.out.println("DataProcessing:deleteOneLine(String cardID, String bookID)�������޸���ʱ��ʧ��");
		}
	}
}
