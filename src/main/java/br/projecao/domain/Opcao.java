package br.projecao.domain;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;


@Data	
public class Opcao implements Serializable {
	private static final long serialVersionUID = -8861636218458516279L;
	private Long id;
	private String text;
	private Long idPergunta;
	private Boolean correta = false;
	
//	private Pergunta pergunta;
}