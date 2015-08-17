/**
 * LICENCE[[
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1/CeCILL 2.O
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is kelis.fr code.
 *
 * The Initial Developer of the Original Code is 
 * samuel.monsarrat@kelis.fr
 *
 * Portions created by the Initial Developer are Copyright (C) 2009-2015
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * or the CeCILL Licence Version 2.0 (http://www.cecill.info/licences.en.html),
 * in which case the provisions of the GPL, the LGPL or the CeCILL are applicable
 * instead of those above. If you wish to allow use of your version of this file
 * only under the terms of either the GPL, the LGPL or the CeCILL, and not to allow
 * others to use your version of this file under the terms of the MPL, indicate
 * your decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL, the LGPL or the CeCILL. If you do not
 * delete the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL, the LGPL or the CeCILL.
 * ]]LICENCE
 */

/* === SCENARI Dynamic image manager ======================================== */
var scImgMgr = {
	fPathAnim : [],
	fPathGal : [],
	fPathZoom : [],
	fPathImg : [],
	fAnims : null,
	fGals : null,
	fZooms : null,
	fPathPgeFra : "des:div",
	fPathFocusables : "des:a|input|button",
	fCurrItem : null,
	fOverAlpha : .6,
	fDefaultStep : 3 * 1000,
	fMinStep : 1 * 100,
	fMaxStep : 10 * 1000,
	fTypAnm : "scImgAnm",
	fTypZm : "scImgZm",
	fTypGal : "scImgGal",
	fFocus : true,
	fSourceRoot : null,
	fDisplayRoot : null,
	fLocalize : true,
	fNavie6 : parseFloat(scCoLib.userAgent.substring(scCoLib.userAgent.indexOf("msie")+5)) < 7,
	fNavie8 : parseFloat(scCoLib.userAgent.substring(scCoLib.userAgent.indexOf("msie")+5)) < 9,
	fMaxDeviceWidth : Math.min(window.screen.width, window.screen.height),
	fListeners : {"onOverlayOpen":[],"onOverlayClose":[],"onAnimationOpen":[],"onAnimationClose":[],"onZoomOpen":[],"onZoomClose":[]}
}

/** SCENARI Dynamic image manager strings */
scImgMgr.fStrings = ["précédent","image précédente (flèche de gauche)",
/*02*/               "suivant","image suivante (flèche de droite)",
/*04*/               "fermer","fermer le diaporama (Echap)",
/*06*/               "lancer","lancer le diaporama (p)",
/*08*/               "arrêter","arrêter le diaporama (p)",
/*10*/               "Cette page est en cours de chargement. Veuillez patienter.","",
/*12*/               "précédent","image précédente",
/*14*/               "suivant","image suivante",
/*16*/               "lancer","lancer l\'animation",
/*18*/               "arrêter","arrêter l\'animation",
/*20*/               "début","aller au début de l\'animation",
/*22*/               "fin","aller à la fin de l\'animation",
/*24*/               "vitesse","changer la vitesse de l\'animation",
/*26*/               "image","Consulter le diaporama à partir de :",
/*28*/               "boucle","jouer en boucle continue",
/*30*/               "fermer","fermer le zoom (Echap)",
/*32*/               "zoomer sur cette image",""];
/** scImgMgr.init. */
scImgMgr.init = function() {
	// Init image animations...
	try{
		if (!("scDynUiMgr" in window)) throw "Library scDynUiMgr not found.";
		for(var i=0; i<this.fPathAnim.length; i++) {
			var vAnims = scPaLib.findNodes(this.fPathAnim[i].fPath);
			for(var j=0; j<vAnims.length; j++) {
				var vAnim = vAnims[j];
				try {
					var vImgs = scPaLib.findNodes("chi:",vAnim);
					for(var k=0; k<vImgs.length; k++) {
						if (k>0) {
							vImgs[k].style.visibility = "hidden";
							vImgs[k].style.position = "absolute";
							vImgs[k].style.left = "-2000px";
							vImgs[k].style.top = "-2000px";
						}
					}
				} catch(e){
					scCoLib.log("scImgMgr.init::Anim init Error"+e);
				}
			}
		}
		// Load images ...
		this.xInitImgs(this.fSourceRoot);
		//Register listeners...
		scDynUiMgr.collBlk.addOpenListener(this.sCollBlkOpen);
		scDynUiMgr.collBlk.addCloseListener(this.sCollBlkClose);
		scOnLoads[scOnLoads.length] = this;
	} catch(e){scCoLib.log("ERROR - scImgMgr.init : "+e)}
}
/** scImgMgr.registerAnimation.
 * @param pPathAnim scPaLib path vers les animations.
 * @param pOpts options de l'animation.
 *           toolbar : 0 = pas de toolbar / 1 = toolbar flotant / 2 toolbar permanent
 *           auto : true = démarrage auto
 *           loop : true = lecture en boucle
 *           lpBtn : true = bouton ctrl lecture en boucle
 *           speed : vitesse de défilement en ms
 *           spdBtns : true = boutons de contrôle de la vitesse
 *           counter : true = compteur d'image
 *           soft : true = fondu entre images
 *           extBtns : true = boutons supplémentaires
 *           clsPre : préfix de classe CSS
 */
scImgMgr.registerAnimation = function(pPathAnim, pOpts) {
	var vAnim = new Object;
	vAnim.fPath = pPathAnim;
	vAnim.fOpts = (typeof pOpts == "undefined" ? {toolbar:1,auto:true,loop:true,lpBtn:false,speed:this.fDefaultStep,spdBtns:false,counter:false,soft:true,extBtns:false,clsPre:this.fTypAnm} : pOpts);
	vAnim.fOpts.toolbar = (typeof vAnim.fOpts.toolbar == "undefined" ? 1 : vAnim.fOpts.toolbar);
	vAnim.fOpts.auto = (typeof vAnim.fOpts.auto == "undefined" ? true : vAnim.fOpts.auto);
	vAnim.fOpts.loop = (typeof vAnim.fOpts.loop == "undefined" ? true : vAnim.fOpts.loop);
	vAnim.fOpts.lpBtn = (typeof vAnim.fOpts.lpBtn == "undefined" ? false : vAnim.fOpts.lpBtn);
	vAnim.fOpts.speed = (typeof vAnim.fOpts.speed == "undefined" ? this.fDefaultStep : vAnim.fOpts.speed);
	vAnim.fOpts.spdBtns = (typeof vAnim.fOpts.spdBtns == "undefined" ? false : vAnim.fOpts.spdBtns);
	vAnim.fOpts.counter = (typeof vAnim.fOpts.counter == "undefined" ? false : vAnim.fOpts.counter);
	vAnim.fOpts.soft = (typeof vAnim.fOpts.soft == "undefined" ? true : vAnim.fOpts.soft);
	vAnim.fOpts.extBtns = (typeof vAnim.fOpts.extBtns == "undefined" ? false : vAnim.fOpts.extBtns);
	vAnim.fOpts.clsPre = (typeof vAnim.fOpts.clsPre == "undefined" ? this.fTypAnm : vAnim.fOpts.clsPre);
	this.fPathAnim[this.fPathAnim.length] = vAnim;
}
/** scImgMgr.registerGallery.
 * @param pPathGal scPaLib path vers les zooms.
 * @param pOpts options de la gallerie.
 *           clsPre : préfix de classe CSS
 */
scImgMgr.registerGallery = function(pPathGal, pOpts) {
	var vGal = new Object;
	vGal.fPath = pPathGal;
	vGal.fOpts = (typeof pOpts == "undefined" ? {clsPre:this.fTypGal,centerThumbs:true} : pOpts);
	vGal.fOpts.clsPre = (typeof vGal.fOpts.clsPre == "undefined" ? this.fTypGal : vGal.fOpts.clsPre);
	vGal.fOpts.centerThumbs = (typeof vGal.fOpts.centerThumbs == "undefined" ? true : vGal.fOpts.centerThumbs);
	this.fPathGal[this.fPathGal.length] = vGal;
}
/** scImgMgr.registerZoom.
 * @param pPathZoom scPaLib path vers les zooms.
 * @param pOpts options du zoom.
 *           toolbar : 0 = pas de toolbar / 1 = toolbar
 *           type : img = zoom d'image / iframe = zoom chargé dans une iframe
 *           mag : 0 = pas de loupe /  1 = ajouter une loupe si besoin
 *           magScale : relative size of the zoom area compared to the visible image
 *           magMax : 0 = pas de mode max /  1 = mode max sur click
 *           magPan : 0 = pas de pan en mode max /  1 = pan en mode max
 *           titlePath : scPaLib path to a title relative to the anchor.
 *           clsPre : préfix de classe CSS
 */
scImgMgr.registerZoom = function(pPathZoom, pOpts) {
	var vZm = new Object;
	vZm.fPath = pPathZoom;
	vZm.fOpts = (typeof pOpts == "undefined" ? {toolbar:0,type:"img",clsPre:this.fTypZm} : pOpts);
	vZm.fOpts.type = (typeof vZm.fOpts.type == "undefined" ? "img" : vZm.fOpts.type);
	vZm.fOpts.toolbar = (typeof vZm.fOpts.toolbar == "undefined" ? 1 : vZm.fOpts.toolbar);
	vZm.fOpts.mag = (typeof vZm.fOpts.mag == "undefined" ? 0 : vZm.fOpts.mag);
	vZm.fOpts.magScale = (typeof vZm.fOpts.magScale == "undefined" ? 0.33 : vZm.fOpts.magScale);
	vZm.fOpts.magMax = (typeof vZm.fOpts.magMax == "undefined" ? 1 : vZm.fOpts.magMax);
	vZm.fOpts.magPan = (typeof vZm.fOpts.magPan == "undefined" ? 1 : vZm.fOpts.magPan);
	vZm.fOpts.clsPre = (typeof vZm.fOpts.clsPre == "undefined" ? this.fTypZm : vZm.fOpts.clsPre);
	vZm.fOpts.titlePath = (typeof vZm.fOpts.titlePath == "undefined" ? null : vZm.fOpts.titlePath);
	if ((vZm.fOpts.mag > 0 || vZm.fOpts.titlePath) && vZm.fOpts.toolbar == 0) vZm.fOpts.toolbar = 1;
	this.fPathZoom[this.fPathZoom.length] = vZm;
}
/** scImgMgr.registerAdaptedImage.
 * @param pPathImage scPaLib path vers les images.
 */
