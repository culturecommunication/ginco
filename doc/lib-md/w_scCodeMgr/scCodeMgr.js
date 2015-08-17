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
 * Portions created by the Initial Developer are Copyright (C) 2013
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

/* === SCENARI code block manager ======================================== */
var scCodeMgr	 = {
	fPathCode : [],
	fCodes : null,
	fCurrItem : null,
	fOverAlpha : .6,
	fDefaultStep : 3 * 1000,
	fMinStep : 1 * 100,
	fMaxStep : 10 * 1000,
	fTypCode : "scCode",
	fLineNumPath : scPaLib.compilePath("des:div.CodeMirror-linenumber"),
	fSourceRoot : null,
	fEnabled : true,
	fLocalize : true
}
/** SCENARI code block manager strings */
scCodeMgr.fStrings = ["num.","Désactiver les numéros de ligne",
/*02*/                "texte","Activer la vue texte brut",
/*04*/                "copie","Sélectionner le texte brut pour le copier",
/*06*/                "Ligne","Désactiver le retour à la ligne automatique",
/*08*/                "CTRL+C pour copier, CTRL+V pour coller","",
/*10*/                "Activer les numéros de ligne","Désactiver la vue texte brut",
/*12*/                "Activer le retour à la ligne automatique",""];
/** scCodeMgr.init. */
scCodeMgr.init = function() {
}
/** scCodeMgr.registerCode.
 * @param pPathCode scPaLib path vers les block de code.
 * @param pOpts options du block de code.
 *           toolbar : 0 = pas de toolbar / 1 = toolbar
 *           clsPre : préfix de classe CSS
 */
scCodeMgr.registerCode = function(pPathCode, pOpts) {
	var vCode = new Object;
	vCode.fPath = pPathCode;
	vCode.fOpts = (typeof pOpts == "undefined" ? {toolbar:1,pathCode:"chi:div",pathRaw:"chi:pre",clsPre:this.fTypCode} : pOpts);
	vCode.fOpts.toolbar = (typeof vCode.fOpts.toolbar == "undefined" ? 1 : vCode.fOpts.toolbar);
	vCode.fOpts.pathCode = (typeof vCode.fOpts.pathCode == "undefined" ? 1 : vCode.fOpts.pathCode);
	vCode.fOpts.pathRaw = (typeof vCode.fOpts.pathRaw == "undefined" ? 1 : vCode.fOpts.pathRaw);
	vCode.fOpts.clsPre = (typeof vCode.fOpts.clsPre == "undefined" ? this.fTypCode : vCode.fOpts.clsPre);
	this.fPathCode[this.fPathCode.length] = vCode;
}
/** scCodeMgr.setEnabled. */
scCodeMgr.setEnabled = function(pEnable) {
	this.fEnabled = pEnable;
}
/** scCodeMgr.setSourceRoot. */
scCodeMgr.setSourceRoot = function(pRoot) {
	this.fSourceRoot = pRoot;
}
/** scCodeMgr.setLocalize. */
scCodeMgr.setLocalize = function(pLocalize) {
	this.fLocalize = pLocalize;
}

/** scCodeMgr.onLoad - called by the scenari framework, inits the manager. */
scCodeMgr.onLoad = function() {
	
	if (!this.fEnabled) return;

	if (!this.fSourceRoot) this.fSourceRoot = document.body;

	// Load code blocks...
	this.xInitCodes(this.fSourceRoot);
}

/* === Global managers ====================================================== */
/** scCodeMgr.xBtnMgr - centralized button manager */
scCodeMgr.xBtnMgr = function(pBtn) {
	var vObj = pBtn.fObj;
	switch(pBtn.fName){
		case this.fTypCode+"BtnLineNums":
			scCodeMgr.xTgleLineNums(vObj);break;
		case this.fTypCode+"BtnRaw":
			scCodeMgr.xTgleRaw(vObj);break;
		case this.fTypCode+"BtnCopy":
			scCodeMgr.xCopy(vObj);break;
		case this.fTypCode+"BtnWrap":
			scCodeMgr.xTgleWrap(vObj);break;
			
	}
	return false;
}

