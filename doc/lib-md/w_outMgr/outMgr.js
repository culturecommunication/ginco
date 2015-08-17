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
 * Portions created by the Initial Developer are Copyright (C) 2012-2015
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

/* === Doctek outline manager =============================================== */
var outMgr = {
	fPathRoot : "",
	fPathTools : "ide:header/des:.tplTools",
	fPathContext : "ide:header/des:.tplContextMenu",
	fPathBranches : "des:div.mnu_b",
	fTarget : "contentFrame",
	fUrlOutline : null,
	fMenus : [],
	fUrlHashTable : {},
	fIdHashTable : {},
	sFilterTgleClosed : scPaLib.compileFilter(".mnu_tgle_c"),
	fMaxFilterDisplay : 100,
	fCurrentMenu : 0,
	fOverflowMethod : "hidden",
	fWheelScrollFactor : 6,

	fStrings : ["défilement haut","Faire défiler le menu vers le haut",
	/*02*/       "défilement bas","Faire défiler le menu vers le bas",
	/*04*/       "Documentation",""],

	
/* === Public functions ===================================================== */
	init : function (pPathRoot){
		this.fIsLocal = window.location.protocol == "file:";
		if (typeof pPathRoot != "undefined") this.fPathRoot = pPathRoot;
		this.fFilterIsClosed = scPaLib.compileFilter(".mnu_sub_c");
		this.fFilterIsBranch = scPaLib.compileFilter(".mnu_b");
		
		scOnLoads[scOnLoads.length] = this;
	},

	declareOutline : function(pUrl){
		this.fUrlOutline = pUrl;
	},
	
	onLoad : function(){
		try{
			frameMgr.registerListener("loadPage", this.sOnLoadPage);
			frameMgr.registerListener("showSearch", this.sOnFilter);
			frameMgr.registerListener("resetSearch", this.sOnReset);

			this.fRoot = scPaLib.findNode(this.fPathRoot);
			this.fTools = scPaLib.findNode(this.fPathTools);
			this.fContext = scPaLib.findNode(this.fPathContext);
			var vOutline = this.xGetOutline();

			var iHashTableInit = function(pItem){
				if (pItem.url && pItem.id){
					outMgr.fUrlHashTable[pItem.url] = pItem.id;
					outMgr.fIdHashTable[pItem.id] = pItem.url;
				}
				if (pItem.children) {
					for (var i=0; i<pItem.children.length; i++) iHashTableInit(pItem.children[i]);
				}
			}

			var vSrcMenu = vOutline.menu;
			iHashTableInit(vSrcMenu);
			vSrcMenu.url = null;
			var vMenu = new this.MenuManager(this.fRoot, vSrcMenu, {target:this.fTarget,addScroller:true,contextRoot:this.fContext});
			vMenu.rebuildMenu();
			this.fMenus.push(vMenu);

			var vSrcTools = vOutline.tools;
			iHashTableInit(vSrcTools);
			if (!vSrcTools.children) vSrcTools.children = [];
			vSrcTools.children.unshift(this.xCloneObject(vSrcMenu.children[0]));
			vSrcTools.children[0].source = "refHome";
			vSrcTools.children[0].className = "toolHome";
			vSrcTools.children[0].label = this.fStrings[4];
			vSrcTools.url = null;
			var vTools = new this.MenuManager(this.fTools, vSrcTools, {target:this.fTarget,addTitleAttributes:true,neverFilter:true});
			vTools.rebuildMenu();
			this.fMenus.push(vTools);

		} catch(e){
			scCoLib.log("ERROR - outMgr.onLoad: "+e);
		}
	},

	/** outMgr.getPageUrlFromId : return the URL of the page who's ID is provided */
	getPageUrlFromId : function(pId){
		return this.fIdHashTable[pId];
	},
	
	/** outMgr.getPageIdFromUrl : return the ID of the page who's URL is provided */
	getPageIdFromUrl : function(pUrl){
		return this.fUrlHashTable[pUrl];
	},
	
	/** outMgr.getFirstPage : return the URL of the current displayed menu's first visible item. */
	getFirstPage : function(){
		return this.fMenus[this.fCurrentMenu].getFirstPageUrl();
	},
	
	/** outMgr.hasPage */
	hasPage : function(pUrl){
		return this.fMenus[this.fCurrentMenu].hasPage(pUrl);
	},

	/** outMgr.hasNextPage */
	hasNextPage : function(pUrl){
		return (this.fMenus[this.fCurrentMenu].getNextPageUrl(pUrl, true) ? true : false);
	},

	/** outMgr.hasPreviousPage */
	hasPreviousPage : function(pUrl){
		return (this.fMenus[this.fCurrentMenu].getPreviousPageUrl(pUrl, true) ? true : false);
	},

	/** outMgr.getNextPage : return the URL of next visible item of the given url in the current displayed menu. */
	getNextPage : function(pUrl){
		return this.fMenus[this.fCurrentMenu].getNextPageUrl(pUrl);
	},

	/** outMgr.getPreviousPage : return the URL of previous visible item of the given url in the current displayed menu. */
	getPreviousPage : function(pUrl){
		return this.fMenus[this.fCurrentMenu].getPreviousPageUrl(pUrl);
	},

	/** outMgr.getPageCount : return the visible page cout the current displayed menu. */
	getPageCount : function(){
		return this.fMenus[this.fCurrentMenu].getPageCount();
	},

	/** outMgr.getPreviousPage : return the rank of the given url in the current displayed menu. */
	getPageRank : function(pUrl){
		return this.fMenus[this.fCurrentMenu].getPageRank(pUrl);
	},
	
/* === Callback functions =================================================== */
	/** outMgr.sOnLoadPage : Page load callback. */
	sOnLoadPage : function(pUri){
		for (var i=0; i< outMgr.fMenus.length;i++) outMgr.fMenus[i].loadPage(pUri);
	},
	/** outMgr.sOnFilter : Filter callback : called on search a successful. */
	sOnFilter : function(pPageList){
		for (var i=0; i< outMgr.fMenus.length;i++) outMgr.fMenus[i].applyFilter(pPageList);
	},
	/** outMgr.sOnReset : Filter reset callback : called search reset. */
	sOnReset : function(){
		for (var i=0; i< outMgr.fMenus.length;i++) outMgr.fMenus[i].resetFilter();
	},
	

/* === Private functions ==================================================== */
	/** outMgr.xGetOutline : Load and return the full outline description object. */
	xGetOutline : function() {
		try{
			var vReq = this.xGetHttpRequest();
			vReq.open("GET",this.fUrlOutline,false);
			vReq.send();
			return this.xDeserialiseObjJs(vReq.responseText);
		}catch(e){}
	},

	xCloneObject : function(obj) {
	    if (null == obj || "object" != typeof obj) return obj;
	    var copy = obj.constructor();
	    for (var attr in obj) {
	        if (obj.hasOwnProperty(attr)) copy[attr] = obj[attr];
	    }
	    return copy;
	},

/* === Utilities ============================================================ */

	/** outMgr.xAddBtn : Add a HTML button to a parent node. */
	xAddBtn : frameMgr.xAddBtn,

	/** outMgr.xSwitchClass - replace a class name. */
	xSwitchClass : frameMgr.xSwitchClass,

	/** outMgr.xGetHttpRequest - return an ajax object. */
	xGetHttpRequest: function(){
		if (window.XMLHttpRequest && (!this.fIsLocal || !window.ActiveXObject)) return new XMLHttpRequest();
		else if (window.ActiveXObject) return new ActiveXObject("Microsoft.XMLHTTP");
	},

	/** outMgr.xDeserialiseObjJs - JASON to object converter. */
	xDeserialiseObjJs : function(pStr){
		if(!pStr) return {};
		var vVal;
		eval("vVal="+pStr);
		return vVal;
	},

	loadSortKey : "A"
}

