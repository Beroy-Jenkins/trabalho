package br.projecao.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
@Data	
public class Pergunta implements Serializable {
	private static final long serialVerisionUID = 1L;
	private Long id;
	private String text;
	private List<Opcao> opcoes  = new ArrayList<Opcao>();
	
	public void addOpcao(Opcao o) {
		this.opcoes.add(o);
	}

	
}