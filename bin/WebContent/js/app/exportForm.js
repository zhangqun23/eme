function getExportFlagCookie() {
	var cookie = {};
	var cookies = document.cookie;
	if (cookies === "")
		return cookie;
	var list = cookies.split(";");
	for (var i = 0; i < list.length; i++) {
		var cookieString = list[i];
		var p = cookieString.indexOf("=");
		var name = cookieString.substring(0, p);
		if (name.trim() != "exportFlag") {
			continue;
		}
		var value = cookieString.substring(p + 1, cookieString.length);
		if (value == "1") {
			$(".tipLoading").fadeOut(200);
			$(".overlayer").fadeOut(200);
			clearInterval(id);
			document.cookie = "exportFlag=''; path=/";
		}
		break;
	}
}
// 设置cookie
function setExportFlagCookie() {
	document.cookie = "exportFlag=" + "0" + "; path=/";
	$(".tipLoading").fadeIn(200);
	$(".overlayer").fadeIn(200);
	id = setInterval(getExportFlagCookie, 500);
}