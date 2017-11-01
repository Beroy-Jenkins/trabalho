package br.projecao.domain;

import java.io.Serializable;

import lombok.Data;

@Data	
public class Usuario implements Serializable {
	private Long id;
	private String login;
	private String senha;
}