package com.dinamica.transportePoA.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.dinamica.transportePoA.model.Linha;

public interface LinhaRepository extends Repository<Linha, Integer> {

	@Query("from Linha l where upper(l.nome) like %:nome%")
	public List<Linha> findByName(@Param("nome") String nome);
}
