package com.previred.app.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.previred.app.controllers.models.entity.Empresa;
import com.previred.app.controllers.models.service.IEmpresaService;
import com.previred.app.util.Util;


@RestController
@RequestMapping("/api")
public class EmpresaRestController {

	@Autowired
	private IEmpresaService empresaService;
	
	@GetMapping("/getEmpresas/")
	public ResponseEntity<?> getEmpresas() {

		Map<String, Object> response = new HashMap<>();
		List<Empresa> listado = null;
		try {
			
			listado = empresaService.findAll();
			
		} catch (DataAccessException e) {
			response.put("code", "1");
			response.put("message", "Error al realizar la consulta a la bd");
			response.put("error", e.getMessage() + "::" + e.getMostSpecificCause());
			response.put("trace", e);
			System.out.println(e);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (listado == null || listado.size() == 0) {
			response.put("code", "1");
			response.put("message", "No se encontraron datos empresas en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<List<Empresa>>(listado, HttpStatus.OK);
	}
	
	@GetMapping("/getEmpresa/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Empresa empresa = null;
		Map<String, Object> response = new HashMap<>();

		try {
			empresa = empresaService.findById(id);
		} catch (DataAccessException e) {
			response.put("code", "1");
			response.put("message", "Error al realizar la consulta a la bd");
			response.put("error", e.getMessage() + "::" + e.getMostSpecificCause());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (empresa == null) {
			response.put("code", "1");
			response.put("message", "La Empresa ID: " + id + " no existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Empresa>(empresa, HttpStatus.OK);
	}
	
	@DeleteMapping("/empresa/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Empresa empresa = null;
		Map<String, Object> response = new HashMap<>();
		try {
			empresa = empresaService.findById(id);
			if(null == empresa) {
				response.put("code", "1");
				response.put("message", "La empresa con ID : "+ id + " no existe en la bd");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			empresaService.delete(id);			
		} catch (DataAccessException e) {
			response.put("code", "1");
			response.put("message", "Error al realizar la eliminación a la bd");
			response.put("error", e.getMessage() + "::" + e.getMostSpecificCause());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("code", "0");
		response.put("message", "La empresa ID: "+ id + " sido eliminado con exito");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@PostMapping("/empresa")
	public ResponseEntity<?> create(@Valid @RequestBody Empresa empresa, BindingResult result) {
		Empresa aux = null;
		Map<String, Object> response = new HashMap<>();
		
		List<String> errors = new ArrayList<String>();
		if(result.hasErrors()) {
			for (FieldError err : result.getFieldErrors()) {
				errors.add("El campo '" + err.getField() + "' " + err.getDefaultMessage());
			}
			response.put("code", "1");
			response.put("error", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		}
		
		try {
			if(Util.validaRut(empresa.getRut(),true)) {
				aux = empresaService.findByRut(empresa.getRut());
				if(aux == null || aux.getId() == empresa.getId()) {
					empresaService.save(empresa);									
				}else {
					response.put("code", "1");
					response.put("message", "rut empresa ya existe en bd");
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
				}
			}else {
				response.put("code", "1");
				response.put("message", "rut empresa es incorrecto");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
			}

		}catch (DataAccessException e) {
			response.put("code", "1");
			response.put("message", "Error al realizar la consulta a la bd");
			response.put("error", e.getMessage() + "::" + e.getMostSpecificCause());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		}catch(Exception e) {
			response.put("code", "1");
			response.put("message", "Ha ocurrido un error inesperado");
			response.put("error", e.getMessage() + "::" + e.getCause());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
		}
		
		response.put("code", "0");
		response.put("message", "La empresa ha sido creado con exito");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@PutMapping("empresa/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Empresa empresa, BindingResult result,@PathVariable Long id) {

		Empresa empresaUpdate= null;
		Map<String, Object> response = new HashMap<>();
		
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(
					err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("code", "1");
			response.put("error", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		

		Empresa empresaActual = empresaService.findById(id);
		if (empresaActual == null) {
			response.put("code", "1");
			response.put("message", "La empresa ID: " + id + " no existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			empresaActual.setRut(empresa.getRut());
			empresaActual.setRazonSocial(empresa.getRazonSocial());
			empresaActual.setIdentificador(empresa.getIdentificador());
			empresaUpdate = empresaService.save(empresaActual);
		} catch (DataAccessException e) {
			response.put("message", "Error al realizar la consulta a la bd");
			response.put("error", e.getMessage() + "::" + e.getMostSpecificCause());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("code", "0");
		response.put("message", "La empresa ha sido actualizado con exito");
		response.put("empresa", empresaUpdate);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
}
