package cafe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Account extends Customer {
	DB db = new DB();
	
	Account() {};
	
	public String findAccountID(String name, String phone) {
		try {
			db.connectDB();
			
			String sql = "select customer_id, customer_name from customers where customer_name=? and customer_phone=?";
			db.PS = db.CN.prepareStatement(sql);
			db.PS.setString(1, name);
			db.PS.setString(2, phone);
			db.RS = db.PS.executeQuery();
			
			return (db.RS.next() == true) ? db.RS.getString("customer_id") : "empty";
			
		} catch (Exception e) {
			return "error";
		}
	}
	
	public String findAccountPWD(String name, String id) {
		try {
			db.connectDB();
			
			String sql = "select customer_pwd, customer_name from customers where customer_name=? and customer_id=?";
			db.PS = db.CN.prepareStatement(sql);
			db.PS.setString(1, name);
			db.PS.setString(2, id);
			db.RS = db.PS.executeQuery();
			
			return (db.RS.next() == true) ? db.RS.getString("customer_pwd") : "empty";
			
		} catch (Exception e) {
			return "error";
		}
	}
	
	public void addAccount(String[] customerData) {
		try {
			db.connectDB();
			
			String sql = "INSERT INTO customers(customer_id, customer_pwd, customer_name, customer_phone) VALUES(?, ?, ?, ?)";
			db.PS = db.CN.prepareStatement(sql);	
			for (int i=0; i<customerData.length; i++) {
				db.PS.setObject(i+1, customerData[i]);
			}
			db.PS.executeUpdate();
			
			System.out.println("회원가입이 완료되었습니다.");
			
			//이 부분은 마이페이지의 내 정보 보기로 가야할 듯...?
//			String printFormat = "| %-5s | %-10s | %-15s | %-15s | %-6s |\n";
//			Customer customer = new Customer(this.name, this.id, this.pwd, this.phone);
//			System.out.format("+-------+------------+-----------------+-----------------+--------+\n");
//			System.out.printf(printFormat, "Name", "ID", "Password", "Phone Number", "Coupon");
//			System.out.format("+-------+------------+-----------------+-----------------+--------+\n");
//			System.out.println(customer);
//			System.out.format("+-------+------------+-----------------+-----------------+--------+\n");
//			System.out.println("회원가입이 완료되었습니다.");
			
		} catch (Exception e) {
			System.out.println("error");
		}
	}	
	
}
