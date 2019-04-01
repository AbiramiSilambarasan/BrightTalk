package com.brighttalk.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;

import javax.management.InvalidAttributeValueException;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.brighttalk.model.RealmEntity;
import com.brighttalk.service.KeyGeneration;

@Controller
public class CreateGetRealmController {

	@Autowired
	private DataSource dataSource;

	final String secretKey = "secret";

	@RequestMapping(value = "/service/user/realm", method = RequestMethod.POST)
	public RealmEntity createRealm(@RequestBody RealmEntity realmEntity) throws Exception {

		if (realmEntity.getName().equals("") || realmEntity.getName() == null) {

			String emptyRealmNameExcptn = "InvalidAttributeValueException";
			if (emptyRealmNameExcptn.equalsIgnoreCase("InvalidAttributeValueException")) {
				throw new InvalidAttributeValueException("InvalidAttributeValueException");
			}
		}

		RealmEntity realmEnty = new RealmEntity();

		String encryptedString = KeyGeneration.encrypt(realmEntity.getName(), secretKey);
		int generatedId = 0;
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmtquery = null;
		String sql = "INSERT INTO brighttalk.brighttalk_tb VALUES(?,?,?,?)";
		try {
			con = dataSource.getConnection();
			if (con != null) {

				pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				pstmt.setString(1, null);
				pstmt.setString(2, realmEntity.getName());
				pstmt.setString(3, realmEntity.getDescription());
				pstmt.setString(4, encryptedString);
				int insert = pstmt.executeUpdate();

				if (insert == 1) {
					// get generated id
					rs = pstmt.getGeneratedKeys();
				}
				if (rs.next()) {
					generatedId = rs.getInt(1);

				}
				String query = "Select * from brighttalk.brighttalk_tb where id = ?";
				pstmtquery = dataSource.getConnection().prepareStatement(query);
				pstmtquery.setInt(1, generatedId);
				rs = pstmtquery.executeQuery();// then execute the statement
				if (rs.next()) {
					realmEnty.setId(rs.getInt(1));
					realmEnty.setName(rs.getString(2));
					realmEnty.setDescription(rs.getString(3));
					realmEnty.setKey(KeyGeneration.decrypt(rs.getString(4), secretKey));
				}
			}
		} catch (Exception e) {

			String sqlConstraintExcptn = "SQLIntegrityConstraintViolationException";
			if (sqlConstraintExcptn.equalsIgnoreCase("SQLIntegrityConstraintViolationException")) {
				throw new SQLIntegrityConstraintViolationException("SQLIntegrityConstraintViolationException");
			}
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (pstmtquery != null) {
					pstmtquery.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				System.out.println("Exception in finally block : " + e);
			}
		}
		return realmEnty;
	}

	@RequestMapping(value = "/service/user/realm/{id}", method = RequestMethod.GET)
	public RealmEntity getRealm(@PathVariable("id") int realmId) throws Exception {
		Connection con = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		RealmEntity realmEntyObj = new RealmEntity();

		try {
			con = dataSource.getConnection();
			if (con != null) {

				String query = "Select * from brighttalk.brighttalk_tb where id = ?";

				pstmt = con.prepareStatement(query);

				pstmt.setInt(1, realmId);

				rs = pstmt.executeQuery();// then execute the statement

				if (rs.next()) {

					realmEntyObj.setId(rs.getInt(1));
					realmEntyObj.setName(rs.getString(2));
					realmEntyObj.setDescription(rs.getString(3));
					realmEntyObj.setKey(KeyGeneration.decrypt(rs.getString(4), secretKey));
				} else {
					throw new SQLException("SQLException");
				}
			}
		} catch (Exception e) {
			System.out.println("Exception in getRealm: " + e.getMessage() + " ::: " + e.getCause());
			throw new SQLException("SQLException");
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (Exception e) {
				System.out.println("Exception in finally block : " + e);
			}
		}
		return realmEntyObj;

	}
}