/** outMgr.MenuManager - Menu manager class. */
outMgr.MenuManager = function (pRoot, pOutline, pOpt) {
	try{
		this.fOpt = {target:"_self",addScroller:false,addTitleAttributes:false,contextRoot:null,neverFilter:false};
		if (typeof pOpt != "undefined"){
			if (typeof pOpt.target != "undefined") this.fOpt.target = pOpt.target;
			if (typeof pOpt.addScroller != "undefined") this.fOpt.addScroller = pOpt.addScroller;
			if (typeof pOpt.addTitleAttributes != "undefined") this.fOpt.addTitleAttributes = pOpt.addTitleAttributes;
			if (typeof pOpt.contextRoot != "undefined") this.fOpt.contextRoot = pOpt.contextRoot;
			if (typeof pOpt.neverFilter != "undefined") this.fOpt.neverFilter = pOpt.neverFilter;
		}
		this.fRoot = pRoot;
		this.fOutline = pOutline;
		this.fRoot.fSrc = pOutline;
		this.fFilter = false;
		var vFirstItem = null;
		var vItemIndex = this.fItemIndex = {};
		var iOutlineInit = function(pItem){
			if (pItem.url){
				if (!vItemIndex[pItem.url]) vItemIndex[pItem.url] = [];
				if (!vFirstItem) vFirstItem = pItem;
				vItemIndex[pItem.url].push(pItem);
			}
			if (pItem.children) {
				for (var i=0; i<pItem.children.length; i++) {
					var vChi = pItem.children[i];
					vChi.par = pItem;
					if (i<pItem.children.length-1) vChi.ctxNxt = pItem.children[i+1];
					if (i>0) vChi.ctxPrv = pItem.children[i-1];
					vChi.idx = i;
					iOutlineInit(vChi);
				}
			}
		}
		iOutlineInit(this.fOutline);
		var iOutlineWalker = function(pItem){
			if (pItem.children) pItem.nxt = pItem.children[0];
			else if (pItem.ctxNxt) pItem.nxt = pItem.ctxNxt;
			else if (pItem.par){
				var vPar = pItem.par;
				while (vPar && !vPar.ctxNxt) vPar = vPar.par;
				if (vPar && vPar.ctxNxt) pItem.nxt = vPar.ctxNxt;
			}
			if (typeof pItem.idx != "undefined" && pItem.idx==0) pItem.prv = pItem.par;
			else {
				var vPrv = pItem.ctxPrv;
				while(vPrv && typeof vPrv.children != "undefined") vPrv = vPrv.children[vPrv.children.length-1];
				if (vPrv) pItem.prv = vPrv;
			}
			if (pItem.children) {
				for (var i=0; i<pItem.children.length; i++) {
					iOutlineWalker( pItem.children[i]);
				}
			}
		}
		iOutlineWalker(this.fOutline);
		this.fFirstItem = vFirstItem;
		this.fFirstFilteredItem = null;

		if (this.fOpt.addScroller) this.buildMenuScroller();
		if (this.fOpt.contextRoot) this.fContext = scDynUiMgr.addElement("span",this.fOpt.contextRoot,"ctx_root");

	} catch(e){scCoLib.log("outMgr.MenuManager init : "+e);}
}
outMgr.MenuManager.prototype = {
	/** MenuManager.buildMenuScroller - build a menu scroller infrastructre. */
	buildMenuScroller : function() {
		// Init Scroll
		this.fRoot.fMgr = this;
		this.fScrollerEnabled = true;
		this.fRoot.style.overflow = outMgr.fOverflowMethod;
		var vFra = this.fRoot.parentNode;

		// Init Scroll up button
		this.fSrlUp = scDynUiMgr.addElement("div", vFra, "mnuSrlUpFra", this.fRoot);
		this.fSrlUp.fMgr = this;
		this.fSrlUp.onclick = function(){
			this.fMgr.fSpeed -= 2;
		}
		this.fSrlUp.onmouseover = function(){
			if(this.fMgr.fSpeed >= 0) {
				this.fMgr.fSpeed = -2; 
				scTiLib.addTaskNow(this.fMgr);
			}
		}
		this.fSrlUp.onmouseout = function(){
			this.fMgr.fSpeed = 0;
		}
		this.fSrlUpBtn = outMgr.xAddBtn(this.fSrlUp, "mnuSrlUpBtn", outMgr.fStrings[0], outMgr.fStrings[1]);
		this.fSrlUpBtn.fMgr = this;
		this.fSrlUpBtn.onclick = function(){
			this.fMgr.setScrollStep(-20); 
			return false;
		}
		// Init Scroll down button
		this.fSrlDwn = scDynUiMgr.addElement("div", vFra, "mnuSrlDwnFra");
		this.fSrlDwn.fMgr = this;
		this.fSrlDwn.onclick = function(){
			this.fMgr.fSpeed += 2;
		}
		this.fSrlDwn.onmouseover = function(){
			if(this.fMgr.fSpeed <= 0) {
				this.fMgr.fSpeed = 2; 
				scTiLib.addTaskNow(this.fMgr);
			}
		}
		this.fSrlDwn.onmouseout = function(){
			this.fMgr.fSpeed = 0;
		}
		this.fSrlDwnBtn = outMgr.xAddBtn(this.fSrlDwn, "mnuSrlDwnBtn", outMgr.fStrings[2], outMgr.fStrings[3]);
		this.fSrlDwnBtn.fMgr = this;
		this.fSrlDwnBtn.onclick = function(){
			this.fMgr.setScrollStep(20);
			return false;
		}
		// Init scroll manager
		this.checkScrollBtns();
		this.ensureVisible();
		scSiLib.addRule(this.fRoot, this);
		this.fRoot.onscroll = function(){this.fMgr.checkScrollBtns()};
		this.fRoot.onmousewheel = function(){this.fMgr.setScrollStep(Math.round(-event.wheelDelta/(scCoLib.isIE ? 60 : 40)*outMgr.fWheelScrollFactor))}; //IE, Safari, Chrome, Opera.
		if(this.fRoot.addEventListener) this.fRoot.addEventListener('DOMMouseScroll', function(pEvent){this.fMgr.setScrollStep(pEvent.detail*outMgr.fWheelScrollFactor)}, false);

	},
	/** MenuManager.buildSubMenu - build the sub menu of a given root dom node. */
	buildSubMenu : function (pRoot, pHidden) {
		var i,vChi,vUl,vBtn,vTyp;
		for (i=0; i< pRoot.fSrc.children.length; i++){
			vChi = pRoot.fSrc.children[i];
			if (!this.fFilter && !vChi.prn || this.fFilter && vChi.vis){
				vTyp = vChi.children ? "b" : "l";
				this.buildMenuEntry(pRoot, vChi, pHidden);
				if (vTyp == "b"){
					vBtn = outMgr.xAddBtn(vChi.fLbl,"mnu_tgle_c",">");
					vBtn.onclick = this.sToggleMnuItem;
					vUl = scDynUiMgr.addElement("ul",vChi.fLi,"mnu_sub mnu_sub_c");
					vChi.fLbl.fTglBtn = vBtn;
					vChi.fLnk.fTglBtn = vBtn;
					vUl.fTglBtn = vBtn;
					vUl.fSrc = vChi;
					vUl.fMgr = this;
					vBtn.fLbl = vChi.fLbl;
					vBtn.fUl = vUl
					vChi.fUl = vUl;
				}
			}
		}
		pRoot.fBuilt = true;
		if (this.fOpt.addScroller) this.checkScrollBtns();
	},
	/** MenuManager.buildMenuEntry - build the menu entry of a given source node. */
	buildMenuEntry : function(pParent, pSrc, pHidden) {
		var vUl,vLi,vDiv,vLnk,vTyp,vCls;
		vTyp = pSrc.children ? "b" : "l";
		vCls = "mnu_sel_no mnu_"+vTyp+" mnu_src_"+pSrc.source+" mnu_dpt_"+(scPaLib.findNodes("anc:ul.mnu_sub", pParent).length + 1)+" "+pSrc.className+" mnu_sch_"+(this.fFilter && pSrc.act ? "yes" : "no");
		vLi = scDynUiMgr.addElement("li",pParent,vCls,pHidden);
		vDiv = scDynUiMgr.addElement("div",vLi, "mnuLbl "+vCls);
		vLnk = scDynUiMgr.addElement("a",vDiv,"mnu_i mnu_lnk");
		if (pSrc.url) {
			vLnk.href = scServices.scLoad.getPathFromRoot(pSrc.url);
			vLnk.target = this.fOpt.target;
		} else {
			vLnk.href = "#";
			vLnk.onclick = function(){try{if(this.fTglBtn && this.fTglBtn.className.indexOf("mnu_tgle_c")>=0) outMgr.xToggleMnuItem(this.fTglBtn)} catch(e){};return false;};
		}
		vLnk.fSrc = pSrc;
		vLnk.fMgr = this;
		if (this.fFilter && !pSrc.act) vLnk.onclick = function(){return false};
		else vLnk.onclick = function(){try{
			this.fMgr.fRequestedItem = this.fSrc;
		}catch(e){}};
		vLnk.innerHTML = "<span class='mnu_sch'><span>"+pSrc.label+"</span></span>";
		if (this.fOpt.addTitleAttributes) vLnk.title = pSrc.label;
		pSrc.fLbl = vDiv;
		pSrc.fLi = vLi;
		pSrc.fLnk = vLnk;
	},
	/** MenuManager.buildAncestorMenus - garantee that all ancestors of the given item are present. */
	buildAncestorMenus : function(pItem, pHidden) {
		var vAncs = [];
		var vItem = pItem
		while(vItem.par && !vItem.fLbl){
			vAncs.push(vItem.par);
			vItem = vItem.par;
		}
		for (var i=vAncs.length-1; i>=0; i--){
			this.buildSubMenu(vAncs[i].fUl, pHidden);
		}
	},
	/** MenuManager.buildContextMenu - build an context menu of the given item. */
	buildContextMenu : function(pItem) {
		var vAncs = [];
		var vCtx = [];
		var vItem = pItem.par
		while(vItem && vItem.url){
			vAncs.push(vItem);
			vItem = vItem.par;
		}
		for (var i=vAncs.length-1; i>=0; i--){
			var vAnc = vAncs[i];
			vCtx.push('<a href="'+scServices.scLoad.getPathFromRoot(vAnc.url)+'" target="'+this.fOpt.target+'" class="ctx_lnk"><span>'+vAnc.label+'</span></a>');
		}
		return vCtx.join('<span> > </span>')+(vCtx.length>0 ? '<span> > </span><span>' + pItem.label + '</span>' : '');
	},
	/** MenuManager.resetMenu - reset all filtering info in the menu: . */
	resetMenu : function() {
		var iResetMenu = function(pItem){
			pItem.act = false;
			pItem.vis = false;
			pItem.cnt = null;
			if (pItem.children) for (var i=0; i<pItem.children.length; i++) iResetMenu(pItem.children[i]);
		}
		iResetMenu(this.fOutline);
		this.fOutline.cntAct = 0;
	},
	/** MenuManager.rebuildMenu - Rebuild the menu from scrach. */
	rebuildMenu : function() {
		var vMgr = this;
		var iResetMenu = function(pItem){
			pItem.fLbl = null;
			pItem.fUl = null;
			pItem.fLi = null;
			if (!vMgr.fFilter){
				pItem.act = false;
				pItem.vis = false;
				pItem.cnt = null;
			}
			if (pItem.children) for (var i=0; i<pItem.children.length; i++) iResetMenu(pItem.children[i]);
		}
		iResetMenu(this.fOutline);
		if (!this.fFilter) this.fOutline.cntAct=0;
		this.fRoot.innerHTML = "";
		var vRootUl = scDynUiMgr.addElement("ul",this.fRoot,"mnu_root mnu_sub mnu_sch_no");
		vRootUl.fSrc = this.fOutline;
		this.buildSubMenu(vRootUl);
	},
	/** MenuManager.getFirstPageUrl - return the URL of the first visible page. */
	getFirstPageUrl : function(){
		this.fRequestedItem = (this.fFilter ? this.fFirstFilteredItem : this.fFirstItem);
		return (this.fRequestedItem ? this.fRequestedItem.url : null);
	},
	/** MenuManager.hasPage - returns if the given url exists in this menu. */
	hasPage : function(pUrl){
		if (this.fItemIndex[pUrl]) return true;
		return false;
	},
	/** MenuManager.getNextPageUrl - return the URL of the next visible page of the given url. */
	getNextPageUrl : function(pUrl, pNoRequest){
		var vCurrItem = null;
		if (this.fCurrItem && this.fCurrItem.url == pUrl) vCurrItem = this.fCurrItem;
		if (!vCurrItem){
			var vItems = this.fItemIndex[pUrl];
			if (!vItems) return null;
			vCurrItem = vItems[0];
		}
		var vNxtItem = vCurrItem.nxt;
		if (this.fFilter){
			while (vNxtItem && !vNxtItem.act) vNxtItem = vNxtItem.nxt;
		}
		if (pNoRequest) return (vNxtItem ? vNxtItem.url : null);
		this.fRequestedItem = vNxtItem;
		return (this.fRequestedItem ? this.fRequestedItem.url : null);
	},
	/** MenuManager.getPreviousPageUrl - return the URL of the previous visible page of the given url. */
	getPreviousPageUrl : function(pUrl, pNoRequest){
		var vCurrItem = null;
		if (this.fCurrItem && this.fCurrItem.url == pUrl) vCurrItem = this.fCurrItem;
		if (!vCurrItem){
			var vItems = this.fItemIndex[pUrl];
			if (!vItems) return null;
			vCurrItem = vItems[0];
		}
		var vPrvItem = vCurrItem.prv;
		if (this.fFilter){
			while (vPrvItem && !vPrvItem.act) vPrvItem = vPrvItem.prv;
		}
		if (pNoRequest) return (vPrvItem ? vPrvItem.url : null);
		this.fRequestedItem = vPrvItem;
		return (this.fRequestedItem ? this.fRequestedItem.url : null);
	},
	/** MenuManager.getPageCount - return filtered page cout. */
	getPageCount : function(){
		return this.fOutline.cntAct;
	},
	/** MenuManager.getPageCount - return the page rank of the given url. */
	getPageRank : function(pUrl){
		var vCurrItem = null;
		if (this.fCurrItem && this.fCurrItem.url == pUrl) vCurrItem = this.fCurrItem;
		if (!vCurrItem){
			var vItems = this.fItemIndex[pUrl];
			if (!vItems) return null;
			vCurrItem = vItems[0];
		}
		return (vCurrItem ? vCurrItem.cnt : null);
	},
	/** MenuManager.applyFilter - apply a filter on the menu based on the given array of visible pages. */
	applyFilter : function(pPageList) {
		if (this.fOpt.neverFilter) return;
		this.resetMenu();
		this.fFilter = true;
		for (var i=0; i<pPageList.length; i++){
			var vItems = this.fItemIndex[pPageList[i]];
			if (vItems){
				for (var j=0; j<vItems.length; j++){
					var vItem = vItems[j];
					vItem.vis = true;
					vItem.act = true;
					this.fOutline.cntAct++;
					while (vItem.par && !vItem.par.vis){
						vItem = vItem.par;
						vItem.vis = true;
					}
				}
			}
		}
		var vCnt = 0;
		var iSetRank = function (pItem){
			if (pItem.act) pItem.cnt = ++vCnt;
			if (pItem.children) {
				for (var i=0; i<pItem.children.length; i++) iSetRank(pItem.children[i]);
			}
		}
		iSetRank(this.fOutline);
		this.rebuildMenu();
		var iFindFirstItems = function (pItem, pArray){
			if (pItem.act) pArray.push(pItem);
			if (pArray.length >= outMgr.fMaxFilterDisplay) return true;
			if (pItem.children) {
				for (var i=0; i<pItem.children.length; i++) if (iFindFirstItems(pItem.children[i], pArray)) return true;
			}
			else return false;
		}
		var vFirstItems = [];
		iFindFirstItems(this.fOutline, vFirstItems);
		for (var i=0; i<vFirstItems.length; i++){
			this.buildAncestorMenus(vFirstItems[i],false);
			this.openAncestors(vFirstItems[i]);
		}
		if (vFirstItems.length>0) this.fFirstFilteredItem = vFirstItems[0];
		outMgr.xSwitchClass(this.fRoot, "mnu_sch_no", "mnu_sch_yes");
	},
	/** MenuManager.resetFilter - resert the current filter and rebuild the menu. */
	resetFilter : function(pPageList) {
		this.fFilter = false;
		this.rebuildMenu();
		this.fFirstFilteredItem = null;
		outMgr.xSwitchClass(this.fRoot, "mnu_sch_yes", "mnu_sch_no");
		if (this.fCurrItem) this.loadPage(this.fCurrItem.url);
	},
	/** MenuManager.loadPage - set the given url as the 'active' page. */
	loadPage : function(pUri) {
		var vItems;
		if (this.fCurrItem) {
			vItems = this.fItemIndex[this.fCurrItem.url];
			for (var i=0; i<vItems.length; i++) if (vItems[i].fLbl) outMgr.xSwitchClass(vItems[i].fLbl, "mnu_sel_yes", "mnu_sel_no");
		}
		vItems = this.fItemIndex[pUri];
		if (!vItems) {
			if (this.fContext) this.fContext.innerHTML = "";
			this.fCurrItem = null;
			return;
		}
		var vItemPresent = false;
		for (var i=0; i<vItems.length; i++){
			var vItem = vItems[i];
			if (!this.fFilter || vItem.act){
				if (!vItem.fLbl) this.buildAncestorMenus(vItem);
				this.openAncestors(vItem);
				if (vItem.fLbl.fTglBtn && scPaLib.checkNode(outMgr.sFilterTgleClosed,vItem.fLbl.fTglBtn)) this.toggleMnuItem(vItem.fLbl.fTglBtn);
				outMgr.xSwitchClass(vItem.fLbl, "mnu_sel_no", "mnu_sel_yes");
				vItemPresent = true;
			}
		}
		if (!vItemPresent) {
			if (this.fContext) this.fContext.innerHTML = "";
			this.fCurrItem = null;
			return;
		}
		if (this.fRequestedItem && this.fRequestedItem.url == pUri) this.fCurrItem = this.fRequestedItem;
		else this.fCurrItem = vItems[0];
		if (this.fScrollerEnabled) this.ensureVisible();
		if (this.fContext) this.fContext.innerHTML = this.buildContextMenu(this.fCurrItem);
	},
	/** MenuManager.openAncestors - open all closed ancestors of the given item. */
	openAncestors : function(pItem) {
		// Make shure this label is visible (open all ancestors)
		var vClosedSubMnus = scPaLib.findNodes("anc:ul.mnu_sub_c",pItem.fLbl);
		for (var i=0; i < vClosedSubMnus.length; i++){
			this.toggleMnuItem(vClosedSubMnus[i].fTglBtn);
		}
	},
	/** MenuManager.sToggleMnuItem - sub-menu toggle button callback function. */
	sToggleMnuItem : function() {
		try{
			this.fUl.fMgr.toggleMnuItem(this);
		} catch(e){}
		return false;
	},
	/** MenuManager.toggleMnuItem - toggle the sub-menu of the given toggle button. */
	toggleMnuItem : function(pBtn) {
		if (!pBtn) return;
		var vStatus = pBtn.className;
		var vUl = pBtn.fUl;
		if (!vUl) return;
		if (!vUl.fBuilt) this.buildSubMenu(vUl);
		if(vStatus == "mnu_tgle_c") {
			pBtn.className = "mnu_tgle_o";
			pBtn.innerHTML = "<span>v</span>"
			vUl.className = vUl.className.replace("mnu_sub_c", "mnu_sub_o");
			if (scCoLib.isIE) this.fRoot.style.visibility = "hidden"; // controunement bug ie7
			vUl.style.display = "";
			if (scCoLib.isIE) this.fRoot.style.visibility = "";
			vUl.fClosed = false;
		} else {
			pBtn.className = "mnu_tgle_c";
			pBtn.innerHTML = "<span>></span>"
			vUl.className = vUl.className.replace("mnu_sub_o", "mnu_sub_c");
			vUl.style.display = "none";
			vUl.fClosed = true;
			var vOpendSubMnus = scPaLib.findNodes("chi:li/chi:ul.mnu_sub_o",vUl);
			for (var i=0; i < vOpendSubMnus.length; i++) this.toggleMnuItem(vOpendSubMnus[i].fTglBtn);
		}
		if (this.fOpt.addScroller) this.checkScrollBtns();
	},
	/** MenuManager scroll timer & size task */
	fClassOffUp : "btnOff",
	fClassOffDown : "btnOff",
	fSpeed : 0,
	execTask : function(){
		try {
			if(this.fSpeed == 0) return false;
			this.fRoot.scrollTop += this.fSpeed;
			return true;
		}catch(e){
			this.fSpeed = 0;
			return false;
		}
	},
	setScrollStep: function(pPx) {
		try {this.fRoot.scrollTop += pPx;}catch(e){}
	},
	ensureVisible: function(){
		if (!this.fCurrItem) return;
		var vParent = this.fCurrItem.fLbl.offsetParent;
		if( !vParent) return;
		var vOffset = this.fCurrItem.fLbl.offsetTop;
		while(vParent != this.fRoot) {
			var vNewParent = vParent.offsetParent;
			vOffset += vParent.offsetTop;
			vParent = vNewParent;
		}
		if (vOffset < this.fRoot.scrollTop) this.fRoot.scrollTop = vOffset;
		else if (vOffset + this.fCurrItem.fLbl.offsetHeight > this.fRoot.scrollTop + this.fRoot.clientHeight) this.fRoot.scrollTop = vOffset - this.fRoot.clientHeight + this.fCurrItem.fLbl.offsetHeight;
	},
	checkScrollBtns: function(){
		var vScrollTop = this.fRoot.scrollTop;
		var vBtnUpOff = this.fSrlUp.className.indexOf(this.fClassOffUp);
		if(vScrollTop <= 0) {
			if(vBtnUpOff < 0) this.fSrlUp.className+= " "+this.fClassOffUp;
		} else {
			if(vBtnUpOff >= 0) this.fSrlUp.className = this.fSrlUp.className.substring(0, vBtnUpOff);
		}
		var vContentH = scSiLib.getContentHeight(this.fRoot);
		var vBtnDownOff = this.fSrlDwn.className.indexOf(this.fClassOffDown);
		if( vContentH - vScrollTop <= this.fRoot.offsetHeight){
			if(vBtnDownOff < 0) this.fSrlDwn.className+= " "+this.fClassOffDown;
		} else {
			if(vBtnDownOff >=0) this.fSrlDwn.className = this.fSrlDwn.className.substring(0, vBtnDownOff);
		}
	},
	onResizedAnc:function(pOwnerNode, pEvent){
		if(pEvent.phase==1 || pEvent.resizedNode == pOwnerNode) return;
		this.ensureVisible();
		this.checkScrollBtns();
	},
	onResizedDes:function(pOwnerNode, pEvent){
		if(pEvent.phase==1) return;
		this.ensureVisible();
	},
	ruleSortKey : "checkScrollBtns"

}