/* === Code block manager ==================================================== */
scCodeMgr.xInitCodes = function(pCo) {
	for(var i=0; i<this.fPathCode.length; i++) {
		var vCodes = scPaLib.findNodes(this.fPathCode[i].fPath, pCo);
		for(var j=0; j<vCodes.length; j++) this.xInitCode(vCodes[j],this.fPathCode[i].fOpts);
	}
}
scCodeMgr.xInitCode = function(pCode,pOpts) {
	try {
//		if (this.xIsVisible(pCode)){
		if (true){
			pCode.fCode = scPaLib.findNode(pOpts.pathCode,pCode);
			if (!pCode.fCode) return;
			pCode.fClass = pCode.className;
			pCode.fCode.className = pCode.fClass + " " + pOpts.clsPre+"Code";
			pCode.fOpts = pOpts;
			pCode.fLang = pCode.fCode.firstChild.getAttribute("data-lang");
			pCode.fIsPlain = pCode.fLang == "text/plain";
			pCode.fRaw = scPaLib.findNode(pOpts.pathRaw,pCode);
			if (pCode.fRaw){
				pCode.fRaw.className = pCode.fRaw.className + " " + pOpts.clsPre+"Raw";
				pCode.fRawInvisible = true;
			}
			
			if (pOpts.toolbar > 0){
				pCode.fCtrl = scDynUiMgr.addElement("div",pCode,pOpts.clsPre + "Ctrl noIndex",pCode.firstChild);
				var vNumLines = scPaLib.findNodes(this.fLineNumPath, pCode.fCode).length;
				if (vNumLines && !pCode.fIsPlain){
					pCode.fBtnLineNums = this.xAddBtn(pCode.fCtrl,pCode,this.fTypCode,"BtnLineNums",this.xGetStr(0),this.xGetStr(1));
					scCodeMgr.xAddSep(pCode.fCtrl,pCode);
				}
				pCode.fBtnWrap = this.xAddBtn(pCode.fCtrl,pCode,this.fTypCode,"BtnWrap",this.xGetStr(6),this.xGetStr(7));
				scCodeMgr.xAddSep(pCode.fCtrl,pCode);
				pCode.fWrapOn = false;
				if (pCode.fRaw){
					pCode.fBtnRaw = this.xAddBtn(pCode.fCtrl,pCode,this.fTypCode,"BtnRaw",this.xGetStr(pCode.fIsPlain?0:2),this.xGetStr(pCode.fIsPlain?1:3));
					scCodeMgr.xAddSep(pCode.fCtrl,pCode);
					pCode.fBtnCopy = this.xAddBtn(pCode.fCtrl,pCode,this.fTypCode,"BtnCopy",this.xGetStr(4),this.xGetStr(5));
					scCodeMgr.xAddSep(pCode.fCtrl,pCode);
					pCode.fCopyMsg = scDynUiMgr.addElement("div",pCode,pOpts.clsPre + "CopyMsg "+pOpts.clsPre+"Hidden noIndex",pCode.fCtrl.nextSibling);
					pCode.fCopyMsg.innerHTML = "<span>"+this.xGetStr(8)+"</span>";
				}
			}
			pCode.className = pCode.fClass + " " + pOpts.clsPre+"Raw-invisible " + pOpts.clsPre+"Wrap-off " + pOpts.clsPre+"Active" + (vNumLines? " " + pOpts.clsPre+"LineNums-visible" : "") + (pCode.fIsPlain? " " + pOpts.clsPre+"Plain" : "");
			//if (vNumLines==1) scCodeMgr.xTgleLineNums(pCode);
			if (pCode.fRaw) pCode.fRaw.style.display="";
		}
	} catch(e){
		scCoLib.log("scCodeMgr.xInitCode::Error" + e);
	}
}
/** scCodeMgr.xTgleLineNums : . */
scCodeMgr.xTgleLineNums = function(pObj){
	if (pObj.fLinesInvisible) this.xSwitchClass(pObj, pObj.fOpts.clsPre+"LineNums-invisible", pObj.fOpts.clsPre+"LineNums-visible");
	else this.xSwitchClass(pObj, pObj.fOpts.clsPre+"LineNums-visible", pObj.fOpts.clsPre+"LineNums-invisible");
	pObj.fLinesInvisible = !pObj.fLinesInvisible;
	pObj.fBtnLineNums.setAttribute("title", this.xGetStr(pObj.fLinesInvisible ? 10 : 1));
}
/** scCodeMgr.xTgleRaw : . */
scCodeMgr.xTgleRaw = function(pObj, pForce){
	if (pForce) pObj.fRawInvisible = true;
	if (pObj.fRawInvisible) this.xSwitchClass(pObj, pObj.fOpts.clsPre+"Raw-invisible", pObj.fOpts.clsPre+"Raw-visible");
	else this.xSwitchClass(pObj, pObj.fOpts.clsPre+"Raw-visible", pObj.fOpts.clsPre+"Raw-invisible");
	pObj.fRawInvisible = !pObj.fRawInvisible;
	pObj.fBtnRaw.setAttribute("aria-checked", !pObj.fRawInvisible);
	pObj.fBtnRaw.setAttribute("title", this.xGetStr(pObj.fRawInvisible ? 5 : 11));
}
/** scCodeMgr.xTgleWrap : . */
scCodeMgr.xTgleWrap = function(pObj){
	if (pObj.fWrapOn) this.xSwitchClass(pObj, pObj.fOpts.clsPre+"Wrap-on", pObj.fOpts.clsPre+"Wrap-off");
	else this.xSwitchClass(pObj, pObj.fOpts.clsPre+"Wrap-off", pObj.fOpts.clsPre+"Wrap-on");
	pObj.fWrapOn = !pObj.fWrapOn;
	pObj.fBtnWrap.setAttribute("aria-checked", pObj.fWrapOn);
	pObj.fBtnWrap.setAttribute("title", this.xGetStr(pObj.fWrapOn ? 7 : 12));
}
/** scCodeMgr.xTgleLineNums : . */
scCodeMgr.xCopy = function(pObj){
	this.xTgleRaw(pObj, true);
	var vRange, vSelection;
	if (document.body.createTextRange) {
		vRange = document.body.createTextRange();
		vRange.moveToElementText(pObj.fRaw);
		vRange.select();
	} else if (window.getSelection) {
		vSelection = window.getSelection();
		vRange = document.createRange();
		vRange.selectNodeContents(pObj.fRaw);
		vSelection.removeAllRanges();
		vSelection.addRange(vRange);
	}
	this.xSwitchClass(pObj.fCopyMsg, pObj.fOpts.clsPre+"Hidden", pObj.fOpts.clsPre+"Visible");
	window.setTimeout(function(){
		scCodeMgr.xSwitchClass(pObj.fCopyMsg, pObj.fOpts.clsPre+"Visible", pObj.fOpts.clsPre+"Hidden");
	}, 4000);
}

