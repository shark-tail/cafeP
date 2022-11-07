package cafe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DB {
	Connection CN = null;
	PreparedStatement PS = null;
	ResultSet RS = null;
	
	
	//데이터베이스 연결
	public void connectDB() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String url = "jdbc:mysql://localhost:3306/cafeorder?characterEndofing=UTF-8&serverTimezone=UTC";
			CN = DriverManager.getConnection(url, "test", "1234");
			System.out.println("*** 데이터베이스 연결 성공 ***");  //나중에 제거
			
		} catch(ClassNotFoundException e) {
			System.out.println("JDBC Driver 로드 실패");
		} catch(SQLException e) {
			System.out.println("데이터베이스 연결 실패");
		} catch (Exception e) {
			System.out.println("에러 발생");
		}
	}
	
	
}
