var dokielMgr = {

	fPathPlayer : ["des:.stepListPlayer"],
	fPathSteps : "chi:ol/chi:.step",
	fPathOp : "anc:.stepList",
	fClsPreSlp : "slp",
	fPathFocusables : "des:a|input|button",

	fScreens : [],
	fPathScreen : ["des:.screenFra"],
	fPathZones : "des:.screenZone",
	fPathZoneList : "des:ul.screenZones",
	fPathZoneLnks : "des:a.screenZoneLnk",
	fPathTitleFra : "des:div.screenTitles",
	fPathZoneTitles : "des:div.screenTitles/des:li",
	fClsPreScr : "scr"
}
dokielMgr.fStrings = ["Pas à pas","Afficher les étapes une à une",
/*02*/                "Retour","Retourner au contenu",
/*04*/                "précédent","étape précédente",
/*06*/                "suivant","étape suivante",
/*08*/                "Etape : ","",
/*10*/                "Mode liste","Afficher les zones en liste",
/*12*/                "Mode interactif","Afficher les zones en mode interactif",
                      "",""];

/** dokielMgr.init */
dokielMgr.init = function() {
	try{
		// Init players...
		this.fNavie6 = scCoLib.isIE && parseFloat(scCoLib.userAgent.substring(scCoLib.userAgent.indexOf("msie")+5)) < 7;
		
		for(var i=0; i<this.fPathScreen.length; i++) {
			var vScreens = scPaLib.findNodes(this.fPathScreen[i]);
			for(var j=0; j<vScreens.length; j++) {
				var vScreen = vScreens[j];
				vScreen.fZones = scPaLib.findNodes(this.fPathZones,vScreen);
				for(var k=0; k<vScreen.fZones.length; k++) {
					var vZone = vScreen.fZones[k];
					vZone.style.visibility = "hidden";
					vZone.style.position = "absolute";
					vZone.style.left = "-20000px";
					vZone.style.top = "-20000px";
				}
				this.fScreens.push(vScreen);
			}
		}
		scOnLoads[scOnLoads.length] = this;
	}catch(e){scCoLib.log("ERROR - dokielMgr.init : "+e)}
}
/** dokielMgr.onLoad */
dokielMgr.onLoad = function() {
	this.xIniPlayers();
	this.xIniScreens();
}
/** dokielMgr.registerPlayer */
dokielMgr.registerPlayer = function(pPath) {
	this.fPathPlayer[this.fPathPlayer.length] = pPath;
}
/** dokielMgr.registerScreen */
dokielMgr.registerScreen = function(pPath) {
	this.fPathScreen[this.fPathScreen.length] = pPath;
}
/** dokielMgr.cancelStepMode */
dokielMgr.cancelStepMode = function() {
	if (this.fActiveStepPlayer) this.xSlpLstMode(this.fActiveStepPlayer);
}

/** dokielMgr.setInteractiveScreenMode */
dokielMgr.setInteractiveScreenMode = function(pEnabled) {
	for(var i=0; i<this.fScreens.length; i++) {
		var vScreen = this.fScreens[i];
		if (!pEnabled){
			if (vScreen.fIntMode) {
				this.xScrLstMode(vScreen);
				vScreen.fLstModeForced = true;
			}
			vScreen.fToolbar.style.display = "none";
		} else {
			if (vScreen.fLstModeForced) {
				this.xScrIntMode(vScreen);
				vScreen.fLstModeForced = false;
			}
			vScreen.fToolbar.style.display = "";
		}
	}
}

