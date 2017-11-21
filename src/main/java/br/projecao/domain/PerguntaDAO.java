package br.projecao.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mysql.jdbc.Statement;

public class PerguntaDAO extends BaseDAO {

	public Pergunta getbyid(String id) throws SQLException {
		Pergunta p = null;
		List<Pergunta> l = getPerguntas(id);

		if (l.size() > 0) {
			p = l.get(0);
		}

		return p;
	}

	public List<Pergunta> getPerguntas() throws SQLException {
		return getPerguntas(null);
	}

	private List<Pergunta> getPerguntas(String id) throws SQLException {
		String sql = "\n select 											"
				+ "\n  	p.id as p_id                                "
				+ "\n  	, p.text p_text                             "
				+ "\n  	, o.id as o_id                              "
				+ "\n  	, o.text o_text                             "
				+ "\n  	, o.correta o_correta                       "
				+ "\n  from                                            "
				+ "\n  	pergunta p                                  "
				+ "\n  	inner join opcao o on (p.id = o.pergunta_id)" + "\n \n  %s "
				+ "\n  order by                                        " + "\n  	p.id, o.id";

		String where = "";

		// se o parametro text for nullo ou em branco
		if ((id != null) && (id.trim().length() > 0)) {
			where = "where p.id = " + id;
		}
		sql = String.format(sql, where);
		System.out.println(sql);

		List<Pergunta> l = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			l = montaListaPerguntas(rs);
			rs.close();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return l;
	}

	public List<Pergunta> pesquisa(String text) throws SQLException {
		String sql = "\n select 				" + "\n  	p.id as p_id        " + "\n  	, p.text p_text     "
				+ "\n  from                 " + "\n  	pergunta p          " + "\n  %s " + "\n  order by "
				+ "\n  	p.id ";

		String where = "";

		// se o parametro text for nullo ou em branco
		if ((text == null) || (text.trim().length() > 0)) {
			where = "where text like '%" + text + "%'";
		}

		sql = String.format(sql, where);
		System.out.println(sql);
		List<Pergunta> l = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				// Pergunta p = rsToPergunta(rs);

				Pergunta p = new Pergunta();
				p.setId(rs.getLong("p_id"));
				p.setText(rs.getString("p_text"));

				l.add(p);
			}

