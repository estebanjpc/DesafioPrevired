package com.previred.app.controllers.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.previred.app.controllers.models.dao.ITrabajadorDao;
import com.previred.app.controllers.models.entity.Trabajador;

@Service
public class TrabajadorServiceImpl implements ITrabajadorService {

	@Autowired
	private ITrabajadorDao trabajadorDao;

	@Override
	public List<Trabajador> findAll() {
		return (List<Trabajador>) trabajadorDao.findAll();
	}

	@Override
	public Trabajador findById(Long id) {
		return trabajadorDao.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		trabajadorDao.deleteById(id);
	}

	@Override
	public Trabajador save(Trabajador trabajador) {
		return trabajadorDao.save(trabajador);
	}

	@Override
	public Trabajador findByRut(String rut) {
		return trabajadorDao.findByRut(rut);
	}

}
