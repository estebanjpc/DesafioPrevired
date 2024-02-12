package com.previred.app.controllers.models.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.previred.app.controllers.models.dao.IEmpresaDao;
import com.previred.app.controllers.models.entity.Empresa;

@Service
public class EmpresaServiceImpl implements IEmpresaService {

	@Autowired
	private IEmpresaDao empresaDao;

	@Override
	public List<Empresa> findAll() {
		return (List<Empresa>) empresaDao.findAll();
	}

	@Override
	public Empresa findById(Long id) {
		return empresaDao.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		empresaDao.deleteById(id);
	}

	@Override
	public Empresa save(@Valid Empresa empresa) {
		return empresaDao.save(empresa);
	}

	@Override
	public Empresa findByRut(String rut) {
		return empresaDao.findByRut(rut);
	}

}