scImgMgr.registerAdaptedImage = function(pPathImage) {
	var vImg = new Object;
	vImg.fPath = pPathImage;
	this.fPathImg[this.fPathImg.length] = vImg;
}

/** register a listener. */
scImgMgr.registerListener = function(pType, pFunc) {
	this.fListeners[pType].push(pFunc);
}
/** scImgMgr.setSourceRoot. */
scImgMgr.setSourceRoot = function(pRoot) {
	this.fSourceRoot = pRoot;
}
/** scImgMgr.setDisplayRoot. */
scImgMgr.setDisplayRoot = function(pRoot) {
	this.fDisplayRoot = pRoot;
}
/** scImgMgr.setPathPgeFra. */
scImgMgr.setPathPgeFra = function(pPathPgeFra) {
	this.fPathPgeFra = pPathPgeFra;
}
/** scImgMgr.setFocus. */
scImgMgr.setFocus = function(pFocus) {
	this.fFocus = pFocus;
}
/** scImgMgr.setLocalize. */
scImgMgr.setLocalize = function(pLocalize) {
	this.fLocalize = pLocalize;
}

/** scImgMgr.onLoad - called by the scenari framework, inits the manager. */
scImgMgr.onLoad = function() {
	this.fPgeFra = scPaLib.findNode(scImgMgr.fPathPgeFra, this.fDisplayRoot);
	if (!this.fSourceRoot) this.fSourceRoot = document.body;
	if (!this.fDisplayRoot) this.fDisplayRoot = document.body;

	// Load image galleries...
	this.xInitSss(this.fSourceRoot);
	// Load image zooms...
	this.xInitZms(this.fSourceRoot);
	// Load image animations...
	this.xInitAnims(this.fSourceRoot);
}

/** scImgMgr.loading. */
scImgMgr.loading = function() {
	alert(scImgMgr.xGetStr(10));
}
/** scImgMgr.sCollBlkOpen - scDynUiMgr collapsable block callback function */
scImgMgr.sCollBlkOpen = function(pCo) {
	// Reinit image animations...
	if (!pCo.fAnimInitDone){
		scImgMgr.xInitAnims(pCo);
		pCo.fAnimInitDone = true;
	}
}
/** scImgMgr.sCollBlkClose - scDynUiMgr collapsable block callback function */
scImgMgr.sCollBlkClose = function(pCo) {
}

