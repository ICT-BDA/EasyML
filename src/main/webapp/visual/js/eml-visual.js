/**
 * Page switching navigation
 * 
 * @param obj
 */
function navSelect(obj) {
	var objId = obj.id;
	if (objId == "data_link") {
		$(".active").removeClass("active");
		$("#data_nav").addClass("active");
		$(".main-content").load("tables.html" + ' #data_cont', function(result) {
			$result = $(result);
			$result.find("script").appendTo(".main-content")
		}).fadeIn('slow');

	} else if (objId == "statics_link") {
		$(".active").removeClass("active");
		$("#statics_nav").addClass("active");
		$(".main-content").load("statistics.html" + ' #statics_cont', function(result) {
			$result = $(result);
			$result.find("script").appendTo(".main-content")
		}).fadeIn('slow');
	} else {
		$(".active").removeClass("active");
		$("#charts_nav").addClass("active");
		$(".main-content").empty();
		$(".main-content").load("charts.html" + ' #chart_cont', function(result) {
			$result = $(result);
			$result.find("script").appendTo(".main-content")
		}).fadeIn('slow');

	}
}

/**
 * Encryption
 * @param word
 * @returns {*}
 */
function encrypt(word){
    var key = CryptoJS.enc.Utf8.parse("bdaictvisualabcd");
    var srcs = CryptoJS.enc.Utf8.parse(word);
    var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
    return encrypted.toString();
}

/**
 * Decryption
 * @param word
 * @returns {*}
 */
function decrypt(word){
    var key = CryptoJS.enc.Utf8.parse("bdaictvisualabcd");
    var decrypt = CryptoJS.AES.decrypt(word, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
    return CryptoJS.enc.Utf8.stringify(decrypt).toString();
}

/**
 * Extract the parameter value in the URL
 * 
 * @param name  parameter name
 * @returns
 */
function getUrlParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); 
	var r = window.location.search.substr(1).match(reg); 
	if (r != null) return decrypt(unescape(r[2])); return null;
}

/**
 * Extract the application name path in the URL as a prefix of the restful api
 * 
 * @returns
 */
function getAppPath(){
	var strPath = window.document.location.pathname; 
	var appPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
	return appPath;
}

