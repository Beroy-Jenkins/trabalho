package br.projecao.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

	
}
