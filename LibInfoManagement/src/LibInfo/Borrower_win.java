package LibInfo;
import java.sql.*;
import java.text.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class Borrower_win extends JFrame 
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
	
	Font f=new Font("楷体",Font.PLAIN,16);
	{
		UIManager.put("Button.font", f);
		UIManager.put("Label.font", f);
		UIManager.put("TextField.font", f);
		UIManager.put("PasswordField.font", f);
		UIManager.put("ComboBox.font", f);
	}
	
	CardLayout card=new CardLayout();
	Borrower_win(String ID)
	{
		super("Borrower");
		setLayout(new BorderLayout());
		
		JButton jb1=new JButton("用户信息");
		JButton jb2=new JButton("查看借阅历史");
		JPanel bjp=new JPanel();
		bjp.add(jb1);bjp.add(jb2);
		add(bjp, BorderLayout.NORTH);
		
		JPanel jp=new JPanel(card);
		
		JLabel jl=new JLabel("ID:"+ID);
		add(jl,BorderLayout.SOUTH);
		
		JPanel jp1=InitInfoTable(ID);
		jp.add("1",jp1);
		
		JPanel jp2=InitHisTable(ID);
		jp.add("2",jp2);
		
		jb1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				card.show(jp, "1");
			}
		});
		jb2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				card.show(jp, "2");
			}
		});
		
		JButton jb=new JButton("借书");
		jb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					new Borrow(ID);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		add(jp,BorderLayout.CENTER);
		add(jb,BorderLayout.SOUTH);
		
		this.setSize(1024,633);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	public JPanel InitInfoTable(String ID){
		JPanel jp=new JPanel();
		JTable table=new JTable(new InfoTable(ID));
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount()==2)
				{
					int row=((JTable)e.getSource()).rowAtPoint(e.getPoint());
					new BorrowerBook(ID,getInfo(ID),row);
					dispose();
				}
			}
		});
		JScrollPane scroll=new JScrollPane(table);
		jp.add(scroll);
		
		return jp;
	}
	private class InfoTable extends AbstractTableModel{

		private static final long serialVersionUID = 1L;
		String[] columnNames={"书号","书名","借阅时间","归还期限"};
		String[][] obj;
		public InfoTable(String ID){
			obj=getInfo(ID);
		}
		
		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return obj.length;
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return columnNames.length;
		}

		@Override
	    public String getColumnName(int column) {
	        return columnNames[column];
	    }

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return obj[rowIndex][columnIndex];
		}
		
	}
	public String[][] getInfo(String ID){
		String[][] obj = null;
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
            Connection con=DriverManager.getConnection(URL, USERNAME,PASSWORD);
            ps=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs=ps.executeQuery("select * from BorrowRecord");
            rs.last();
            int rowcnt=rs.getRow();
            obj=new String[rowcnt][4];
            rs=ps.executeQuery("select * from BorrowRecord where Borrower_ID='"+ID+"' and Status=0");
            int i=0;
            while(rs.next())
            {
            	String isbn=rs.getString("ISBN");
            	obj[i][0]=isbn;
            	Statement st=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            	ResultSet rs1=st.executeQuery("select * from BookInfo where ISBN='"+isbn+"'");
            	if(rs1.next())
            	{
            		String bkname=rs1.getString("name");
            		DateFormat df=new SimpleDateFormat();
            		String borrowdate=df.format(rs.getTimestamp("Borrow_Date").getTime());
            		String deadline=df.format(rs.getTimestamp("Deadline").getTime());
            		obj[i][1]=bkname;
            		obj[i][2]=borrowdate;
                	obj[i][3]=deadline;
            	}
            	i++;
            }
		}catch (ClassNotFoundException|SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return obj;
	}
	
	public JPanel InitHisTable(String ID){
		JPanel jp=new JPanel();
		JTable table=new JTable(new HisTable(ID));
		/*table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount()==2)
				{
					int row=((JTable)e.getSource()).rowAtPoint(e.getPoint());
					new BorrowerBook(ID,getInfo(ID),row);
					dispose();
				}
			}
		});*/
		JScrollPane scroll=new JScrollPane(table);
		jp.add(scroll);
		
		return jp;
	}
	private class HisTable extends AbstractTableModel{

		private static final long serialVersionUID = 1L;
		String[] columnNames={"书号","书名"};
		String[][] obj;
		public HisTable(String ID){
			obj=HisInfo(ID);
		}
		
		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return obj.length;
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return columnNames.length;
		}

		@Override
	    public String getColumnName(int column) {
	        return columnNames[column];
	    }

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return obj[rowIndex][columnIndex];
		}
		
	}
	public String[][] HisInfo(String ID){
		String[][] obj = null;
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
            Connection con=DriverManager.getConnection(URL, USERNAME,PASSWORD);
            ps=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs=ps.executeQuery("select * from BorrowRecord");
            rs.last();
            int rowcnt=rs.getRow();
            obj=new String[rowcnt][2];
            rs=ps.executeQuery("select * from BorrowRecord where Borrower_ID='"+ID+"'");
            int i=0;
            while(rs.next())
            {
            	String isbn=rs.getString("ISBN");
            	obj[i][0]=isbn;
            	Statement st=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            	ResultSet rs1=st.executeQuery("select * from BookInfo where ISBN='"+isbn+"'");
            	if(rs1.next())
            	{
            		String bkname=rs1.getString("name");
            		obj[i][1]=bkname;
            	}
            	i++;
            }
		}catch (ClassNotFoundException|SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return obj;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}