/* === Toolbox ============================================================== */
/** scCodeMgr.xAddSep : Add a simple textual separator : " | ". */
scCodeMgr.xAddSep = function(pParent, pObj){
	var vSep = document.createElement("span");
	vSep.className = pObj.fOpts.clsPre+"Hidden";
	vSep.innerHTML = " | "
	pParent.appendChild(vSep);
}
/** scCodeMgr.xAddBtn : Add a HTML button to a parent node. */
scCodeMgr.xAddBtn = function(pParent,pObj,pType,pName,pCapt,pTitle){
	var vBtn = scDynUiMgr.addElement("a", pParent, pObj.fOpts.clsPre+pName);
	vBtn.fName = pType+pName;
	vBtn.href = "#";
	vBtn.target = "_self";
	vBtn.setAttribute("role", "button");
	vBtn.onclick=function(){return scCodeMgr.xBtnMgr(this);}
	vBtn.onkeydown=function(pEvent){scDynUiMgr.handleBtnKeyDwn(pEvent);}
	vBtn.onkeyup=function(pEvent){scDynUiMgr.handleBtnKeyUp(pEvent);}
	if (pTitle) vBtn.setAttribute("title", pTitle);
	if (pCapt) vBtn.innerHTML = "<span>" + pCapt + "</span>"
	vBtn.fObj = pObj;
	return vBtn;
}
/** scCodeMgr.xFocus : */
scCodeMgr.xFocus = function(pNode) {
	if (this.fFocus) try{pNode.focus();}catch(e){};
}
/** scCodeMgr.xIsVisible : */
scCodeMgr.xIsVisible = function(pNode) {
	var vAncs = scPaLib.findNodes("anc:", pNode);
	for(var i=0; i<vAncs.length; i++) if (vAncs[i].nodeType == 1 && scDynUiMgr.readStyle(vAncs[i],"display") == "none") return false;
	return true;
}
/** scCodeMgr.xGetStr : Reteive a string. */
scCodeMgr.xGetStr = function(pStrId) {
	return (this.fLocalize ? this.fStrings[pStrId] : "");
}
/** scCodeMgr.xSwitchClass : Replace a CSS class. */
scCodeMgr.xSwitchClass = function(pNode, pClassOld, pClassNew) {
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
scCodeMgr.loadSortKey = "ZZZ";
scOnLoads[scOnLoads.length] = scCodeMgr;