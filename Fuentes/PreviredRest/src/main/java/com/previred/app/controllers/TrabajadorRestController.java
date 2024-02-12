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
import com.previred.app.controllers.models.entity.Trabajador;
import com.previred.app.controllers.models.service.IEmpresaService;
import com.previred.app.controllers.models.service.ITrabajadorService;
import com.previred.app.util.Util;


@RestController
@RequestMapping("/api")
public class TrabajadorRestController {

	@Autowired
	private ITrabajadorService trabajadorService;
	
	@Autowired
	private IEmpresaService empresaService;
	
	@GetMapping("/getTrabajadores/")
	public ResponseEntity<?> getTrabajadors() {

		Map<String, Object> response = new HashMap<>();
		List<Trabajador> listado = null;
		try {
			
			listado = trabajadorService.findAll();
			
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
			response.put("message", "No se encontraron datos trabajadores en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<List<Trabajador>>(listado, HttpStatus.OK);
	}
	
	@GetMapping("/getTrabajador/{id}")
	public ResponseEntity<?> show(@PathVariable Long id) {
		Trabajador trabajador = null;
		Map<String, Object> response = new HashMap<>();

		try {
			trabajador = trabajadorService.findById(id);
		} catch (DataAccessException e) {
			response.put("code", "1");
			response.put("message", "Error al realizar la consulta a la bd");
			response.put("error", e.getMessage() + "::" + e.getMostSpecificCause());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (trabajador == null) {
			response.put("code", "1");
			response.put("message", "La Trabajador ID: " + id + " no existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Trabajador>(trabajador, HttpStatus.OK);
	}
	
	@DeleteMapping("/trabajador/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Trabajador trabajador = null;
		Map<String, Object> response = new HashMap<>();
		try {
			trabajador = trabajadorService.findById(id);
			if(null == trabajador) {
				response.put("code", "1");
				response.put("message", "El trabajador ID : "+ id + " no existe en la bd");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}
			trabajadorService.delete(id);			
		} catch (DataAccessException e) {
			response.put("code", "1");
			response.put("message", "Error al realizar la eliminación a la bd");
			response.put("error", e.getMessage() + "::" + e.getMostSpecificCause());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("code", "0");
		response.put("message", "El trabajador ID: "+ id + " sido eliminado con exito");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@PostMapping("/trabajador")
	public ResponseEntity<?> create(@Valid @RequestBody Trabajador trabajador, BindingResult result) {
		Trabajador aux = null;
		Empresa emp = null;
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
			if(Util.validaRut(trabajador.getRut(),false)) {
				aux = trabajadorService.findByRut(trabajador.getRut());
				if(aux == null || aux.getId() == trabajador.getId()) {
					emp = empresaService.findById(trabajador.getEmpresa().getId());
					if(emp != null) {
						trabajadorService.save(trabajador);											
					}else {
						response.put("code", "1");
						response.put("message", "empresa ingresada NO existe en bd");
						return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
					}
				}else {
					response.put("code", "1");
					response.put("message", "rut ingresado ya existe en bd");
					return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
				}
			}else {
				response.put("code", "1");
				response.put("message", "rut ingresado es incorrecto");
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
		response.put("message", "El trabajador ha sido creado con exito");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@PutMapping("trabajador/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody Trabajador trabajador, BindingResult result,@PathVariable Long id) {

		Trabajador trabajadorUpdate= null;
		Map<String, Object> response = new HashMap<>();
		
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors().stream().map(
					err -> "El campo '" + err.getField() + "' " + err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("code", "1");
			response.put("error", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		

		Trabajador trabajadorActual = trabajadorService.findById(id);
		if (trabajadorActual == null) {
			response.put("code", "1");
			response.put("message", "La trabajador ID: " + id + " no existe en la base de datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		try {
			trabajadorActual.setRut(trabajador.getRut());
			trabajadorActual.setNombre(trabajador.getNombre());
			trabajadorActual.setApellidoPaterno(trabajador.getApellidoPaterno());
			trabajadorActual.setApellidoMaterno(trabajador.getApellidoMaterno());
			trabajadorActual.setDireccion(trabajador.getDireccion());
			trabajadorUpdate = trabajadorService.save(trabajadorActual);
		} catch (DataAccessException e) {
			response.put("message", "Error al realizar la consulta a la bd");
			response.put("error", e.getMessage() + "::" + e.getMostSpecificCause());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("code", "0");
		response.put("message", "La trabajador ha sido actualizado con exito");
		response.put("trabajador", trabajadorUpdate);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
}
