package cafe;

import java.sql.SQLException;
import java.util.Scanner;

public class Payment {
	static DB db = new DB();
	static Scanner sc = new Scanner(System.in);
	static int coupon = 0;
	static int discount = 0;
	static int coupon_check = 0;
	
	public static void paymentHome() {
		
		try {
			db.connectDB();
			
			String sql = "SELECT customer_coupon FROM customers where customer_id=?";
			db.PS = db.CN.prepareStatement(sql);
			db.PS.setString(1, "asd"); //아이디 선택
			db.RS = db.PS.executeQuery();
			
			while(db.RS.next()) {
				coupon = db.RS.getInt("customer_coupon");
			}
			System.out.println("======결제======");
			System.out.println("orderCart 내역");
			System.out.println("       총 금액");
			System.out.println("----------------");
			System.out.println("현재 가지고 있는 쿠폰 수는 "+coupon+"개");
			System.out.println("1. 일반 결제");
			System.out.println("2. 혼합 결제");
			int select = sc.nextInt();
			if(select==1) {
				paymentNomal();
			}
			else if(select==2) {
				paymentCoupon();
			}
		}catch (Exception e) {
			System.out.println("에러 발생");
		}
	}
	
	public static void paymentNomal() {
		int order = 5000; //주문금액 테스트
		System.out.println("주문 하신 총 금액 : "+order);
		System.out.println("결제하실 금액을 입력하세요");
		while(order!=0) {
			int pay = sc.nextInt();
			if(order==pay || order==0) {
				System.out.println("결제 완료 되었습니다.\n");
				paymentAddCoupon();
				System.out.println("영수증 출력중(내역추가해야함)\n\n\n");
				break;
			}
			else if(order<pay) {
				System.out.println("주문 금액보다 결제금액이 큽니다\n다시입력하세요");
				continue;
			}
			else if(order>pay) {
				System.out.println(pay+"원 결제 되었습니다.\n남은 금액("+(order-pay)+")을 입력하세요.");
				order -= pay;
				continue;
			}
		}
	}
	
	public static void paymentCoupon() throws SQLException {
		int order = 5000; //주문금액 테스트
		String sql = "SELECT customer_coupon FROM customers where customer_id=?";
		String sql2 = "SELECT customer_coupon FROM customers where customer_id='admin'";
		String sql3 = "UPDATE customers set customer_coupon = customer_coupon-? where customer_id=?";
		db.PS = db.CN.prepareStatement(sql);
		db.PS.setString(1, "asd");
		db.RS = db.PS.executeQuery();
		
		System.out.println("주문 하신 총 금액 : "+order);
		while(db.RS.next()) {
			coupon = db.RS.getInt("customer_coupon");
		}
		System.out.println("보유한 쿠폰의 개수는 "+coupon+"개 입니다.\n사용할 쿠폰 수를 입력하세요.");
		
		db.PS = db.CN.prepareStatement(sql2);
		db.RS = db.PS.executeQuery();
		while(db.RS.next()) {
			discount = db.RS.getInt("customer_coupon");
		}
		while(order!=0) {
			int coupon_sel = sc.nextInt();
			if(coupon_sel<=0) {
				System.out.println("일반결제를 해주세요");
				break;
			}
			else if(coupon>=coupon_sel) {
				System.out.println((coupon_sel*discount)+"원 차감되었습니다.");
				if(order<(coupon_sel*discount)) {
					db.PS = db.CN.prepareStatement(sql3);
					db.PS.setLong(1, coupon_sel);
					db.PS.setString(2, "asd"); //아이디 선택
					db.PS.executeUpdate();
					System.out.println("결제 완료 되었습니다.\n");
					paymentAddCoupon();
					System.out.println("영수증 출력하세요.\n\n\n");
					break;
				}
				
				System.out.println("결제하실 남은 금액은 "+(order-(coupon_sel*discount))+"원 입니다.");
				order -= (coupon_sel*discount);
				
				System.out.println("결제하실 금액을 입력하세요");
				int pay = sc.nextInt();
				if(order==pay || order<=0) {
					db.PS = db.CN.prepareStatement(sql3);
					db.PS.setLong(1, coupon_sel);
					db.PS.setString(2, "asd"); //아이디 선택
					db.PS.executeUpdate();
					System.out.println("결제 완료 되었습니다.\n");
					paymentAddCoupon();
					System.out.println("영수증 출력중(내역추가해야함)\n\n\n");
					db.PS.close();
					break;
				}
				else if(order<pay) {
					System.out.println("주문 금액보다 결제금액이 큽니다\n다시입력하세요");
					continue;
				}
				else if(order>pay) {
					System.out.println(pay+"원 결제 되었습니다.\n남은 금액("+(order-pay)+")을 입력하세요.");
					order -= pay;
					continue;
				}
			}
			else if(coupon<coupon_sel) {
				System.out.println("보유하신 쿠폰보다 더 입력하셨습니다.\n다시입력하세요");
				continue;
			}
		}
		db.RS.close();
		db.PS.close();
		db.CN.close();
	}
	public static void paymentAddCoupon() {
		try {
			String sql = "SELECT customer_couponcheck FROM customers where customer_id=?";
			String sql2 = "UPDATE customers set customer_couponcheck = customer_couponcheck+1 where customer_id=?"; //스탬프 1개 추가
			String sql3 = "UPDATE customers set customer_couponcheck = customer_couponcheck-10, customer_coupon = customer_coupon+1 where customer_id=?"; //스탬프 10개 제거, 쿠폰 1개추가
			db.PS = db.CN.prepareStatement(sql);
			db.PS.setString(1, "asd");
			db.RS = db.PS.executeQuery();
			
			while(db.RS.next()) { //쿠폰 적립 수 확인
				coupon_check = db.RS.getInt("customer_couponcheck");
			}
			System.out.println("이전 스탬프 개수 : "+coupon_check+"\n");
			db.PS = db.CN.prepareStatement(sql2);
			db.PS.setString(1, "asd");
			System.out.println("스탬프 1개가 추가 되었습니다.\n");
			db.PS.executeUpdate();
			db.PS = db.CN.prepareStatement(sql);
			db.PS.setString(1, "asd");
			db.RS = db.PS.executeQuery();
			while(db.RS.next()) { //쿠폰 적립 수 확인
				coupon_check = db.RS.getInt("customer_couponcheck");
			}
			System.out.println("현재 스탬프 개수 : "+(coupon_check)+"\n");
			if(coupon_check == 10){
				db.PS = db.CN.prepareStatement(sql3);
				db.PS.setString(1, "asd"); //아이디 선택
				System.out.println("스탬프가 10개 적립되어 쿠폰 1개로 변경되었습니다.");
				db.PS.executeUpdate();
			}
			db.RS.close();
			db.PS.close();
			db.CN.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
	}
}