/* === Global managers ====================================================== */
/** scImgMgr.xBtnMgr - centralized button manager */
scImgMgr.xBtnMgr = function(pBtn) {
	var vObj = pBtn.fObj;
	switch(pBtn.fName){
		case this.fTypZm+"Zm":
			scImgMgr.xOpenZm(pBtn);break;
		case this.fTypZm+"BtnCls":
		case this.fTypZm+"BtnImgCls":
			scImgMgr.xClsZm(vObj);break;
			
		case this.fTypGal+"Pv":
			scImgMgr.xOpenSs(vObj,pBtn);break;
		case this.fTypGal+"BtnPrv":
			if (scImgMgr.fCurrItem.fSsAutoPly) scImgMgr.xPseSs(vObj);
			scImgMgr.xPrvSs(vObj);break;
		case this.fTypGal+"BtnNxt":
			if (scImgMgr.fCurrItem.fSsAutoPly) scImgMgr.xPseSs(vObj);
			scImgMgr.xNxtSs(vObj);break;
		case this.fTypGal+"BtnCls":
			scImgMgr.xClsSs(vObj);break;
		case this.fTypGal+"BtnPly":
			scImgMgr.xPlySs(vObj);break;
		case this.fTypGal+"BtnPse":
			scImgMgr.xPseSs(vObj);break;
			
		case this.fTypAnm+"BtnPrv":
			scImgMgr.xAnimCtrlOn(vObj);
			if (vObj.fAutoPly) scImgMgr.xPseAnm(vObj);
			scImgMgr.xPrvAnm(vObj);break;
		case this.fTypAnm+"BtnNxt":
			scImgMgr.xAnimCtrlOn(vObj);
			if (vObj.fAutoPly) scImgMgr.xPseAnm(vObj);
			scImgMgr.xNxtAnm(vObj);break;
		case this.fTypAnm+"BtnSrt":
			scImgMgr.xAnimCtrlOn(vObj);
			if (vObj.fAutoPly) scImgMgr.xPseAnm(vObj);
			scImgMgr.xSrtAnm(vObj);break;
		case this.fTypAnm+"BtnEnd":
			scImgMgr.xAnimCtrlOn(vObj);
			if (vObj.fAutoPly) scImgMgr.xPseAnm(vObj);
			scImgMgr.xEndAnm(vObj);break;
		case this.fTypAnm+"BtnPly":
		case this.fTypAnm+"BtnInitPly":
			scImgMgr.xAnimCtrlOn(vObj);
			scImgMgr.xPlyAnm(vObj);break;
		case this.fTypAnm+"BtnPse":
			scImgMgr.xAnimCtrlOn(vObj);
			scImgMgr.xPseAnm(vObj);break;
		case this.fTypAnm+"BtnSpdDwn":
			scImgMgr.xAnimCtrlOn(vObj);
			scImgMgr.xSetAnmSpd(vObj,+200);break;
		case this.fTypAnm+"BtnSpdUp":
			scImgMgr.xAnimCtrlOn(vObj);
			scImgMgr.xSetAnmSpd(vObj,-200);break;
		case this.fTypAnm+"BtnLp":
			scImgMgr.xAnimCtrlOn(vObj);
			scImgMgr.xSetAnmLp(vObj,pBtn.checked);return true;
	}
	return false;
}
/** scImgMgr.xKeyMgr - centralized keyboard manager */
scImgMgr.xKeyMgr = function(pEvent){
	var vEvent = pEvent || window.event;
	var vCharCode = vEvent.which || vEvent.keyCode;
	if (!scImgMgr.fCurrItem) return;
	switch(vCharCode){
		case 34://pg_dwn
		case 39://left
			if (scImgMgr.fCurrItem.fName == "gal") {
				if (scImgMgr.fCurrItem.fSsAutoPly) scImgMgr.xPseSs(scImgMgr.fCurrItem);
				scImgMgr.xNxtSs(scImgMgr.fCurrItem);
			}
			return false;
		case 8://bksp
		case 33://pg_up
		case 37://right
			if (scImgMgr.fCurrItem.fName == "gal") {
				if (scImgMgr.fCurrItem.fSsAutoPly) scImgMgr.xPseSs(scImgMgr.fCurrItem);
				scImgMgr.xPrvSs(scImgMgr.fCurrItem);
			}
			return false;
		case 27://escape
			if (scImgMgr.fCurrItem.fName == "gal") {
				scImgMgr.xClsSs(scImgMgr.fCurrItem);
			} else {
				scImgMgr.xClsZm(scImgMgr.fCurrItem);
			}
			return false;
		case 80:// p
			if (scImgMgr.fCurrItem.fName == "gal") {
				if(scImgMgr.fCurrItem.fSsAutoPly) scImgMgr.xPseSs(scImgMgr.fCurrItem);
				else scImgMgr.xPlySs(scImgMgr.fCurrItem);
			}
			return false;
	}
}
/* === Image size manager =================================================== */
scImgMgr.xInitImgs = function(pCo) {
	for(var i=0; i<this.fPathImg.length; i++) {
		var vImgs = scPaLib.findNodes(this.fPathImg[i].fPath, pCo);
		for(var j=0; j<vImgs.length; j++) this.xInitImg(vImgs[j]);
	}
}
scImgMgr.xInitImg = function(pImg) {
	if (pImg.width>this.fMaxDeviceWidth){
		pImg.setAttribute("width", "");
		pImg.setAttribute("height", "");
		pImg.style.maxWidth = "100%";
		pImg.style.height = "auto";
		pImg.fIsAdapted = true;
	}
}
/* === Animation manager ==================================================== */
scImgMgr.xInitAnims = function(pCo) {
	for(var i=0; i<this.fPathAnim.length; i++) {
		var vAnims = scPaLib.findNodes(this.fPathAnim[i].fPath, pCo);
		for(var j=0; j<vAnims.length; j++) this.xInitAnim(vAnims[j],this.fPathAnim[i].fOpts,this.fTypAnm+i+j);
	}
}
scImgMgr.xInitAnim = function(pAnim,pOpts,pId) {
	try {
		if (this.xIsVisible(pAnim)){
			pAnim.fImgs = scPaLib.findNodes("chi:",pAnim);
			pAnim.fOpts = pOpts;
			var vMaxHeight = 0;
			var vMaxWidth = 0;
			for(var i=0; i<pAnim.fImgs.length; i++) {
				var vImg = pAnim.fImgs[i];
				vImg.style.position = "absolute";
				vImg.fHeight = vImg.clientHeight;
				vImg.fWidth = scPaLib.findNode("des:img",vImg).width;
				vMaxHeight = Math.max(vMaxHeight,vImg.fHeight);
				vMaxWidth = Math.max(vMaxWidth,vImg.fWidth);
				vImg.style.visibility = "hidden";
				vImg.style.top = "0";
				vImg.style.left = "0";
				vImg.style.width = "100%";
			}
			pAnim.style.height = vMaxHeight+0.01*vMaxHeight + "px";
			pAnim.style.width = vMaxWidth+0.01*vMaxWidth + "px";
			for(var i=0; i<pAnim.fImgs.length; i++) {
				var vImg = pAnim.fImgs[i];
				vImg.style.marginTop = (vMaxHeight - vImg.fHeight)/2 + "px";
			}
			if (!pOpts.auto && pOpts.toolbar<2) {
				pAnim.fBtnInitPly = scImgMgr.xAddBtn(pAnim,pAnim,this.fTypAnm,"BtnInitPly",scImgMgr.xGetStr(16),scImgMgr.xGetStr(17));
			}
			if (pOpts.toolbar > 0){
				if(pOpts.toolbar == 1) pAnim.fCtrl = scDynUiMgr.addElement("div",pAnim,pOpts.clsPre + "Ctrl");
				else pAnim.fCtrl = scDynUiMgr.addElement("div",pAnim.parentNode,pOpts.clsPre + "Ctrl",pAnim.nextSibling);
				if (pOpts.extBtns) {
					pAnim.fBtnSrt = scImgMgr.xAddBtn(pAnim.fCtrl,pAnim,this.fTypAnm,"BtnSrt",scImgMgr.xGetStr(20),scImgMgr.xGetStr(21));
					scImgMgr.xAddSep(pAnim.fCtrl);
				}
				pAnim.fBtnPrv = scImgMgr.xAddBtn(pAnim.fCtrl,pAnim,this.fTypAnm,"BtnPrv",scImgMgr.xGetStr(12),scImgMgr.xGetStr(13));
				scImgMgr.xAddSep(pAnim.fCtrl);
				pAnim.fBtnPly = scImgMgr.xAddBtn(pAnim.fCtrl,pAnim,this.fTypAnm,"BtnPly",scImgMgr.xGetStr(16),scImgMgr.xGetStr(17));
				pAnim.fBtnPly.style.display = (pOpts.auto ? "none" : "");
				pAnim.fBtnPse = scImgMgr.xAddBtn(pAnim.fCtrl,pAnim,this.fTypAnm,"BtnPse",scImgMgr.xGetStr(18),scImgMgr.xGetStr(19));
				pAnim.fBtnPse.style.display = (pOpts.auto ? "" : "none");
				scImgMgr.xAddSep(pAnim.fCtrl);
				pAnim.fBtnNxt = scImgMgr.xAddBtn(pAnim.fCtrl,pAnim,this.fTypAnm,"BtnNxt",scImgMgr.xGetStr(14),scImgMgr.xGetStr(15));
				if (pOpts.extBtns) {
					scImgMgr.xAddSep(pAnim.fCtrl);
					pAnim.fBtnEnd = scImgMgr.xAddBtn(pAnim.fCtrl,pAnim,this.fTypAnm,"BtnEnd",scImgMgr.xGetStr(22),scImgMgr.xGetStr(23));
				}
				if (pOpts.spdBtns) {
					scImgMgr.xAddSep(pAnim.fCtrl);
					pAnim.fBtnSpdDwn = scImgMgr.xAddBtn(pAnim.fCtrl,pAnim,this.fTypAnm,"BtnSpdDwn","-",scImgMgr.xGetStr(25));
					scDynUiMgr.addElement("span",pAnim.fCtrl,pOpts.clsPre + "Spd").innerHTML = " "+scImgMgr.xGetStr(24)+" ";
					pAnim.fBtnSpdUp = scImgMgr.xAddBtn(pAnim.fCtrl,pAnim,this.fTypAnm,"BtnSpdUp","+",scImgMgr.xGetStr(25));
				}
				if (pOpts.lpBtn) {
					scImgMgr.xAddSep(pAnim.fCtrl);
					pAnim.fBtnLp = scDynUiMgr.addElement("input",pAnim.fCtrl,pOpts.clsPre + "BtnLp");
					pAnim.fBtnLp.setAttribute("type","checkbox");
					pAnim.fBtnLp.fName = this.fTypAnm + "BtnLp";
					pAnim.fBtnLp.setAttribute("id",pId);
					pAnim.fBtnLp.setAttribute("title",scImgMgr.xGetStr(29));
					if (pOpts.loop){
						var vAttChk = document.createAttribute("checked"); // For IE the attr checked must be created
						vAttChk.nodeValue = "true";
						pAnim.fBtnLp.setAttributeNode(vAttChk);
					}
					pAnim.fBtnLp.fObj = pAnim;
					pAnim.fBtnLp.onclick = function(){return scImgMgr.xBtnMgr(this);}
					var vLblLp = scDynUiMgr.addElement("label",pAnim.fCtrl,pOpts.clsPre + "LpLbl");
					vLblLp.innerHTML = scImgMgr.xGetStr(28);
					vLblLp.setAttribute("for",pId);
					vLblLp.setAttribute("title",scImgMgr.xGetStr(29));
				}
				if (pOpts.counter) {
					scImgMgr.xAddSep(pAnim.fCtrl);
					scDynUiMgr.addElement("span",pAnim.fCtrl,pOpts.clsPre + "CtrLbl").innerHTML = scImgMgr.xGetStr(26) + " ";
					pAnim.fCtrIdx = scDynUiMgr.addElement("span",pAnim.fCtrl,pOpts.clsPre + "CtrIdx");
					pAnim.fCtrIdx.innerHTML = "1";
					scDynUiMgr.addElement("span",pAnim.fCtrl,pOpts.clsPre + "CtrSep").innerHTML = "/";
					scDynUiMgr.addElement("span",pAnim.fCtrl,pOpts.clsPre + "CtrCnt").innerHTML = pAnim.fImgs.length;
				}
				if (pOpts.toolbar == 1) {
					pAnim.onmouseover = function () {scImgMgr.xAnimCtrlOn(pAnim);}
					pAnim.fCtrl.style.visibility = "hidden";
					pAnim.fCtrl.fOn = false;
				}
			}
			pAnim.fImgs[0].style.visibility = "";
			pAnim.fCurrImgIdx = 0;
			pAnim.fStep = pOpts.speed;
			pAnim.fAutoPly = pOpts.auto;
			pAnim.fSoft = pOpts.soft;
			pAnim.fLoop = pOpts.loop;
			if (pAnim.fAutoPly && pAnim.fImgs.length > 1) pAnim.fNxtImgProc = window.setTimeout(function(){scImgMgr.xAutoAnim(pAnim)}, pAnim.fStep);
			//Reinit zooms under pAnim
			this.xInitZms(pAnim);
		}
	} catch(e){
		scCoLib.log("scImgMgr.xInitAnim::Error : "+e);
	}
}
scImgMgr.xAutoAnim = function(pAnim) {
	if (pAnim && pAnim.fAutoPly){
		if (!pAnim.fLoop && pAnim.fCurrImgIdx == pAnim.fImgs.length - 1) {
			scImgMgr.xPseAnm(pAnim);
		} else {
			scImgMgr.xNxtAnm(pAnim);
			pAnim.fNxtImgProc = window.setTimeout(function(){scImgMgr.xAutoAnim(pAnim)}, pAnim.fStep);
		}
	}
}
scImgMgr.xAnimCtrlOn = function(pAnim) {
	if (!pAnim.fCtrl || typeof pAnim.fCtrl.fOn == "undefined") return;
	if (pAnim.fOffProc) window.clearTimeout(pAnim.fOffProc);
	if (!pAnim.fCtrl.fOn){
		new scImgMgr.FadeEltTask(pAnim.fCtrl, 1);
		pAnim.fCtrl.fOn = true;
	}
	pAnim.fOffProc = window.setTimeout(function(){scImgMgr.xAnimCtrlOff(pAnim)}, 3000);
}
scImgMgr.xAnimCtrlOff = function(pAnim) {
	if (pAnim.fCtrl.fOn){
		new scImgMgr.FadeEltTask(pAnim.fCtrl, 0);
		pAnim.fCtrl.fOn = false;
		pAnim.fOffProc = null;
	}
}
scImgMgr.xSrtAnm = function(pAnim) {
	new scImgMgr.switchAnimTask(pAnim, 0);
}
scImgMgr.xEndAnm = function(pAnim) {
	new scImgMgr.switchAnimTask(pAnim, pAnim.fImgs.length - 1);
}
scImgMgr.xPrvAnm = function(pAnim) {
	new scImgMgr.switchAnimTask(pAnim, pAnim.fCurrImgIdx == 0 ? pAnim.fImgs.length - 1 : pAnim.fCurrImgIdx - 1);
}
scImgMgr.xNxtAnm = function(pAnim) {
	new scImgMgr.switchAnimTask(pAnim, pAnim.fCurrImgIdx < pAnim.fImgs.length - 1 ? pAnim.fCurrImgIdx + 1 : 0);
}
scImgMgr.xPlyAnm = function(pAnim) {
	pAnim.fAutoPly = true;
	pAnim.fBtnPly.style.display="none";
	pAnim.fBtnPse.style.display="";
	scImgMgr.xNxtAnm(pAnim);
	pAnim.fNxtImgProc = window.setTimeout(function(){scImgMgr.xAutoAnim(pAnim)}, pAnim.fStep);
}
scImgMgr.xPseAnm = function(pAnim) {
	pAnim.fAutoPly = false;
	pAnim.fBtnPly.style.display="";
	pAnim.fBtnPse.style.display="none";
	window.clearTimeout(pAnim.fNxtImgProc);
}
scImgMgr.xSetAnmSpd = function(pAnim,pDelta) {
	pAnim.fStep += pDelta;
	pAnim.fStep = Math.min(Math.max(pAnim.fStep,scImgMgr.fMinStep),scImgMgr.fMaxStep);
}
scImgMgr.xSetAnmLp = function(pAnim,pLp) {
	pAnim.fLoop = pLp;
}
scImgMgr.switchAnimTask = function(pAnim,pNewIdx){
	this.fIdx = -1;
	this.fRateOld = [.9, .8, .7, .6, .5, .4, .3, .2, .1];
	this.fRateNew = [.1, .2, .3, .4, .5, .6, .7, .8, .9];
	try{
		if (pAnim.fBtnInitPly) pAnim.fBtnInitPly.style.display="none";
		this.fAnim = pAnim;
		if (this.fIsRunning) this.terminate();
		this.fNewIdx = pNewIdx;
		this.fOldImg = this.fAnim.fImgs[this.fAnim.fCurrImgIdx];
		this.fNewImg = this.fAnim.fImgs[this.fNewIdx];
		scImgMgr.xStartOpacityEffect(this.fOldImg, 1);
		scImgMgr.xStartOpacityEffect(this.fNewImg, 0);
		if (!this.fAnim.fSoft) {
			this.terminate();
			return;
		}
		this.fEndTime = ( Date.now  ? Date.now() : new Date().getTime() ) + 100;
		this.fIdx = -1;
		this.fIsRunning = true;
		scTiLib.addTaskNow(this);
	}catch(e){scCoLib.log("ERROR scImgMgr.switchAnimTask : "+e);}
}
scImgMgr.switchAnimTask.prototype.execTask = function(){
	while(this.fEndTime < (Date.now ? Date.now() : new Date().getTime()) && this.fIdx < this.fRateOld.length) {
		this.fIdx++;
		this.fEndTime += 100;
	}
	this.fIdx++;
	this.fEndTime += 100;
	if(this.fIdx >= this.fRateOld.length) {
		scImgMgr.xEndOpacityEffect(this.fOldImg, 0);
		scImgMgr.xEndOpacityEffect(this.fNewImg, 1);
		this.fAnim.fCurrImgIdx = this.fNewIdx;
		if (this.fAnim.fCtrIdx) this.fAnim.fCtrIdx.innerHTML = this.fNewIdx + 1;
		this.fIsRunning = false;
		return false;
	}
	scImgMgr.xSetOpacity(this.fOldImg, this.fRateOld[this.fIdx]);
	scImgMgr.xSetOpacity(this.fNewImg, this.fRateNew[this.fIdx]);
	return true;
}
scImgMgr.switchAnimTask.prototype.terminate = function(){
	this.fIdx = this.fRateOld.length;
	this.execTask();
}

