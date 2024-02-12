document.querySelectorAll(".nav-link").forEach((link) => {
	link.classList.remove('active');
	if (link.href === window.location.href) {
		link.classList.add("active");
		link.setAttribute("aria-current", "page");
	}
});

function deleteEmpresa(id) {
	const url = `${location.origin}/delete/${id}/`;
	Swal.fire({
		title: 'Eliminar Empresa?',
		text: "Se borraran tambien trabajdores asociados",
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#dc3545',
		confirmButtonText: 'Eliminar'
	}).then((result) => {
		if (result.isConfirmed) {
			window.location = url;
		} else
			return false;
	});
}

function deleteTrabajador(id) {
	const url = `${location.origin}/deleteTrabajador/${id}/`;
	Swal.fire({
		title: 'Eliminar Trabajador?',
		text: "Se eliminara toda la información",
		icon: 'warning',
		showCancelButton: true,
		confirmButtonColor: '#dc3545',
		confirmButtonText: 'Eliminar'
	}).then((result) => {
		if (result.isConfirmed) {
			window.location = url;
		} else
			return false;
	});
}

