package br.projecao.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;


public class UsuarioDAO extends BaseDAO {

	public void castrar(Usuario u) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement("INSERT INTO usuario (login, senha) VALUES (?,?)",
						Statement.RETURN_GENERATED_KEYS);
				;
			stmt.setString(1, u.getLogin());
			stmt.setString(2, u.getSenha());

			int count = stmt.executeUpdate();
			if (count == 0) {
				throw new SQLException("Erro ao inserir usuario ");
			}
			Long id = getGeneratedId(stmt);
			u.setId(id);
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
	}

	public List<Usuario> getUsuarios() throws SQLException {
		List<Usuario> l = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement("SELECT * FROM usuario");
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				Usuario u = rsToUsuario(rs);
				l.add(u);
			}
			rs.close();
		}finally {
			if(stmt != null) {
				stmt.close();
			}
			if(conn != null) {
				conn.close();
			}
		}
		return l;
	}

	private Usuario rsToUsuario(ResultSet rs) throws SQLException{
		Usuario u = new Usuario();
		u.setId(rs.getLong("id"));
		u.setLogin(rs.getString("login"));
		u.setSenha(rs.getString("senha"));
		return u;
	}

	public Usuario logar(Usuario u) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement("SELECT * FROM usuario WHERE login = ? AND senha = ?");
			stmt.setString(1, u.getLogin());
			stmt.setString(2, u.getSenha());
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				u = rsToUsuario(rs);
				rs.close();
				//return u;
			}
		}finally {
			if(stmt != null) {
				stmt.close();
			}
			if(conn != null) {
				conn.close();
			}
		}
		return u;
	}

	public Boolean checaruser(String login) throws SQLException {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement("SELECT * FROM usuario WHERE login = ? ");
			stmt.setString(1, login);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				return true;
			} else {
				return false;
			}
		}finally {
			if(stmt != null) {
				stmt.close();
			}
			if(conn != null) {
				conn.close();
			}
		}
	}
}
