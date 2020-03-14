package LibInfo;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Book_win
implements ActionListener{

	public static String URL="jdbc:sqlserver://localhost:1433;DatabaseName=BookManage";  
    public static String USERNAME="olly";  
    public static String PASSWORD="199811"; 
    Statement ps;
	ResultSet rs;
    
	Font f=new Font("����",Font.PLAIN,26);
	{
		UIManager.put("Button.font", f);
		UIManager.put("Label.font", f);
		UIManager.put("TextField.font", f);
		UIManager.put("PasswordField.font", f);
		UIManager.put("ComboBox.font", f);
	}
	
	Book_win(String isbn){
		
			try{
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
	            Connection con=DriverManager.getConnection(URL, USERNAME,PASSWORD);
	            ps=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	            rs=ps.executeQuery("select * from BookInfo where ISBN='"+isbn.trim()+"'");
				if(rs.next())
				{
					rs=ps.executeQuery("exec bkinfo '"+isbn+"'");
					/*CallableStatement cst=con.prepareCall("call bkinfo(?,?,?,?,?,?)");
					System.out.println(isbn);
					cst.setString(1, isbn);
					cst.registerOutParameter(2, Types.VARCHAR);
					cst.registerOutParameter(3, Types.VARCHAR);
					cst.registerOutParameter(4, Types.VARCHAR);
					cst.registerOutParameter(5, Types.INTEGER);
					cst.registerOutParameter(6, Types.INTEGER);
					cst.execute();*/
					if(rs.next())
					{
						String bname=rs.getString("name");
						String writer=rs.getString("author");					
						String pub=rs.getString("publisher");
						int cop=rs.getInt("copies");
						int sto=rs.getInt("stock");
						JPanel jp1=new JPanel(new GridLayout(7,1));
						JLabel jl1=new JLabel("��ţ�"+isbn);
						JLabel jl2=new JLabel("������"+bname);
						JLabel jl3=new JLabel("���ߣ�"+writer);
						JLabel jl4=new JLabel("�����磺"+pub);
						JLabel jl5=new JLabel("�ղ�����"+String.valueOf(cop));
						JLabel jl6=new JLabel("����ʣ������"+String.valueOf(sto));
						JButton jb=new JButton("ȷ��");
						jp1.add(jl1);jp1.add(jl2);jp1.add(jl3);jp1.add(jl4);jp1.add(jl5);jp1.add(jl6);jp1.add(jb);
						JFrame jf=new JFrame();
						jf.add(jp1);
						jb.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								// TODO Auto-generated method stub
								jf.dispose();
							}
						});
						jf.setSize(300,500);
						jf.setLocationRelativeTo(null);
						jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
						jf.setVisible(true);
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "�����޴���");
				}
			}catch (ClassNotFoundException|SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