			rs.close();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
		return l;
	}

	private List<Pergunta> montaListaPerguntas(ResultSet rs) throws SQLException {
		List<Pergunta> l = new ArrayList<>();
		Pergunta p = null;

		// cria variavel de controle
		Long p_id = null;

		while (rs.next()) {

			// se p for null, e a primera passagem ou
			// se o p_id for dirente do registro
			if (p == null || !(rs.getLong("p_id") == p_id)) {

				// cria nova pergunta e ja adiona na lista
				p = rsToPergunta(rs);
				p_id = rs.getLong("p_id");
				l.add(p);
			}

			rsToOpcao(p, rs);

		}
		return l;
	}

	private void rsToOpcao(Pergunta p, ResultSet rs) throws SQLException {
		Opcao o = new Opcao();
		o.setId(rs.getLong("o_id"));
		o.setText(rs.getString("o_text"));
		o.setCorreta(rs.getBoolean("o_correta"));
		p.addOpcao(o);
	}

	private Pergunta rsToPergunta(ResultSet rs) throws SQLException {
		Pergunta u = new Pergunta();
		u.setId(rs.getLong("p_id"));
		u.setText(rs.getString("p_text"));

		return u;
	}

	public void save(Pergunta p) throws SQLException {
		Connection conn = null;
		try {
			conn = getConnection();
			deletarOpcao(conn, p);
			salvarPergunta(conn, p);

			List<Opcao> l = p.getOpcoes();

			/*
			 * for (int i = 0; i < l.size(); i++) { Opcao o = l.get(i);
			 * 
			 * }
			 * 
			 * for (Iterator iterator = l.iterator(); iterator.hasNext();) { Opcao opcao =
			 * (Opcao) iterator.next();
			 * 
			 * }
			 */
			for (Opcao o : l) {
				o.setIdPergunta(p.getId());
				salvarOpcao(conn, o);
			}

		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	private void deletarOpcao(Connection conn, Pergunta p) throws SQLException {
		ArrayList<String> l = new ArrayList<String>();
		
/*		for (Iterator<Opcao> iterator = p.getOpcoes().iterator(); iterator.hasNext();) {
			Opcao o = (Opcao) iterator.next();
			if ((o.getStatus() != null ) && (o.getStatus().equals("deletado"))) {
				//adiciona o id da opção na lista a ser deleta
				l.add(o.getId().toString());
				//remove a opção da lista de opções da pergunta
				iterator.remove();
			}
			
		}*/
		
		for (int i = p.getOpcoes().size() - 1; i > -1 ; i--) {
			Opcao o = p.getOpcoes().get(i);
			if ((o.getStatus() != null ) && (o.getStatus().equals("deletado"))) {
				//adiciona o id da opção na lista a ser deleta
				l.add(o.getId().toString());
				//remove a opção da lista de opções da pergunta
				p.getOpcoes().remove(i);
			}
		}
		if (l.size() > 0) {
			String joined = String.join(",", l);
			String sqlDeletaOpcao = "delete from opcao where id in ( %s )";
			execsql(conn, String.format(sqlDeletaOpcao, joined));
		}

	}

	private void salvarPergunta(Connection conn, Pergunta p) throws SQLException {
		String SqlPerguntaInsert = "INSERT INTO pergunta (text) VALUES (?)";
		String SqlPerguntaUpdate = "UPDATE pergunta set text = ? where id = ?";
		PreparedStatement stmt = null;
		boolean insercao = false;

		try {
			insercao = (p.getId() == null);

			if (insercao) { // se for inserção
				stmt = conn.prepareStatement(SqlPerguntaInsert, Statement.RETURN_GENERATED_KEYS);
			} else { // se nao
				stmt = conn.prepareStatement(SqlPerguntaUpdate, Statement.RETURN_GENERATED_KEYS);
			}

			stmt.setString(1, p.getText());

			if (!insercao) { // se nao for inserção
				stmt.setLong(2, p.getId());
			}

			// executa o camando
			int count = stmt.executeUpdate();

			if (count == 0) {
				throw new SQLException("Erro ao inserir a pergunta");
			}

			if (insercao) { // se for inserção recupera o id
				Long id = getGeneratedId(stmt);
				p.setId(id);
			}

		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

	}

	private void salvarOpcao(Connection conn, Opcao o) throws SQLException {
		String SqlOpcaoInsert = "INSERT INTO opcao (text,pergunta_id,correta) VALUES (?,?,?)";
		String SqlOpcaoUpdate = "UPDATE opcao SET text = ?, correta = ? WHERE id = ?";

		PreparedStatement stmt = null;
		boolean insercao = false;

		try {
			insercao = (o.getId() == null);

			if (insercao) { // se for inserção
				stmt = conn.prepareStatement(SqlOpcaoInsert, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, o.getText());
				stmt.setLong(2, o.getIdPergunta());
				stmt.setBoolean(3, o.getCorreta());

			} else { // se nao
				stmt = conn.prepareStatement(SqlOpcaoUpdate, Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, o.getText());
				stmt.setBoolean(2, o.getCorreta());
				stmt.setLong(3, o.getId());
			}
			// executa o camando
			int count = stmt.executeUpdate();

			if (count == 0) {
				throw new SQLException("Erro ao inserir a opcao");
			}

			if (insercao) { // se for inserção recupera o id
				Long id = getGeneratedId(stmt);
				o.setId(id);
			}

		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}

	}

	public void remove(String sid) throws SQLException {
		String sqlDeletaOpcao = "delete from opcao where pergunta_id = %s";
		String sqlDeletaPergunta = "delete from pergunta where id = %s";
		Connection conn = null;

		try {
			conn = getConnection();
			execsql(conn, String.format(sqlDeletaOpcao, sid));
			execsql(conn, String.format(sqlDeletaPergunta, sid));
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	private void execsql(Connection conn, String sql) throws SQLException {
		PreparedStatement stmt = null;
		System.out.println(sql);
		try {
			stmt = conn.prepareStatement(sql);
			int count = stmt.executeUpdate();
		} finally {
			if (stmt != null) {
				stmt.close();
			}
		}
	}

}
