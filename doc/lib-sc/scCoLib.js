


var scCoLib = {
	fDebug : false,
	fOnLoadDone:false, 
	fOnUnloadDone:false
}




var scOnLoads = [];




var scOnUnloads = [];


function scOnLoad() {
	scOnLoads.sort(function (p1, p2){
			if(!p1.loadSortKey) return p2.loadSortKey ? -1 : 0;
			if(scCoLib.isIE) return p1.loadSortKey.localeCompare(p2.loadSortKey||"");
			try{
				return p1.loadSortKey > (p2.loadSortKey||"") ? 1 : p1.loadSortKey == p2.loadSortKey ? 0 : -1;
			}catch(e){
				return p1.loadSortKey.localeCompare(p2.loadSortKey||"");
			}
		}
	);	for (var i=0; i<scOnLoads.length; i++) try{scOnLoads[i].onLoad();}catch(e){}
	scCoLib.fOnLoadDone = true;
}


function scOnUnload() {
	scOnUnloads.sort(function (p1, p2){
			if(!p1.unloadSortKey) return p2.unloadSortKey ? -1 : 0;
			if(scCoLib.isIE) return p1.unloadSortKey.localeCompare(p2.unloadSortKey||"");
			try{
				return p1.unloadSortKey > (p2.unloadSortKey||"") ? 1 : p1.unloadSortKey == p2.unloadSortKey ? 0 : -1;
			}catch(e){
				return p1.unloadSortKey.localeCompare(p2.unloadSortKey||"");
			}
		}
	);
	for (var i=0; i<scOnUnloads.length; i++) try{scOnUnloads[i].onUnload();}catch(e){}
	scCoLib.fOnUnloadDone = true;
}




var scOnResizes = [];


function scOnResize(pEvent) {
	var vLen = scOnResizes.length;
	if(vLen > 1 && vLen != window.onresize.lastLen) {
		scOnResizes.sort(function (p1, p2){
				if(!p1.resizeSortKey) return p2.resizeSortKey ? -1 : 0;
				if(scCoLib.isIE) return p1.resizeSortKey.localeCompare(p2.resizeSortKey||"");
				try{
					return p1.resizeSortKey > p2.resizeSortKey||"" ? 1 : p1.resizeSortKey == p2.resizeSortKey ? 0 : -1;
				}catch(e){
					return p1.resizeSortKey.localeCompare(p2.resizeSortKey||"");
				}
			}
		);
		window.onresize.lastLen = vLen;
	}
	for (var i =0; i < vLen; i++) try{scOnResizes[i].onResize(pEvent);}catch(e){}
}


window.onload = scOnLoad;
window.onunload = scOnUnload;
window.onresize = scOnResize;


function sc$(pId) {return document.getElementById(pId);}
function $(pId) {return sc$(pId);}


scCoLib.addOnLoadHandler = function(pHanlder){
	if(scCoLib.fOnLoadDone) try{pHanlder.onLoad();}catch(e){}
	else scOnLoads[scOnLoads.length] = pHanlder;
}


scCoLib.addOnUnloadHandler = function(pHanlder){
	if(scCoLib.fOnUnloadDone) try{pHanlder.onUnload();}catch(e){}
	else scOnUnloads[scOnUnloads.length] = pHanlder;
}


scCoLib.userAgent = navigator.userAgent.toLowerCase();
scCoLib.isIE = scCoLib.userAgent.indexOf("msie")!=-1;


scCoLib.toInt = function(pX){
	var vY;
	return isNaN(vY = parseInt(pX))? 0 : vY;
}

scCoLib.hrefBase = function(pHref){
	var vHref = pHref || window.location.href;
	if (vHref.indexOf("?")>-1) vHref = vHref.substring(0,vHref.indexOf("?"));
	if (vHref.indexOf("#")>-1) vHref = vHref.substring(0,vHref.indexOf("#"));
	return vHref;
}

scCoLib.log = function(pMsg) {
	if (!scCoLib.fDebug) return;
	if(window.console) {
		window.console.log(pMsg);
	} else if (scCoLib.fScConsole){
		var vMsgDiv = document.createElement("div");
		vMsgDiv.innerHTML = pMsg;
		scCoLib.fScConsole.appendChild(vMsgDiv);
	}
}

