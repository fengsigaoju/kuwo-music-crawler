import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DB {//�������ݿ�������,ʹ�õ���ģʽ
 private String driver="com.mysql.jdbc.Driver";
 private String url="jdbc:mysql://127.0.0.1:3306/song";
 private String user="root";
 private String password="960404";
 private  Connection connection=null;
 private static DB single=new DB();
 private ResultSet rs=null;
 private  DB(){
	 try {
		Class.forName(driver);
	} catch (ClassNotFoundException e) {
		System.out.println("��������ʧ��");
		e.printStackTrace();
	}
	 try {
		connection=DriverManager.getConnection(url,user,password);
	} catch (SQLException e) {
		System.out.println("��������ʧ��");
		e.printStackTrace();
	}
 }
 public static DB GetSingle(){
	 return single;
 }
 public  void Update(String query){
	 try {
		Statement statement = connection.createStatement();
		System.out.println(query);
		statement.execute(query);
	} catch (SQLException e) {
		System.out.println("�������ݿ�������");
		e.printStackTrace();
	}  
 }
 public ResultSet Query(String query){
	try {
		Statement statement=connection.createStatement();
		System.out.println(query);
		rs=statement.executeQuery(query);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return rs;
}
 
}
