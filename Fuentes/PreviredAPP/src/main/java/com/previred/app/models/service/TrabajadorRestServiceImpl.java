package com.previred.app.models.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.previred.app.controllers.models.entity.Trabajador;
import com.previred.app.controllers.models.entity.Response;

@Service
public class TrabajadorRestServiceImpl implements ITrabajadorRestService {

	@Value("${previred.url.trabajador.all}")
	private String urlGetTrabajadores;
	
	@Value("${previred.url.trabajador.by.id}")
	private String urlGetTrabajador;
	
	@Value("${previred.url.trabajador.delete.id}")
	private String urlDeleteTrabajador;
	
	@Value("${previred.url.trabajador.save}")
	private String urlSaveTrabajador;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<Trabajador> findAllTrabajadores() {

		List<Trabajador> trabajadores = new ArrayList<Trabajador>();

		try {
			ResponseEntity<List<Trabajador>> response = restTemplate.exchange(urlGetTrabajadores, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Trabajador>>() {
					});

			trabajadores = response.getBody();

		} catch (Exception e) {
			System.out.println(e);
		}

		return trabajadores;

	}

	@Override
	public Trabajador findById(Long id) {
		Trabajador empresa = null;
		String url = urlGetTrabajador + id;
		try {
			empresa = restTemplate.getForObject(url,Trabajador.class);
		} catch (Exception e) {
			System.out.println(e);
		}
		return empresa;
	}

	@Override
	public Response deleteById(Long id) {
		Response resp = null;
		String url = urlDeleteTrabajador + id;
		try {
			restTemplate.delete(url,Collections.singletonMap("id", id));
		} catch (Exception e) {
			System.out.println(e);
		}
		return resp;
	}

	@Override
	public Response saveTrabajador(Trabajador empresa) {
		Response resp = null;
		try {
			ResponseEntity<Response> result = restTemplate.postForEntity(urlSaveTrabajador, empresa, Response.class);
			resp = result.getBody();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return resp;
	}

}
