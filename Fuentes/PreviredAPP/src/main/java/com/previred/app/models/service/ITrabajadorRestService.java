package com.previred.app.models.service;

import java.util.List;

import com.previred.app.controllers.models.entity.Trabajador;
import com.previred.app.controllers.models.entity.Response;

public interface ITrabajadorRestService {

	
	public List<Trabajador> findAllTrabajadores();

	public Trabajador findById(Long id);

	public Response deleteById(Long id);

	public Response saveTrabajador(Trabajador empresa);
	
}