/** dokielMgr.xBtnMgr - centralized button manager */
dokielMgr.xBtnMgr = function(pBtn) {
	var vFra = pBtn.fFra;
	switch(pBtn.fName){
		case this.fClsPreSlp + "BtnStp":
			dokielMgr.xSlpStpMode(vFra);
			dokielMgr.xToggleFocusables(vFra.parentNode);
			vFra.fBtnNxt.focus();
			break;
		case this.fClsPreSlp + "BtnLst":
			dokielMgr.xSlpLstMode(vFra);
			dokielMgr.xToggleFocusables(vFra.parentNode);
			vFra.fBtnStep.focus();
			break;
		case this.fClsPreSlp + "BtnPrv":
			dokielMgr.xSlpPrv(vFra);break;
		case this.fClsPreSlp + "BtnNxt":
			dokielMgr.xSlpNxt(vFra);break;

		case this.fClsPreScr + "BtnLst":
			dokielMgr.xScrLstMode(vFra);break;
		case this.fClsPreScr + "BtnAct":
			dokielMgr.xScrIntMode(vFra);break;
	}
	return false;
}

/* ============================== Screen ============================== */
/** dokielMgr.xIniScreens */
dokielMgr.xIniScreens = function() {
	try{
		for(var i=0; i<this.fScreens.length; i++) {
			var vScreen = this.fScreens[i];
			vScreen.fZoneLnks = scPaLib.findNodes(this.fPathZoneLnks,vScreen);
			vScreen.fTitleFra = scPaLib.findNode(this.fPathTitleFra,vScreen);
			vScreen.fZoneTitles = scPaLib.findNodes(this.fPathZoneTitles,vScreen);
			vScreen.fZoneList = scPaLib.findNode(this.fPathZoneList,vScreen);
			for(var k=0; k<vScreen.fZoneLnks.length; k++) {
				var vZoneLnk = vScreen.fZoneLnks[k];
				vZoneLnk.fZone = vScreen.fZones[k];
				vZoneLnk.fScreen = vScreen;
				vZoneLnk.fImg = scDynUiMgr.addElement("span", vZoneLnk, this.fClsPreScr + "ZneImg", null, {display:"none",position:"absolute",top:"0",bottom:"0",left:"0",right:"0",backgroundImage:'url("'+scPaLib.findNode("des:.screenZone_pre/des:img", vZoneLnk.fZone).src+'")'});
				if (vScreen.fTitleFra){
					vZoneLnk.fTitle = vScreen.fZoneTitles[k];
					vZoneLnk.fTitle.fClass = vZoneLnk.fTitle.className;
					var vTitleLink = scPaLib.findNode("des:a", vZoneLnk.fTitle);
					vTitleLink.fZne = vZoneLnk;
					vTitleLink.onclick = function(){ return this.fZne.onclick()};
				}
				vZoneLnk.onclick = this.sScrLnkClick;
			}
			vScreen.className = vScreen.className + " " + this.fClsPreScr + "Fra";
			vScreen.fClass = vScreen.className;
			
			vScreen.fToolbar = scDynUiMgr.addElement("div", vScreen, this.fClsPreScr + "Tools", vScreen.firstChild);
			vScreen.fBtnList = this.xAddBtn(vScreen.fToolbar, vScreen, this.fClsPreScr + "BtnLst", this.xGetStr(10), this.xGetStr(11));
			vScreen.fBtnAct = this.xAddBtn(vScreen.fToolbar, vScreen, this.fClsPreScr + "BtnAct", this.xGetStr(12), this.xGetStr(13));
			this.xScrIntMode(vScreen);

			for(var k=0; k<vScreen.fZones.length; k++) {
				var vZone = vScreen.fZones[k];
				vZone.style.visibility = "";
				vZone.style.position = "";
				vZone.style.left = "";
				vZone.style.top = "";
			}
		}
	} catch(e){scCoLib.log("dokielMgr.xIniScreens::Error : "+e)}
}

