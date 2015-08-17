


var scPaLib = {};


scPaLib.findNode= function(pPath, pFrom) {
	return this.xFindNode(pPath.scPath ? pPath : this.compilePath(pPath), pFrom || document, 0);
}



scPaLib.findNodes = function(pPath, pFrom) {
	var vResult = [];
	this.xFindNodes(pPath.scPath ? pPath : this.compilePath(pPath), pFrom || document, 0, vResult);
	return vResult;
}


scPaLib.checkNode = function(pFilter, pNode){
	var vFilter = pFilter.scFilter ? pFilter : this.compileFilter(pFilter);
	if (vFilter.scFilter == 1) return this.xCheckNode(vFilter,pNode);
	else {
		var vMatch = false;
		for(var i = 0; i < vFilter.length; i++) {
			if (vMatch = this.xCheckNode(vFilter[i],pNode)) break; 
		}	
		return vMatch;
	}
}


scPaLib.containWord = function(pString, pWord, pMaxCharSep){
	pMaxCharSep = pMaxCharSep || 48;
	var vIdx = pString.indexOf(pWord);
	while(vIdx >= 0) {
		var vEnd = vIdx+pWord.length;
		if( (vIdx == 0 || pString.charCodeAt(vIdx-1) < pMaxCharSep) && (vEnd==pString.length || pString.charCodeAt(vEnd) < pMaxCharSep) ) return true;
		vIdx = pString.indexOf(pWord, vEnd);
	}
	return false;
}


scPaLib.compilePath = function(pPath){
	var vPath = pPath.split("/");
	for(var i=0; i<vPath.length; i++) {
		var vPart = vPath[i] = new String(vPath[i]);
		var vAxisCd = vPart.substring(0, 3);
		vPart.axis = this.xAxis[vAxisCd];
		if(vAxisCd == "ide") {
			if(vPart.length>4) vPart.filterId = vPart.substring(4);
		} else {
			if(vPart.length>4) vPart.filter = this.compileFilter(vPart.substring(4));
		}
	}
	vPath.scPath = true;
	return vPath;
}


scPaLib.compileFilter = function(pFilter){
	var vFilter = pFilter.split("|");
	if(vFilter.length>1){
		for(var i = 0; i < vFilter.length; i++) {
			vFilter[i] = vFilter[i].split(".");

			if(vFilter[i].length>0 && vFilter[i][0].length>0) vFilter[i][0] = new RegExp("^(.*:)?"+vFilter[i][0]+"$", "i");
			vFilter.scFilter = 2;
		}
	}else{
		vFilter = pFilter.split(".");

		if(vFilter.length>0 && vFilter[0].length>0) vFilter[0] = new RegExp("^(.*:)?"+vFilter[0]+"$", "i");
		vFilter.scFilter = 1;
	}
	return vFilter;
}




scPaLib.xAxis= {
	can:{
		first:function(pFrom){return pFrom;},
		next:function(pFrom){return pFrom.parentNode;}
	},
	anc:{
		first:function(pFrom){return pFrom.parentNode;}
	},
	par:{
		next:function (){return null;}
	},
	chi:{
		first:function(pFrom){return pFrom.firstChild;},
		next:function(pFrom){return pFrom.nextSibling;}
	},
	chl:{
		first:function(pFrom){return pFrom.lastChild;}
	},
	nsi:{
	},
	psi:{
		first:function(pFrom){return pFrom.previousSibling;}
	},
	cde:{
		first:function(pFrom){return pFrom;},
		next:function(pFrom, pOri){
			if(pFrom.hasChildNodes()) return pFrom.firstChild;
			if(pFrom.nextSibling) return pFrom.nextSibling;
			while(pFrom != pOri && (pFrom = pFrom.parentNode) != null && pFrom != pOri) {
				var vNext = pFrom.nextSibling;
				if(vNext) return vNext;
			}
			return null;
		}
	},
	des:{
	},
	bod:{
		first:function(pFrom){
			if(pFrom.ownerDocument) return pFrom.ownerDocument.body;
			else if (pFrom.document) return pFrom.document.body;
			else return document.body;
		}
	},
	ide:{
 		first:function(pFrom, pPart){
			if(pFrom.ownerDocument) return pFrom.ownerDocument.getElementById(pPart.filterId);
			else if (pFrom.document) return pFrom.document.getElementById(pPart.filterId);
			else if (pFrom.getElementById) return pFrom.getElementById(pPart.filterId);
			else document.getElementById(pPart.filterId);
		}
	},
	init: function(){
		this.anc.next = this.anc.first;
		this.des.first = this.chi.first;
		this.des.next = this.cde.next;
		this.par.first = this.anc.first;
		this.nsi.first = this.chi.next;
		this.nsi.next = this.chi.next;
		this.psi.next = this.psi.first;
		this.chl.next = this.psi.next;
		this.bod.next = this.par.next;
		this.ide.next = this.par.next;
	}
}
scPaLib.xAxis.init();

scPaLib.xFindNode= function(pPath, pFrom, pOffset) {
	var vPart = pPath[pOffset++];
	var vCurr = vPart.axis.first(pFrom, vPart);
	while(vCurr) {
		if( ! vPart.filter || this.checkNode(vPart.filter, vCurr)) {
			if(pOffset < pPath.length) {
				var vRes = this.xFindNode(pPath, vCurr, pOffset);
				if(vRes) return vRes;
			} else {
				return vCurr;
			} 
		}
		vCurr = vPart.axis.next(vCurr, pFrom);
	}
	return null;
}

scPaLib.xFindNodes = function(pPath, pFrom, pOffset, pArrayRes) {
	var vPart = pPath[pOffset++];
	var vCurr = vPart.axis.first(pFrom, vPart);
	while(vCurr) {
		if( ! vPart.filter || this.checkNode(vPart.filter, vCurr)) {
			if(pOffset < pPath.length) {
				this.xFindNodes(pPath, vCurr, pOffset, pArrayRes);
			} else {
				pArrayRes[pArrayRes.length] = vCurr;
			} 
		}
		vCurr = vPart.axis.next(vCurr, pFrom);
	}
}

scPaLib.xCheckNode = function(pFilter, pNode){
	var vLen = pFilter.length;
	if(vLen==0) return true;
	var vStep0 = pFilter[0];
	if(vStep0.test && ! vStep0.test(pNode.nodeName)) return false;
	if(vLen>1) {
		var vClasses = pNode.nodeType==1 ? pNode.className : null;
		if(typeof vClasses != "string") return false;
		for(var i = 1; i < vLen; i++) if( ! this.containWord(vClasses, pFilter[i], 33)) return false;
	}
	return true;
}