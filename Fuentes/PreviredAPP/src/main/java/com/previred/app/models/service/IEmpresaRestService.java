package com.previred.app.models.service;

import java.util.List;

import com.previred.app.controllers.models.entity.Empresa;
import com.previred.app.controllers.models.entity.Response;

public interface IEmpresaRestService {

	
	public List<Empresa> findAllEmpresas();

	public Empresa findById(Long id);

	public Response deleteById(Long id);

	public Response saveEmpresa(Empresa empresa);
	
}
