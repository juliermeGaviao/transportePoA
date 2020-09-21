package com.dinamica.transportePoA.dto;

public class PontoTaxiDto {

	private String nome;

	private Float latitude;

	private Float longitude;

	private String dataCadastro;

	public PontoTaxiDto() {
	}

	public PontoTaxiDto(String nome, Float latitude, Float longitude) {
		this.setNome(nome);
		this.setLatitude(latitude);
		this.setLongitude(longitude);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Float getLatitude() {
		return latitude;
	}

	public void setLatitude(Float latitude) {
		this.latitude = latitude;
	}

	public Float getLongitude() {
		return longitude;
	}

	public void setLongitude(Float longitude) {
		this.longitude = longitude;
	}

	public String getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(String dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

}
