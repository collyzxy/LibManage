package LibInfo;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AddBook extends JFrame 
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
	
	JPanel jp1,jp2,jp3,jp4,jp5,jp6;
	JPanel logPanel=new JPanel();
	JLabel jl1,jl2,jl3,jl4,jl5;
	JButton jb1,jb2;
	JTextField jtf1,jtf2,jtf3,jtf4,jtf5;
	
	AddBook(String ID){
		super("add book");
		setLayout(new GridLayout(6,1,5,5));
		
		jp1=new JPanel();
		jl1=new JLabel("书号：");
		jtf1=new JTextField(20);
		jp1.add(jl1);
		jp1.add(jtf1);
		
		jp2=new JPanel();
		jl2=new JLabel("书名：");
		jtf2=new JTextField(20);
		jp2.add(jl2);jp2.add(jtf2);
		
		jp3=new JPanel();
		jl3=new JLabel("作者：");
		jtf3=new JTextField(20);
		jp3.add(jl3);jp3.add(jtf3);
		
		jp4=new JPanel();
		jl4=new JLabel("出版社：");
		jtf4=new JTextField(20);
		jp4.add(jl4);jp4.add(jtf4);
		
		jp5=new JPanel();
		jl5=new JLabel("数量：");
		jtf5=new JTextField(20);
		jp5.add(jl5);jp5.add(jtf5);
		
		jp6=new JPanel();
		jb1=new JButton("确定");
		jb2=new JButton("取消");
		jp6.add(jb1);jp6.add(jb2);
		
		this.add(jp1);
		this.add(jp2);
		this.add(jp3);
		this.add(jp4);
		this.add(jp5);
		this.add(jp6);
		
		this.setSize(633,1024);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
							JOptionPane.showMessageDialog(null, "此书已存在");
						else
						{
							ps.executeUpdate("insert into BookInfo values('"+jtf1.getText()+"','"+Integer.valueOf(jtf5.getText())+"','"+Integer.valueOf(jtf5.getText())+"','"+jtf3.getText()+"','"+jtf4.getText()+"','"+jtf2.getText()+"')");
							JOptionPane.showMessageDialog(null, "入库成功");
							new Manager_win(ID);
							dispose();
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
				new Manager_win(ID);
				dispose();
			}
			
		});
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
