package com.previred.app.controllers.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.previred.app.controllers.models.entity.Trabajador;

public interface ITrabajadorDao  extends CrudRepository<Trabajador, Long>{

	@Query("select t from Trabajador t where t.rut = ?1")
	public Trabajador findByRut(String rut);

}
