package pay;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class PayDAO {
	
	private Connection conn;
	private ResultSet rs;
	
	public PayDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost/wjdqo3122?characterEncoding=UTF-8&serverTimezone=UTC";
			String dbID = "wjdqo3122";
			String dbPassword = "dPtjd0522";
			Class.forName("com.mysql.cj.jdbc.Driver"); 
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getDate() {
		String 	SQL = "SELECT NOW()";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ""; //데이터베이스 오류
	}	
	public int getNext() {
		String 	SQL = "SELECT payID FROM PAY ORDER BY payID DESC";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; //첫 번째 게시물인 경우
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
	
	public int write(String payTitle, String userID, String securityNum, String phoneNumber, String payContent) {
		String 	SQL = "INSERT INTO PAY VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, payTitle);
			pstmt.setString(3, userID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, securityNum);
			pstmt.setString(6, phoneNumber);
			pstmt.setString(7, payContent);
			pstmt.setInt(8, 1);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
	
	public ArrayList<Pay> getList(int pageNumber) {
		String SQL = "SELECT * FROM PAY WHERE payID < ? AND payAvailable = 1 ORDER BY payID DESC LIMIT 10";
		ArrayList<Pay> list = new ArrayList<Pay>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Pay pay = new Pay();
				pay.setPayID(rs.getInt(1));
				pay.setPayTitle(rs.getString(2));
				pay.setUserID(rs.getString(3));
				pay.setPayDate(rs.getString(4));
				pay.setSecurityNum(rs.getString(5));
				pay.setPhoneNumber(rs.getString(6));
				pay.setPayContent(rs.getString(7));
				pay.setPayAvailable(rs.getInt(8));
				list.add(pay);
			}
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return list; // 데이터베이스 오류
	}
	
	public boolean nextPage(int pageNumber) {
		String SQL = "SELECT * FROM PAY WHERE payID < ? AND payAvailable = 1";
		ArrayList<Pay> list = new ArrayList<Pay>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return false;
	}
	
	public Pay getPay(int payID) {
		String SQL = "SELECT * FROM PAY WHERE payID = ?";
		ArrayList<Pay> list = new ArrayList<Pay>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, payID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Pay pay = new Pay();
				pay.setPayID(rs.getInt(1));
				pay.setPayTitle(rs.getString(2));
				pay.setUserID(rs.getString(3));
				pay.setPayDate(rs.getString(4));
				pay.setSecurityNum(rs.getString(5));
				pay.setPhoneNumber(rs.getString(6));
				pay.setPayContent(rs.getString(7));
				pay.setPayAvailable(rs.getInt(8));
				return pay;
			}
		} catch (Exception e) {
			e.printStackTrace();			
		}
		return null;
	}
		
	public int delete(int payID) {
		String 	SQL = "UPDATE PAY SET payAvailable = 0 WHERE payID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, payID);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; //데이터베이스 오류
	}
}
