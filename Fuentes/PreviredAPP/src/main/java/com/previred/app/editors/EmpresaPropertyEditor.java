package com.previred.app.editors;

import java.beans.PropertyEditorSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.previred.app.models.service.IEmpresaRestService;

@Component
public class EmpresaPropertyEditor extends PropertyEditorSupport{
	
	@Autowired
	IEmpresaRestService empresaRest;
	
	public void setAsText(String id) throws IllegalArgumentException {
		try {
			this.setValue(empresaRest.findById(Long.parseLong(id)));				
		}catch(NumberFormatException e) {
			this.setValue(null);
		}
		
	}

}
