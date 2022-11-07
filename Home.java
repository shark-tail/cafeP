package cafe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Home {	
	static DB db = new DB();
	static Customer customer = new Customer();
	static Scanner scan = new Scanner(System.in);
	static String[] customerData = new String[4]; //아이디, 비밀번호, 이름, 전화번호를 담을 배열
	
	////////// 앱 실행 //////////
	public static void main(String[] args) {		
		boolean sw = true;
		while (sw) {
			System.out.print("[1. 로그인]\t[2. 회원가입]\n>>> ");
			switch (scan.nextLine()) {
				case "1": login(); sw = false; break;
				case "2": signUp(); break;
				default: System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
			}
		}
	}
	
	
	////////// 로그인 //////////
	public static void login() {
		System.out.println("\n========== 로그인 ==========");
		boolean sw = true;
		while (sw) {
			System.out.print("[1. 아이디/비밀번호 입력]\t[2. 아이디 찾기]\t[3. 비밀번호 찾기]\n>>> ");
			switch (scan.nextLine()) {
				case "1": inputIDPWD(); sw = false; break;
				case "2": findID(); sw = false; break;
				case "3": findPWD(); sw = false; break;
				default: System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
			}
		}
	}
	
	public static void inputIDPWD() {
		System.out.println("\n========== 아이디/비밀번호 입력 ==========");
		loop: while (true) {
			System.out.print("아이디를 입력해주세요.\n>>> ");
			String id = scan.nextLine();
			System.out.print("비밀번호를 입력해주세요.\n>>> ");
			String pwd = scan.nextLine();
			
			switch (handleLogin(id, pwd)) {
				case "empty":
					System.out.println("아이디 또는 비밀번호를 다시 확인해주세요.\n");
					continue loop;
				case "admin":
					System.out.println("관리자로 로그인하였습니다.");
					/*  관리자 화면으로 이동... */
					break;
				case "customer":
					createCustomerObj(id, pwd);
					System.out.println("로그인이 완료되었습니다.");
					/* 회원 화면으로 이동... */
					break;
			}
		}
	}
	
	public static void findID() {
		System.out.println("\n========== 아이디 찾기 ==========");
		System.out.print("이름을 입력해주세요.\n>>> ");
		String name = scan.nextLine();
		System.out.print("전화번호를 입력해주세요. 구분 단위는 '-' 입니다.\n>>> ");
		String phone = scan.nextLine();
		
		String foundID = new Account().findAccountID(name, phone);
		switch (foundID) {
			case "error": 
				System.out.println("에러 발생"); 
				login(); 
				break;
			case "empty": 
				System.out.println("일치하는 회원정보가 없습니다."); 
				login();
				break;
			default: 
				System.out.printf("[%s]님의 아이디는 [%s]입니다.\n", name, foundID); 
				login();
				
		}
	}
	
	public static void findPWD() {
		System.out.println("\n========== 비밀번호 찾기 ==========");
		System.out.print("이름을 입력해주세요.\n>>> ");
		String name = scan.nextLine();
		System.out.print("아이디를 입력해주세요.\n>>> ");
		String id = scan.nextLine();
	
		String foundPWD = new Account().findAccountPWD(name, id);
		switch (foundPWD) {
			case "error": 
				System.out.println("에러 발생"); 
				login(); 
				break;
			case "empty": 
				System.out.println("일치하는 회원정보가 없습니다."); 
				login(); 
				break;
			default: 
				System.out.printf("[%s]님의 비밀번호는 [%s]입니다.\n", name, decrypt(foundPWD)); 
				login();
		}
	}
	
	
	////////// 회원가입 //////////
	public static void signUp() {
		System.out.println("\n========== 회원가입 ==========");
		saveID();
		savePWD();
		saveName();
		savePhone();
		new Account().addAccount(customerData);
	}
	
	public static void saveID() {
		while (true) {
			//아이디 입력 받기
			System.out.print("아이디를 입력해주세요.\n>>> ");
			String id = scan.nextLine();
			
			//입력값 유효성 체크 및 배열에 데이터 저장
			if (checkBlank(id)) {
				System.out.println("잘못된 아이디입니다. 다시 입력해주세요.\n");
				continue;
			} else if (checkAdminID(id)) {
				System.out.println("사용할 수 없는 아이디입니다. 다시 입력해주세요.\n");
				continue;
			} else if (checkDuplicateID(id)) {
				System.out.println("이미 존재하는 아이디입니다. 다시 입력해주세요.\n");
				continue;
			} else {
				customerData[0] = id;
				break;
			}
		}
	}
	
	public static void savePWD() {
		while (true) {
			//이름 입력 받기
			System.out.print("비밀번호를 입력해주세요.\n>>> ");
			String pwd = scan.nextLine();
			
			//입력값 유효성 체크 및 암호화 후 배열에 데이터 저장
			if (checkBlank(pwd)) {
				System.out.println("잘못된 비밀번호입니다. 다시 입력해주세요.\n");
				continue;
			} else if (checkReconfirmPWD(pwd)) {
				System.out.println("비밀번호가 일치하지 않습니다. 다시 입력해주세요.\n");
			} else {
				customerData[1] = encrypt(pwd);
				break;
			}
		}
	}
	
	public static void saveName() {
		while (true) {
			//이름 입력 받기
			System.out.print("이름을 입력해주세요.\n>>> ");
			String name = scan.nextLine();
			
			//입력값 유효성 체크 및 배열에 데이터 저장
			if (checkBlank(name)) {
				System.out.println("잘못된 이름입니다. 다시 입력해주세요.\n");
				continue;
			} else {
				customerData[2] = name;
				break;
			}
		}
	}
	
	public static void savePhone() {
		while (true) {
			//전화번호 입력 받기
			System.out.print("전화번호를 입력해주세요. 구분 단위는 '-' 입니다.\n>>> ");
			String phone = scan.nextLine();
			
			//입력값 유효성 체크 및 배열에 데이터 저장
			if (checkPhoneFormat(phone)) {
				System.out.println("잘못된 전화번호입니다. 다시 입력해주세요.\n");
				continue;
			} else {
				customerData[3] = phone;
				break;
			}
		}
	}
	
	
	////////// 로그인할 때 관리자인지 일반 회원인지 판별 //////////
	public static String handleLogin(String id, String pwd) {
		try {
			db.connectDB();
			String sql = "select * from customers where customer_id=? and customer_pwd=?";
			db.PS = db.CN.prepareStatement(sql);
			db.PS.setString(1, id);
			db.PS.setString(2, encrypt(pwd));
			db.RS = db.PS.executeQuery();
			
			if (db.RS.next() == false) {
				return "empty"; 
			} else if (db.RS.getString("customer_id").equals("admin")) {
				return "admin";
			} else {
				return "customer";
			}
		} catch (Exception e) {
			return "handleLogin() error";
		}
	}
	
	public static void createCustomerObj(String id, String pwd) {
		try {
			db.connectDB();
			String sql = "select * from customers where customer_id=? and customer_pwd=?";
			db.PS = db.CN.prepareStatement(sql);
			db.PS.setString(1, id);
			db.PS.setString(2, encrypt(pwd));
			db.RS = db.PS.executeQuery();
			
			customer.setId(db.RS.getString("customer_id"));
			customer.setPwd(db.RS.getString("customer_pwd"));
			customer.setName(db.RS.getString("customer_name"));
			customer.setPhone(db.RS.getString("customer_phone"));
			customer.setCoupon(db.RS.getInt("customer_coupon"));
			
//			Customer customer = new Customer(
//					db.RS.getString("customer_id"),
//					db.RS.getString("customer_name"),
//					db.RS.getString("customer_pwd"),
//					db.RS.getString("customer_phone"),
//					db.RS.getInt("customer_coupon")
//			);
		} catch (Exception e) {
			System.out.println("createCustomerObj() error");
		}
	}
	
	////////// 데이터 유효성 검사 //////////
	public static boolean checkBlank(String data) {
		return (data == null || data.isEmpty()) ? true : false;  //null값이거나 공백일 경우, true
	}
	
	public static boolean checkAdminID(String data) {
		return (data.equals("admin")) ? true : false;  //관리자 아이디일 경우, true
	}
	
	public static boolean checkDuplicateID(String id) {
		try {
			db.connectDB();
			String sql = "select customer_id from customers";
			db.PS = db.CN.prepareStatement(sql);
			db.RS = db.PS.executeQuery();
			
			boolean duplicate = false;
			while (db.RS.next() == true) {
				if (id.equals(db.RS.getString("customer_id"))) {
					duplicate = true;
					break;
				} 
			}
			return duplicate; //중복된 아이디일 경우, true
		} catch (Exception e) {
			return false;
		}
	}
	
	public static boolean checkReconfirmPWD(String pwd) {
		System.out.print("재확인을 위해 비밀번호를 다시 입력해주세요.\n>>> ");
		String temp = scan.nextLine();
		return !(temp.equals(pwd)) ? true : false;  //비밀번호와 재입력된 비밀번호가 일치하지 않을 경우, true
	}
	
	public static boolean checkPhoneFormat(String phone) {
		return !(Pattern.matches("\\d{3}-\\d{4}-\\d{4}", phone)) ? true : false;  //전화번호 양식에 맞지 않을 경우, true
	}
	
	
	////////// 데이터 복호화 및 암호화 //////////
	public static String encrypt(String data) {
		int KEY = 2;
		String encryptedData = "";
		for (int i = 0; i < data.length(); i++) {
			encryptedData += (char)(data.charAt(i) * KEY);
		}
		return encryptedData;
	}
	
	public static String decrypt(String data) {
		int KEY = 2;
		String decryptedData = "";
		for (int i=0; i<data.length(); i++) {
			decryptedData += (char)(data.charAt(i) / KEY);
		}
		return decryptedData;
	}
}