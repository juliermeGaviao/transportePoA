package com.dinamica.transportePoA.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.dinamica.transportePoA.model.Itinerario;

public interface ItinerarioRepository extends Repository<Itinerario, Integer> {

	@Query("from Itinerario i where i.linha.id = :id order by ordem")
	public List<Itinerario> findByLine(@Param("id") Integer id);

}
