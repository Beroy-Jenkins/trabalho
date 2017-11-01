package br.projecao.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import javax.ws.rs.core.Response;
import br.projecao.domain.Usuario;
import br.projecao.domain.UsuarioService;

@Path("/usuario")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class UsuarioResource {

	private UsuarioService usuarioService = new UsuarioService();

	@POST
	@Path("/checaruser")
	public Response checaruser(Map<String, Object> map) {
		Map<String, Object> o = new HashMap<>();
		try {
			String login = (String) map.get("login");
			Boolean existe = usuarioService.checaruser(login);
			if (!existe) {
				o.put("erro", "Usuario não cadastrado");
			}
		} catch (Exception e) {
			e.printStackTrace();
			o.put("erro", e.getMessage());
		}
		Response build = Response.ok().entity(o).build();
		return build;	
	}

	@GET
	public List<Usuario> get() {
		List<Usuario> l = usuarioService.getUsuarios();
		return l;
	}
	
	@POST
	@Path("/checarsenha")
	public Response checarsenha(Map<String, Object> map) {
		Map<String, Object> o = new HashMap<>();
		try {
			String login = (String) map.get("login");
			String senha = (String) map.get("senha");
			Usuario usuario = usuarioService.checarsenha(login, senha);
			o.put("usuario", usuario);
		} catch (Exception e) {
			e.printStackTrace();
			o.put("erro", e.getMessage());
		}
		Response build = Response.ok().entity(o).build();
		return build;	
	}	

	@POST
	@Path("/cadastrar")
	public Response cadastrar(Map<String, Object> map) {
	
		Map<String, Object> o = new HashMap<>();
		
		try {
			String login = (String) map.get("login");
			String senha = (String) map.get("senha");
			Usuario usuario = new Usuario();
			usuario.setLogin(login);
			usuario.setSenha(senha);
			usuario = usuarioService.cadastrar(usuario);
			usuario.setSenha("");
			o.put("usuario", usuario);
		} catch (Exception e) {
			e.printStackTrace();
			o.put("erro", e.getMessage());
		}
		Response build = Response.ok().entity(o).build();
		return build;
	}
	
}