/* === Zoom manager ========================================================= */
scImgMgr.xInitZms = function(pCo) {
	for(var i=0; i<this.fPathZoom.length; i++) {
		var vZooms = scPaLib.findNodes(this.fPathZoom[i].fPath, pCo);
		for(var j=0; j<vZooms.length; j++) {
			var vAnc = vZooms[j];
			try {
				var vSubImg = scPaLib.findNode("des:img", vAnc);
				if (vSubImg && vSubImg.fIsAdapted) {
					vAnc.onclick=function(){return true;}
				} else {
					vAnc.fZmUri = vAnc.href;
					vAnc.fOpts = this.fPathZoom[i].fOpts;
					vAnc.target = "_self";
					vAnc.fName=this.fTypZm+"Zm";
					vAnc.fObj=vAnc;
					vAnc.setAttribute("role", "button");
					vAnc.title = this.xGetStr(32);
					vAnc.onclick=function(){return scImgMgr.xBtnMgr(this);}
					vAnc.onkeydown=function(pEvent){scDynUiMgr.handleBtnKeyDwn(pEvent);}
					vAnc.onkeyup=function(pEvent){scDynUiMgr.handleBtnKeyUp(pEvent);}
				}
			} catch(e){
				scCoLib.log("scImgMgr.xInitZms::Error : "+e);
			}
		}
	}
}
scImgMgr.xInitZm = function(pAnc) {
	var vOpts = pAnc.fOpts;
	pAnc.fImg = scPaLib.findNode("des:img", pAnc);
	pAnc.fOver = scDynUiMgr.addElement("div", this.fDisplayRoot,vOpts.clsPre+"Over", null, {display:"none"});
	pAnc.fOver.fAnc = pAnc;
	pAnc.fOver.onclick=function(){return scImgMgr.xClsZm(this.fAnc);}
	pAnc.fCvs = scDynUiMgr.addElement("div", this.fDisplayRoot,vOpts.clsPre+"Cvs", null, {display:"none"});
	pAnc.fCvs.fAnc = pAnc;
	pAnc.fCvs.setAttribute("role", "dialog");
	pAnc.fCvs.onclick=function(){return scImgMgr.xClsZm(this.fAnc);}
	if(this.fNavie6 && this.xReadStyle(pAnc.fCvs,"position") == "fixed") pAnc.fCvs.style.position = "absolute"; // IE6 does not display fixed content properly.
	pAnc.fFra = scDynUiMgr.addElement("div", pAnc.fCvs,vOpts.clsPre+"Fra", null, {visibility:"hidden"});
	pAnc.fFra.onclick=function(pEvt){
		var vEvt = scImgMgr.xGetEvt(pEvt);
		vEvt.cancelBubble = true;
		if (vEvt.stopPropagation) vEvt.stopPropagation();
	}
	var vCo = pAnc.fCo = scDynUiMgr.addElement("div",pAnc.fFra,vOpts.clsPre+"Co");
	vCo.style.position = "relative";
	var vImgBtn = null;
	var vImg = null;
	if (vOpts.type == "iframe"){
		vImg = vCo.fImg = scDynUiMgr.addElement("iframe",vCo,null);
		vImg.fAnc = pAnc;
		vCo.fOvr = scDynUiMgr.addElement("div",vCo,null);
		vCo.fOvr.fAnc = pAnc;
		vCo.fOvr.onclick=function(){return scImgMgr.xClsZm(this.fAnc);}
		vCo.fOvr.style.cursor = "pointer";
	} else {
		var vAddMag = !this.fNavie6 & vOpts.mag > 0;
		if (!vAddMag){
			vImgBtn = scImgMgr.xAddBtn(vCo,pAnc,scImgMgr.fTypZm,"BtnImgCls","","");
			vImgBtn.innerHTML = "";
			vImgBtn.style.display = "inline-block";
		}
		vImg = vCo.fImg = scDynUiMgr.addElement("img",(vAddMag ? vCo : vImgBtn),null);
		vImg.fAnc = pAnc;
//		vImg.style.cursor = "pointer";
		vImg.setAttribute("alt",pAnc.fImg && pAnc.fImg.alt ? pAnc.fImg.alt : "");
		vImg.onload = scImgMgr.sLoadZmImg;
		if (vAddMag){
			vImg.onmouseover = this.sZmMagShow;
			vImg.onmousemove = this.sZmImgMove;
			var vMag = vCo.fImg.fMag = scDynUiMgr.addElement("div", vCo, vOpts.clsPre+"Mag", null, {display:"none"});
			vMag.fClass = vOpts.clsPre+"Mag";
			vMag.fClassMax = vOpts.clsPre+"MagMax";
			vMag.style.position="absolute";
			vMag.fAnc = pAnc;
			vMag.style.backgroundColor = "white";
			vMag.style.backgroundImage = 'url("'+pAnc.fZmUri+'")';
			vMag.style.zIndex = "100";
			vMag.onmousemove = this.sZmMagMove;
			vMag.onmouseout = this.sZmMagHide;
			if (vOpts.magMax > 0) vMag.onclick = this.sZmMagClick;
		}
	}
	if (vOpts.toolbar == 1){
		pAnc.fTlb = scDynUiMgr.addElement("div",pAnc.fFra,vOpts.clsPre+"Tlb");
		pAnc.fClsBtn = scImgMgr.xAddBtn(pAnc.fTlb,pAnc,scImgMgr.fTypZm,"BtnCls",this.xGetStr(30),this.xGetStr(31));
		if (vOpts.titlePath){
			var vTiSrc =scPaLib.findNode(vOpts.titlePath, pAnc);
			if (vTiSrc){
				var vTiElt = scDynUiMgr.addElement("div",pAnc.fTlb,vOpts.clsPre+"Ti");
				vTiElt.appendChild(vTiSrc.cloneNode(true));
			}
		}
	} else pAnc.fClsBtn = vImgBtn;
	var vResizer = {
		onResizedDes : function(pOwnerNode, pEvent) {},
		onResizedAnc : function(pOwnerNode, pEvent) {
			if(pEvent.phase==1) {
				if(scImgMgr.fCurrItem == pOwnerNode.fAnc) scImgMgr.xRedrawZm(pOwnerNode.fAnc);
			}
		}
	}
	scSiLib.addRule(vCo.fImg, vResizer);
}
scImgMgr.xOpenZm = function(pAnc) {
	if ("scDragMgr" in window) { // do not open the zoom if the image is in a scDragMgr label that has just been dropped.
		var vAncs = scPaLib.findNodes("anc:",pAnc);
		for(var i=0; i<vAncs.length; i++) if (vAncs[i].fGroup && vAncs[i].fGroup._isThresholdExceeded) return;
	}
	if(!pAnc.fCo) scImgMgr.xInitZm(pAnc);
	if(this.xReadStyle(pAnc.fCvs,"position") == "absolute") window.scroll(0,0); // if position:absolute, we must scroll the SS into view.
	scImgMgr.fadeInTask.initTask(pAnc);
	scTiLib.addTaskNow(scImgMgr.fadeInTask);
	if(pAnc.fCo && !pAnc.fCo.fImg.src) pAnc.fCo.fImg.setAttribute("src", pAnc.fZmUri);
	else scImgMgr.xRedrawZm(pAnc);
	scImgMgr.fCurrItem = pAnc;
	pAnc.fKeyUpOld = document.onkeyup;
	document.onkeyup = scImgMgr.xKeyMgr;
	this.xNotifyListeners("onZoomOpen", pAnc);
	this.xNotifyListeners("onOverlayOpen", pAnc);
	this.xToggleFocusables();
	this.xFocus(pAnc.fClsBtn);
}
scImgMgr.xClsZm = function(pAnc) {
	scImgMgr.fadeOutTask.initTask(pAnc,function(){
		scImgMgr.xNotifyListeners("onZoomClose", pAnc);
		scImgMgr.xNotifyListeners("onOverlayClose", pAnc);
	});
	scTiLib.addTaskNow(scImgMgr.fadeOutTask);
	document.onkeyup = pAnc.fKeyUpOld;
	scImgMgr.fCurrItem = null;
	scImgMgr.xToggleFocusables();
	scImgMgr.xFocus(pAnc);
}
scImgMgr.sLoadZmImg = function() {
	var vAnc = this.fAnc;
	vAnc.fDefHeight = this.height;
	vAnc.fDefWidth = this.width;
	vAnc.fRatio = vAnc.fDefWidth/vAnc.fDefHeight;
	vAnc.fDeltaHeight = scImgMgr.xGetEltHeight(vAnc.fFra) - scImgMgr.xGetEltHeight(vAnc.fCo) + scCoLib.toInt(scImgMgr.xReadStyle(vAnc.fCvs,"paddingTop")) + scCoLib.toInt(scImgMgr.xReadStyle(vAnc.fCvs,"paddingBottom"));
	vAnc.fDeltaWidth = scImgMgr.xGetEltWidth(vAnc.fFra) - scImgMgr.xGetEltWidth(vAnc.fCo) + scCoLib.toInt(scImgMgr.xReadStyle(vAnc.fCvs,"paddingLeft")) + scCoLib.toInt(scImgMgr.xReadStyle(vAnc.fCvs,"paddingRight"));
	vAnc.fFra.style.position="absolute";
	scImgMgr.xRedrawZm(vAnc);
	vAnc.fFra.style.visibility="";
}
scImgMgr.sZmMagShow = function(pEvt) {
	var vEvt = scImgMgr.xGetEvt(pEvt);
	var vImg = vEvt.target;
	try {
		var vMag = vImg.fMag;
		var vAnc = vImg.fAnc
		if (!vMag.fEnabled) return;
		vMag.fAct = true;
		var vX = vEvt.offsetX || vEvt.layerX;
		var vY = vEvt.offsetY || vEvt.layerY;
		if (vMag.fMaxDefault) scImgMgr.xZmMagMax(vMag,true);
		else scImgMgr.xZmMagUpdate(vAnc, vMag, vX, vY,true);
		vMag.style.display = "";
	} catch(e){
		scCoLib.log("scImgMgr.sZmMagShow::Error : "+e);
	}
}
scImgMgr.sZmImgMove = function(pEvt) {
	var vEvt = scImgMgr.xGetEvt(pEvt);
	var vImg = vEvt.target;
	try {
		var vMag = vImg.fMag;
		if (!vMag.fEnabled) return;
		if (!vMag.fAct) scImgMgr.sZmMagShow(pEvt);
	} catch(e){
		scCoLib.log("scImgMgr.sZmImgMove::Error : "+e);
	}
}
scImgMgr.sZmMagHide = function(pEvt) {
	var vEvt = scImgMgr.xGetEvt(pEvt);
	var vMag = vEvt.target;
	try {
		vMag.fAct = false;
		scImgMgr.xZmMagMax(vMag,false);
		vMag.style.display = "none";
	} catch(e){
		scCoLib.log("scImgMgr.sZmMagHide::Error : "+e);
	}
}
scImgMgr.sZmMagMove = function(pEvt) {
	var vEvt = scImgMgr.xGetEvt(pEvt);
	var vMag = vEvt.target;
	var vAnc = vMag.fAnc
	var vX = vMag.offsetLeft + (vEvt.offsetX || vEvt.layerX);
	var vY = vMag.offsetTop + (vEvt.offsetY || vEvt.layerY);
	try {
		if (!vMag.fMax) {
			scImgMgr.xZmMagUpdate(vAnc,vMag,vX,vY,true);
		} else if (vMag.fMaxDefault || vAnc.fOpts.magPan == 1) {
			scImgMgr.xZmMagUpdate(vAnc,vMag,vX,vY,false);
		}
	} catch(e){
		scCoLib.log("scImgMgr.sZmMagMove::Error : "+e);
	}
}
scImgMgr.sZmMagClick = function(pEvt) {
	var vEvt = scImgMgr.xGetEvt(pEvt);
	var vMag = vEvt.target;
	if (!vMag.fMaxDefault) {
		scImgMgr.xZmMagMax(vMag,!vMag.fMax);
		if (!vMag.fMax) scImgMgr.sZmMagMove(vEvt);
	}
}
scImgMgr.xZmMagUpdate = function(pAnc, pMag, pX, pY, pUpdtPos) {
	try {
		var vTop = Math.round(Math.min(pAnc.fCurrHeight-pMag.fHeight, Math.max(0,pY - pMag.fHeight/2)));
		var vLeft = Math.round(Math.min(pAnc.fCurrWidth-pMag.fWidth, Math.max(0,pX - pMag.fWidth/2)));
		if (pUpdtPos) {
			pMag.style.left = (vLeft)+"px";
			pMag.style.top = (vTop)+"px";
		}
		pMag.style.backgroundPosition = Math.round(Math.min(vLeft/(pAnc.fCurrWidth-pMag.fWidth)*100,100))+"% "+Math.round(Math.min(vTop/(pAnc.fCurrHeight-pMag.fHeight)*100,100))+"%";
	} catch(e){
		scCoLib.log("scImgMgr.xZmMagUpdate::Error : "+e);
	}
}
scImgMgr.xZmMagMax = function(pMag, pMax) {
	try {
		var vAnc = pMag.fAnc;
		if (pMax){
			pMag.fMax = true;
			pMag.style.top = "0px";
			pMag.style.left = "0px";
			pMag.style.width = vAnc.fCurrWidth+"px";
			pMag.style.height = vAnc.fCurrHeight+"px";
			scImgMgr.xSwitchClass(pMag, pMag.fClass, pMag.fClassMax);
		} else {
			pMag.fMax = false;
			pMag.style.width = pMag.fWidth+"px";
			pMag.style.height = pMag.fHeight+"px";
			scImgMgr.xSwitchClass(pMag, pMag.fClassMax, pMag.fClass);
		}
	} catch(e){
		scCoLib.log("scImgMgr.xZmMagMax::Error : "+e);
	}
}
scImgMgr.xRedrawZm = function(pAnc) {
	try {
		if (pAnc.fOpts.type == "iframe") return;
		var vCoHeight = pAnc.fCvs.clientHeight - pAnc.fDeltaHeight;
		var vCoWidth = pAnc.fCvs.clientWidth - pAnc.fDeltaWidth;
		if (vCoHeight == 0 || vCoWidth == 0) return;
		var vCoRatio = vCoWidth/vCoHeight;
		var vFra = pAnc.fFra;
		var vCo = pAnc.fCo;
		var vImg = vCo.fImg;
		var vNewHeight = 0;
		var vNewWidth = 0;
		if (pAnc.fRatio <= vCoRatio && vCoHeight < pAnc.fDefHeight) vNewHeight = vCoHeight;
		if (pAnc.fRatio >= vCoRatio && vCoWidth < pAnc.fDefWidth) vNewWidth = vCoWidth;
		vImg.style.width = (vNewWidth>0 ? scCoLib.toInt(vNewWidth)+"px" : "");
		vImg.style.height = (vNewHeight>0 ? scCoLib.toInt(vNewHeight)+"px" : "");
		var vImgHeight = pAnc.fCurrHeight = scCoLib.toInt(vNewHeight > 0 ? vNewHeight : vNewWidth > 0 ? vNewWidth/pAnc.fRatio : pAnc.fDefHeight);
		var vImgWidth = pAnc.fCurrWidth = scCoLib.toInt(vNewWidth > 0 ? vNewWidth : vNewHeight > 0 ? vNewHeight*pAnc.fRatio : pAnc.fDefWidth);
		vCo.style.width = vImgWidth+"px";
		vCo.style.height = vImgHeight+"px";
		if (pAnc.fOpts.mag){
			var vMag = vImg.fMag;
			vMag.fEnabled = vImgWidth < pAnc.fDefWidth;
			vMag.fWidth = scCoLib.toInt(vImgWidth * pAnc.fOpts.magScale);
			vMag.fHeight = scCoLib.toInt(vImgHeight * pAnc.fOpts.magScale);
			vMag.style.width = vMag.fWidth+"px";
			vMag.style.height = vMag.fHeight+"px";
		}
		vFra.style.marginTop = scCoLib.toInt((vCoHeight - vImgHeight) / 2) + "px";
		vFra.style.marginLeft = scCoLib.toInt((vCoWidth - vImgWidth) / 2) + "px";
		pAnc.fOver.style.height = (scImgMgr.xPageHeight()>scImgMgr.xClientHeight() ? scImgMgr.xPageHeight()+"px" : "");
		pAnc.fOver.style.width = scCoLib.toInt(scImgMgr.xPageWidth()>scImgMgr.xClientWidth() ? scImgMgr.xPageWidth() : scImgMgr.xClientWidth())+"px";
		
	} catch(e){
		scCoLib.log("scImgMgr.xRedrawZm::Error : "+e);
	}
}

