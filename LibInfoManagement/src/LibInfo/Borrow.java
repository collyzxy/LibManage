package LibInfo;
import java.sql.*;
import java.sql.Date;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Borrow extends JFrame 
implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static String URL="jdbc:sqlserver://localhost:1433;DatabaseName=BookManage";  
    public static String USERNAME="olly";  
    public static String PASSWORD="199811"; 
    Statement ps;
	ResultSet rs;
    
	Font f=new Font("楷体",Font.PLAIN,26);
	{
		UIManager.put("Button.font", f);
		UIManager.put("Label.font", f);
		UIManager.put("TextField.font", f);
		UIManager.put("PasswordField.font", f);
		UIManager.put("ComboBox.font", f);
	}
	
	JPanel jp1,jp2;
	JPanel logPanel=new JPanel();
	JLabel jl1;
	JButton jb1,jb2,jb3;
	JTextField jtf1;
	
	Borrow(String ID) throws Exception{
		super("borrow");
		setLayout(new GridLayout(2,1,5,5));
		
		Date date = new Date(System.currentTimeMillis());
//		String year = String.format("%tY", date);   
//		String month = String.format("%tb", date);   
//		String day = String.format("%te", date);
		//@SuppressWarnings("deprecation")
		
		/*
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		String dl=String.format("%s-%d-%s", year,Integer.valueOf(month)+1,day);
		Date deadline=dateformat.parse(dl);*/
		
		jp1=new JPanel();
		jl1=new JLabel("书号：");
		jtf1=new JTextField(20);
		jp1.add(jl1);
		jp1.add(jtf1);
		
		jp2=new JPanel();
		jb1=new JButton("确定");
		jb2=new JButton("取消");
		jb3=new JButton("查询");
		jp2.add(jb3);jp2.add(jb1);jp2.add(jb2);
		
		this.add(jp1);
		this.add(jp2);
		
		this.setSize(300,200);
		setLocationRelativeTo(null);
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		jb1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				if(!jtf1.getText().equals(""))
				{
					try{
						Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
			            Connection con=DriverManager.getConnection(URL, USERNAME,PASSWORD);
			            ps=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			            rs=ps.executeQuery("select * from BookInfo where ISBN='"+jtf1.getText().trim()+"'");
						if(rs.next())
						{
							if(rs.getInt("stock")==0)
								JOptionPane.showMessageDialog(null, "此书无库存");
							else
							{
								
								ps.executeUpdate("insert into BorrowRecord values('"+ID+"','"+jtf1.getText()+"','"+date+"','"+date+"','"+0+"')");
								 PreparedStatement pres=con.prepareStatement("update BorrowRecord set Deadline=DATEADD(mm,1,Deadline) where Borrower_ID='"+ID+"' and ISBN='"+jtf1.getText()+"'");
						         pres.execute();
								JOptionPane.showMessageDialog(null, "借阅成功");
								new Borrower_win(ID);
								dispose();
							}
						}
						else
						{
							JOptionPane.showMessageDialog(null, "本馆无此书");
						}
					}catch (ClassNotFoundException|SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
				else 
					JOptionPane.showMessageDialog(null, "书号不能为空");
			}
		});
		jb2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new Borrower_win(ID);
				dispose();
			}
			
		});
		jb3.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(!jtf1.getText().equals(""))
					new Book_win(jtf1.getText());
				else 
					JOptionPane.showMessageDialog(null, "书号不能为空");
			}
			
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
