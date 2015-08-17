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

/* Doctek frame manager */
var frameMgr = {
	fPathRoot : "des:div",
	fPathContentFrame : "ide:content/des:iframe",
	fPathNavBar : "ide:navigation",
	fPathDocBtn : "ide:menu/des:li.toolHome/des:a",
	fListeners : {loadPage:[],showSearch:[],resetSearch:[]},

	init : function(){
		this.fStore = new this.LocalStore();
		this.fRoot = scPaLib.findNode(this.fPathRoot);
		this.fFra = scPaLib.findNode(this.fPathContentFrame);
		this.fFra.loadPage = function(pUrl, pTitle, pHash){frameMgr.xLoadPage(pUrl, pTitle, pHash)}
		this.fFra.unloadPage = function(){frameMgr.xUnloadPage()}
		this.fFra.fStore = this.fStore;
		this.fRootTitle = document.title;

		scOnLoads[scOnLoads.length] = this;
	},
	
	onLoad : function(){

		// init current page
		// Process page URL
		var vPageURL = null;
		var vHash = null;
		if(window.location.search.length > 0) {
			var vCtxt = window.location.search.substring(1).split("&");
			for (var i = 0, n = vCtxt.length; i < n; i++) {
				var vCmd = vCtxt[i].split("=");
				switch (vCmd[0]) {
					case "p" : 
						vPageURL = decodeURIComponent(vCmd[1]);
						break;
				}
			}
		}
		if(window.location.hash.length > 0){
			vHash = window.location.hash.substring(1).split("_");
			vPageURL = outMgr.getPageUrlFromId(vHash[0]);
			if (vPageURL && vHash.length==2) vHash = vHash[1];
			else vHash = null;
		}

		if(!vPageURL) vPageURL = this.fStore.get("lastPage");
		if(!vPageURL) vPageURL = outMgr.getFirstPage();
		this.loadPage(vPageURL, vHash);
	},
	
	loadSortKey : "Z",

	registerListener : function(pListener, pFunc){
		if (this.fListeners[pListener]) this.fListeners[pListener].push(pFunc);
		else scCoLib.log("ERROR - frameMgr.registerListener - non-existent listener : " + pListener);
	},

	loadPage : function(pUrl, pHash){
		if (pUrl && pUrl.length>0) this.fFra.src = scServices.scLoad.getPathFromRoot(pUrl) + (pHash ? "#" + pHash : "");
	},

	reload : function(){
		var vPageURL = this.fStore.get("lastPage")
		if (vPageURL && vPageURL.length>0) this.loadPage(vPageURL);
	},

	goToFirstPage : function(){
		this.loadPage(outMgr.getFirstPage());
	},

	hasNextPage : function(){
		return outMgr.hasNextPage(this.fPageCurrent);
	},

	hasPreviousPage : function(){
		return outMgr.hasPreviousPage(this.fPageCurrent);
	},

	goToNextPage : function(){
		if (this.hasNextPage()) this.loadPage(outMgr.getNextPage(this.fPageCurrent));
	},

	goToPreviousPage : function(){
		if (this.hasPreviousPage()) this.loadPage(outMgr.getPreviousPage(this.fPageCurrent));
	},

	getPageRank : function(){
		return outMgr.getPageRank(this.fPageCurrent);
	},

	getPageCount : function(){
		return outMgr.getPageCount();
	},

	scrollTo : function(pId){
		this.loadPage(this.fPageCurrent +"#" + pId);
	},

	fireEvent : function(pListener, pParam){
		if (this.fListeners[pListener]) for (var i=0; i< this.fListeners[pListener].length; i++) this.fListeners[pListener][i](pParam);
		else scCoLib.log("ERROR - frameMgr.fireEvent - non-existent listener : " + pListener);
	},

	toggleMenu : function(pMnuClosed){
		frameMgr.xToggleMenu(pMnuClosed);
	},

/* === Callback functions =================================================== */
	sPrv: function(){
		frameMgr.goToPreviousPage();
		return false;
	},

	sNxt: function(){
		frameMgr.goToNextPage();
		return false;
	},

/* === Private functions ==================================================== */
	xLoadPage : function (pUri, pTitle, pHash) {
		document.title = pTitle + (this.fRootTitle!=pTitle ? " ["+this.fRootTitle+"]" : "");
		var vDocBtn = scPaLib.findNode(this.fPathDocBtn);
		if (outMgr.hasPage(pUri)) {
			this.toggleMenu(false);
			vDocBtn.href = scServices.scLoad.getPathFromRoot(vDocBtn.fSrc.url);
			this.xSwitchClass(vDocBtn,"mnu_ret_yes", "mnu_ret_no", true);
		} else {
			this.toggleMenu(true);
			if (this.fLastMenuPage){
				vDocBtn.href = scServices.scLoad.getPathFromRoot(this.fLastMenuPage); 
				this.xSwitchClass(vDocBtn,"mnu_ret_no", "mnu_ret_yes", true);
			}
		}
		this.fPageCurrent = pUri;
		this.fireEvent("loadPage",pUri);
		this.fStore.set("lastPage",pUri);
		var vId = outMgr.getPageIdFromUrl(pUri);
		if (vId) window.location.href = scCoLib.hrefBase() + "#" + vId + (pHash ? "_" + pHash : "");
	},

	xUnloadPage : function () {
		if (this.fPageCurrent && outMgr.hasPage(this.fPageCurrent)) this.fLastMenuPage = this.fPageCurrent;
	},

	xToggleMenu : function (pIsMnuClosed) {
		if (pIsMnuClosed){
			this.xSwitchClass(this.fRoot,"tplMnuCls_no", "tplMnuCls_yes", true);
		} else {
			this.xSwitchClass(this.fRoot,"tplMnuCls_yes", "tplMnuCls_no", true);
		}
	},


/* === Utilities ============================================================ */
	/** frameMgr.xAddBtn : Add a HTML button to a parent node. */
	xAddBtn : function(pParent, pClassName, pCapt, pTitle, pNxtSib) {
		var vBtn = pParent.ownerDocument.createElement("a");
		vBtn.className = pClassName;
		vBtn.fName = pClassName;
		vBtn.href = "#";
		vBtn.target = "_self";
		if (pTitle) vBtn.setAttribute("title", pTitle);
		if (pCapt) vBtn.innerHTML = "<span>" + pCapt + "</span>"
		if (pNxtSib) pParent.insertBefore(vBtn,pNxtSib)
		else pParent.appendChild(vBtn);
		return vBtn;
	},

	/** frameMgr.xSwitchClass - replace a class name. */
	xSwitchClass : function(pNode, pClassOld, pClassNew, pAddIfAbsent, pMatchExact) {
		var vAddIfAbsent = typeof pAddIfAbsent == "undefined" ? false : pAddIfAbsent;
		var vMatchExact = typeof pMatchExact == "undefined" ? true : pMatchExact;
		var vClassName = pNode.className;
		var vReg = new RegExp("\\b"+pClassNew+"\\b");
		if (vMatchExact && vClassName.match(vReg)) return;
		var vClassFound = false;
		if (pClassOld && pClassOld != "") {
			if (vClassName.indexOf(pClassOld)==-1){
				if (!vAddIfAbsent) return;
				else if (pClassNew && pClassNew != '') pNode.className = vClassName + " " + pClassNew;
			} else {
				var vCurrentClasses = vClassName.split(' ');
				var vNewClasses = new Array();
				for (var i = 0, n = vCurrentClasses.length; i < n; i++) {
					var vCurrentClass = vCurrentClasses[i];
					if (vMatchExact && vCurrentClass != pClassOld || !vMatchExact && vCurrentClass.indexOf(pClassOld) != 0) {
						vNewClasses.push(vCurrentClasses[i]);
					} else {
						if (pClassNew && pClassNew != '') vNewClasses.push(pClassNew);
						vClassFound = true;
					}
				}
				pNode.className = vNewClasses.join(' ');
			}
		}
		return vClassFound;
	},

	/** Local Storage API (localStorage/userData/cookie) */
	LocalStore : function (pId){
		if (pId && !/^[a-z][a-z0-9]+$/.exec(pId)) throw new Error("Invalid store name");
		this.fId = pId || "";
		this.fRootKey = document.location.pathname.substring(0,document.location.pathname.lastIndexOf("/")) +"/";
		if (typeof localStorage != "undefined") {
			this.get = function(pKey) {var vRet = localStorage.getItem(this.fRootKey+this.xKey(pKey));return (typeof vRet == "string" ? unescape(vRet) : null)};
			this.set = function(pKey, pVal) {localStorage.setItem(this.fRootKey+this.xKey(pKey), escape(pVal))};
		} else if (window.ActiveXObject){
			this.get = function(pKey) {this.xLoad();return this.fIE.getAttribute(this.xEsc(pKey))};
			this.set = function(pKey, pVal) {this.fIE.setAttribute(this.xEsc(pKey), pVal);this.xSave()};
			this.xLoad = function() {this.fIE.load(this.fRootKey+this.fId)};
			this.xSave = function() {this.fIE.save(this.fRootKey+this.fId)};
			this.fIE=document.createElement('div');
			this.fIE.style.display='none';
			this.fIE.addBehavior('#default#userData');
			document.body.appendChild(this.fIE);
		} else {
			this.get = function(pKey){var vReg=new RegExp(this.xKey(pKey)+"=([^;]*)");var vArr=vReg.exec(document.cookie);if(vArr && vArr.length==2) return(unescape(vArr[1]));else return null};
			this.set = function(pKey,pVal){document.cookie = this.xKey(pKey)+"="+escape(pVal)};
		}
		this.xKey = function(pKey){return this.fId + this.xEsc(pKey)};
		this.xEsc = function(pStr){return "LS" + pStr.replace(/ /g, "_")};
	}
}