/* === Slide-show manager =================================================== */
scImgMgr.xInitSss = function(pCo) {
	for(var i=0; i<this.fPathGal.length; i++) {
		var vGals = scPaLib.findNodes(this.fPathGal[i].fPath,pCo);
		for(var j=0; j<vGals.length; j++) {
			var vGal = vGals[j];
			vGal.fOpts = this.fPathGal[i].fOpts;
			try {
				vGal.fAncs = scPaLib.findNodes("des:a.galPvLnk", vGal);
				// Init anchors & images
				for(var k=0; k<vGal.fAncs.length; k++) {
					var vAnc = vGal.fAncs[k];
					vAnc.fSsUri = vAnc.href;
					vAnc.fIdx = k;
					vAnc.href = "#";
					vAnc.target = "_self";
					vAnc.fName=this.fTypGal+"Pv";
					if (vAnc.title && vAnc.title.length>0){
						vAnc.fTitle=vAnc.title;
						vAnc.title = scImgMgr.xGetStr(27) + " " + vAnc.fTitle;
					}
					vAnc.onclick=function(){return scImgMgr.xBtnMgr(this);}
					vAnc.fImg = scPaLib.findNode("des:img.imgPv", vAnc);
					if (vGal.fOpts.centerThumbs) vAnc.fImg.style.marginTop = ((scCoLib.toInt(this.xReadStyle(vAnc, "height")) - vAnc.fImg.height - scCoLib.toInt(this.xReadStyle(vAnc.fImg, "borderTopWidth")) - scCoLib.toInt(this.xReadStyle(vAnc.fImg, "borderBottomWidth"))) / 2) + "px";
					vAnc.fObj = vGal;
				}
				// Init SlideShow elements
				this.xInitSs(vGal);
				vGal.fSsStep = scImgMgr.fDefaultStep;
				vGal.fName="gal";
			} catch(e){
				scCoLib.log("scImgMgr.onLoad::Gallery init Error : "+e);
			}
		}
	}
}
scImgMgr.xInitSs = function(pAlbFra) {
	var vOpts = pAlbFra.fOpts;
	pAlbFra.fOver = scDynUiMgr.addElement("div",this.fDisplayRoot,vOpts.clsPre+"Over", null, {display:"none"});
	pAlbFra.fOver.fAlbFra = pAlbFra;
	pAlbFra.fOver.onclick=function(){return scImgMgr.xClsSs(this.fAlbFra);}
	pAlbFra.fCvs = scDynUiMgr.addElement("div",this.fDisplayRoot,vOpts.clsPre+"Cvs", null, {display:"none"});
	pAlbFra.fCvs.setAttribute("role", "dialog");
	pAlbFra.fFra = scDynUiMgr.addElement("div",pAlbFra.fCvs,vOpts.clsPre+"Fra");
	if(this.fNavie6 && this.xReadStyle(pAlbFra.fCvs,"position") == "fixed") pAlbFra.fCvs.style.position = "absolute"; // IE6 does not display fixed content properly.
	
	pAlbFra.fSsCo = scDynUiMgr.addElement("ul",pAlbFra.fFra,vOpts.clsPre+"Co");
	pAlbFra.fSsImgFras = [];
	for(var i=0; i<pAlbFra.fAncs.length; i++) {
		pAlbFra.fSsImgFras[i] = scDynUiMgr.addElement("li",pAlbFra.fSsCo,vOpts.clsPre+"ImgFra", null, {visibility:"hidden"});
		pAlbFra.fSsImgFras[i].fImg = scDynUiMgr.addElement("img",pAlbFra.fSsImgFras[i],null);
		pAlbFra.fSsImgFras[i].fImg.setAttribute("alt",pAlbFra.fAncs[i].fImg.alt ? pAlbFra.fAncs[i].fImg.alt : "");
		pAlbFra.fSsImgFras[i].fImg.onload = scImgMgr.sLoadSsImg;
	}
	pAlbFra.fSsTbr = scDynUiMgr.addElement("div",pAlbFra.fFra,vOpts.clsPre+"Tbr");
	//pAlbFra.fSsTbr.setAttribute("role", "toolbar");
	pAlbFra.fSsTi = scDynUiMgr.addElement("div",pAlbFra.fSsTbr,vOpts.clsPre+"Ti");
	pAlbFra.fSsTi.setAttribute("aria-live", "polite");
	scImgMgr.xAddSep(pAlbFra.fSsTbr);
	if (pAlbFra.fAncs.length>1){
		pAlbFra.fSsBtnPrv = scImgMgr.xAddBtn(pAlbFra.fSsTbr,pAlbFra,this.fTypGal,"BtnPrv",scImgMgr.xGetStr(0),scImgMgr.xGetStr(1));
		scImgMgr.xAddSep(pAlbFra.fSsTbr);
		pAlbFra.fSsBtnPly = scImgMgr.xAddBtn(pAlbFra.fSsTbr,pAlbFra,this.fTypGal,"BtnPly",scImgMgr.xGetStr(6),scImgMgr.xGetStr(7));
		pAlbFra.fSsBtnPse = scImgMgr.xAddBtn(pAlbFra.fSsTbr,pAlbFra,this.fTypGal,"BtnPse",scImgMgr.xGetStr(8),scImgMgr.xGetStr(9));
		pAlbFra.fSsBtnPse.style.display = "none";
		scImgMgr.xAddSep(pAlbFra.fSsTbr);
		pAlbFra.fSsBtnNxt = scImgMgr.xAddBtn(pAlbFra.fSsTbr,pAlbFra,this.fTypGal,"BtnNxt",scImgMgr.xGetStr(2),scImgMgr.xGetStr(3));
		scImgMgr.xAddSep(pAlbFra.fSsTbr);
	}
	pAlbFra.fSsBtnCls = scImgMgr.xAddBtn(pAlbFra.fSsTbr,pAlbFra,this.fTypGal,"BtnCls",scImgMgr.xGetStr(4),scImgMgr.xGetStr(5));
	scImgMgr.xAddSep(pAlbFra.fSsTbr);
	pAlbFra.fSsCount = scDynUiMgr.addElement("span",pAlbFra.fSsTbr,vOpts.clsPre+"Count")
}
scImgMgr.xSsStart = function(pAlbFra) {
	scImgMgr.xOpenSs(pAlbFra,pAlbFra.fAncs[0]);
	scImgMgr.xPlySs(pAlbFra);
}
scImgMgr.xOpenSs = function(pAlbFra,pAnc) {
	if(this.xReadStyle(pAlbFra.fCvs,"position") == "absolute") window.scroll(0,0); // if position:absolute, we must scroll the SS into view.
	scImgMgr.fadeInTask.initTask(pAlbFra);
	scTiLib.addTaskNow(scImgMgr.fadeInTask);
	scImgMgr.xUdtSs(pAlbFra,pAnc);
	scImgMgr.fCurrItem = pAlbFra;
	pAlbFra.fInitAnc = pAnc;
	pAlbFra.fKeyUpOld = document.onkeyup;
	document.onkeyup = scImgMgr.xKeyMgr;
	this.xNotifyListeners("onAnimationOpen", pAlbFra);
	this.xNotifyListeners("onOverlayOpen", pAlbFra);
	this.xToggleFocusables();
	this.xFocus(pAlbFra.fSsBtnPly);
}
scImgMgr.xUdtSs = function(pAlbFra,pNewAnc) {
	var vOpts = pAlbFra.fOpts;
	pAlbFra.fSsHasPrv = pNewAnc.fIdx != 0;
	pAlbFra.fSsHasNxt = pNewAnc.fIdx != pAlbFra.fAncs.length - 1;
	if(!pAlbFra.fSsImgFras[pNewAnc.fIdx].fImg.src) pAlbFra.fSsImgFras[pNewAnc.fIdx].fImg.setAttribute("src", pNewAnc.fSsUri);
	if (pAlbFra.fSsHasNxt){
		pAlbFra.fNxtSsAnc = pAlbFra.fAncs[Math.min(pNewAnc.fIdx + 1,pAlbFra.fAncs.length - 1)];
		if(!pAlbFra.fSsImgFras[pAlbFra.fNxtSsAnc.fIdx].fImg.src) pAlbFra.fSsImgFras[pAlbFra.fNxtSsAnc.fIdx].fImg.setAttribute("src", pAlbFra.fNxtSsAnc.fSsUri);
	} else if(pAlbFra.fSsAutoPly) scImgMgr.xPseSs(pAlbFra);
	if (pAlbFra.fSsHasPrv){
		pAlbFra.fPrvSsAnc = pAlbFra.fAncs[Math.max(pNewAnc.fIdx - 1,0)];
		if(!pAlbFra.fSsImgFras[pAlbFra.fPrvSsAnc.fIdx].fImg.src) pAlbFra.fSsImgFras[pAlbFra.fPrvSsAnc.fIdx].fImg.setAttribute("src", pAlbFra.fPrvSsAnc.fSsUri);
	}
	pAlbFra.fSsTi.innerHTML = (pNewAnc.fTitle ? pNewAnc.fTitle : "");
	pAlbFra.fSsCount.innerHTML = (pNewAnc.fIdx+1)+"/"+pAlbFra.fAncs.length;
	if (pAlbFra.fSsBtnPrv) {
		scImgMgr.xSwitchClass(pAlbFra.fSsBtnPrv,(pAlbFra.fSsHasPrv?vOpts.clsPre+"BtnNoPrv":vOpts.clsPre+"BtnPrv"),(pAlbFra.fSsHasPrv?vOpts.clsPre+"BtnPrv":vOpts.clsPre+"BtnNoPrv"));
		if (pAlbFra.fSsHasPrv) pAlbFra.fSsBtnPrv.removeAttribute("aria-disabled");
		else pAlbFra.fSsBtnPrv.setAttribute("aria-disabled", "true");
	}
	if (pAlbFra.fSsBtnNxt) {
		scImgMgr.xSwitchClass(pAlbFra.fSsBtnNxt,(pAlbFra.fSsHasNxt?vOpts.clsPre+"BtnNoNxt":vOpts.clsPre+"BtnNxt"),(pAlbFra.fSsHasNxt?vOpts.clsPre+"BtnNxt":vOpts.clsPre+"BtnNoNxt"));
		if (pAlbFra.fSsHasNxt) pAlbFra.fSsBtnNxt.removeAttribute("aria-disabled");
		else pAlbFra.fSsBtnNxt.setAttribute("aria-disabled", "true");
	}

	scImgMgr.switchSsTask.initTask(pAlbFra,pNewAnc);
	scTiLib.addTaskNow(scImgMgr.switchSsTask);
}
scImgMgr.xNxtSs = function(pAlbFra) {
	if (!pAlbFra.fSsHasNxt) return false;
	scImgMgr.xUdtSs(pAlbFra,pAlbFra.fNxtSsAnc);
	return true;
}
scImgMgr.xPrvSs = function(pAlbFra) {
	if (!pAlbFra.fSsHasPrv) return false;
	scImgMgr.xUdtSs(pAlbFra,pAlbFra.fPrvSsAnc);
	return true;
}
scImgMgr.xClsSs = function(pAlbFra) {
	scImgMgr.fadeOutTask.initTask(pAlbFra,function(){
		scImgMgr.xNotifyListeners("onAnimationClose", pAlbFra);
		scImgMgr.xNotifyListeners("onOverlayClose", pAlbFra);
	});
	scTiLib.addTaskNow(scImgMgr.fadeOutTask);
	document.onkeyup = pAlbFra.fKeyUpOld;
	pAlbFra.fSsAutoPly = false;
	scImgMgr.fCurrItem = null;
	scImgMgr.xToggleFocusables();
	scImgMgr.xFocus(pAlbFra.fInitAnc);
}
scImgMgr.xPlySs = function(pAlbFra) {
	if (pAlbFra.fAncs.length<=1) return;
	pAlbFra.fSsAutoPly = true;
	pAlbFra.fSsBtnPly.style.display="none";
	pAlbFra.fSsBtnPse.style.display="";
	scImgMgr.xFocus(pAlbFra.fSsBtnPse);
	if (! scImgMgr.xNxtSs(pAlbFra)) scImgMgr.xUdtSs(pAlbFra,pAlbFra.fAncs[0]);
	pAlbFra.fNxtSsProc = window.setTimeout(scImgMgr.xAutoSs, pAlbFra.fSsStep);
}
scImgMgr.xPseSs = function(pAlbFra) {
	if (pAlbFra.fAncs.length<=1) return;
	pAlbFra.fSsAutoPly = false;
	pAlbFra.fSsBtnPly.style.display="";
	pAlbFra.fSsBtnPse.style.display="none";
	scImgMgr.xFocus(pAlbFra.fSsBtnPly);
	window.clearTimeout(pAlbFra.fNxtSsProc);
//	pAlbFra.fNxtSsProc = -1;
}
scImgMgr.sLoadSsImg = function() {
	this.style.marginTop = (this.parentNode.clientHeight - this.clientHeight) / 2 + "px";
}
scImgMgr.xAutoSs = function() {
	if (scImgMgr.fCurrItem){
		if (scImgMgr.fCurrItem.fSsAutoPly){
			scImgMgr.xNxtSs(scImgMgr.fCurrItem);
			if (scImgMgr.fCurrItem.fSsHasNxt) scImgMgr.fCurrItem.fNxtSsProc = window.setTimeout(scImgMgr.xAutoSs, scImgMgr.fCurrItem.fSsStep);
		}
	}
}
scImgMgr.switchSsTask = {
	fIdx: -1,
	fRateOld: [.9, .8, .7, .6, .5, .4, .3, .2, .1],
	fRateNew: [.1, .2, .3, .4, .5, .6, .7, .8, .9],
	execTask : function(){
		while(this.fEndTime < (Date.now ? Date.now() : new Date().getTime()) && this.fIdx < this.fRateOld.length) {
			this.fIdx++;
			this.fEndTime += 100;
		}
		this.fIdx++;
		this.fEndTime += 100;
		if(this.fIdx >= this.fRateOld.length) {
			if (this.fAlbFra.fCurrSsAnc) scImgMgr.xSetOpacity(this.fAlbFra.fSsImgFras[this.fAlbFra.fCurrSsAnc.fIdx],0);
			if (this.fAlbFra.fCurrSsAnc && this.fAlbFra.fCurrSsAnc.fIdx != this.fNewAnc.fIdx) this.fAlbFra.fSsImgFras[this.fAlbFra.fCurrSsAnc.fIdx].style.visibility = "hidden";
			scImgMgr.xSetOpacity(this.fAlbFra.fSsImgFras[this.fNewAnc.fIdx],1);
			this.fAlbFra.fCurrSsAnc = this.fNewAnc;
			this.fIsRunning = false;
			return false;
		}
		if (this.fAlbFra.fCurrSsAnc) scImgMgr.xSetOpacity(this.fAlbFra.fSsImgFras[this.fAlbFra.fCurrSsAnc.fIdx], this.fRateOld[this.fIdx]);
		scImgMgr.xSetOpacity(this.fAlbFra.fSsImgFras[this.fNewAnc.fIdx], this.fRateNew[this.fIdx]);
		return true;
	},
	terminate : function(){
		this.fIdx = this.fRateOld.length;
		this.execTask();
	},
	initTask : function(pAlbFra,pNewAnc){
		this.fAlbFra = pAlbFra;
		if (this.fIsRunning) this.terminate();
		this.fNewAnc = pNewAnc;
		scImgMgr.xSetOpacity(this.fAlbFra.fSsImgFras[this.fNewAnc.fIdx],0);
		this.fAlbFra.fSsImgFras[this.fNewAnc.fIdx].style.visibility = "";
		
		this.fEndTime = ( Date.now  ? Date.now() : new Date().getTime() ) + 100;
		this.fIdx = -1;
		this.fIsRunning = true;
	}
}

