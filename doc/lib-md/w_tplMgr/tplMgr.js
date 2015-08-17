/* Doctek page manager */
var tplMgr = {
	fCbkInit : true,
	fCbkPath : "des:.cbkClosed",
	fAnchorPath : "des:a.blockAnchor|a.part_ti_lo",
	fAbsPath : "des:.nestedTopics|.idxEntryCo/des:.abstract",
	fBkBtnPath : "des:.outBkBtn/des:a",

	init : function(){

		var vHash = window.location.hash;
		if (vHash.length>0); vHash = vHash.substring(1);

		if (window.frameElement && window.frameElement.loadPage){
			window.frameElement.loadPage(scServices.scLoad.getUrlFromRoot(scCoLib.hrefBase()), document.title, vHash);
		} else {
			scCoLib.log("WARNING: no loadPage");
			var vLinks = document.getElementsByTagName("link");
			if (!window.frameElement) for (var i=0; i< vLinks.length; i++){if (vLinks[i].getAttribute("rel").indexOf("start")==0) window.location.replace(vLinks[i].href+"?p="+scServices.scLoad.getUrlFromRoot(scCoLib.hrefBase()))}
		}
		this.fStore = window.frameElement.fStore;
		
		// Close collapsable blocks that are closed by default.
		if (this.fCbkInit){
			var vCbks = scPaLib.findNodes(this.fCbkPath);
			for (var i=0; i<vCbks.length; i++) {
				if (!vHash || vHash && vHash != scPaLib.findNode("chi:", vCbks[i]).id) {
					var vTgl = scPaLib.findNode("des:a", vCbks[i]);
					if (vTgl) vTgl.onclick();
				}
			}
		}

		var vAnchors = scPaLib.findNodes(this.fAnchorPath);
		for (var i=0; i<vAnchors.length; i++) vAnchors[i].onclick = this.sAnchorClick;

		//Section outline
		this.fSecOutCo = scPaLib.findNode("ide:content/des:div.secOutFra/chi:div.secOutUi/chi:ol");
		if(this.fSecOutCo){
			this.fSecOutBtn = scPaLib.findNode("ide:content/des:div.secOutFra/chi:div.secOutTi/chi:a");
			if(this.fStore && this.fStore.get("secOutCollapse")=="true") this.secOutToggle();
		}
		
		if ("scTooltipMgr" in window ) {
			scTooltipMgr.addShowListener(this.sTtShow);
			scTooltipMgr.addHideListener(this.sTtHide);
		}
		if (window.parent && window.parent.frameMgr) this.fStore = window.parent.frameMgr.fStore;
		scOnLoads[scOnLoads.length] = this;
		scOnUnloads[scOnUnload.length] = this;
	},

	onLoad : function(){
	},

	onUnload : function(){
		if (window.frameElement && window.frameElement.unloadPage) window.frameElement.unloadPage();
	},
	
	secOutToggle : function() {
		if (!this.fSecOutCo || !this.fSecOutBtn) return false;
		scDynUiMgr.collBlkToggle(this.fSecOutBtn,this.fSecOutCo,"secOut_op","secOut_cl");
		if (this.fStore) this.fStore.set("secOutCollapse", this.fSecOutCo.style.display == "none");
		return false;
	},

	makeVisible : function(pNode){
		// Ouvre bloc collapsable contenant pNode
		var vCollBlk = scPaLib.findNode("anc:.collBlk_closed",pNode);
		if(vCollBlk) vCollBlk.fTitle.onclick();
	},

	setExpandAll : function(pExpand){
		if ("dokielMgr" in window){
			if (pExpand) dokielMgr.cancelStepMode();
			dokielMgr.setInteractiveScreenMode(!pExpand);
		}
		if("scSiLib" in window) scSiLib.fireResizedNode(sc$("page"));
	},
/* === Callback functions =================================================== */
	/** Tooltip lib show callback */
	sTtShow: function(pNode) {
		var vClsBtn = scPaLib.findNode("des:a.tt_x", scTooltipMgr.fCurrTt);
		if (vClsBtn) window.setTimeout(function(){vClsBtn.focus();}, pNode.fOpt.DELAY + 10);
	},
	/** Tooltip lib hide callback : this = scTooltipMgr */
	sTtHide: function(pNode) {
		if (pNode) pNode.focus();
	},
	
	sAnchorClick: function() {
		if (window.frameElement && window.frameElement.loadPage){
			window.frameElement.loadPage(scServices.scLoad.getUrlFromRoot(scCoLib.hrefBase()), document.title, this.hash.substring(1));
		}
	},
	
/* === Utilities ============================================================ */
	/** tplMgr.xAddBtn : Add a HTML button to a parent node. */
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

	/** tplMgr.xSwitchClass - replace a class name. */
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
	}
}
