package com.xnx3.ui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

/**
 * {@link JTextArea}显示窗口。常用于日志显示
 * @author 管雷鸣
 */
public class Log extends JFrame {

	private JPanel contentPane;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Log frame = new Log();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Log() {
		setBounds(100, 100, 319, 290);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 309, Short.MAX_VALUE)
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
		);
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		textArea.setLineWrap(true);
		contentPane.setLayout(gl_contentPane);
	}
	
	/**
	 * 获取主体的JTextArea对其进行操作。
	 * @return {@link JTextArea}
	 */
	public JTextArea getTextArea() {
		return textArea;
	}
	
	/**
	 * 在窗口中的JTextArea的最后加入一行
	 * @param 要加入的那行文本
	 */
	public void appendLineForLast(String text){
		textArea.append("\n"+text);
	}
	
	/**
	 * 在窗口中的JTextArea的最后加入一行.并且使其滚动到最后
	 * @param 要加入的那行文本
	 */
	public void appendLineForLastAndPositionLast(String text){
		appendLineForLast(text);
		textArea.setCaretPosition(textArea.getText().length());
	}
}
