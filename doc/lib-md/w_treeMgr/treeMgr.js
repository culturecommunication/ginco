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
 * Portions created by the Initial Developer are Copyright (C) 2012-2014
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
var treeMgr = {
	fPathTrees : "des:div.treeRoot",
	fPathLnks : "des:a.nodeTi",
	fPathNodes : "des:div.nodeTi/chi:.nodeTi",
	fPathDefaultVisible : "des:div.nodeDefaultVisible",
	fPathNodeRoot : "anc:div.nodeRoot",
	fPathTreeRoot : "des:div.nodeDepth_0",
	fFilterNodeInitDepth : "div.nodeDepth_0",
	fToolbarMinNumNodes : 20,
	fLocalize : true,
	fFocus : true
}
/** SCENARI tree manager strings */
treeMgr.fStrings = ["Ouvrir","Ouvrir tous les nœuds",
/*02*/              "Fermer","Fermer tous les nœuds",
/*04*/              "Chercher","Activer/désactiver la recherche de nœuds",
/*06*/              "Chercher un nœud :","Tapez votre recherche",
/*08*/              "Recherche textuelle dans les titres des nœuds (trois caractères minimum)","Veuillez précisez votre recherche.",
/*10*/              "Veuillez tapper votre recherche dans la barre d\'outils.","%s nœud(s) trouvé(s).",
/*12*/              "Prev.","Nœud précedent",
/*14*/              "Suiv.","Nœud suivant",
/*16*/              "Cacher le contenu de \'%s\'","Afficher le contenu de \'%s\'"];

