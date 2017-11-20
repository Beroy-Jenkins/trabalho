package br.projecao.domain;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PerguntaService {
	private PerguntaDAO db = new PerguntaDAO();

	public List<Pergunta> getPerguntas() {
		try {
			List<Pergunta> l = db.getPerguntas();
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<Pergunta>();
		}
	}

	public boolean save(Pergunta p) throws Exception {
		try {
			db.save(p);
			return true;
		}catch(SQLException e) {
			e.printStackTrace();
			throw new Exception (e);
		}
	}

	public List<Pergunta> pesquisa(String text) {
		try {
			List<Pergunta> l = db.pesquisa(text);
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<Pergunta>();
		}	
	}

	public Pergunta getbyid(String id) throws Exception {
		Pergunta p = null;
		try {
			p = db.getbyid(id);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception (e);

		}
		return p;
	}

	public void remove(String sid) throws Exception {
		try {
			db.remove(sid);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception (e);

		}
	}



}
