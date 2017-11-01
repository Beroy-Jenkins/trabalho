package br.projecao.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;


public class PerguntaDAO extends BaseDAO {

	public List<Pergunta> getPerguntas() throws SQLException {
		String sql = 
			 " select 											"
			+ " 	p.id as p_id                                "
			+ " 	, p.text p_text                             "
			+ " 	, o.id as o_id                              "
			+ " 	, o.text o_text                             "
			+ " 	, o.correta o_correta                       "
			+ " from                                            "
			+ " 	pergunta p                                  "
			+ " 	inner join opcao o on (p.id = o.pergunta_id)"
			+ " order by                                        "
			+ " 	p.id, o.id"
			;
		
		List<Pergunta> l = new ArrayList<>();
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = getConnection();
			stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();
			
			l = montaListaPerguntas(rs);
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
	
	private List<Pergunta> montaListaPerguntas (ResultSet rs) throws SQLException {
		List<Pergunta> l = new ArrayList<>();
		Pergunta p = null;
		
		// cria variavel de controle
		Long p_id = null;

		
		while(rs.next() ) {

			// se p for null, e a primera passagem ou  
			// se o p_id for dirente do registro
			if (p == null || !(rs.getLong("p_id") == p_id)) {

				//cria nova pergunta e ja adiona na lista
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
	
	private Pergunta rsToPergunta(ResultSet rs) throws SQLException{
		Pergunta u = new Pergunta();
		u.setId(rs.getLong("p_id"));
		u.setText(rs.getString("p_text"));

		return u;
	}

}