/** treeMgr.init. */
treeMgr.init = function(){
	try{
		this.fFilterNodeInitDepth = scPaLib.compileFilter(this.fFilterNodeInitDepth);
		this.fTrees = scPaLib.findNodes(this.fPathTrees);
		for (var i=0; i<this.fTrees.length; i++){
			var vTree = this.fTrees[i];
			vTree.fScroll = vTree.firstChild;
			vTree.fRoot = scPaLib.findNode(this.fPathTreeRoot,vTree);
			vTree.fLnks = scPaLib.findNodes(this.fPathLnks,vTree);
			vTree.fNodes = scPaLib.findNodes(this.fPathNodes,vTree);
			var vToolbarAttr = vTree.getAttribute("data-toolbar");
			if (vToolbarAttr == "false") vTree.fHasToolbar = false;
			else if (vToolbarAttr == "true") vTree.fHasToolbar = true;
			else vTree.fHasToolbar = this.fToolbarMinNumNodes<=vTree.fNodes.length;
			vTree.fClass = vTree.className;
			for (var j=0; j<vTree.fNodes.length; j++){
				var vNode = vTree.fNodes[j];
				vNode.fText = vNode.textContent ? vNode.textContent.toLowerCase() : vNode.innerText.toLowerCase();
				var vNodeRoot = vNode.fNodeRoot = scPaLib.findNode(this.fPathNodeRoot,vNode);
				vNodeRoot.fRoot = vTree;
				vNodeRoot.fLabel = vNode.firstChild;
				if (vTree.fHasToolbar) vNodeRoot.className = vNodeRoot.className + " treeSearch_nomach treeSearch_nocur";
			}
			vTree.fScroll.style.width="3000px";
			vTree.fRoot.style.width = vTree.fRoot.clientWidth+"px";
			vTree.fScroll.style.width="";
			for (var j=0; j<vTree.fLnks.length; j++){
				var vLnk = vTree.fLnks[j];
				vLnk.onclick = this.sToggle;
				vLnk.title = this.fStrings[16].replace("%s", (vLnk.innerText ? vLnk.innerText: vLnk.textContent));
				if (!scPaLib.checkNode(this.fFilterNodeInitDepth,vLnk.fNodeRoot)) this.toggle(vLnk);
			}
			var vDefaultVisibleNodes = scPaLib.findNodes(this.fPathDefaultVisible, vTree);
			for (var j=0; j<vDefaultVisibleNodes.length; j++) this.makeVisible(vDefaultVisibleNodes[j]);
		}
		scOnLoads[scOnLoads.length] = this;
	}catch(e){scCoLib.log("ERROR - treeMgr.init : "+e)}
}
/** treeMgr.onLoad. */
treeMgr.onLoad = function(){
	for (var i=0; i<this.fTrees.length; i++){
		var vTree = this.fTrees[i];
		if (vTree.fHasToolbar){
			vTree.fStatusbar = scDynUiMgr.addElement("div", vTree, "treeStatusbar");
			vTree.fStatusbar.innerHTML = this.xGetStr(10);
			vTree.fScroll.style.position = "relative";
			var vToolbar = vTree.fToolbar = scDynUiMgr.addElement("div", vTree, "treeToolbar", vTree.fScroll);
			vTree.className = vTree.fClass + " treeHasToolbar treeSearch_off treeSearch_noact";
			var vForm = scDynUiMgr.addElement("form", vToolbar, "treeSearchForm");
			scDynUiMgr.addElement("span", vForm, "treeSearchLabel").innerHTML = this.xGetStr(6);
			var vSearch = vTree.fSearch = scDynUiMgr.addElement("input", vForm, "treeSearchInput");
			vSearch.type = "text";
			vSearch.placeholder = this.xGetStr(7);
			vSearch.title = this.xGetStr(8);
			vSearch.fObj = vTree;
			vSearch.onkeyup = this.sKeyUp;
			this.xAddSep(vForm);
			this.xAddBtn(vForm, vTree, "treeBtnPrv", this.xGetStr(12), this.xGetStr(13));
			this.xAddSep(vForm);
			this.xAddBtn(vForm, vTree, "treeBtnNxt", this.xGetStr(14), this.xGetStr(15));
			this.xAddSep(vForm);
			vTree.fResultLabel = scDynUiMgr.addElement("span", vForm, "treeSearchResultLabel");
			this.xAddSep(vToolbar);
			this.xAddBtn(vToolbar, vTree, "treeBtnSearch", this.xGetStr(4), this.xGetStr(5));
			this.xAddSep(vToolbar);
			this.xAddBtn(vToolbar, vTree, "treeBtnOpenAll", this.xGetStr(0), this.xGetStr(1));
			this.xAddSep(vToolbar);
			this.xAddBtn(vToolbar, vTree, "treeBtnCloseAll", this.xGetStr(2), this.xGetStr(3));
		}
	}
}
/** treeMgr.makeVisible. */
treeMgr.makeVisible = function(pNode){
	var vTreeNodes = scPaLib.findNodes("anc:.nodeChildren_hide", scPaLib.findNode("can:.nodeRoot",pNode));
	for (var i=0; i<vTreeNodes.length; i++){
		this.toggle(scPaLib.findNode("chi:.nodeLblFra/des:a.nodeTi",vTreeNodes[i]));
	}
}
/** treeMgr.toggleSearch. */
treeMgr.toggleSearch = function(pTree){
	pTree.fSearchEnabled = !pTree.fSearchEnabled;
	if (pTree.fSearchEnabled) {
		treeMgr.xSwitchClass(pTree, "treeSearch_off", "treeSearch_on");
		this.xFocus(pTree.fSearch);
		var vAvailHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0)	
		if (pTree.fScroll.clientHeight+50 > vAvailHeight) pTree.fScroll.style.height = (vAvailHeight-50)+"px";
	} else {
		treeMgr.xSwitchClass(pTree, "treeSearch_on", "treeSearch_off");
		pTree.fScroll.style.height = "";
		this.xResetSearch(pTree);
	}
	if("scSiLib" in window) scSiLib.fireResizedNode(pTree);
}
/** treeMgr.toggle. */
treeMgr.toggle = function(pBtn,pDeep){
	if (typeof pDeep == "undefined") pDeep = false;
	pBtn.fClosed = !pBtn.fClosed;
	pBtn.title = this.fStrings[(pBtn.fClosed? 17 : 16)].replace("%s", (pBtn.innerText ? pBtn.innerText: pBtn.textContent));
	var vNodeRoot = pBtn.fNodeRoot;
	vNodeRoot.className = vNodeRoot.className.replace(/nodeChildren_[a-zA-Z]*/gi,"nodeChildren_"+(pBtn.fClosed ? "hide" : "show"));
	if (pDeep){
		var vTreeNodes = scPaLib.findNodes("des:.nodeChildren_"+(pBtn.fClosed ? "show" : "hide"), vNodeRoot);
		for (var i=0; i<vTreeNodes.length; i++){
			this.toggle(scPaLib.findNode("chi:.nodeLblFra/des:a.nodeTi",vTreeNodes[i]));
		}
	}
	if("scSiLib" in window) scSiLib.fireResizedNode(pBtn);
	return false;
}
/** treeMgr.next. */
treeMgr.next = function(pTree){
	if (pTree.fResults.length==0) return;
	if (pTree.fResultIdx>-1) this.xSwitchClass(pTree.fResults[pTree.fResultIdx].fNodeRoot, "treeSearch_cur", "treeSearch_nocur");
	if (pTree.fResultIdx < pTree.fResults.length-1){
		var vNode = pTree.fResults[++pTree.fResultIdx];
		this.xSwitchClass(vNode.fNodeRoot, "treeSearch_nocur", "treeSearch_cur");
		this.xUpdateResults(pTree, vNode);
	} else if (pTree.fResults.length>1){
		pTree.fResultIdx = -1;
		this.next(pTree);
	}
}
/** treeMgr.previous. */
treeMgr.previous = function(pTree){
	if (pTree.fResults.length==0) return;
	this.xSwitchClass(pTree.fResults[pTree.fResultIdx].fNodeRoot, "treeSearch_cur", "treeSearch_nocur");
	if (pTree.fResultIdx>0){
		var vNode = pTree.fResults[--pTree.fResultIdx];
		this.xSwitchClass(vNode.fNodeRoot, "treeSearch_nocur", "treeSearch_cur");
		this.xUpdateResults(pTree, vNode);
	} else if (pTree.fResults.length>1){
		pTree.fResultIdx = pTree.fResults.length - 2;
		this.next(pTree);
	}
}

