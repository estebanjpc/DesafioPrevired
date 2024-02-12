package com.previred.app.controllers;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.previred.app.controllers.models.entity.Empresa;
import com.previred.app.controllers.models.entity.Response;
import com.previred.app.controllers.models.entity.Trabajador;
import com.previred.app.editors.EmpresaPropertyEditor;
import com.previred.app.models.service.IEmpresaRestService;
import com.previred.app.models.service.ITrabajadorRestService;

@Controller
@SessionAttributes("trabajador")
public class TrabajadorController {

	
	@Autowired
	ITrabajadorRestService trabajadorRest;
	
	@Autowired
	IEmpresaRestService empresaRest;
	
	@Autowired
	EmpresaPropertyEditor empresaEditor;
	
	@Value("${previred.key}")
	private String key;	
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Empresa.class,"empresa",empresaEditor);
	}
	
	@GetMapping({"/trabajadores"})
	public String listadoTrabajadores(Model model) {
		
		List<Trabajador> trabajadores = trabajadorRest.findAllTrabajadores();
		
		model.addAttribute("titulo","Listado Trabajadores");
		model.addAttribute("trabajadores",trabajadores);
		
		return "trabajadores";
	}
	
	@RequestMapping(value = {"/editTrabajador/{id}"})
	public String edit(@PathVariable(value = "id") Long id, Map<String, Object> model,RedirectAttributes flash) {
		
		Trabajador trabajador = trabajadorRest.findById(id);
		
		if(trabajador == null) {
			model.put("msjLayout","error;No se encontro Trabajador;No se encontro trabajador en bd");
			return "redirect:trabajadores";
		}
		
		model.put("titulo","Editar Trabajador");
		model.put("trabajador",trabajador);
		model.put("btn","Actualizar");
	
		
		return "trabajador";
	}
	
	@RequestMapping(value = {"/deleteTrabajador/{id}"})
	public String delete(@PathVariable(value = "id") Long id, Map<String, Object> model,RedirectAttributes flash) {
		
		trabajadorRest.deleteById(id);
		flash.addFlashAttribute("msjLayout","success;Trabajador Eliminado!;La trabajador ha sido eliminada correctamente");
		
		return "redirect:/trabajadores/";
	}
	
	@RequestMapping(value = {"/crearTrabajador"})
	public String crear(Map<String, Object> model,RedirectAttributes flash) {
		
		Trabajador trabajador = new Trabajador();
		
		model.put("titulo","Crear Trabajador");
		model.put("trabajador",trabajador);
		model.put("btn","Crear");
		
		return "trabajador";
	}
	
	
	
	@PostMapping("/guardarTrabajador")
	public String guardarUsuario(@Valid Trabajador trabajador, BindingResult result, Map<String, Object> model,
			RedirectAttributes flash, SessionStatus status) {

		model.put("titulo", "Editar Trabajador");
		model.put("btn", "Actualizar");
		
		String msje = "Editado con Exito";
		
		if(trabajador.getId() == null) {
			msje = "Creado con Exito";
			model.put("btn", "Crear");
		}

		if (result.hasErrors()) return "trabajador";
		
		if(trabajador.getEmpresa().getId() == null) {
			model.put("msjLayout","error;Seleccione Empresa;Debe seleccionar una empresa para el trabajdor");
			return "trabajador";
		}
		
		Response resp = trabajadorRest.saveTrabajador(trabajador);
		if(resp.getCode().equalsIgnoreCase("0")) {
			flash.addFlashAttribute("msjLayout","success;"+msje+"!;Trabajador "+msje);			
		}else {
			model.put("msjLayout","error;Error Trabajador;" + resp.getMessage());
			return "trabajador";
		}
		
		return "redirect:trabajadores";
	}
	
	@ModelAttribute("empresas")
	public List<Empresa>listadoEmpresas(){
		return empresaRest.findAllEmpresas(); 
	}
}
