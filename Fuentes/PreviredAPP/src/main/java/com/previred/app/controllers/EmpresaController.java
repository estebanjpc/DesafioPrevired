package com.previred.app.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.previred.app.controllers.models.entity.Empresa;
import com.previred.app.controllers.models.entity.Response;
import com.previred.app.models.service.IEmpresaRestService;
import com.previred.app.util.Util;

@Controller
@SessionAttributes("empresa")
public class EmpresaController {

	
	@Autowired
	IEmpresaRestService empresaRest;
	
	@Value("${previred.key}")
	private String key;	
	
	@GetMapping({"/","/empresas"})
	public String listadoEmpresas(Model model) {
		
		List<Empresa> empresas = empresaRest.findAllEmpresas();
		
		model.addAttribute("titulo","Listado Empresas");
		model.addAttribute("empresas",empresas);
		
		return "empresas";
	}
	
	@RequestMapping(value = {"/edit/{id}"})
	public String edit(@PathVariable(value = "id") Long id, Map<String, Object> model,RedirectAttributes flash) {
		
		Empresa empresa = empresaRest.findById(id);
		
		if(empresa == null) {
			model.put("msjLayout","error;No se encontro Empresa;No se encontro empresa en bd");
			return "redirect:empresas";
		}
		
		model.put("titulo","Editar Empresa");
		model.put("empresa",empresa);
		model.put("btn","Actualizar");
	
		
		return "empresa";
	}
	
	@RequestMapping(value = {"/delete/{id}"})
	public String delete(@PathVariable(value = "id") Long id, Map<String, Object> model,RedirectAttributes flash) {
		
		empresaRest.deleteById(id);
		flash.addFlashAttribute("msjLayout","success;Empresa Eliminada!;La empresa ha sido eliminada correctamente");
		
		return "redirect:/empresas/";
	}
	
	@RequestMapping(value = {"/crearEmpresa"})
	public String crear(Map<String, Object> model,RedirectAttributes flash) {
		
		Empresa empresa = new Empresa();
		
		model.put("titulo","Crear Empresa");
		model.put("empresa",empresa);
		model.put("btn","Crear");
		
		return "empresa";
	}
	
	
	
	@PostMapping("/guardarEmpresa")
	public String guardarUsuario(@Valid Empresa empresa, BindingResult result, Map<String, Object> model,
			RedirectAttributes flash, SessionStatus status) {

		model.put("titulo", "Editar Empresa");
		model.put("btn", "Actualizar");
		
		String msje = "Editado con Exito";
		
		if(empresa.getId() == null) {
			msje = "Creado con Exito";
			model.put("btn", "Crear");
		}

		empresa.setIdentificador(Util.identificador(empresa.getRazonSocial(),key));
		
		if (result.hasErrors()) return "empresa";
		
		Response resp = empresaRest.saveEmpresa(empresa);
		if(resp.getCode().equalsIgnoreCase("0")) {
			flash.addFlashAttribute("msjLayout","success;"+msje+"!;Empresa "+msje);			
		}else {
			model.put("msjLayout","error;Error Empresa;" + resp.getMessage());
			return "empresa";
		}
		
		return "redirect:empresas";
	}

}
