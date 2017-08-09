if (!window.pfaces) {
window.pfaces = {};
}

if (!pfaces.calendarControl) {
	pfaces.calendarControl = {};
}

pfaces.calendarControl.removeValue = function removeValue(obj){		
	document.getElementById(obj).value = "";
	
}
