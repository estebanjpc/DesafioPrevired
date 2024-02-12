package com.previred.app.controllers.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.previred.app.controllers.models.entity.Empresa;

public interface IEmpresaDao  extends CrudRepository<Empresa, Long>{

	
	@Query("select e from Empresa e where e.rut = ?1")
	public Empresa findByRut(String rut);

}
