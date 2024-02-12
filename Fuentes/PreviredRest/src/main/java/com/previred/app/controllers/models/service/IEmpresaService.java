package com.previred.app.controllers.models.service;

import java.util.List;

import javax.validation.Valid;

import com.previred.app.controllers.models.entity.Empresa;

public interface IEmpresaService {

	
	public List<Empresa> findAll();

	public Empresa findById(Long id);

	public void delete(Long id);

	public Empresa save(@Valid Empresa empresa);

	public Empresa findByRut(String rut);
}