function ScLoad(pScLoadParams) {
	if (pScLoadParams) {
		this.fPathToRoot = pScLoadParams.pathToRoot;
		this.fRootUrl = scCoLib.hrefBase().substring(0, scCoLib.hrefBase().length - pScLoadParams.destUri.length);
		this.fFrameId = pScLoadParams.frameId;
		var vSkinLoc = pScLoadParams.skinLoc;
		if (vSkinLoc && vSkinLoc != "/skin") {
			if (vSkinLoc.lastIndexOf("url:", 0) == 0) this.fSkinAbsLoc = vSkinLoc.substring(4);
			else this.fSkinRelLoc = vSkinLoc;
		}
		var vLibScLoc = pScLoadParams.libScLoc;
		if (vLibScLoc && vLibScLoc != "/lib-sc") {
			if (vLibScLoc.lastIndexOf("url:", 0) == 0) this.fLibScAbsLoc = vLibScLoc.substring(4);
			else this.fLibScRelLoc = vLibScLoc;
		}
		var vLibMdLoc = pScLoadParams.libMdLoc;
		if (vLibMdLoc && vLibMdLoc != "/lib-md") {
			if (vLibMdLoc.lastIndexOf("url:", 0) == 0) this.fLibMdRelLoc = vLibMdLoc.substring(4);
			else this.fLibMdRelLoc = vLibMdLoc;
		}
	} else {
		this.fPathToRoot = "";
		this.fRootUrl = scCoLib.hrefBase().substring(0, scCoLib.hrefBase().lastIndexOf("/"));
	}
	this.fRootOffset = this.fRootUrl.length + 1;
}
ScLoad.prototype = {
 /** 
  * Charge un document dans la frame cible à partir d'une url relative à la racine du site publié. 
  * Utilisable uniquement pour pointer une ressource dynamique du site publié (co, res).
  */
 loadFromRoot : function(pUrlFromRoot){
  if(this.fFrameId) sc$(this.fFrameId).src = this.getUrlFromRoot(pUrlFromRoot);
  else window.location.href = this.getUrlFromRoot(pUrlFromRoot);
 },
 
 /** 
  * Retourne une url relative à la racine du site publié.
  * Utilisable uniquement pour pointer une ressource dynamique du site publié (co, res).
  */
 getUrlFromRoot : function(pHref){return pHref.substring(this.fRootOffset)},
 
 /** 
  * Retourne une url complète à partir d'une url relative à la racine du site publié.
  * Utilisable uniquement pour pointer une ressource dynamique du site publié (co, res).
  */
 getPathFromRoot : function(pUrlFromRoot){return this.fPathToRoot + pUrlFromRoot},
 
 /** 
  * Retourne l'url absolue racine du site publié.
  */
 getRootUrl : function(){return this.fRootUrl},
 
 /** 
  * Retourne une url vers une ressource quelconque (skin, site, lib-sc, lib-md, res, ou co).
  */
 resolveDestUri : function(pDestUri){
   if(pDestUri.lastIndexOf("/skin/", 0)==0) {
      if(this.fSkinRelLoc) return this.fRootUrl + this.fSkinRelLoc + pDestUri.substring(5);
      if(this.fSkinAbsLoc) return this.fSkinAbsLoc + pDestUri.substring(5);
   }
   if(pDestUri.lastIndexOf("/lib-sc/", 0)==0) {
      if(this.fLibScRelLoc) return this.fRootUrl + this.fLibScRelLoc + pDestUri.substring(7);
      if(this.fLibScAbsLoc) return this.fLibScAbsLoc + pDestUri.substring(7);
   }
   if(pDestUri.lastIndexOf("/lib-md/", 0)==0) {
      if(this.fLibMdRelLoc) return this.fRootUrl + this.fLibMdRelLoc + pDestUri.substring(7);
      if(this.fLibMdAbsLoc) return this.fLibMdAbsLoc + pDestUri.substring(7);
   }
   return this.fRootUrl + pDestUri;
 }
}
if (!scServices.scLoad) scServices.scLoad = new ScLoad(scLoadParams);


scCoLib.util = {
	logError : function(pPre, pEx) {
		var vMsg = pPre + ((pEx != null) ? " - "+((typeof pEx.message != "undefined") ? pEx.message : pEx) : "");
		scCoLib.log("scCoLib.util.logError() DEPRECEATED - "+vMsg);
	},
	log : function(pMsg) {
		scCoLib.log("scCoLib.util.log() DEPRECEATED - "+pMsg);
	}
}
