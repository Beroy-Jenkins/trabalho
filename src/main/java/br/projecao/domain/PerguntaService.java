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

}