/* === Internal ============================================================== */
/** treeMgr.sToggle. */
treeMgr.sToggle = function(pEvt){
	var pEvt = pEvt || window.event;
	return treeMgr.toggle(this,pEvt.ctrlKey);
}
/** treeMgr.sKeyUp. */
treeMgr.sKeyUp = function(pEvt){
	var pEvt = pEvt || window.event;
	var vTree = this.fObj;
	treeMgr.xResetSearchResults(vTree);
	if (this.value.length>2) treeMgr.xSearch(vTree, this.value);
	else vTree.fStatusbar.innerHTML = treeMgr.xGetStr(this.value.length==0?10:9);
}
/** treeMgr.xUpdateResults. */
treeMgr.xUpdateResults = function(pTree, pNode) {
	this.xScrollToNode(pTree, pNode);
	pTree.fResultLabel.innerHTML = pTree.fResultIdx+1 + "/" + pTree.fResults.length;
	var vContext = "";
	var vAncNodes = scPaLib.findNodes(this.fPathNodeRoot,pNode);
	for (var i=vAncNodes.length-1; i>=0; i--){
		vContext += vAncNodes[i].fLabel.innerHTML+ (i>0 ? " > " : "");
	}
	pTree.fStatusbar.innerHTML = vContext;
}
/** treeMgr.xScrollToNode. */
treeMgr.xScrollToNode = function(pTree, pNode) {
	pTree.fScroll.scrollTop = pNode.fNodeRoot.offsetTop - .3 * pTree.fScroll.clientHeight;
}
/** treeMgr.xReset. */
treeMgr.xResetSearch = function(pTree) {
	this.xResetSearchResults(pTree);
	pTree.fSearch.value = "";
	pTree.fResults = [];
	pTree.fStatusbar.innerHTML = this.xGetStr(10);
}
/** treeMgr.xReset. */
treeMgr.xResetSearchResults = function(pTree) {
	for (var i=0; i<pTree.fNodes.length; i++){
		var vNode = pTree.fNodes[i];
		this.xSwitchClass(vNode.fNodeRoot, "treeSearch_mach", "treeSearch_nomach");
		this.xSwitchClass(vNode.fNodeRoot, "treeSearch_cur", "treeSearch_nocur");
	}
	treeMgr.xSwitchClass(pTree, "treeSearch_act", "treeSearch_noact");
	pTree.fResultLabel.innerHTML = "";
}
/** treeMgr.xSearch. */
treeMgr.xSearch = function(pTree, pVal) {
	if (!pVal) return;
	var vCount = 0;
	pTree.fResultIdx = -1;
	pTree.fResults = [];
	pVal = pVal.toLowerCase();
	for (var i=0; i<pTree.fNodes.length; i++){
		var vNode = pTree.fNodes[i];
		if (vNode.fText.indexOf(pVal)>=0){
			this.xSwitchClass(vNode.fNodeRoot, "treeSearch_nomach", "treeSearch_mach");
			this.makeVisible(vNode);
			pTree.fResults.push(vNode);
			vCount++;
		} else {
			this.xSwitchClass(vNode.fNodeRoot, "treeSearch_mach", "treeSearch_nomach");
		}
	}
	pTree.fStatusbar.innerHTML = this.xGetStr(11).replace("%s", vCount);
	if (vCount>0) {
		this.xSwitchClass(pTree, "treeSearch_noact", "treeSearch_act");
		this.xScrollToNode(pTree, pTree.fResults[0]);
	}
	else this.xSwitchClass(pTree, "treeSearch_act", "treeSearch_noact");
	return vCount;
}
/** treeMgr.xBtnMgr - centralized button manager */
treeMgr.xBtnMgr = function(pBtn) {
	var vObj = pBtn.fObj;
	switch(pBtn.fName){
		case "treeBtnOpenAll":
			vObj.fLnks[0].fClosed = true;
			treeMgr.toggle(vObj.fLnks[0], true);break;
		case "treeBtnCloseAll":
			vObj.fLnks[0].fClosed = false;
			treeMgr.toggle(vObj.fLnks[0], true);
			treeMgr.toggle(vObj.fLnks[0]); break;
		case "treeBtnSearch":
			treeMgr.toggleSearch(vObj);break;
		case "treeBtnPrv":
			treeMgr.previous(vObj);break;
		case "treeBtnNxt":
			treeMgr.next(vObj);break;
	}
	return false;
}