/** dokielMgr.xScrLstMode */
dokielMgr.xScrLstMode = function(pScreen){
	pScreen.fIntMode = false;
	pScreen.className = pScreen.fClass;
	for(var i=0; i<pScreen.fZones.length; i++) {
		pScreen.fZones[i].style.display = "";
		pScreen.fZoneLnks[i].fImg.style.display = "none";
		pScreen.fZoneLnks[i].fAct = false;
		if (pScreen.fTitleFra) pScreen.fZoneTitles[i].className = pScreen.fZoneTitles[i].fClass;
	}
	pScreen.fZoneList.setAttribute("aria-live", "polite");
	pScreen.fBtnList.setAttribute("aria-selected", "true");
	pScreen.fBtnAct.setAttribute("aria-selected", "false");
	if("scSiLib" in window) scSiLib.fireResizedNode(pScreen);
}

/** dokielMgr.xScrIntMode */
dokielMgr.xScrIntMode = function(pScreen){
	pScreen.fIntMode = true;
	pScreen.className = pScreen.fClass + " " + this.fClsPreScr + "FraInt";
	for(var i=0; i<pScreen.fZones.length; i++) pScreen.fZones[i].style.display = "none";
	pScreen.fZoneList.removeAttribute("aria-live");
	pScreen.fBtnList.setAttribute("aria-selected", "false");
	pScreen.fBtnAct.setAttribute("aria-selected", "true");
	if("scSiLib" in window) scSiLib.fireResizedNode(pScreen);
}

/** dokielMgr.sScrLnkClick */
dokielMgr.sScrLnkClick = function(pEvt){
	if (this.fScreen.fIntMode){
		var vAct = this.fAct;
		for(var i=0; i<this.fScreen.fZones.length; i++) {
			this.fScreen.fZones[i].style.display = "none";
			this.fScreen.fZoneLnks[i].fImg.style.display = "none";
			this.fScreen.fZoneLnks[i].fAct = false;
			if (this.fScreen.fTitleFra) this.fScreen.fZoneTitles[i].className = this.fScreen.fZoneTitles[i].fClass;
		}
		if (vAct){
			this.fAct = false;
			this.fScreen.className = this.fScreen.fClass + " " + dokielMgr.fClsPreScr + "FraInt";
		} else {
			this.fAct = true;
			this.fZone.style.display = "";
			this.fImg.style.display = "";
			this.fScreen.className = this.fScreen.fClass + " " + dokielMgr.fClsPreScr + "FraAct";
			if (this.fTitle) {
				this.fTitle.className = this.fTitle.fClass + " " + dokielMgr.fClsPreScr + "TiAct";
				if (this.fTitle.offsetTop < this.fScreen.fTitleFra.scrollTop) this.fScreen.fTitleFra.scrollTop = this.fTitle.offsetTop;
				else if (this.fTitle.offsetTop+this.fTitle.offsetHeight > this.fScreen.fTitleFra.scrollTop + this.fScreen.fTitleFra.clientHeight) this.fScreen.fTitleFra.scrollTop = this.fTitle.offsetTop + this.fTitle.offsetHeight - this.fScreen.fTitleFra.clientHeight;
			}
		}
		if("scSiLib" in window) scSiLib.fireResizedNode(this.fScreen);
		return false;
	} else {
		return true;
	}
}

