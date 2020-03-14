package LibInfo;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class BorrowerBook extends JFrame 
implements ActionListener{

	private static final long serialVersionUID = 1L;
	public static String URL="jdbc:sqlserver://localhost:1433;DatabaseName=BookManage";  
    public static String USERNAME="olly";  
    public static String PASSWORD="199811"; 
    Statement ps;
	ResultSet rs;
	
	Font f=new Font("楷体",Font.PLAIN,16);
	{
		UIManager.put("Button.font", f);
		UIManager.put("Label.font", f);
		UIManager.put("TextField.font", f);
		UIManager.put("PasswordField.font", f);
		UIManager.put("ComboBox.font", f);
	}
	BorrowerBook(String ID,String[][] obj,int row){
		
		setLayout(new BorderLayout());
		
		JPanel jp1=new JPanel(new GridLayout(7,1));
		String[] bkinfo=getBookInfo(obj[row][0]);
		JLabel jl1=new JLabel("书号："+bkinfo[0]);
		JLabel jl2=new JLabel("书名："+bkinfo[1]);
		JLabel jl3=new JLabel("作者："+bkinfo[2]);
		JLabel jl4=new JLabel("出版社："+bkinfo[3]);
		JLabel jl5=new JLabel("借阅时间："+obj[row][2]);
		JLabel jl6=new JLabel("归还期限："+obj[row][3]);
		jp1.add(jl1);jp1.add(jl2);jp1.add(jl3);jp1.add(jl4);jp1.add(jl5);jp1.add(jl6);
		
		JButton jb1=new JButton("续借");
		jb1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try{
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
		            Connection con=DriverManager.getConnection(URL, USERNAME,PASSWORD);
		            PreparedStatement pres=con.prepareStatement("update BorrowRecord set Deadline=DATEADD(mm,1,Deadline) where Borrower_ID='"+ID+"' and ISBN='"+bkinfo[0]+"'");
		            pres.execute();
		            new Borrower_win(ID);
		            dispose();
				}catch (ClassNotFoundException|SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		JButton jb2=new JButton("还书");
		jb2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try{
					Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
		            Connection con=DriverManager.getConnection(URL, USERNAME,PASSWORD);
		            PreparedStatement pres=con.prepareStatement("update BorrowRecord set Status=Status+1 where Borrower_ID='"+ID+"' and ISBN='"+bkinfo[0]+"'");
		            pres.execute();
		            new Borrower_win(ID);
		            dispose();
				}catch (ClassNotFoundException|SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		JPanel jp2=new JPanel();
		jp2.add(jb1);jp2.add(jb2);
		
		add(jp1,BorderLayout.CENTER);add(jp2,BorderLayout.SOUTH);
		
		this.setSize(300,500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public String[] getBookInfo(String ISBN){
		String[] bkinfo = new String[4];
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
            Connection con=DriverManager.getConnection(URL, USERNAME,PASSWORD);
            ps=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs=ps.executeQuery("select * from BookInfo where ISBN='"+ISBN+"'");
            while(rs.next())
            {
            	String isbn=rs.getString("ISBN");
            	bkinfo[0]=isbn;
            	String name=rs.getString("name");
            	bkinfo[1]=name;
            	String author=rs.getString("author");
            	bkinfo[2]=author;
            	String publisher=rs.getString("publisher");
            	bkinfo[3]=publisher;
            }
		}catch (ClassNotFoundException|SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return bkinfo;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
