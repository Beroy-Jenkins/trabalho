package br.projecao.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseDAO {
	public BaseDAO() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected Connection getConnection() throws SQLException {
		String url = "jdbc:mysql://localhost/trabalho?useOldAliasMetadataBehavior = true";
		Connection conn = DriverManager.getConnection(url, "trabalho", "trabalho123");
		return conn;
	}

	public static void main(String[] args) throws SQLException {
		BaseDAO db = new BaseDAO();
		Connection conn = db.getConnection();
		System.out.println(conn);
	}

	public static Long getGeneratedId(PreparedStatement stmt) throws SQLException {
		ResultSet rs = stmt.getGeneratedKeys();
		if(rs.next()) {
			Long id = rs.getLong(1);
			return id;
		}
		return 0L;
	}
}