/* ============================== StepList player ============================== */
/** dokielMgr.xIniPlayers */
dokielMgr.xIniPlayers = function() {
	if (this.fNavie6) return;
	try{
		for(var i in this.fPathPlayer) {
			var vPlayers = scPaLib.findNodes(this.fPathPlayer[i]);
			for(var j in vPlayers) {
				var vPlayer = vPlayers[j];
				vPlayer.fOp = scPaLib.findNode(this.fPathOp,vPlayer);
				vPlayer.fOp.fClass = vPlayer.fOp.className;
				vPlayer.fOver = scDynUiMgr.addElement("div", vPlayer.fOp.parentNode, this.fClsPreSlp + "Over", vPlayer.fOp);
				vPlayer.fOver.style.display = "none";
				vPlayer.fSteps = scPaLib.findNodes(this.fPathSteps,vPlayer);
				for(var k in vPlayer.fSteps) {
					var vStep = vPlayer.fSteps[k];
				}
				vPlayer.className = vPlayer.className + " " + this.fClsPreSlp + "Fra";
				vPlayer.fClass = vPlayer.className;
				var vToolbar = scDynUiMgr.addElement("div", vPlayer.parentNode, this.fClsPreSlp + "Tools", vPlayer);
				vPlayer.fBtnStep = this.xAddBtn(vToolbar, vPlayer, this.fClsPreSlp + "BtnStp", this.xGetStr(0), this.xGetStr(1));
				vPlayer.fBtnList = this.xAddBtn(vToolbar, vPlayer, this.fClsPreSlp + "BtnLst", this.xGetStr(2), this.xGetStr(3));
				this.xAddSep(vToolbar);
				vPlayer.fNavBtns = scDynUiMgr.addElement("span", vToolbar, this.fClsPreSlp + "NavBtns");
				vPlayer.fBtnPrv = this.xAddBtn(vPlayer.fNavBtns, vPlayer, this.fClsPreSlp + "BtnPrv", this.xGetStr(4), this.xGetStr(5));
				this.xAddSep(vPlayer.fNavBtns);
				vPlayer.fBtnNxt = this.xAddBtn(vPlayer.fNavBtns, vPlayer, this.fClsPreSlp + "BtnNxt", this.xGetStr(6), this.xGetStr(7));
				this.xAddSep(vPlayer.fNavBtns);
				var vLblCount = scDynUiMgr.addElement("span", vPlayer.fNavBtns, this.fClsPreSlp + "CountLbl");
				vLblCount.innerHTML = "<span>"+this.xGetStr(8)+"</span>"
				vPlayer.fCount = scDynUiMgr.addElement("span", vPlayer.fNavBtns, this.fClsPreSlp + "CountTxt");
				this.xSlpLstMode(vPlayer);
			}
		}
	} catch(e){scCoLib.log("dokielMgr.xIniPLayers::Error : "+e)}
}

/** dokielMgr.xSlpStpMode */
dokielMgr.xSlpStpMode = function(pPlayer){
	if (this.fActiveStepPlayer) this.xSlpLstMode(this.fActiveStepPlayer);
	pPlayer.fBtnStep.style.display = "none";
	pPlayer.fBtnList.style.display = "";
	pPlayer.fNavBtns.style.display = "";
	pPlayer.fIdx = -1;
	for(var i in pPlayer.fSteps) {
		var vStep = pPlayer.fSteps[i];
		vStep.style.display = "none";
		vStep.style.position = "";
		vStep.style.visibility = "";
		vStep.style.left = "";
		vStep.style.top = "";
	}
	pPlayer.fOp.className = pPlayer.fOp.fClass + " " + this.fClsPreSlp + "OpAct";
	pPlayer.className = pPlayer.fClass + " " + this.fClsPreSlp + "FraAct";
	pPlayer.fOver.style.display = "";
	this.fActiveStepPlayer = pPlayer;
	if("scSiLib" in window) scSiLib.fireResizedNode(pPlayer);
	this.xSlpNxt(pPlayer);
}

/** dokielMgr.xSlpLstMode */
dokielMgr.xSlpLstMode = function(pPlayer){
	pPlayer.fBtnStep.style.display = "";
	pPlayer.fBtnList.style.display = "none";
	pPlayer.fNavBtns.style.display = "none";
	for(var i in pPlayer.fSteps) {
		var vStep = pPlayer.fSteps[i];
		vStep.style.display = "";
		vStep.style.position = "";
		vStep.style.visibility = "";
		vStep.style.left = "";
		vStep.style.top = "";
	}
	pPlayer.fOp.className = pPlayer.fOp.fClass;
	pPlayer.className = pPlayer.fClass;
	pPlayer.fOver.style.display = "none";
	this.fActiveStepPlayer = null;
	if("scSiLib" in window) scSiLib.fireResizedNode(pPlayer);
}

