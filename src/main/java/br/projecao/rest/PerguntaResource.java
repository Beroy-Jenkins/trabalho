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

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import br.projecao.domain.Pergunta;
import br.projecao.domain.PerguntaService;

@Path("/pergunta")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class PerguntaResource {
	private PerguntaService PerguntaService = new PerguntaService();

	@GET
	public Response get() {
		Map<String, Object> o = new HashMap<>();

		List<Pergunta> l = PerguntaService.getPerguntas();
		o.put("list", l);

		Response build = Response.ok().entity(o).build();
		return build;

	}

	@POST
	@Path("/pesquisa")
	public Response pesquisa(Map<String, Object> map) {
		Map<String, Object> o = new HashMap<>();
		try {
			String text = (String) map.get("text");

			List<Pergunta> l = PerguntaService.pesquisa(text);
			o.put("list", l);

			Response build = Response.ok().entity(o).build();
			return build;
		} catch (Exception e) {
			e.printStackTrace();
			o.put("erro", e.getMessage());
		}
		Response build = Response.ok().entity(o).build();
		return build;
	}

	@POST
	@Path("/getbyid")
	public Response getbyid(Map<String, Object> map) {
		Map<String, Object> o = new HashMap<>();
		try {
			Double id = (Double) map.get("id");
			String sid = new Integer(id.intValue()).toString();
			Pergunta p = PerguntaService.getbyid(sid);
			o.put("o", p);

			Response build = Response.ok().entity(o).build();
			return build;
		} catch (Exception e) {
			e.printStackTrace();
			o.put("erro", e.getMessage());
		}
		Response build = Response.ok().entity(o).build();
		return build;
	}

	@POST
	@Path("/cadastro")
	public Response post(Map<String, Object> map) {

		Gson gson = new Gson();
		Map o = ((Map) map.get("o"));

		Map<String, Object> mresponse = new HashMap<>();
		try {
			JsonElement jsonElement = gson.toJsonTree(o);
			Pergunta p = gson.fromJson(jsonElement, Pergunta.class);

			PerguntaService.save(p);

			mresponse.put("o", p);
		} catch (Exception e) {
			e.printStackTrace();
			mresponse.put("erro", e.getMessage());
		}
		Response build = Response.ok().entity(mresponse).build();
		return build;
	}

	@POST
	@Path("/remove")
	public Response delete(Map<String, Object> map) {
		Map<String, Object> o = new HashMap<>();
		try {
			Double id = (Double) map.get("id");
			String sid = new Integer(id.intValue()).toString();
			PerguntaService.remove(sid);
		} catch (Exception e) {
			e.printStackTrace();
			o.put("erro", e.getMessage());
		}
		Response build = Response.ok().entity(o).build();
		return build;

	}

}
