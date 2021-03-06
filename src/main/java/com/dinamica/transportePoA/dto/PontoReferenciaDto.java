package com.dinamica.transportePoA.dto;

public class PontoReferenciaDto {

	private Float latitude;

	private Float longitude;

	private Float raio;

    public PontoReferenciaDto() {
    }

    public PontoReferenciaDto(Float latitude, Float longitude, Float raio) {
    	this.setLatitude(latitude);
    	this.setLongitude(longitude);
    	this.setRaio(raio);
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

	public Float getRaio() {
		return raio;
	}

	public void setRaio(Float raio) {
		this.raio = raio;
	}

}
