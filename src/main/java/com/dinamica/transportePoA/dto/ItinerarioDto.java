package com.dinamica.transportePoA.dto;

public class ItinerarioDto {

	private Integer id;

	private Integer idLinha;

	private Integer ordem;

	private Float latitude;

	private Float longitude;

	public ItinerarioDto() {
	}

	public ItinerarioDto(Integer id, Integer ordem, Float latitude, Float longitude) {
		this.setId(id);
		this.setOrdem(ordem);
		this.setLatitude(latitude);
		this.setLongitude(longitude);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdLinha() {
		return idLinha;
	}

	public void setIdLinha(Integer idLinha) {
		this.idLinha = idLinha;
	}

	public Integer getOrdem() {
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
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

}
