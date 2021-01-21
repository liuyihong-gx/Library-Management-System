package frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.FontUIResource;

import bookandreadermanagement.DataProcessing;

import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class SpecificNewBook extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	private JTextField floorNumText;
	private JTextField shelfNumText;
	private JTextField shelfFloorNumText;
	private JTextField specificOrderText;

	private int n = 1;
	private int num = 0;

	private int FloorNum;
	private int ShelfNum;
	private int ShelfFloorNum;
	private int SpecificOrder;

	private Toolkit toolkit = Toolkit.getDefaultToolkit();

	public SpecificNewBook(String ISBN, int bookNum) {

		num = bookNum;

		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 800, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		UIManager.put("OptionPane.buttonFont", new FontUIResource(new Font("微软雅黑", Font.BOLD, 30)));
		UIManager.put("OptionPane.messageFont", new FontUIResource(new Font("微软雅黑 Light", Font.PLAIN, 35)));

		JLabel bookNumCounter = new JLabel("");
		bookNumCounter.setBackground(new Color(220, 220, 220));
		bookNumCounter.setHorizontalAlignment(SwingConstants.CENTER);
		bookNumCounter.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		bookNumCounter.setBounds(0, 5, 800, 60);
		contentPane.add(bookNumCounter);
		bookNumCounter.setOpaque(true);
		bookNumCounter.setText("正在录入《" + DataProcessing.ISBNToName(ISBN) + "》第1本书的具体信息");

		JLabel floorNum = new JLabel("\u8BF7\u8F93\u5165\u6240\u5728\u697C\u5C42:");
		floorNum.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		floorNum.setBounds(90, 100, 330, 50);
		contentPane.add(floorNum);

		floorNumText = new JTextField();
		floorNumText.setHorizontalAlignment(SwingConstants.CENTER);
		floorNumText.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		floorNumText.setBounds(420, 100, 80, 50);
		contentPane.add(floorNumText);
		floorNumText.setColumns(10);

		JLabel shelfNum = new JLabel("\u8BF7\u8F93\u5165\u4E66\u67B6\u53F7:");
		shelfNum.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		shelfNum.setBounds(90, 180, 330, 50);
		contentPane.add(shelfNum);

		shelfNumText = new JTextField();
		shelfNumText.setHorizontalAlignment(SwingConstants.CENTER);
		shelfNumText.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		shelfNumText.setBounds(420, 180, 80, 50);
		contentPane.add(shelfNumText);
		shelfNumText.setColumns(10);

		JLabel shelfFloorNum = new JLabel("\u8BF7\u8F93\u5165\u6240\u5728\u4E66\u67B6\u5C42:");
		shelfFloorNum.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		shelfFloorNum.setBounds(90, 260, 330, 50);
		contentPane.add(shelfFloorNum);

		shelfFloorNumText = new JTextField();
		shelfFloorNumText.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		shelfFloorNumText.setHorizontalAlignment(SwingConstants.CENTER);
		shelfFloorNumText.setBounds(420, 260, 80, 50);
		contentPane.add(shelfFloorNumText);
		shelfFloorNumText.setColumns(10);

		JLabel specificOrder = new JLabel("\u8BF7\u8F93\u5165\u6446\u653E\u5E8F\u53F7:");
		specificOrder.setFont(new Font("微软雅黑 Light", Font.PLAIN, 40));
		specificOrder.setBounds(90, 340, 330, 50);
		contentPane.add(specificOrder);

		specificOrderText = new JTextField();
		specificOrderText.setHorizontalAlignment(SwingConstants.CENTER);
		specificOrderText.setFont(new Font("微软雅黑", Font.PLAIN, 40));
		specificOrderText.setBounds(420, 340, 80, 50);
		contentPane.add(specificOrderText);
		specificOrderText.setColumns(10);

		JButton confirm = new JButton("\u786E\u5B9A");

		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (floorNumText.getText().isBlank() || shelfNumText.getText().isBlank()
						|| shelfFloorNumText.getText().isBlank() || specificOrderText.getText().isBlank())
					JOptionPane.showMessageDialog(null, "请确保每一项位置信息填写完全!");
				else {
					FloorNum = Integer.parseInt(floorNumText.getText());
					ShelfNum = Integer.parseInt(shelfNumText.getText());
					ShelfFloorNum = Integer.parseInt(shelfFloorNumText.getText());
					SpecificOrder = Integer.parseInt(specificOrderText.getText());
					String bookid = DataProcessing.bookID(FloorNum, ShelfNum, ShelfFloorNum, SpecificOrder);

					// 先搜索一下这个位置是不是有书了,若已有书,弹出提示框,直接返回
					if (DataProcessing.searchBookID(bookid)) {
						JOptionPane.showMessageDialog(null, "该位置已有图书!");
						floorNumText.setText("");
						shelfNumText.setText("");
						shelfFloorNumText.setText("");
						specificOrderText.setText("");
					}

					else {
						n++; // 先将n变为2
						if (n <= num) {
							DataProcessing.specificNewBookIn(ISBN, bookid);
							// 清空页面中输入的内容 进行页面的更新(n值)
							specificOrderText.setText("");
							bookNumCounter.setText("正在录入《" + DataProcessing.ISBNToName(ISBN) + "》第" + n + "本书的具体信息");
						} else if (n == num + 1) {
							// 最后一本书的操作
							// 只有一个if的话，最后一本书录不进来，而最后一个操作也不需要再修改
							FloorNum = Integer.parseInt(floorNumText.getText());
							ShelfNum = Integer.parseInt(shelfNumText.getText());
							ShelfFloorNum = Integer.parseInt(shelfFloorNumText.getText());
							SpecificOrder = Integer.parseInt(specificOrderText.getText());

							// 执行相关的数据库操作
							DataProcessing.specificNewBookIn(ISBN, bookid);
							dispose();
							JOptionPane.showMessageDialog(null, "图书信息已经全部录入!");
						}
					}
				}
			}
		});
		confirm.setFont(new Font("微软雅黑", Font.PLAIN, 50));
		confirm.setBounds(300, 420, 200, 60);
		contentPane.add(confirm);

		JLabel inputBckground = new JLabel("");
		inputBckground.setBackground(new Color(220, 220, 220));
		inputBckground.setBounds(88, 98, 414, 294);
		contentPane.add(inputBckground);
		inputBckground.setOpaque(true);

		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("./img/pagebackground800500.png"));
		} catch (IOException e) {
			System.out.println("SpecificNewBook:背景图片载入失败");
		}

		JLabel bckground = new JLabel(new ImageIcon(img));
		bckground.setText("");
		bckground.setBounds(0, -30, 810, 550);
		contentPane.add(bckground);

		Image icon = toolkit.getImage("./img/open-book.png");
		setIconImage(icon);
	}
}
