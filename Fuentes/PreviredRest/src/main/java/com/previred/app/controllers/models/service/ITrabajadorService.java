package com.previred.app.controllers.models.service;

import java.util.List;

import com.previred.app.controllers.models.entity.Trabajador;

public interface ITrabajadorService {

	
	public List<Trabajador> findAll();

	public Trabajador findById(Long id);

	public void delete(Long id);

	public Trabajador save(Trabajador trabajador);

	public Trabajador findByRut(String rut);
}
