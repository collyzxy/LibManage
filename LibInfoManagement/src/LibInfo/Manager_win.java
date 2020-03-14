package LibInfo;
import java.sql.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class Manager_win extends JFrame 
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
	
	CardLayout card=new CardLayout();
	Manager_win(String ID){
		
		super("Borrower");
		setLayout(new BorderLayout());
		
		JButton jb1=new JButton("管理读者");
		JButton jb2=new JButton("管理图书");
		JPanel bjp=new JPanel();
		bjp.add(jb1);bjp.add(jb2);
		add(bjp, BorderLayout.NORTH);
		
		JPanel jp=new JPanel(card);
		
		JLabel jl=new JLabel("ID:"+ID);
		add(jl,BorderLayout.SOUTH);
		
		JPanel jp1=InitReaderTable();
		jp.add("1",jp1);
		
		JPanel jp2=new JPanel(new BorderLayout());
		JPanel jpt=InitBookTable();
		jp2.add(jpt,BorderLayout.CENTER);
		JButton jb=new JButton("新书入库");
		jb.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new AddBook(ID);
				dispose();
			}
		});
		jp2.add(jb,BorderLayout.SOUTH);
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
		
		add(jp,BorderLayout.CENTER);
		
		this.setSize(1024,633);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public JPanel InitReaderTable(){
		JPanel jp=new JPanel();
		JTable table=new JTable(new ReaderTable());
		/*table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount()==2)
				{
					int row=((JTable)e.getSource()).rowAtPoint(e.getPoint());
					new ManageBorrower(ID,getInfo(ID),row);
					dispose();
				}
			}
		});*/
		JScrollPane scroll=new JScrollPane(table);
		jp.add(scroll);
		
		return jp;
	}
	private class ReaderTable extends AbstractTableModel{

		private static final long serialVersionUID = 1L;
		String[] columnNames={"读者ID","借阅量"};
		String[][] obj;
		public ReaderTable(){
			obj=getReaderInfo();
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
	public String[][] getReaderInfo(){
		String[][] obj = null;
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
            Connection con=DriverManager.getConnection(URL, USERNAME,PASSWORD);
            ps=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs=ps.executeQuery("select * from Borrower");
            rs.last();
            int rowcnt=rs.getRow();
            obj=new String[rowcnt][2];
            int i=0;
            rs=ps.executeQuery("select * from Borrower");
            while(rs.next())
            {
            	String id=rs.getString("Borrower_ID");
            	obj[i][0]=id;
            	Statement st=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            	ResultSet rs1=st.executeQuery("select count(*)num from BorrowRecord where Borrower_ID='"+id+"' and Status=0");
            	int bnum=0;
            	if(rs1.next())
            		bnum=rs1.getInt("num");
            	obj[i][1]=String.valueOf(bnum);
            	i++;
            }
		}catch (ClassNotFoundException|SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return obj;
	}
	
	public JPanel InitBookTable(){
		JPanel jp=new JPanel();
		JTable table=new JTable(new BookTable());
		/*table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount()==2)
				{
					int row=((JTable)e.getSource()).rowAtPoint(e.getPoint());
					new ManageBook(ID,getInfo(ID),row);
					dispose();
				}
			}
		});*/
		JScrollPane scroll=new JScrollPane(table);
		jp.add(scroll);
		
		return jp;
	}
	private class BookTable extends AbstractTableModel{

		private static final long serialVersionUID = 1L;
		String[] columnNames={"书号","书名","作者","出版社","收藏量","库存"};
		String[][] obj;
		public BookTable(){
			obj=getBookInfo();
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
	public String[][] getBookInfo(){
		String[][] obj = null;
		try{
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");  
            Connection con=DriverManager.getConnection(URL, USERNAME,PASSWORD);
            ps=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs=ps.executeQuery("select * from BookInfo");
            rs.last();
            int rowcnt=rs.getRow();
            obj=new String[rowcnt][6];
            int i=0;
            rs=ps.executeQuery("select * from BookInfo");
            while(rs.next())
            {
            	String isbn=rs.getString("ISBN");
            	String name=rs.getString("name");
            	String author=rs.getString("author");
            	String publisher=rs.getString("publisher");
            	int copies=rs.getInt("copies");
            	int stock=rs.getInt("stock");
            	obj[i][0]=isbn;obj[i][1]=name;
            	obj[i][2]=author;obj[i][3]=publisher;
            	obj[i][4]=String.valueOf(copies);obj[i][5]=String.valueOf(stock);
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
