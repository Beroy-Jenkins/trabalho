package br.projecao.domain;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {
	private UsuarioDAO db = new UsuarioDAO();

	public Usuario cadastrar(Usuario u) throws Exception {
		try {
			
			Boolean existe = db.checaruser(u.getLogin()); 
			if(existe) {
				throw new Exception("usuario já cadastrado");
			}
			db.castrar(u);
			return u;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("Não foi possivel cadastrar \n" + e.getMessage());
		}
	}

	public List<Usuario> getUsuarios() {
		try {
			List<Usuario> l = db.getUsuarios();
			return l;
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<Usuario>();
		}
	}

	public Usuario logar(Usuario u) throws Exception {
		try {
			u = db.logar(u);
			if(u.getId() == null) {
				throw new Exception("usuario não existe");
			}
			return u;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("Não foi possivel consultar \n" + e.getMessage());
		}
	}

	public Boolean checaruser(String login) throws Exception {
		try {
			return db.checaruser(login);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new Exception("Não foi possivel consultar \n" + e.getMessage());
		}
		
	}

	public Usuario checarsenha(String login, String senha) throws Exception {
		Usuario u = new Usuario();
		u.setLogin(login);
		u.setSenha(senha);
		return logar(u);
	}

}