/* === Tasks ================================================================ */
scImgMgr.fadeInTask = {
	fIdx: -1,
	fRate: [.1, .2, .3, .4, .5, .6, .7, .8, .9],
	execTask : function(){
		while(this.fEndTime < (Date.now ? Date.now() : new Date().getTime()) && this.fIdx < this.fRate.length) {
			this.fIdx++;
			this.fEndTime += 100;
		}
		this.fIdx++;
		this.fEndTime += 100;
		if(this.fIdx >= this.fRate.length) {
			scImgMgr.xSetOpacity(this.fObj.fOver,scImgMgr.fOverAlpha);
			scImgMgr.xSetOpacity(this.fObj.fCvs,1);
			return false;
		}
		scImgMgr.xSetOpacity(this.fObj.fOver, Math.min(this.fRate[this.fIdx], scImgMgr.fOverAlpha));
		scImgMgr.xSetOpacity(this.fObj.fCvs, this.fRate[this.fIdx]);
		return true;
	},
	terminate : function(){
		this.fIdx = this.fRate.length;
		this.execTask();
	},
	initTask : function(pObj){
		this.fObj = pObj;
		this.fEndTime = ( Date.now  ? Date.now() : new Date().getTime() ) + 100;
		scImgMgr.xSetOpacity(this.fObj.fOver, .0);
		scImgMgr.xSetOpacity(this.fObj.fCvs, .0);
		this.fObj.fOver.style.display = "";
		this.fObj.fOver.style.height = (scImgMgr.xPageHeight()>scImgMgr.xClientHeight() ? scImgMgr.xPageHeight()+"px" : "");
		this.fObj.fOver.style.width = (scImgMgr.xPageWidth()>scImgMgr.xClientWidth() ? scImgMgr.xPageWidth() : scImgMgr.xClientWidth())+"px";
		this.fObj.fCvs.style.display = "";
		this.fIdx = -1;
	}
}
scImgMgr.fadeOutTask = {
	fIdx: -1,
	fRate: [.8, .6, .4, .3, .2, .1],
	execTask : function(){
		while(this.fEndTime < (Date.now ? Date.now() : new Date().getTime()) && this.fIdx < this.fRate.length) {
			this.fIdx++;
			this.fEndTime += 100;
		}
		this.fIdx++;
		this.fEndTime += 100;
		if(this.fIdx >= this.fRate.length) {
			scImgMgr.xSetOpacity(this.fObj.fOver,0);
			scImgMgr.xSetOpacity(this.fObj.fCvs,0);
			this.fObj.fOver.style.display = "none";
			this.fObj.fCvs.style.display = "none";
			if (this.fObj.fCurrSsAnc) scImgMgr.xSetOpacity(this.fObj.fSsImgFras[this.fObj.fCurrSsAnc.fIdx],0);
			if (this.fObj.fCurrSsAnc) this.fObj.fSsImgFras[this.fObj.fCurrSsAnc.fIdx].style.visibility = "hidden";
			if (this.fEndFunc) this.fEndFunc();
			return false;
		}
		scImgMgr.xSetOpacity(this.fObj.fOver, Math.min(this.fRate[this.fIdx], scImgMgr.fOverAlpha));
		scImgMgr.xSetOpacity(this.fObj.fCvs, this.fRate[this.fIdx]);
		return true;
	},
	terminate : function(){
		this.fIdx = this.fRate.length;
		this.execTask();
	},
	initTask : function(pObj, pEndFunc){
		this.fObj = pObj;
		this.fEndFunc = pEndFunc;
		this.fEndTime = ( Date.now  ? Date.now() : new Date().getTime() ) + 100;
		this.fIdx = -1;
	}
}
/** scImgMgr.FadeEltTask : scTiLib task that fades a given element in or out.
 * @param pElt element to fade.
 * @param pDir fade direction : 0=out, 1=in.
 * @param pInstant optionnal parameter if true no animation.
 */
