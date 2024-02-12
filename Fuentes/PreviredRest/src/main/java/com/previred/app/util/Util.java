package com.previred.app.util;public class Util {

	private static int inicio = 60000000;
	
	
	public static boolean validaRut(String rut,boolean isEmpresa) {
		
		boolean flag = false;
		int rutE = 0;
	    try {
	        rut =  rut.toUpperCase();
	        rut = rut.replace(".", "");
	        rut = rut.replace("-", "");
	        int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));
	        rutE = rutAux;
	        char dv = rut.charAt(rut.length() - 1);

	        int m = 0, s = 1;
	        for (; rutAux != 0; rutAux /= 10) {
	            s = (s + rutAux % 10 * (9 - m++ % 6)) % 11;
	        }
	        if (dv == (char) (s != 0 ? s + 47 : 75)) {
	        	flag = true;
	        }

	        if(isEmpresa) {
	        	if(rutE < inicio) {
	        		return false;
	        	}
	        }
	        
	    } catch (java.lang.NumberFormatException e) {
	    } catch (Exception e) {
	    }
	    
	    return flag;
	}

	
	
}
