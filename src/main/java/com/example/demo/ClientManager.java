package com.example.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.example.demo.dto.Clients;

import connection.DbConnection;

public class ClientManager {
	
	public void saveIntoDb(Clients c) {
		Connection conn=null;
		PreparedStatement pstmt=null;
		ResultSet rs = null;
		try {
			Calendar cal = Calendar.getInstance();
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
	        String submission_time = String.valueOf(sdf.format(cal.getTime()));
	        System.out.println("submission_time=>>"+submission_time);
	        
			conn = DbConnection.getInstance().getConnection();
			String sql = "insert into clients (sender_details, message, status, submission_date) values (?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, c.getSender_details());
			pstmt.setString(2, c.getMessage());
			pstmt.setString(3, "1");
			pstmt.setString(4, submission_time);
			pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(conn!=null) {
					conn.close();
				}if(rs!=null) {
					rs.close();
				}if(pstmt!=null) {
					pstmt.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			e2.printStackTrace();
			}
		} 
		
	}
	
	public Clients getPendingMessages() {
		Clients c = new Clients();
		Connection conn=null;
		Statement pstmt=null;
		ResultSet rs = null;
		try {
			conn = DbConnection.getInstance().getConnection();
			String sql = "select * from clients where status = 1";
			pstmt = conn.createStatement();
			rs = pstmt.executeQuery(sql);
			while(rs.next()) {
				
				c.setId(rs.getInt("id"));
				c.setSender_details(rs.getString("sender_details"));
				c.setMessage(rs.getString("message"));
				
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(conn!=null) {
					conn.close();
				}if(rs!=null) {
					rs.close();
				}if(pstmt!=null) {
					pstmt.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			e2.printStackTrace();
			}
			return c;
		} 
	}
	
	public void updateStatusById(Clients c) {
		Connection conn=null;
		Statement pstmt=null;
		ResultSet rs = null;
		try {
			conn = DbConnection.getInstance().getConnection();
			String sql = "update clients set status = 0 where id="+c.getId();
			pstmt = conn.createStatement();
			pstmt.executeUpdate(sql);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
