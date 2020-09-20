package com.dinamica.transportePoA.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.dinamica.transportePoA.model.Linha;

public interface LinhaRepository extends CrudRepository<Linha, Integer> {

	@Query("from Linha l where upper(l.nome) like %:nome%")
	public List<Linha> containsName(@Param("nome") String nome);

	@Query("from Linha l where upper(l.nome) = :nome")
	public List<Linha> findByName(@Param("nome") String nome);

	@Query("from Linha l where upper(l.codigo) = :codigo")
	public List<Linha> findByCode(@Param("codigo") String codigo);
}
