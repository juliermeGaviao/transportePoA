package com.dinamica.transportePoA.service;

import org.springframework.stereotype.Service;

@Service
public class GeodeticService {

	/**
	 * Calcula a distância, em Km, entre duas coordenadas geodésicas.
	 * 
	 * @param lat1  Latitude da primeira coordenada geodésica.
	 * @param lon1 Longitude da primeira coordenada geodésica.
	 * @param lat2  Latitude da segunda coordenada geodésica.
	 * @param lon2 Longitude da segunda coordenada geodésica.
	 * @return A distância em Km.
	 */
	public double getDistanceBetweenCoordinates(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2))
				+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
		dist = Math.acos(dist);
		dist = Math.toDegrees(dist);
		// 1 milha = 1.609344 quilômetros
		dist = dist * 60 * 1.1515 * 1.609344;

		return dist;
	}

}
