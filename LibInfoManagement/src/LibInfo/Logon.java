package LibInfo;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Logon 	extends JFrame 
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

	JPanel jp1,jp2,jp3,jp4;
	JPanel logPanel=new JPanel();
	JLabel jl1,jl2,jl3;
	JButton jb2,jb3;
	JComboBox<String> jcb=new JComboBox<>();
	{
		jcb.addItem("借阅");
		jcb.addItem("管理员");
	}
	JTextField jtf1,jtf2;
	
	public Logon(){
		super("log on");
		setLayout(new GridLayout(4,1,5,5));
		
		jp1=new JPanel();
		jl1=new JLabel("用户名：");
		jtf1=new JTextField(20);
		jp1.add(jl1);
		jp1.add(jtf1);
		
		jp2=new JPanel();
		jl2=new JLabel("密  码：");
		jtf2=new JPasswordField(20);
		jp2.add(jl2);jp2.add(jtf2);
		
		jp3=new JPanel();
		jl3=new JLabel("身份类型：");
		jp3.add(jl3);jp3.add(jcb);
		
		jp4=new JPanel();
		jb2=new JButton("注册");
		jb3=new JButton("取消");
		jp4.add(jb2);jp4.add(jb3);
		
		this.add(jp1);
		this.add(jp2);
		this.add(jp3);
		this.add(jp4);
		
		this.setSize(500,309);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		jb2.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				if((!jtf1.getText().equals(""))&&(!jtf2.getText().equals("")))
				{
					try{
						Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
			            Connection con=DriverManager.getConnection(URL, USERNAME,PASSWORD);
			            ps=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					}catch (ClassNotFoundException|SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					switch(jcb.getSelectedItem().toString())
					{
					case "管理员":
						try {
							rs=ps.executeQuery("select * from Manager where Manager_ID='"+jtf1.getText().trim()+"'");
							if(rs.next())
								JOptionPane.showMessageDialog(null, "此账户已存在");
							else
							{
								ps.executeUpdate("insert into Manager values('"+jtf1.getText()+"','"+jtf2.getText()+"')");
								JOptionPane.showMessageDialog(null, "注册成功");
							}
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							String error=e1.getMessage();
							JOptionPane.showMessageDialog(null, error);
							e1.printStackTrace();
						}
						break;
					case "借阅":
						try{
							rs=ps.executeQuery("select * from Borrower where Borrower_ID='"+jtf1.getText()+"'");
							if(rs.next())
								JOptionPane.showMessageDialog(null, "该用户已存在");
							else
							{
								ps.executeUpdate("insert into Borrower values('"+jtf1.getText()+"','"+jtf2.getText()+"')");
								JOptionPane.showMessageDialog(null, "注册成功");
								new Login();
								dispose();
							}
						}catch(SQLException e1){
							String error=e1.getMessage();
							JOptionPane.showMessageDialog(null, error);
							e1.printStackTrace();
						}
						break;
					}
				}
				else if(jtf1.getText().equals(""))
					JOptionPane.showMessageDialog(null, "用户名不能为空");
				else
					JOptionPane.showMessageDialog(null, "密码不能为空");
			}
		});
		jb3.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.exit(0);
			}
			
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