scImgMgr.FadeEltTask = function(pElt,pDir,pInstant){
	this.fRate = new Array();
	this.fRate[0] = [.9, .85, .8, .7, .6, .5, .4, .3, .2, .15, .1];
	this.fRate[1] = [.1, .15, .2, .3, .4, .5, .6, .7, .8, .85, .9];
	try{
		this.fElt = pElt;
		this.fDir = (pDir >= 1 ? 1 : 0);
		if (pInstant) {
			this.terminate();
			return;
		}
		if (this.fElt.fFadeTask) {
			this.fElt.fFadeTask.changeDir(this.fDir);
		} else {
			scImgMgr.xStartOpacityEffect(this.fElt, 1-this.fDir);
			this.fEndTime = ( Date.now  ? Date.now() : new Date().getTime() ) + 100;
			this.fIdx = -1;
			this.fElt.fFadeTask = this;
			scTiLib.addTaskNow(this);
		}
	}catch(e){scCoLib.log("ERROR scImgMgr.FadeEltTask: "+e);}
}
scImgMgr.FadeEltTask.prototype.execTask = function(){
	while(this.fEndTime < (Date.now ? Date.now() : new Date().getTime()) && this.fIdx < this.fRate[this.fDir].length) {
		this.fIdx++;
		this.fEndTime += 100;
	}
	this.fIdx++;
	this.fEndTime += 100;
	if(this.fIdx >= this.fRate[this.fDir].length) {
		scImgMgr.xEndOpacityEffect(this.fElt, this.fDir);
		this.fElt.fFadeTask = null;
		return false;
	}
	scImgMgr.xSetOpacity(this.fElt, this.fRate[this.fDir][this.fIdx]);
	return true;
}
scImgMgr.FadeEltTask.prototype.changeDir = function(pDir){
	var vDir = (pDir >= 1 ? 1 : 0)
	if (vDir != this.fDir) this.fIdx = this.fRate[this.fDir].length - this.fIdx - 1;
	this.fDir = vDir;
}
scImgMgr.FadeEltTask.prototype.terminate = function(){
	this.fIdx = this.fRate[this.fDir].length;
	this.execTask();
}

