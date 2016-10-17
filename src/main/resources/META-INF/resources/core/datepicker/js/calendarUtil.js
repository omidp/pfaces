if (!window.Richfaces) {
window.Richfaces = {};
}

if (!Richfaces.calendarControl) {
	Richfaces.calendarControl = {};
}

Richfaces.calendarControl.removeValue = function removeValue(obj){		
	document.getElementById(obj).value = "";
	
}