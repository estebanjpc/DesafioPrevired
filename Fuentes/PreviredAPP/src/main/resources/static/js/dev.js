$(document).ready(function() {
	
	var id = document.getElementById("idEmpresa").value;
	document.getElementById('cbEmpresa').selectedIndex = getIndexCB('#cbEmpresa > option',id);
	
});

function getIndexCB(selector,id){
	var index = 0;
	var i = 0;
	
	$(selector).each(function() {
		if(this.value === id){
	    	i = index;
	    } 
	    index++;
	});

	return i;
}