/* === Toolbox ============================================================== */
/** treeMgr.xAddSep : Add a simple textual separator : " | ". */
treeMgr.xAddSep = function(pParent){
	var vSep = document.createElement("span");
	vSep.className = "treeSep";
	vSep.innerHTML = " | "
	pParent.appendChild(vSep);
}
/** treeMgr.xAddBtn : Add a HTML button to a parent node. */
treeMgr.xAddBtn = function(pParent,pObj,pName,pCapt,pTitle,pNoCmd){
	var vBtn = pParent.ownerDocument.createElement("a");
	vBtn.className = pName;
	vBtn.fName = pName;
	vBtn.href = "#";
	vBtn.target = "_self";
	vBtn.setAttribute("role", "button");
	if (!pNoCmd) {
		vBtn.onclick=function(){return treeMgr.xBtnMgr(this);}
		vBtn.onkeyup=function(pEvent){scDynUiMgr.handleBtnKeyUp(pEvent);}
	}
	vBtn.setAttribute("title",pTitle);
	vBtn.innerHTML="<span>"+pCapt+"</span>"
	vBtn.fObj = pObj;
	pParent.appendChild(vBtn);
	return vBtn;
}
/** treeMgr.xFocus. */
treeMgr.xFocus = function(pNode) {
	if (this.fFocus) try{pNode.focus();}catch(e){};
}
/** treeMgr.xGetStr : Reteive a string. */
treeMgr.xGetStr = function(pStrId) {
	return (this.fLocalize ? this.fStrings[pStrId] : "");
}
/** treeMgr.xSwitchClass : Replace a CSS class. */
treeMgr.xSwitchClass = function(pNode, pClassOld, pClassNew) {
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