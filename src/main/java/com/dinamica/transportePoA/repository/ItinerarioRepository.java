package com.dinamica.transportePoA.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dinamica.transportePoA.model.Itinerario;

public interface ItinerarioRepository extends CrudRepository<Itinerario, Integer> {

	@Query("from Itinerario i where i.linha.id = :idLinha order by ordem")
	public List<Itinerario> findByLine(@Param("idLinha") Integer idLinha);

	@Query("select max(ordem) from Itinerario i where i.linha.id = :idLinha")
	public Integer getMaxItineraryOrder(@Param("idLinha") Integer idLinha);

}
