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
 * Portions created by the Initial Developer are Copyright (C) 2012
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

/* ========== Search manager ================================================ */
var searchMgr = {
	fPathRoot : "des:div",
	fPathSchBox : "",
	fPathResBox : "",
	fStrings : ["Annuler","Annuler la recherche",                                                    //0
	            "Rechercher un mot","Aucun résultat.",                                               //2
	            "1 page trouvée","%s pages trouvées",                                                //4
	            "Précisez votre recherche...","Termes recherchés : <span class=\'schTerm\'>%s</span>", //6
	            "Précédent","Occurrence précédente",                                                 //8
	            "Suivant","Occurrence Suivante",                                                     //10
	            "Page précédente","Page Suivante"],                                                  //12

/* ========== Public functions ============================================== */

	declareIndex: function(pIdx){
		this.fIdxUrl = pIdx;
	},

	init: function(pPathSchBox,pPathResBox){
		if (typeof pPathSchBox != "undefined") this.fPathSchBox = pPathSchBox;
		if (typeof pPathResBox != "undefined") this.fPathResBox = pPathResBox;
		this.fRoot = scPaLib.findNode(this.fPathRoot);
		var vSchBox = scPaLib.findNode(this.fPathSchBox);
		if (!vSchBox) return;
		this.fSearchCmds = scDynUiMgr.addElement("div",vSchBox,"schCmds");
		this.fSearchRes = scPaLib.findNode(this.fPathResBox);
		scOnLoads[scOnLoads.length] = this;
	},

	onLoad: function(){
		try{
			frameMgr.registerListener("loadPage", this.sOnLoadPage);
			this.fRoot.className = this.fRoot.className + " schActive";
			this.fSearchInput = scDynUiMgr.addElement("input",this.fSearchCmds,"schInput");
			this.fSearchInput.title=this.fStrings[2];
			this.fSearchInput.onkeyup = this.sKeyUp;
			this.fSearchLaunch = this.xAddBtn(this.fSearchCmds,"schBtnLaunch","?",this.fStrings[2]);
			this.fSearchLaunch.onclick = this.sFind;
			this.fSearchPropose = scDynUiMgr.addElement("div",this.fSearchCmds,"schPropose schProp_no");

			this.fSearchReset = this.xAddBtn(this.fSearchRes,"schBtnReset","X",this.fStrings[1]);
			this.fSearchReset.onclick = this.sReset;

			var vSchResBox = scDynUiMgr.addElement("div",this.fSearchRes,"schResBox");
			this.fSearchLbl = scDynUiMgr.addElement("span",vSchResBox,"schResLbl");
			this.fBtnPrv = this.xAddBtn(vSchResBox,"schBtnPrv",this.fStrings[8],this.fStrings[12]);
			this.fBtnPrv.onclick = this.sPrv;
			this.fSearchCnt = scDynUiMgr.addElement("span",vSchResBox,"schResCnt");
			this.fBtnNxt = this.xAddBtn(vSchResBox,"schBtnNxt",this.fStrings[10],this.fStrings[13]);
			this.fBtnNxt.onclick = this.sNxt;

			var vSchHitBox = scDynUiMgr.addElement("div",this.fSearchRes,"schHitBox");
			this.fHitLbl = scDynUiMgr.addElement("span",vSchHitBox,"schHitLbl");
			this.fBtnPrvHit = this.xAddBtn(vSchHitBox,"schBtnPrvHit",this.fStrings[8],this.fStrings[9]);
			this.fBtnPrvHit.onclick = this.sPrvHit;
			this.fHitCnt = scDynUiMgr.addElement("span",vSchHitBox,"schHitCnt");
			this.fBtnNxtHit = this.xAddBtn(vSchHitBox,"schBtnNxtHit",this.fStrings[10],this.fStrings[11]);
			this.fBtnNxtHit.onclick = this.sNxtHit;
		}catch(e){scCoLib.log("ERROR - searchMgr.onLoad : "+e)}
	},

	focus: function(){
	 if (this.fSearchInput) this.fSearchInput.focus();
	},

	propose: function(){
		try{
			var vStr = this.fSearchInput.value;
			var vWds = scServices.scSearch.propose(this.fIdxUrl, vStr,{async:true});
			var vShowProp = !vWds || (vWds && vWds.length==0 && vStr.length<3) || vWds && vWds.length>0;
			this.xSwitchClass(this.fSearchPropose,"schProp_"+(vShowProp ? "no" : "yes"), "schProp_"+(vShowProp ? "yes" : "no"), true);
			this.fSearchPropose.fShown = vShowProp ? true : false;
		
			var vProp;
			this.fSearchPropose.innerHTML = "";
			if (vWds && vWds.length>0){
				for(var i = 0;i<vWds.length;i++){
					vProp = this.xAddBtn(this.fSearchPropose,"schBtnPropose",vWds[i].wrd);
					vProp.onclick = this.sProp;
					vProp.onkeyup = this.sPropKeyUp;
				}
			} else if (!vWds || (vWds && vWds.length==0 && vStr.length<3)){
				scDynUiMgr.addElement("span", this.fSearchPropose,"schProposeExceeded").innerHTML=this.fStrings[6];
			}
		}catch(e){scCoLib.log("ERROR - searchMgr.propose : "+e)}
	},

	find: function(){
		this.xResetHighlight();
		this.xSwitchClass(this.fSearchPropose,"schProp_yes", "schProp_no", true);
		this.fSearchPropose.fShown = false;
		this.fSearchPropose.innerHTML = "";
		var vRes = scServices.scSearch.find(this.fIdxUrl, this.fSearchInput.value);

		this.fSearchDisplay = true;
		this.xSwitchClass(searchMgr.fRoot, "schDisplay_off", "schDisplay_on", true);

		this.fPageList = {ctrl:{},list:[]};
		if (vRes && vRes.length > 0){
			for (var i = 0; i < vRes.length; i++){
				var vPageUrl = vRes[i].url;
				//vPageUrl = escape(vPageUrl.substring(vPageUrl.lastIndexOf("/")+1));
				this.fPageList.ctrl[vPageUrl] = true;
				this.fPageList.list.push(vPageUrl);
			}
		} else {
			this.fSearchLbl.innerHTML = this.fStrings[3];
			this.xSwitchClass(this.fSearchRes, "schDisplay_", "schDisplay_none", true, false);
		}
		frameMgr.fireEvent("showSearch", this.fPageList.list);
		frameMgr.goToFirstPage();
	},

	reset: function(){
		if (!this.fSearchDisplay) return;
		this.fSearchDisplay = false;
		this.xSwitchClass(this.fRoot, "schDisplay_on", "schDisplay_off", true);
		this.xSwitchClass(this.fSearchCmds,"schCmds_act", "schCmds_noact", true);
		this.fSearchInput.value = "";
		this.fPageList = {ctrl:{},list:[]};
		this.fSearchLbl.innerHTML = "";
		frameMgr.fireEvent("resetSearch");
		this.xResetHighlight();
		frameMgr.reload();
	},

/* === Callback functions =================================================== */
	sOnLoadPage: function(pUri){
		if (!searchMgr.fSearchDisplay) return;
		if (searchMgr.fPageList.ctrl[pUri]){
			searchMgr.xSwitchClass(searchMgr.fRoot, "schDisplay_off", "schDisplay_on", true);
			searchMgr.xSwitchClass(searchMgr.fSearchRes, "schDisplay_", "schDisplay_"+(searchMgr.fPageList.length == 1 ? "one" : "many"), true, false);
			var vRoot = scPaLib.findNode("ide:content",frameMgr.fFra.contentWindow.document);
			if (!vRoot) return;
			searchMgr.xHighlight(vRoot, searchMgr.fSearchInput.value);
			searchMgr.xUpdateResUi();
		} else {
			searchMgr.xSwitchClass(searchMgr.fSearchRes, "schDisplay_", "schDisplay_none", true, false);
		}
	},

	sReset: function(){
		searchMgr.reset();
		return false;
	},

	sFind: function(){
		searchMgr.find();
		return false;
	},

	sProp: function(){
		searchMgr.fSearchInput.value = this.firstChild.innerHTML;
		searchMgr.find();
		return false;
	},

	sPropKeyUp: function(pEvt){
		var vEvt = pEvt || window.event;
		var vNode;
		switch(vEvt.keyCode){
		case 40:
			vNode = scPaLib.findNode("nsi:a",this);
			break;
		case 38:
			vNode = scPaLib.findNode("psi:a",this);
			if(!vNode) vNode = scPaLib.findNode("anc:.schCmds/chi:.schInput",this);
		}
		if (vNode) vNode.focus();
	},

	sKeyUp: function(pEvt){
		var vEvt = pEvt || window.event;
		if (this.value.length>0) searchMgr.xSwitchClass(searchMgr.fSearchCmds,"schCmds_noact", "schCmds_act", true);
		else searchMgr.xSwitchClass(searchMgr.fSearchCmds,"schCmds_act", "schCmds_noact", true);
		
		if (this.value.length==0) searchMgr.reset();
		if (this.value.length>0) searchMgr.propose();
		else {
			searchMgr.xSwitchClass(searchMgr.fSearchPropose,"schProp_yes", "schProp_no", true);
			searchMgr.fSearchPropose.fShown = false;
			searchMgr.fSearchPropose.innerHTML = "";
		}
		if (this.value.length>0 && vEvt.keyCode == "13") searchMgr.find();
		if (searchMgr.fSearchPropose.fShown && vEvt.keyCode == "40") {
			var vProp = scPaLib.findNode("chi.a",searchMgr.fSearchPropose);
			if (vProp) vProp.focus();
		}
		if (vEvt.stopPropagation) vEvt.stopPropagation();
		else vEvt.cancelBubble = true;
	},

	sPrv: function(){
		frameMgr.goToPreviousPage();
		return false;
	},

	sNxt: function(){
		frameMgr.goToNextPage();
		return false;
	},

	sPrvHit: function(){
		if (searchMgr.fTextHits && searchMgr.fTextHits.length>0 && searchMgr.fCurrHit>0){
			searchMgr.xSwitchClass(searchMgr.fTextHits[searchMgr.fCurrHit], "schHit_current", "schHit");
			searchMgr.xSwitchClass(searchMgr.fTextHits[--searchMgr.fCurrHit], "schHit", "schHit_current");
			frameMgr.scrollTo(searchMgr.fTextHits[searchMgr.fCurrHit].id);
			searchMgr.xUpdateHitUi();
		} else searchMgr.sPrv();
		return false;
	},

	sNxtHit: function(){
		if (searchMgr.fTextHits && searchMgr.fTextHits.length>0 && searchMgr.fCurrHit<searchMgr.fTextHits.length-1){
			if (searchMgr.fCurrHit>=0) searchMgr.xSwitchClass(searchMgr.fTextHits[searchMgr.fCurrHit], "schHit_current", "schHit");
			searchMgr.xSwitchClass(searchMgr.fTextHits[++searchMgr.fCurrHit], "schHit", "schHit_current");
			frameMgr.scrollTo(searchMgr.fTextHits[searchMgr.fCurrHit].id);
			searchMgr.xUpdateHitUi();
		} else searchMgr.sNxt();
		return false;
	},

	/* ========== Private functions =========================================== */
	xLoadIndex: function(){
		scServices.scSearch.declareIndex(this.fIdxUrl);
	},

	xResetUi: function(){
		if (!this.fSearchDisplay) return;
		this.fSearchDisplay = false;
		this.xSwitchClass(this.fRoot, "schDisplay_on", "schDisplay_off", true);
		this.xSwitchClass(this.fRoot, "schDisplayList_on", "schDisplayist_off", true);
		this.fSearchInput.value = "";
		this.fSearchInput.className = "schInput";
		this.fSearchLbl.innerHTML = "";
		this.xSwitchClass(this.fSearchPropose,"schProp_yes", "schProp_no", true);
		this.fSearchPropose.fShown = false;
		this.fSearchPropose.innerHTML = "";
		this.xResetHighlight();
	},

	xUpdateResUi: function(){
		if (frameMgr.hasPreviousPage()) this.xSwitchClass(this.fBtnPrv,"schBtnAct_no", "schBtnAct_yes", true);
		else this.xSwitchClass(this.fBtnPrv,"schBtnAct_yes", "schBtnAct_no", true);
		if (frameMgr.hasNextPage()) this.xSwitchClass(this.fBtnNxt,"schBtnAct_no", "schBtnAct_yes", true);
		else this.xSwitchClass(this.fBtnNxt,"schBtnAct_yes", "schBtnAct_no", true);
		var vPageCount = frameMgr.getPageCount();
		if (vPageCount && vPageCount > 0) {
			this.fSearchCnt.innerHTML = frameMgr.getPageRank() + "/" + vPageCount;
			this.fSearchLbl.innerHTML = (vPageCount.length == 1 ? this.fStrings[4] : this.fStrings[5].replace("%s",vPageCount));
			this.xSwitchClass(this.fSearchRes, "schDisplay_", "schDisplay_"+(vPageCount.length == 1 ? "one" : "many"), true, false);
		}
		else this.fSearchCnt.innerHTML = "";
	},

	xUpdateHitUi: function(){
		if (this.fTextHits && this.fTextHits.length>0 && this.fCurrHit>0) this.xSwitchClass(this.fBtnPrvHit,"schBtnHitAct_no", "schBtnHitAct_yes", true);
		else this.xSwitchClass(this.fBtnPrvHit,"schBtnHitAct_yes", "schBtnHitAct_no", true);
		if (this.fTextHits && this.fTextHits.length>0 && this.fCurrHit<this.fTextHits.length-1) this.xSwitchClass(this.fBtnNxtHit,"schBtnHitAct_no", "schBtnHitAct_yes", true);
		else this.xSwitchClass(this.fBtnNxtHit,"schBtnHitAct_yes", "schBtnHitAct_no", true);
		if (this.fTextHits.length>0 && this.fCurrHit>=0) this.fHitCnt.innerHTML = (this.fCurrHit+1) + "/" + this.fTextHits.length;
		else this.fHitCnt.innerHTML = "";
	},

	xHighlight: function(pRoot, pStr){
		var vTextNodes = [];
		var vNoIdxFilter = scPaLib.compileFilter(".noIndex|.footNotes|script|noscript|object|.tooltip_ref|.CodeMirror-linenumber");
		var textNodeWalker = function (pNde){
			while (pNde){
				if (pNde.nodeType == 3) vTextNodes.push(pNde);
				else if (pNde.nodeType == 1 && !scPaLib.checkNode(vNoIdxFilter,pNde)) textNodeWalker(pNde.firstChild);
				pNde = pNde.nextSibling;
			}
		}
		textNodeWalker(pRoot.firstChild);
		var i,j,k,vTxtNode,vTxtVal,vTxtNorm,vTxtMached,vHolder,vToken,vHits,vHit,vReg,vOffset,vIsOldOffset;
		var vTokens = scServices.scSearch.buildTokens(this.fIdxUrl, pStr);
		for (i = 0; i<vTokens.length;i++) vTokens[i].fCount=0;
		for (i = 0; i<vTextNodes.length;i++){
			vHits = [];
			vTxtNode = vTextNodes[i];
			vTxtNorm = scServices.scSearch.normalizeString(this.fIdxUrl, vTxtNode.nodeValue);
			for (j = 0; j<vTokens.length;j++){
				vToken = vTokens[j];
				if (!vToken.neg && vTxtNorm.length>=vToken.wrd.length){
					if (vToken.exact) vReg = new RegExp("(?:^|\\W)("+vToken.wrd+")(?:$|\\W)","i");
					else vReg = new RegExp(vToken.wrd,"i");
					vOffset = vTxtNorm.search(vReg);
					while(vOffset>=0){
						vToken.fCount++
						if(vToken.exact && /\W/.test(vTxtNorm.charAt(vOffset))) vOffset++;
						vIsOldOffset = false;
						vHit = {start:vOffset,end:vOffset+vToken.wrd.length};
						for (k = 0; k<vHits.length;k++){
							if (vHit.start>=vHits[k].start && vHit.start<=vHits[k].end || vHit.end>=vHits[k].start && vHit.end<=vHits[k].end){
								vHits[k].start = Math.min(vHit.start,vHits[k].start);
								vHits[k].end = Math.max(vHit.end,vHits[k].end);
								vIsOldOffset = true;
							}
						}
						if (!vIsOldOffset) vHits.push(vHit);
						vOffset = vTxtNorm.substring(vHit.end).search(vReg);
						if (vOffset>=0) vOffset = vHit.end + vOffset;
					}
				}
			}
			if (vHits.length>0){
				// Ouvre bloc collapsable contenant un schHit
				var vCollBlk = scPaLib.findNode("anc:.collBlk_closed",vTxtNode);
				if(vCollBlk) vCollBlk.fTitle.onclick();
				// On s'assure de la visibilité du hit si dans un concept-tree
				if ("treeMgr" in window) treeMgr.makeVisible(vTxtNode);
				vHits.sort(function(a,b){return a.start - b.start});
				var vIdx = 0;
				vTxtMached = "";
				vTxtVal = vTxtNode.nodeValue;
				for (var j = 0; j<vHits.length;j++){
					vHit = vHits[j];
					vTxtMached += vTxtVal.substring(vIdx,vHit.start).replace(/</g, "&lt;");
					vTxtMached += "<span class='schHit' id='schId"+i+j+"'>"+vTxtVal.substring(vHit.start,vHit.end).replace(/</g, "&lt;")+"</span>";
					vIdx = vHit.end;
				}
				vTxtMached += vTxtVal.substring(vHits[vHits.length-1].end).replace(/</g, "&lt;");
				vHolder = scDynUiMgr.addElement("span", vTxtNode.parentNode, null, vTxtNode);
				vTxtNode.parentNode.removeChild(vTxtNode);
				vHolder.innerHTML = vTxtMached;
			}
		}
		this.fTextHits = scPaLib.findNodes("des:span.schHit",pRoot);
		var vDispTokens = [];
		for (i = 0; i<vTokens.length;i++){
			vToken = vTokens[i];
			if (!vToken.neg) vDispTokens.push((vToken.exact?'"':'')+vToken.wrd+(vToken.exact?'"':'')+" <em>("+vToken.fCount+")</em>");
		}
		this.fHitLbl.innerHTML = this.fStrings[7].replace("%s",vDispTokens.join(", "));
		this.fCurrHit = -1;
		if(this.fTextHits.length) this.sNxtHit();
	},

	xResetHighlight: function(){
		if (!this.fTextHits || this.fTextHits.length==0) return;
		for (i = 0; i<this.fTextHits.length;i++){
			var vTextHit = this.fTextHits[i];
			var vParent = vTextHit.parentNode;
			var vTextNode = vParent.ownerDocument.createTextNode(String(vTextHit.firstChild.nodeValue));
			vParent.insertBefore(vTextNode,vTextHit);
			vParent.removeChild(vTextHit);
		}
		this.fTextHits = [];
		this.fCurrHit = -1;
		this.fHitLbl.innerHTML = "";
	},
	
	/* === Utilities ========================================================== */

	/** outMgr.xAddBtn : Add a HTML button to a parent node. */
	xAddBtn : frameMgr.xAddBtn,

	/** outMgr.xSwitchClass - replace a class name. */
	xSwitchClass : frameMgr.xSwitchClass,

	loadSortKey: "ZZsearchMgr"
}

