package org.unibl.etf.dr.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.unibl.etf.dr.dto.SubjectOnStudyProgram;

public class SubjectOnStudyProgramDAO {
	
	private static ConnectionPool connectionPool = ConnectionPool.getConnectionPool();

	private static final String SELECT_SOSP = "select sosp.*, s.name, sp.name from subject_on_study_program as sosp inner join subject as s on sosp.idSubject=s.id inner join study_program as sp on sosp.idStudyProgram=sp.id;";
	private static final String INSERT_SOSP = "insert into subject_on_study_program(idSubject, idStudyProgram, typeOfSubject, semester) values(?,?,?,?);";
	private static final String DELETE_SOSP = "delete from subject_on_study_program where idSubject=? and idStudyProgram=?;";
	
	public static JSONArray selectSOSP() {
		JSONObject json = null;
		JSONObject data = null;
		JSONArray array = new JSONArray();
		Connection conn = null;
		ResultSet rs = null;
		conn = connectionPool.getConnection();
		Object parametri[] = { };
		PreparedStatement ps = DAOUtil.preparedStatement(conn, SELECT_SOSP, false, parametri);
		try {
			rs = ps.executeQuery();
			while (rs.next()) {
				json = new JSONObject();
				data = new JSONObject();
				data.put("idSubject", rs.getInt("idSubject"));
				data.put("idStudyProgram", rs.getInt("idStudyProgram"));
				data.put("typeOfSubject", rs.getString("typeOfSubject"));
				data.put("semester", rs.getString("semester"));
				data.put("subjectName", rs.getString("s.name"));
				data.put("studyProgramName", rs.getString("sp.name"));
				json.put("dataObject", data);
				array.put(json);
				json = new JSONObject();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			connectionPool.returnConnectionToConnectionPool(conn);
		}
		return array;
	}

	public static int insertSOSP(SubjectOnStudyProgram sosp) {
		Connection conn = null;
		conn = connectionPool.getConnection();
		Object parametri[] = { sosp.getIdSubject(), sosp.getIdStudyProgram(), sosp.getTypeOfSubject(), sosp.getSemester()};
		int generatedId = 0;
		PreparedStatement ps = DAOUtil.preparedStatement(conn, INSERT_SOSP, true, parametri);
		ResultSet rs = null;
		try {
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if (rs.next()) {
				generatedId = rs.getInt(1);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				connectionPool.returnConnectionToConnectionPool(conn);
				return generatedId;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return generatedId;
	}

	
	public static int deleteSOSP(int idSubject, int idStudyProgram) {
		Connection conn = null;
		conn = connectionPool.getConnection();
		Object parametri[] = { idSubject, idStudyProgram};
		int flagSuccess = -1;
		PreparedStatement ps = DAOUtil.preparedStatement(conn, DELETE_SOSP, true, parametri);
		try {
			flagSuccess = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
				connectionPool.returnConnectionToConnectionPool(conn);
				return flagSuccess;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return flagSuccess;
	}
	
	/*public static JSONObject selectBookBorrowingWithId(int id) {
		JSONObject json = null;
		JSONObject data = null;
		Connection conn = null;
		ResultSet rs = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		conn = connectionPool.getConnection();
		Object parametri[] = { id };
		PreparedStatement ps = DAOUtil.preparedStatement(conn, SELECT_BOOK_BORROWING_WITH_ID, false, parametri);
		try {
			rs = ps.executeQuery();
			if(rs.next()) {
				json = new JSONObject();
				data = new JSONObject();
				data.put("id", rs.getInt("id"));
				data.put("idUser", rs.getString("idUser"));
				data.put("idBook", rs.getString("idBook"));
				data.put("dateOfBorrowing", sdf.format(rs.getDate("dateOfBorrowing")));
				data.put("returned", rs.getBoolean("returned"));
				data.put("username", rs.getString("u.username"));
				data.put("title", rs.getString("b.title"));
				data.put("bookborrowing", rs.getString("u.username") + "-" + rs.getString("b.title"));
				json.put("dataObject", data);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			connectionPool.returnConnectionToConnectionPool(conn);
		}
		return json;
	}*/
}
