

var scTooltipMgr = {

	cTtAbove : false, // tooltip above mousepointer?
	cTtDelay : 500, // time span until tooltip shows up [milliseconds]
	cTtLeft : false, // tooltip on the left of the mouse?
	cTtOffsetX : 12, // horizontal offset of left-top corner from mousepointer
	cTtOffsetY : 15, // vertical offset 
	cTtShadow : false, // Add shadow?
	cTtStatic : false, // tooltip NOT move with the mouse?
	cTtSticky : false, // do NOT hide tooltip on mouseout?
	cTtTemp : 0, // time span after which the tooltip disappears, 0 (zero) means "infinite timespan"
	cTtMaxWidth : 400, // Max width of tooltips
	cTtMaxHeight : 250, // Max height of tooltips (a scrollbar is added)
	cTtFixType : "win", // type of fixed positionning (win,node,id)
	cTtTextAlign : "left",
	cTtHPos : "leftAlign",
	cTtVPos : "topAlign",


	fCurrTt : null, // current tooltip
	fCurrTtId : null, // current tooltip ID
	fIfrm : null, // iframe to cover windowed controls in IE
	fIfrmId : null, // ID of iframe to cover windowed controls in IE
	fCurrTtW : 0, fCurrTtH : 0, // width and height of fCurrTt
	fCurrTtX : 0, fCurrTtY : 0,
	fOffX : 0, fOffY : 0,
	fXlim : 0, fYlim : 0, // right and bottom borders of visible client area
	fSup : false, // true if Opt.ABOVE
	fSticky : false, // fCurrTt sticky?
	fWait : false,
	fAct : false, // tooltip visibility flag
	fSub : false, // true while tooltip below mousepointer
	fArea : false,
	fMmovEvt : null, // stores previous mousemove evthandler
	fWsizEvt : null, // stores previous window.onresize evthandler
	fMupEvt : null, // stores previous mouseup evthandler

	fTag : null, // stores hovered dom node, href and previous statusbar txt
	fDb : null, // Document body
	fNua : null, //userAgent
	fNuav : null, //Navigator version
	fFix : false,
	fFixId : null,
	fFixType : "win",
	fTtHPos : null,
	fTtVPos : null,
	fShowListeners : new Array(),
	fHideListeners : new Array(),
	fHideBasket : true,
	fRegCls : "scTtRegistered",
	

	fNavie:null, fNavie6:null, fNavie8:null, fNavn6:null, fNavw3c:null,
	

	xInitMgr: function() {
		if (this.fDb == null) {
			this.fDb = (document.compatMode && document.compatMode != "BackCompat")? document.documentElement : document.body? document.body : null;
			this.fNua = navigator.userAgent.toLowerCase();
			this.fNuav = navigator.appVersion;
			this.fNavie = this.fNua.indexOf("msie") != -1 && document.all && this.fDb;
			this.fNavie6 = this.fNavie && parseFloat(this.fNuav.substring(this.fNuav.indexOf("MSIE")+5)) < 7;
			this.fNavie8 = this.fNavie && parseFloat(this.fNuav.substring(this.fNuav.indexOf("MSIE")+5)) < 9;
			this.fNavn6 = (document.defaultView && typeof document.defaultView.getComputedStyle != "undefined");
			this.fNavw3c = !this.fNavie && !this.fNavn6 && document.getElementById;
		}
	},
	xInitIfrm: function() {
		if (this.fNavie6 && !this.xGetElt(this.fIfrmId)) {
			var vTmpDiv = document.createElement("DIV"); 
			this.fIfrmId = this.xGenId("TTiEiFrM")
			vTmpDiv.innerHTML = '<iframe id="'+this.fIfrmId+'" src="javascript:false" scrolling="no" frameborder="0" style="filter:Alpha(opacity=0);position:absolute;top:0px;left:0px;display:none;"></iframe>';
			var vTtDiv = vTmpDiv.firstChild;
			while(vTtDiv && vTtDiv.nodeType != 1) vTtDiv = vTtDiv.nextSibling;
			document.body.appendChild(vTtDiv);
			this.fIfrm = sc$(this.fIfrmId);
		}
	},
	xBuildCls: function(pCls, pSufx) {
		var vCls = pCls.split(" ");
		var vRetCls = "";
		for(var i=0; i<vCls.length; i++) vRetCls += vCls[i]+(pSufx ? ('_'+pSufx) : '')+' ';
		return(vRetCls);
	},
	xMakeTt: function(pNode, pCo, pTi, pCls, pClsRoot) {
		var vCo = typeof pCo == "string" ? pCo : "";
		var vTi = typeof pTi == "string" ? pTi : "";
		pCls = pCls + (pNode.fOpt.CLASS ? " "+pNode.fOpt.CLASS : "");
		pNode.ttId = this.xGenId("scTooltip"); //generate a new tooltip ID
		pNode.setAttribute("aria-describedby", pNode.ttId);
		pNode.fTtShownCls = this.xBuildCls(pCls,"act").split(' '); 
		var vMaxX = this.xInt((this.fDb && this.fDb.clientWidth)? this.fDb.clientWidth : window.innerWidth)+this.xInt(window.pageXOffset || (this.fDb? this.fDb.scrollLeft : 0) || 0);
		var vHtml = '<div id="'+pNode.ttId+'" role="dialog"'+(pTi? ' aria-labelledby="'+pNode.ttId+'ti"' : '')+' class="'+this.xBuildCls(pCls,"fra")+(pClsRoot!=''?' '+pClsRoot:'')+'"'+(pNode.fOpt.FIXTYPE!='free' ? ' style="position:absolute;z-index:1010;left:0px;top:0px;width:'+vMaxX+'px;visibility:hidden;text-align:left;"' : '')+'>';
		vHtml += '<div style="position:absolute;" class="'+this.xBuildCls(pCls,"")+'">';
		if(pTi) vHtml += '<div id="'+pNode.ttId+'ti" class="'+this.xBuildCls(pCls,"ti")+'"><span>'+vTi+'</span></div>';
		vHtml += '<div id="'+pNode.ttId+'Scrol" class="'+this.xBuildCls(pCls,"srl")+'"><div id="'+pNode.ttId+'co" class="'+this.xBuildCls(pCls,"co")+'">'+vCo+'</div></div>';
		if (pNode.fOpt.CLSBTN) vHtml += '<a href="#" onclick="scTooltipMgr.hideTooltip(true);return false;" class="'+this.xBuildCls(pCls,"x")+'" title="'+pNode.fOpt.CLSBTNTI+'"><span>'+pNode.fOpt.CLSBTNCAP+'</span></a>';
		vHtml += '</div></div>'
		var vTmpDiv=(pNode.fOpt.PARENT.ownerDocument?pNode.fOpt.PARENT.ownerDocument:pNode.fOpt.PARENT).createElement("DIV"); // Temp div to hold the created tooltip html
		vTmpDiv.innerHTML = vHtml;
		var vTtDiv = vTmpDiv.firstChild;
		while(vTtDiv && vTtDiv.nodeType != 1) vTtDiv = vTtDiv.nextSibling;
		pNode.fOpt.PARENT.appendChild(vTtDiv); //Apend the created tooltip to the required parent
		if (typeof pCo == "object") {
			var vTtCo = sc$(pNode.ttId+"co");
			var vCoElt = pCo.firstChild;
			while(vCoElt){
				vTtCo.appendChild(vCoElt.cloneNode(true));
				vCoElt = vCoElt.nextSibling;
			} 
		}
		if (typeof pTi == "object") {
			var vTtTi = sc$(pNode.ttId+"ti");
			var vTiElt = pTi.firstChild;
			while(vTiElt){
				vTtTi.appendChild(vTiElt.cloneNode(true));
				vTiElt = vTiElt.nextSibling;
			} 
		}
		vTtDiv.fNode = pNode; //Keep pointer to owner node on the tooltip
		this.xSetTtSize(pNode.ttId, pNode.fOpt); //Calculate size & resize if needed


		if (pNode.fOpt.FORCESTICKY) pNode.ttFSticky = true;
		pNode.fOpt.STICKY = pNode.fOpt.STICKY || (pNode.ttFSticky || false);
		if(!pNode.fOpt.STICKY) pNode.onmouseout = this.hideTooltip;
		return(pNode.ttId);
	},
	xShow: function(pEvt, pId, pOpt) {
		if(this.fCurrTt) this.hideTooltip(true);
		this.fMmovEvt = document.onmousemove || null;
		this.fWsizEvt = window.onresize || null;
		if(window.dd && (window.DRAG && this.fMmovEvt == DRAG || window.RESIZE && this.fMmovEvt == RESIZE)) return;
		this.fCurrTt = this.xGetElt(pId);
		this.fCurrTtId = pId;
		if(this.fCurrTt) {
			pEvt = pEvt || window.event;
			if(this.fCurrTt.fNode.alt) {
				this.fCurrTt.fNode.ttAlt = this.fCurrTt.fNode.alt;
				this.fCurrTt.fNode.alt = "";
			}
			if(this.fCurrTt.fNode.title) {
				this.fCurrTt.fNode.ttTitle  = this.fCurrTt.fNode.title;
				this.fCurrTt.fNode.title = "";
			}
			for(var i=0; i<this.fCurrTt.fNode.fTtShownCls.length; i++) this.xAddClass(this.fCurrTt.fNode, this.fCurrTt.fNode.fTtShownCls[i])
			this.fSub = !(this.fSup = pOpt.ABOVE);
			this.fSticky = pOpt.STICKY;
			this.fCurrTtW = this.xGetEltW(this.fCurrTt);
			this.fCurrTtH = this.xGetEltH(this.fCurrTt);
			this.fOffX = pOpt.LEFT ? -(this.fCurrTtW+pOpt.OFFSETX) : pOpt.OFFSETX;
			this.fOffY = pOpt.OFFSETY;
			this.fFix = pOpt.FIX;
			this.fFixId = pOpt.FIXID;
			this.fFixType = pOpt.FIXTYPE;
			this.fFixForce = pOpt.FIXFORCE;
			this.fTtHPos = pOpt.HPOS;
			this.fTtVPos = pOpt.VPOS;
			this.fXlim = this.xInt((this.fDb && this.fDb.clientWidth)? this.fDb.clientWidth : window.innerWidth)+this.xInt(window.pageXOffset || (this.fDb? this.fDb.scrollLeft : 0) || 0)-this.fCurrTtW;
			this.fYlim = this.xInt(window.innerHeight || this.fDb.clientHeight)+this.xInt(window.pageYOffset || (this.fDb? this.fDb.scrollTop : 0) || 0)-this.fCurrTtH-this.fOffY;
			this.xSetDivZ();
			if(pOpt.FIXTYPE != 'free'){
				if(pOpt.FIX) this.xSetDivPosFix(pOpt.FIXTYPE, pOpt.FIX[0], pOpt.FIX[1], pOpt.FIXID, pOpt.HPOS, pOpt.VPOS, pOpt.FIXFORCE);
				else this.xSetDivPos(this.xEvX(pEvt), this.xEvY(pEvt));
			}
			var vTimeOutTxt = 'scTooltipMgr.showDiv(\'true\');';
			if(pOpt.STICKY) vTimeOutTxt += '{scTooltipMgr.releaseMov();scTooltipMgr.releaseSize();scTooltipMgr.fMupEvt = document.onmouseup || null;if(document.captureEvents && !scTooltipMgr.fNavn6) document.captureEvents(Event.MOUSEUP);document.onmouseup = scTooltipMgr.hideTooltip;}';
			else if(pOpt.STATIC) vTimeOutTxt += 'scTooltipMgr.releaseMov();scTooltipMgr.releaseSize();';
			if(pOpt.TEMP > 0) vTimeOutTxt += 'window.tt_rtm = window.setTimeout(\'scTooltipMgr.fSticky = false; scTooltipMgr.hideTooltip();\','+pOpt.TEMP+');';
			window.tt_rdl = window.setTimeout(vTimeOutTxt, pOpt.DELAY);
			if(!pOpt.FIX && (!pOpt.STICKY || (pOpt.STICKY && this.fCurrTt.fNode.onmouseover))) {
				if(document.captureEvents && !this.fNavn6) document.captureEvents(Event.MOUSEMOVE);
				document.onmousemove = this.moveTooltip;
			} else if (pOpt.FIX && pOpt.FIXID) {
				if(document.captureEvents && !this.fNavn6) document.captureEvents(Event.RESIZE);
				window.onresize = this.reposTooltip;
			}
		}
	},
	xInt: function(pX){
		var vY;
		return isNaN(vY = parseInt(pX))? 0 : vY;
	},
	xEvX: function(pEvt){
		var vX = this.xInt(pEvt.pageX || pEvt.clientX || 0)+this.xInt(this.fNavie8? this.fDb.scrollLeft : 0)+this.fOffX;
		if(vX > this.fXlim) vX = this.fXlim;
		var vScr = this.xInt(window.pageXOffset || (this.fDb? this.fDb.scrollLeft : 0) || 0);
		if(vX < vScr) vX = vScr;
		return vX;
	},
	xEvY: function(pEvt) {
		var vY = this.xInt(pEvt.pageY || pEvt.clientY || 0)+this.xInt(this.fNavie8? this.fDb.scrollTop : 0);
		if(this.fSup) vY -= (this.fCurrTtH + this.fOffY - 15);
		else if(vY > this.fYlim || !this.fSub && vY > this.fYlim-24) {
			vY -= (this.fCurrTtH + 5);
			this.fSub = false;
		} else {
			vY += this.fOffY;
			this.fSub = true;
		}
		return (vY<0? 0 : vY);
	},
	xShowIfrm: function(pX) {
		if(!this.fCurrTt || !this.fIfrm) return;
		if(pX)	{
			this.fIfrm.style.width = this.fCurrTtW+'px';
			this.fIfrm.style.height = this.fCurrTtH+'px';
			this.fIfrm.style.display = "block";
		}
		else this.fIfrm.style.display = "none";
	},
	xGetElt: function(pId) {
		return(this.fNavie? (document.all[pId] || null) : (sc$(pId) || null));
	},
	xGetEltW: function(pElt) {
		return(this.xInt(pElt.style.pixelWidth || pElt.offsetWidth)+(this.fNavie? (this.xInt(pElt.currentStyle.borderRightWidth)+this.xInt(pElt.currentStyle.borderLeftWidth)):0));
	},
	xGetEltH: function(pElt) {
		return(this.xInt(pElt.style.pixelHeight || pElt.offsetHeight)+(this.fNavie? (this.xInt(pElt.currentStyle.borderTopWidth)+this.xInt(pElt.currentStyle.borderBottomWidth)):0));
	},
	xGetEltL: function(pElt) {
		var vX;
		if (pElt.style.pixelLeft) {
			vX = this.xInt(pElt.style.pixelLeft);
		} else {
			vX = this.xInt(pElt.offsetLeft);
			if (pElt.offsetParent.tagName.toLowerCase() != 'body' && pElt.offsetParent.tagName.toLowerCase() != 'html') {
				vX -= pElt.offsetParent.scrollLeft;
				vX += this.xGetEltL(pElt.offsetParent);
			}
		}
		return vX;
	},
	xGetEltT: function(pElt) {
		var vY;
		if (pElt.style.pixelTop) {
			vY = this.xInt(pElt.style.pixelTop);
		} else {
			vY = this.xInt(pElt.offsetTop);
			if (pElt.offsetParent.tagName.toLowerCase() != 'body' && pElt.offsetParent.tagName.toLowerCase() != 'html') {
				vY -= pElt.offsetParent.scrollTop;
				vY += this.xGetEltT(pElt.offsetParent);
			}
		}
		return vY;
	},
	xSetEltW: function(pElt, pW) {
		if(typeof pElt.style.pixelWidth != "undefined") pElt.style.pixelWidth = pW;
		else 
		pElt.style.width = pW+'px';
	},
	xSetEltH: function(pElt, pH) {
		if(typeof pElt.style.pixelHeight != "undefined") pElt.style.pixelHeight = pH;
		else pElt.style.height = pH+'px';
	},
	xSetEltT: function(pElt, pT) {
		if(typeof pElt.style.pixelTop != "undefined") pElt.style.pixelTop = pT;
		else pElt.style.top = pT+'px';
	},
	xSetEltL: function(pElt, pL) {
		if(typeof pElt.style.pixelLeft != "undefined") pElt.style.pixelLeft = pL;
		else pElt.style.left = pL+'px';
	},

	xSetDivZ: function() {
		var vTtsh = this.fCurrTt.style || this.fCurrTt;
		if(vTtsh) {
			if(window.dd && dd.z) vTtsh.zIndex = Math.max(dd.z+1, vTtsh.zIndex);
			if(this.fIfrm) this.fIfrm.style.zIndex = vTtsh.zIndex-1;
		}
	},
	xSetDivPosFix: function(pType, pX, pY, pRelId, pHPos, pVPos, pForce) {
		var vX;
		var vY;
		if (pType == 'free') return;
		else if (pType == "win"){
			switch(pHPos){
			case "center":
				vX = this.fXlim / 2 + pX;
				break
			case "rightAlign":
				vX = this.fXlim + pX;
				break
			default :
				vX = pX;
			}
			switch(pVPos){
			case "center":
				vY = this.fYlim / 2 + pX;
				break
			case "bottomAlign":
				vY = this.fYlim + pX;
				break
			default :
				vY = pY;
			}
		} else {
			var vRelBase = null;
			switch(pType){
			case "id":
				vRelBase = sc$(pRelId);
				break;
			case "dom":
				vRelBase = pRelId;
				break;
			default :
				vRelBase = this.fCurrTt.fNode;
			}
			switch(pHPos){
			case "center":
				vX = this.xGetEltL(vRelBase) + (this.xGetEltW(vRelBase) - this.fCurrTtW)/2 + pX;
				break
			case "rightAlign":
				vX = this.xGetEltL(vRelBase) + this.xGetEltW(vRelBase) - this.fCurrTtW + pX;
				break
			case "leftOfElement":
				vX = this.xGetEltL(vRelBase) - this.fCurrTtW + pX;
				if (!pForce && !this.xIsInWinH(vX)) vX = this.xGetEltL(vRelBase) + this.xGetEltW(vRelBase) - pX;
				break
			case "rightOfElement":
				vX = this.xGetEltL(vRelBase) + this.xGetEltW(vRelBase) + pX;
				if (!pForce && !this.xIsInWinH(vX)) vX = this.xGetEltL(vRelBase) - this.fCurrTtW - pX;
				break
			default :
				vX = this.xGetEltL(vRelBase) + pX;
			}
			switch(pVPos){
			case "center":
				vY = this.xGetEltT(vRelBase) + (this.xGetEltH(vRelBase) - this.fCurrTtH)/2 + pY;
				break
			case "bottomAlign":
				vY = this.xGetEltT(vRelBase) + this.xGetEltH(vRelBase) - this.fCurrTtH + pY;
				break
			case "aboveElement":
				vY = this.xGetEltT(vRelBase) - this.fCurrTtH + pY;
				if (!pForce && !this.xIsInWinV(vY)) vY = this.xGetEltT(vRelBase) + this.xGetEltH(vRelBase) - pY;
				break
			case "belowElement":
				vY = this.xGetEltT(vRelBase) + this.xGetEltH(vRelBase) + pY;
				if (!pForce && !this.xIsInWinV(vY)) vY = this.xGetEltT(vRelBase) - this.fCurrTtH - pY;
				break
			default :
				vY = this.xGetEltT(vRelBase) + pY;
			}
		}
		if (!pForce){
			if(vX > this.fXlim) vX = this.fXlim;
			var vScrX = this.xInt(window.pageXOffset || (this.fDb? this.fDb.scrollLeft : 0) || 0);
			if(vX < vScrX) vX = vScrX;
			if(vY > this.fYlim) vY = this.fYlim;
			var vScrY = this.xInt(window.pageYOffset || (this.fDb? this.fDb.scrollTop : 0) || 0);
			if(vY < vScrY) vY = vScrY;
		}
		this.xSetDivPos(vX, vY);
	},
	xIsInWinH: function(pX) {
		if(pX > this.fXlim) return(false);
		var vScr = this.xInt(window.pageXOffset || (this.fDb? this.fDb.scrollLeft : 0) || 0);
		if(pX < vScr) return(false);
		return(true);
	},
	xIsInWinV: function(pY) {
		if(pY > this.fYlim) return(false);
		var vScr = this.xInt(window.pageYOffset || (this.fDb? this.fDb.scrollTop : 0) || 0);
		if(pY < vScr) return(false);
		return(true);
	},
	xSetDivPos: function(pX, pY) {
		if (this.fCurrTt.fNode.fOpt.FIXTYPE == 'free') return;
		var vTtsh = this.fCurrTt.style || this.fCurrTt;
		vTtsh.left = (this.fCurrTtX = pX)+'px';
		vTtsh.top = (this.fCurrTtY = pY)+'px';
		if(this.fIfrm) {
			this.fIfrm.style.left = vTtsh.left;
			this.fIfrm.style.top = vTtsh.top;
		}
	},
	xOpDeHref: function(pEvt) {
		var vTag;
		if(pEvt) {
			vTag = pEvt.target;
			while(vTag) {
				if(vTag.hasAttribute("href")) {
					this.fTag = vTag
					this.fTag.t_href = this.fTag.getAttribute("href");
					this.fTag.removeAttribute("href");
					this.fTag.style.cursor = "hand";
					this.fTag.onmousedown = this.xOpReHref;
					this.fTag.stats = window.status;
					window.status = this.fTag.t_href;
					break;
				}
				vTag = vTag.parentElement;
			}
		}
	},
	xOpReHref: function() {
		if(this.fTag) {
			this.fTag.setAttribute("href", this.fTag.t_href);
			window.status = this.fTag.stats;
			this.fTag = null;
		}
	},
	xSetEltSizePos: function(pElt, pT, pL, pW, pH) {
		this.xSetEltL(pElt, pL);
		this.xSetEltT(pElt, pT);
		this.xSetEltW(pElt, pW);
		this.xSetEltH(pElt, pH);
	},
	xSetTtSize: function(pId, pOpt) {
		if (pOpt.FIXTYPE == 'free') return;
		var vCont = this.xGetElt(pId);
		if (vCont) {
			var vMaxW = pOpt.MAXWIDTH;
			var vMaxH = pOpt.MAXHEIGHT;
			var vTt = vCont.firstChild;
			while(vTt && vTt.nodeType != 1) vTt = vTt.nextSibling;
			var vTtScrol = this.xGetElt(pId+'Scrol');
			var vTtW = this.xGetEltW(vTt);
			if (vTtW > vMaxW) { //Fix max width if needed
				this.xSetEltW(vTt, vMaxW);
				vTtW = this.xGetEltW(vTt);
			}
			var vTtH = this.xGetEltH(vTt);
			if (vTtH > vMaxH) { //Fix max height & add scroll if needed
				vTtH = vMaxH;
				var vTtScrolH = vTtH; 
				var vTtTi = this.xGetElt(pId+'ti');
				if (vTtTi) vTtScrolH -= this.xGetEltH(vTtTi);
				if (typeof vTtScrol.style.overflowY != "undefined") vTtScrol.style.overflowY = 'auto';
				else vTtScrol.style.overflow = 'auto';
				this.xSetEltH(vTtScrol, vTtScrolH);
				vTtH = this.xGetEltH(vTt);
				pOpt.FORCESTICKY = true; //Set force sticky flag if scroll
			}
			var vContW = vTtW;
			var vContH = vTtH;
			var vSdwR = this.xGetElt(pId+'SdwR');
			if (vSdwR) { // Size shadow if it exists
				var vSdwB = this.xGetElt(pId+'SdwB');
				var vSdwW = this.xGetEltW(vSdwR);
				this.xSetEltSizePos(vSdwR,vSdwW,vTtW,vSdwW,vTtH-vSdwW);
				this.xSetEltSizePos(vSdwB,vTtH,vSdwW,vTtW,vSdwW);
				vContW += vSdwW;
				vContH += vSdwW;
			}
			this.xSetEltSizePos(vCont,-vContH,-vContW,vContW,vContH);
		}
	},
	xEltInContId: function(pElt, pId) {
		var vElt = pElt;
		var vFound = false;
		if (vElt) {
			vFound = vElt.id == pId;
			while (vElt.parentNode && !vFound) {
				vElt = vElt.parentNode
				vFound = vElt.id == pId;
			}
		}
		return(vFound);
	},
	xEltInContTtId: function(pElt, pTtId) {
		var vElt = pElt;
		var vFound = false;
		if (vElt) {
			vFound = vElt.ttId == pTtId;
			while (vElt.parentNode && !vFound) {
				vElt = vElt.parentNode
				vFound = vElt.ttId == pTtId;
			}
		}
		return(vFound);
	},
	xGetTargetElt: function(pEvt) {
		var vEvt = pEvt || window.event;
		var vTargetElt = null
		if(vEvt && vEvt.target) vTargetElt = vEvt.target;
		if(!vTargetElt && vEvt && vEvt.srcElement) vTargetElt = vEvt.srcElement;
		return(vTargetElt);
	},
	xGenId: function(pPrefix) {
		var vIndex = 0;
		while((this.xGetElt(pPrefix+vIndex) || this.xGetElt(pPrefix+vIndex+'co') || this.xGetElt(pPrefix+vIndex+'ti') || this.xGetElt(pPrefix+vIndex+'SdwR') || this.xGetElt(pPrefix+vIndex+'SdwB') || this.xGetElt(pPrefix+vIndex+'Scrol')) && vIndex < 10000) vIndex++;
		if (vIndex == 10000) {
			alert("Tooltip creation Error");
			return("");
		} else return (pPrefix + vIndex);
	},
	xInitOpts: function(pOpt) {
		var vOpt = (typeof pOpt != "undefined")? pOpt : {}; //Retreave  display Opts if any...
		vOpt.CLASS = (typeof vOpt.CLASS != "undefined")? vOpt.CLASS : "", 
		vOpt.ABOVE = (typeof vOpt.ABOVE != "undefined")? vOpt.ABOVE : this.cTtAbove, 
		vOpt.DELAY = (typeof vOpt.DELAY != "undefined")? vOpt.DELAY : this.cTtDelay,
		vOpt.FIX = (typeof vOpt.FIX != "undefined")? vOpt.FIX : "",
		vOpt.FIXID = (typeof vOpt.FIXID != "undefined")? vOpt.FIXID : "",
		vOpt.FIXTYPE = (typeof vOpt.FIXTYPE != "undefined")? vOpt.FIXTYPE : ((vOpt.FIXID == "")? this.cTtFixType : "id"),
		vOpt.FIXFORCE = (typeof vOpt.FIXFORCE != "undefined")? vOpt.FIXFORCE : false,
		vOpt.LEFT = (typeof vOpt.LEFT != "undefined")? vOpt.LEFT : this.cTtLeft,
		vOpt.MAXWIDTH = (typeof vOpt.MAXWIDTH != "undefined")? vOpt.MAXWIDTH : this.cTtMaxWidth;
		vOpt.MAXHEIGHT = (typeof vOpt.MAXHEIGHT != "undefined")? vOpt.MAXHEIGHT : this.cTtMaxHeight; 
		vOpt.VPOS = (typeof vOpt.VPOS != "undefined")? vOpt.VPOS : this.cTtVPos,
		vOpt.HPOS = (typeof vOpt.HPOS != "undefined")? vOpt.HPOS : this.cTtHPos,
		vOpt.OFFSETX = (typeof vOpt.OFFSETX != "undefined")? vOpt.OFFSETX : this.cTtOffsetX,
		vOpt.OFFSETY = (typeof vOpt.OFFSETY != "undefined")? vOpt.OFFSETY : this.cTtOffsetY,
		vOpt.STATIC = (typeof vOpt.STATIC != "undefined")? vOpt.STATIC : this.cTtStatic,
		vOpt.STICKY = (typeof vOpt.STICKY != "undefined")? vOpt.STICKY : this.cTtSticky,
		vOpt.TEMP = (typeof vOpt.TEMP != "undefined")? vOpt.TEMP : this.cTtTemp;
		vOpt.CLSBTN = (typeof vOpt.CLSBTN != "undefined")? vOpt.CLSBTN : false;
		vOpt.PARENT = (typeof vOpt.PARENT != "undefined")? vOpt.PARENT : document.body;
		if (vOpt.CLSBTN) {
			vOpt.CLSBTNCAP = (typeof vOpt.CLSBTNCAP != "undefined")? vOpt.CLSBTNCAP : "&nbsp;";
			vOpt.CLSBTNTI = (typeof vOpt.CLSBTNTI != "undefined")? vOpt.CLSBTNTI : "";
		}
		return vOpt;
	},
	xAddClass : function(pNode, pClass) {
		var vNewClassStr = pNode.className;
		for (var i = 1, n = arguments.length; i < n; i++) vNewClassStr += ' '+arguments[i];
		pNode.className = vNewClassStr;
	},
	xDelClass : function(pNode, pClass) {
		if (pClass != '') {
			var vCurrentClasses = pNode.className.split(' ');
			var vNewClasses = new Array();
			for (var i = 0, n = vCurrentClasses.length; i < n; i++) {
				var vClassFound = false;
				for (var j = 1, m = arguments.length; j < m; j++) {
					if (vCurrentClasses[i] == arguments[j]) vClassFound = true;
				}
				if (!vClassFound) vNewClasses.push(vCurrentClasses[i]);
			}
			pNode.className = vNewClasses.join(' ');
		}
	},
	xHasAttr : function(pTag, pAttrName) {
		return pTag.hasAttribute ? pTag.hasAttribute(pAttrName) : typeof pTag[pAttrName] !== "undefined";
	},



	moveTooltip: function(pEvt) {
		if(!scTooltipMgr.fCurrTt) return;
		if(scTooltipMgr.fNavn6 || scTooltipMgr.fNavw3c) {
			if(scTooltipMgr.fWait) return;
			scTooltipMgr.fWait = true;
			setTimeout('scTooltipMgr.fWait = false;', 5);
		}
		var vEvt = pEvt || window.event;
		scTooltipMgr.xSetDivPos(scTooltipMgr.xEvX(vEvt), scTooltipMgr.xEvY(vEvt));
		if(scTooltipMgr.fCurrTt.fNode.onmouseover &&  !scTooltipMgr.xEltInContTtId(scTooltipMgr.xGetTargetElt(vEvt), scTooltipMgr.fCurrTtId)) scTooltipMgr.hideTooltip();
	},
	releaseMov: function() {
		if(document.onmousemove == this.moveTooltip) {
			if(!this.fMmovEvt && document.releaseEvents && !this.fNavn6) document.releaseEvents(Event.MOUSEMOVE);
			document.onmousemove = this.fMmovEvt;
		}
	},
	releaseSize: function() {
		if(window.onresize == this.reposTooltip) {
			if(!this.fWsizEvt && document.releaseEvents && !this.fNavn6) document.releaseEvents(Event.RESIZE);
			window.onresize = this.fWsizEvt;
		}
	},
	showDiv: function(pFlag) {
		this.xShowIfrm(pFlag);
		this.fCurrTt.style.visibility = pFlag? 'visible' : 'hidden';
		this.fAct = pFlag;
	},
	hideTooltip: function(pPara) {
		var vForce = (typeof pPara == "boolean")? pPara : false;
		if(scTooltipMgr.fCurrTt) {
			if(window.tt_rdl) window.clearTimeout(tt_rdl);
			if(!scTooltipMgr.fSticky || !scTooltipMgr.fAct || (scTooltipMgr.fSticky && !scTooltipMgr.xEltInContId(scTooltipMgr.xGetTargetElt(pPara),scTooltipMgr.fCurrTtId)) || vForce) {
				if(window.tt_rtm) window.clearTimeout(tt_rtm);
				scTooltipMgr.showDiv(false);
				scTooltipMgr.xSetDivPos(-scTooltipMgr.fCurrTtW, -scTooltipMgr.fCurrTtH);
				if (scTooltipMgr.fCurrTt.fNode.ttTitle) scTooltipMgr.fCurrTt.fNode.title = scTooltipMgr.fCurrTt.fNode.ttTitle;
				if (scTooltipMgr.fCurrTt.fNode.ttAlt) scTooltipMgr.fCurrTt.fNode.alt = scTooltipMgr.fCurrTt.fNode.ttAlt;
				for(var i=0; i<scTooltipMgr.fCurrTt.fNode.fTtShownCls.length; i++) scTooltipMgr.xDelClass(scTooltipMgr.fCurrTt.fNode, scTooltipMgr.fCurrTt.fNode.fTtShownCls[i]);
				for(var i=0; i<scTooltipMgr.fHideListeners.length; i++) try{scTooltipMgr.fHideListeners[i](scTooltipMgr.fCurrTt.fNode);}catch(e){};
				scTooltipMgr.fCurrTt.fNode.focus();
				scTooltipMgr.fCurrTt = null;
				if(typeof scTooltipMgr.fMupEvt != "undefined") document.onmouseup = scTooltipMgr.fMupEvt;
			}
			scTooltipMgr.releaseMov();
			scTooltipMgr.releaseSize();
		}
		return false;
	},
	reposTooltip: function() {
		scTooltipMgr.xSetDivPosFix(scTooltipMgr.fFixType, scTooltipMgr.fFix[0], scTooltipMgr.fFix[1], scTooltipMgr.fFixId, scTooltipMgr.fTtHPos, scTooltipMgr.fTtVPos, scTooltipMgr.fFixForce);
	},



	registerTooltip: function(pIdAnc, pIdTt, pTrig, pCls, pClsRoot, pOpt) {
		try{
			this.xInitMgr(); // Initialize tooltipMgr if needed
			var vAncNode = this.xGetElt(pIdAnc);
			var vTtSrc = this.xGetElt(pIdTt);
			vTtSrc.ttIds = [];
			vAncNode.fOpt = scTooltipMgr.xInitOpts(pOpt);
			var vTi = vTtSrc.firstChild;
			while(vTi && vTi.nodeType != 1) vTi = vTi.nextSibling;
			var vCo = vTi.nextSibling;
			while(vCo && vCo.nodeType != 1) vCo = vCo.nextSibling;
			vTtSrc.ttIds.push(this.xMakeTt(vAncNode, vCo, vTi, pCls, pClsRoot)); //build the tooltip HTML
			vAncNode[pTrig] = function (pEvt) {return scTooltipMgr.showTooltip(this,pEvt);}
			if (pTrig != "onclick" && vAncNode.href && vAncNode.href.split("#")[0] == window.location.href) vAncNode.onclick = vAncNode[pTrig] // Force onclick for accessibility
			

			if (!pOpt.NOREF){
				var vRef = vAncNode.nextSibling;
				while(vRef && vRef.nodeType != 1) vRef = vRef.nextSibling;
				if(vRef && this.fHideBasket) vRef.style.display = "none";
				else if(vRef) vRef.className = vRef.className + " " + this.fRegCls;
			}

			if (this.fHideBasket) vTtSrc.style.display = "none";
			else vTtSrc.className = vTtSrc.className + " " + this.fRegCls;

			var vBskt = vTtSrc.parentNode;
			var vBsktElts = vBskt.childNodes;
			if (this.xHasAttr(vBskt, "data-titled-basket")) vBskt = vBskt.parentNode;
			var vEmpty = true;
			for(var i = 0; i < vBsktElts.length; i++) if (vBsktElts[i].nodeType==1 && !vBsktElts[i].ttIds) {vEmpty = false; break;} 
			if (this.fHideBasket && vEmpty) vBskt.style.display = "none";
			else vBskt.className = vBskt.className + " " + this.fRegCls;
			
		} catch(e){
			scCoLib.log("scTooltipMgr.registerTooltip - error : "+e);
		}
	},
	addShowListener: function(pFunc) {this.fShowListeners.push(pFunc)},
	addHideListener: function(pFunc) {this.fHideListeners.push(pFunc)},
	showTooltip: function(pNode, pEvt, pCo, pTi, pCls, pClsRoot, pOpt) {
		if(document.scDragMgrDragGroup) return; // no tooltips while draging
		this.xInitMgr(); // Initialize tooltipMgr  if needed
		var vTtId = pNode.ttId || null; //Retreave the tooltip ID if it has already been created
		if (this.fCurrTt != null && this.fCurrTt == this.xGetElt(vTtId)) return; // If the tooltip is already shown, exit (safari bug & moz call of multiple onmouseover)
		

		if (vTtId == null) {
			pNode.fOpt = this.xInitOpts(pOpt);
			vTtId = this.xMakeTt(pNode, pCo, pTi, pCls, pClsRoot); //build the tooltip HTML
			if (!pNode.onclick && pNode.href && pNode.href.split("#")[0] == window.location.href) pNode.onclick = function() {return false;};
		}
		this.xInitIfrm(); // Init ie iframe if needed
		pNode.fOpt.STICKY = pNode.fOpt.STICKY || (pNode.ttFSticky || false);


		this.xShow(pEvt, vTtId, pNode.fOpt);


		for(var i=0; i<this.fShowListeners.length; i++) try{this.fShowListeners[i](pNode);}catch(e){};
		return false;
	}
};