/* === Toolbox ============================================================== */
/** scImgMgr.xReadStyle : cross-browser css rule reader */
scImgMgr.xReadStyle = function(pElt, pProp) {
	try {
		var vVal = null;
		if (pElt.style[pProp]) {
			vVal = pElt.style[pProp];
		} else if (pElt.currentStyle) {
			vVal = pElt.currentStyle[pProp];
		} else {
			var vDefaultView = pElt.ownerDocument.defaultView;
			if (vDefaultView && vDefaultView.getComputedStyle) {
				var vStyle = vDefaultView.getComputedStyle(pElt, null);
				var vProp = pProp.replace(/([A-Z])/g,"-$1").toLowerCase();
				if (vStyle[vProp]) return vStyle[vProp];
				else vVal = vStyle.getPropertyValue(vProp);
			}
		}
		return vVal.replace(/\"/g,""); //Opera returns certain values quoted (literal colors).
	} catch (e) {
		return null;
	}
}
/** scImgMgr.xGetEltTop. */
scImgMgr.xGetEltTop = function(pElt, pRoot) {
	var vY;
	var vRoot = pRoot || null;
	vY = scCoLib.toInt(pElt.offsetTop);
	if (pElt.offsetParent != vRoot && pElt.offsetParent.tagName.toLowerCase() != 'body' && pElt.offsetParent.tagName.toLowerCase() != 'html') {
		vY -= pElt.offsetParent.scrollTop;
		vY += this.xGetEltTop(pElt.offsetParent, vRoot);
	}
	return vY;
}
/** scImgMgr.xGetEltLeft. */
scImgMgr.xGetEltLeft = function(pElt, pRoot) {
	var vX;
	var vRoot = pRoot || null;
	vX = scCoLib.toInt(pElt.offsetLeft);
	if (pElt.offsetParent != vRoot && pElt.offsetParent.tagName.toLowerCase() != 'body' && pElt.offsetParent.tagName.toLowerCase() != 'html') {
		vX -= pElt.offsetParent.scrollLeft;
		vX += this.xGetEltLeft(pElt.offsetParent, vRoot);
	}
	return vX;
}
/** scImgMgr.xGetEltWidth. */
scImgMgr.xGetEltWidth = function(pElt) {
	return(scCoLib.toInt(pElt.style.pixelWidth || pElt.offsetWidth)+(this.fNavie? (scCoLib.toInt(pElt.currentStyle.borderRightWidth)+scCoLib.toInt(pElt.currentStyle.borderLeftWidth)):0));
}
/** scImgMgr.xGetEltHeight. */
scImgMgr.xGetEltHeight = function(pElt) {
	return(scCoLib.toInt(pElt.style.pixelHeight || pElt.offsetHeight)+(this.fNavie? (scCoLib.toInt(pElt.currentStyle.borderTopWidth)+scCoLib.toInt(pElt.currentStyle.borderBottomWidth)):0));
}
/** scImgMgr.xPageHeight. */
scImgMgr.xPageHeight = function() {
	if(this.fPgeFra){
		if(this.fPgeFra.offsetHeight) return this.fPgeFra.offsetHeight + this.xGetEltTop(this.fPgeFra) + scCoLib.toInt(this.xReadStyle(this.fPgeFra, "marginBottom"));
		else if(this.fPgeFra.clientHeight) return this.fPgeFra.clientHeight + this.xGetEltTop(this.fPgeFra) + scCoLib.toInt(this.xReadStyle(this.fPgeFra, "marginBottom"));
	}	
}
/** scImgMgr.xPageWidth. */
scImgMgr.xPageWidth = function() {
	if(this.fPgeFra){
		if(this.fPgeFra.offsetWidth) return this.fPgeFra.offsetWidth + this.xGetEltLeft(this.fPgeFra) + scCoLib.toInt(this.xReadStyle(this.fPgeFra, "marginRight"));
		else if(this.fPgeFra.clientWidth) return this.fPgeFra.clientWidth + this.xGetEltLeft(this.fPgeFra) + scCoLib.toInt(this.xReadStyle(this.fPgeFra, "marginRight"));
	}	
}
/** scImgMgr.xClientHeight. */
scImgMgr.xClientHeight = function() {
	if (document.documentElement) {
		return document.documentElement.clientHeight;
	} else if (window.innerHeight >= 0) {
		return window.innerHeight;
	} else if (this.fDisplayRoot.clientHeight >= 0) {
		return this.fDisplayRoot.clientHeight;
	} else {
		return 0;
	}
}
/** scImgMgr.xClientWidth. */
scImgMgr.xClientWidth = function() {
	if (document.documentElement) {
		return document.documentElement.clientWidth;
	} else if (window.innerWidth >= 0) {
		return window.innerWidth;
	} else if (this.fDisplayRoot.clientWidth >= 0) {
		return this.fDisplayRoot.clientWidth;
	} else {
		return 0;
	}
}
/** scImgMgr.xNotifyListeners - calls all the listeners of a given type. */
scImgMgr.xNotifyListeners = function(pType,pRes) {
	var vListener = scImgMgr.fListeners[pType];
	for(var i=0; i<vListener.length; i++) {
		try {
			vListener[i](pRes);
		} catch(e) {scCoLib.log("ERROR scImgMgr.xNotifyListeners: "+e);}
	}
}
/** scImgMgr.xAddSep : Add a simple textual separator : " | ". */
scImgMgr.xAddSep = function(pParent){
	var vSep = document.createElement("span");
	vSep.className = "scImgSep";
	vSep.innerHTML = " | "
	pParent.appendChild(vSep);
}
/** scImgMgr.xAddBtn : Add a HTML button to a parent node. */
scImgMgr.xAddBtn = function(pParent,pObj,pType,pName,pCapt,pTitle,pNoCmd){
	var vBtn = scDynUiMgr.addElement("a", pParent, pObj.fOpts.clsPre+pName);
	vBtn.fName = pType+pName;
	vBtn.href = "#";
	vBtn.target = "_self";
	vBtn.setAttribute("role", "button");
	if (!pNoCmd) {
		vBtn.onclick=function(){return scImgMgr.xBtnMgr(this);}
		vBtn.onkeydown=function(pEvent){scDynUiMgr.handleBtnKeyDwn(pEvent);}
		vBtn.onkeyup=function(pEvent){scDynUiMgr.handleBtnKeyUp(pEvent);}
	}
	vBtn.setAttribute("title",pTitle);
	vBtn.innerHTML="<span>"+pCapt+"</span>"
	vBtn.fObj = pObj;
	pParent.appendChild(vBtn);
	return vBtn;
}
/** scImgMgr.xTogglePageFocus : */
scImgMgr.xToggleFocusables = function() {
	if (!this.fFocus) return;
	if (this.fFocusablesDisabled && this.fFocusables){
		for (var i=0; i<this.fFocusables.length; i++){
			var vElt = this.fFocusables[i];
			vElt.setAttribute("tabindex", vElt.fScImgMgrTabIndex || "");
			vElt.fScImgMgrTabIndex = null;
		}
		this.fFocusablesDisabled = false;
	} else {
		this.fFocusables = scPaLib.findNodes(this.fPathFocusables, this.fSourceRoot);
		for (var i=0; i<this.fFocusables.length; i++){
			var vElt = this.fFocusables[i];
			if (!this.xIsEltContainedByNode(vElt,this.fCurrItem.fCvs)) {
				vElt.fScImgMgrTabIndex = vElt.getAttribute("tabindex");
				vElt.setAttribute("tabindex", "-1");
			}
		}
		this.fFocusablesDisabled = true;
	}
}
/** scImgMgr.xFocus : */
scImgMgr.xFocus = function(pNode) {
	if (this.fFocus && pNode) try{pNode.focus();}catch(e){};
}
/** imgZoomMgr.xGetEvt : cross-browser event retreiver */
scImgMgr.xGetEvt = function(pEvt) {
	var vEvt = pEvt || window.event;
	if (vEvt.srcElement && !vEvt.target) vEvt.target = vEvt.srcElement;
	return(vEvt);
}
/** scImgMgr.xIsVisible : */
scImgMgr.xIsVisible = function(pNode) {
	var vAncs = scPaLib.findNodes("anc:", pNode);
	for(var i=0; i<vAncs.length; i++) if (vAncs[i].nodeType == 1 && scImgMgr.xReadStyle(vAncs[i],"display") == "none") return false;
	return true;
}
/** scImgMgr.xIsEltContainedByNode : */
scImgMgr.xIsEltContainedByNode = function(pElt, pContainer) {
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
/** scImgMgr.xGetStr : Reteive a string. */
scImgMgr.xGetStr = function(pStrId) {
	return (this.fLocalize ? this.fStrings[pStrId] : "");
}
/** scImgMgr.xSwitchClass : Replace a CSS class. */
scImgMgr.xSwitchClass = function(pNode, pClassOld, pClassNew) {
	if (pClassOld && pClassOld != '') {
		var vCurrentClasses = pNode.className.split(' ');
		var vNewClasses = new Array();
		var vClassFound = false;
		for(var i=0, n=vCurrentClasses.length; i<n; i++) {
			if (vCurrentClasses[i] != pClassOld) {
				vNewClasses.push(vCurrentClasses[i]);
			} else {
				if (pClassNew && pClassNew != '') vNewClasses.push(pClassNew);
				vClassFound = true;
			}
		}
		pNode.className = vNewClasses.join(' ');
	}
}
/** Set the opacity of a given node.
 * @param pRate Variable de 0 à 1.
 */
scImgMgr.xSetOpacity = function(pNode, pRate){
	if(!this.fNavie8) pNode.style.opacity = pRate;
	else pNode.style.filter = "progid:DXImageTransform.Microsoft.Alpha(opacity="+pRate*100+")";
}
/** Start the opacity of a given node.
 * On ajoute le filtre d'opacité sur IE.
 * On place le node en visibility: "".
 * @param pRate 2 valeurs possibles: 0 (invisible) ou 1 (visible).
 */
scImgMgr.xStartOpacityEffect = function(pNode, pRate){
	if(!this.fNavie8) pNode.style.opacity = pRate;
	else pNode.style.filter = pRate==1 ? "progid:DXImageTransform.Microsoft.Alpha(opacity=100)" : "progid:DXImageTransform.Microsoft.Alpha(opacity=0)";
	pNode.style.visibility = "";
}
/** End the opacity of a given node.
 * On supprime le filtre d'opacité sur IE (évite des bugs de refresh).
 * On place le node en visibility: hidden.
 * @param pRate 2 valeurs possibles: 0 (invisible) ou 1 (visible).
 */
scImgMgr.xEndOpacityEffect = function(pNode, pRate){
		if(!this.fNavie8) pNode.style.opacity = pRate;
		else pNode.style.removeAttribute("filter");
	if(pRate == 0) pNode.style.visibility = "hidden";
	else pNode.style.visibility = "";
}
scImgMgr.loadSortKey = "ZZZ";