/** dokielMgr.xSlpPrv */
dokielMgr.xSlpPrv = function(pPlayer){
	if (pPlayer.fIdx <= 0) return;
	this.xSlpGotoStep(pPlayer,--pPlayer.fIdx);
}

/** dokielMgr.xSlpNxt */
dokielMgr.xSlpNxt = function(pPlayer){
	if (pPlayer.fIdx >= pPlayer.fSteps.length-1) return;
	this.xSlpGotoStep(pPlayer,++pPlayer.fIdx);
}

/** dokielMgr.xSlpGotoStep */
dokielMgr.xSlpGotoStep = function(pPlayer, pIdx){
	for(var i in pPlayer.fSteps) {
		var vStep = pPlayer.fSteps[i];
		vStep.style.display = (pIdx == i ? "" : "none");
	}
	pPlayer.fBtnPrv.style.visibility = (pPlayer.fIdx <= 0 ? "hidden" : "");
	pPlayer.fBtnNxt.style.visibility = (pPlayer.fIdx >= pPlayer.fSteps.length-1 ? "hidden" : "");
	pPlayer.fCount.innerHTML = "<span>"+(pPlayer.fIdx+1)+"/"+pPlayer.fSteps.length+"</span>";
	if (pPlayer.fIdx >= pPlayer.fSteps.length-1) pPlayer.fBtnList.focus();
}

/* ============================== Utils ============================== */
/** dokielMgr.xAddSep : Add a simple textual separator : " | ". */
dokielMgr.xAddSep = function(pParent){
	var vSep = document.createElement("span");
	vSep.className = this.fClsPreSlp + "Sep";
	vSep.innerHTML = " | "
	pParent.appendChild(vSep);
}

/** dokielMgr.xAddBtn : Add a HTML button to a parent node. */
dokielMgr.xAddBtn = function(pParent,pFra,pClass,pCapt,pTitle,pNoCmd){
	var vBtn = document.createElement("a");
	vBtn.className = pClass;
	vBtn.fName = pClass;
	vBtn.href = "#";
	vBtn.target = "_self";
	vBtn.setAttribute("role", "button");
	if (!pNoCmd) {
		vBtn.onclick=function(){return dokielMgr.xBtnMgr(this);}
		vBtn.onkeyup=function(pEvent){scDynUiMgr.handleBtnKeyUp(pEvent);}
	}
	vBtn.setAttribute("title",pTitle);
	vBtn.innerHTML="<span>"+pCapt+"</span>";
	vBtn.fFra = pFra;
	pParent.appendChild(vBtn);
	return vBtn;
}

/** dokielMgr.xTogglePageFocus : */
dokielMgr.xToggleFocusables = function(pExludedNode) {
	if (this.fFocusablesDisabled && this.fFocusables){
		for (var i=0; i<this.fFocusables.length; i++){
			this.fFocusables[i].setAttribute("tabindex", "");
		}
		this.fFocusablesDisabled = false;
	} else {
		this.fFocusables = scPaLib.findNodes(this.fPathFocusables);
		for (var i=0; i<this.fFocusables.length; i++){
			var vElt = this.fFocusables[i];
			if (!this.xIsEltContainedByNode(vElt, pExludedNode)) this.fFocusables[i].setAttribute("tabindex", "-1");
		}
		this.fFocusablesDisabled = true;
	}
}

/** dokielMgr.xIsEltContainedByNode : */
dokielMgr.xIsEltContainedByNode = function(pElt, pContainer) {
	var vElt = pElt;
	var vFound = false;
	if (vElt) {
		while (vElt.parentNode && !vFound) {
			vElt = vElt.parentNode;
			vFound = vElt == pContainer;
		}
	}
	return(vFound);
}

/** dokielMgr.xGetStr : Reteive a string. */
dokielMgr.xGetStr = function(pStrId) {
	return this.fStrings[pStrId];
}
dokielMgr.loadSortKey = "ZZ";
