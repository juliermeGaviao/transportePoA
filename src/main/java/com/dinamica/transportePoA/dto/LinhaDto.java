package com.dinamica.transportePoA.dto;

import java.util.List;

public class LinhaDto {

    private Integer id;

    private String codigo;

    private String nome;

    private List<ItinerarioDto> itinerarios;

    public LinhaDto() {
    }

    public LinhaDto(Integer id, String codigo, String nome) {
    	this.setId(id);
    	this.setCodigo(codigo);
    	this.setNome(nome);
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<ItinerarioDto> getItinerarios() {
		return itinerarios;
	}

	public void setItinerarios(List<ItinerarioDto> itinerarios) {
		this.itinerarios = itinerarios;
	}

}
