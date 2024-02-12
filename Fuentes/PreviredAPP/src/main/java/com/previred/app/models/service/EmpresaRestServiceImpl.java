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

import com.previred.app.controllers.models.entity.Empresa;
import com.previred.app.controllers.models.entity.Response;

@Service
public class EmpresaRestServiceImpl implements IEmpresaRestService {

	@Value("${previred.url.empresa.all}")
	private String urlGetEmpresas;
	
	@Value("${previred.url.empresa.by.id}")
	private String urlGetEmpresa;
	
	@Value("${previred.url.empresa.delete.id}")
	private String urlDeleteEmpresa;
	
	@Value("${previred.url.empresa.save}")
	private String urlSaveEmpresa;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<Empresa> findAllEmpresas() {

		List<Empresa> empresas = new ArrayList<Empresa>();

		try {
			ResponseEntity<List<Empresa>> response = restTemplate.exchange(urlGetEmpresas, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Empresa>>() {
					});

			empresas = response.getBody();

		} catch (Exception e) {
			System.out.println(e);
		}

		return empresas;

	}

	@Override
	public Empresa findById(Long id) {
		Empresa empresa = null;
		String url = urlGetEmpresa + id;
		try {
			empresa = restTemplate.getForObject(url,Empresa.class);
		} catch (Exception e) {
			System.out.println(e);
		}
		return empresa;
	}

	@Override
	public Response deleteById(Long id) {
		Response resp = null;
		String url = urlDeleteEmpresa + id;
		try {
			restTemplate.delete(url,Collections.singletonMap("id", id));
		} catch (Exception e) {
			System.out.println(e);
		}
		return resp;
	}

	@Override
	public Response saveEmpresa(Empresa empresa) {
		Response resp = null;
		try {
			ResponseEntity<Response> result = restTemplate.postForEntity(urlSaveEmpresa, empresa, Response.class);
			resp = result.getBody();
		} catch (Exception e) {
			System.out.println(e);
		}
		
		return resp;
	}

}
