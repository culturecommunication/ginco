/**
 * LICENCE[[
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1/CeCILL 2.O
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
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
 * Portions created by the Initial Developer are Copyright (C) 2010-2014
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either of the GNU General Public License Version 2 or later (the "GPL"),
 * or the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * or the CeCILL Licence Version 2.0 (http://www.cecill.info/licences.en.html),
 * in which case the provisions of the GPL, the LGPL or the CeCILL are
 * applicable instead of those above. If you wish to allow use of your version
 * of this file only under the terms of either the GPL, the LGPL or the CeCILL,
 * and not to allow others to use your version of this file under the terms of
 * the MPL, indicate your decision by deleting the provisions above and replace
 * them with the notice and other provisions required by the GPL, the LGPL or
 * the CeCILL. If you do not delete the provisions above, a recipient may use
 * your version of this file under the terms of any one of the MPL, the GPL,
 * the LGPL or the CeCILL.
 * ]]LICENCE
 */

/* ====================== SCENARI Search service ===============================
   Client-side service that manages SCENARI-generated search indexes.
   This service loads, parses and interrogates multiple search indexes. The most
   common usages of this service are calling find() that either returns an array
   of result objects or a resultSet object and calling propose() that returns an
   array of possible words.
   The main purpose is to return a list of urls of pages that are positive
   maches for the given search string.
   Most functions require the index url to be used as their first argument.
   A result object describes a given page and is of the form {url,cat,idxCat}
   where 'url' if the url of the page, 'cat' is the page category count if only
   one index is concerned and 'idxCat' is an object that contains the category
   counts for all indexes concerned by the result.
   A resultSet is an object of the form {ctrl,list,or,neg} where 'list' is an
   array of result objects and 'ctrl' is an object that provides the offset of
   each page in the list. 'or' and 'neg' are optional flags that are used when
   consolidating resultSets and enable consolidateResults() to merge or negate 
   resultSets.
   A search string may contain several words and a limited search grammar. By
   default find() requires all words to be present on a given page for a match
   to be made.
   Here is an example of different search string and what they will return :
    * "foo bar" : pages that contain the strings foo and bar
    * "foo OR bar" : pages that contain the strings foo or bar (OU and | are 
                     also valid) 
    * "+foo -bar" : pages that contain the exact word foo (but not foot) and 
                    do not contain the string bar
    * "*bar" : pages that contain words starting with bar (barrel but not 
               embargo)

   Public functions : 
    * onLoad : SCENARI onLoad system
    * declareIndex : Declare an index file
    * registerListener : Declare a listener function that is called when an 
                         index is successfully loaded
    * query : Execute a query made up of several parts: successive and 
              consolidated find() calls
    * getLastQueryList : Return the last query for a given index
    * getLastQueryResults : Return the last query results for a given index
    * resetLastQuery : Reset the last query and results for a given index
    * find : Find a string in a given index
    * getLastSearch : Return the last searched string for a given index
    * getLastResults : Return the last search results for a given index
    * resetLastSearch : Reset the last searched string and results for a given
                        index
    * propose : Propose a list of possible words from a given index
    * buildTokens : Build an array of search tokens from a given string and a
                    given index
    * consolidateResults : Parse and consolidate an array of resultSets
    * normalizeString : Normalize a string according to a given index's prefs
    * getPages : Retreive an object that lists all the pages available in a
                 given index
    * getCategories : Retreive an array of the categories available in a given
                      index
    * hasIntersections : Returns true if any page is referenced more than once
    * isLoadable : Retruns false when an exception occured during loading
 */
scServices.scSearch = scOnLoads[scOnLoads.length] = {
	fIdxs : {},
	fListeners : [],
	fDebug : false,
	fIsLocal : window.location.protocol == "file:",

	/* == onLoad =================================================================
	   PUBLIC - SCENARI onload system. */
	onLoad: function(){
		this.fStore = new this.xLocalStore("scsearch");
		if (!String.prototype.trim) {
			String.prototype.trim = function () {return this.replace(/^\s+|\s+$/g, '');};
		}
	},
	/* == declareIndex ===========================================================
	   PUBLIC - Declare an index file.
	   pUrl : url to index file
	   pAsync : load asynchonusly
	   return : true if the index is ready */
	declareIndex: function(pUrl,pAsync){
		if (!pUrl) throw new Error("scServices.scSearch.declareIndex() must be called with a url.");
		if (!this.fIdxs[pUrl]) return this.xLoadIndex(pUrl, (typeof pAsync == "undefined" ? true : pAsync));
		else return true;
	},
	/* == registerListener =======================================================
	   PUBLIC - Declare a listener function that is called when an index is successfully loaded.
	   pFunc : function to call when an index is loaded the index id (url) is passed to the listener */
	registerListener: function(pFunc){
		this.fListeners.push(pFunc);
	},
	/* == query ==================================================================
	   PUBLIC - Execute a query made up of several parts.
	   pQryLst : array of query objects : {id,str,opt,or,neg}
	   pOpt : optionnal - object containing search options : see find()
	   return : resultSet object*/
	query: function(pQryLst, pOpt){
		if (typeof pQryLst == "string") pQryLst = this.xDeserialiseObjJs(pQryLst);
		if (typeof pQryLst != "array" && typeof pQryLst != "object") throw new Error("scServices.scSearch.query() must be called with a query array or object.");
		this.xLog("query");
		var vQryLst = pQryLst;
		if (typeof pQryLst.length == "undefined") {
			vQryLst = [];
			vQryLst.push(pQryLst)
		}
		var vResults = [];
		for (var i=0; i<vQryLst.length; i++) {
			var vQry = vQryLst[i];
			var vQryOpt = {singleToken:false,matchExact:false,matchStartOnly:false,catMask:null,returnResultSet:true};
			if (typeof vQry.opt != "undefined"){
				if (typeof vQry.opt.singleToken != "undefined") vQryOpt.singleToken = vQry.opt.singleToken;
				if (typeof vQry.opt.matchExact != "undefined") vQryOpt.matchExact = vQry.opt.matchExact;
				if (typeof vQry.opt.matchStartOnly != "undefined") vQryOpt.matchStartOnly = vQry.opt.matchStartOnly;
				if (typeof vQry.opt.catMask != "undefined") vQryOpt.catMask = vQry.opt.catMask;
				if (typeof vQry.opt.cat != "undefined") vQryOpt.cat = vQry.opt.cat;
			}
			var vResult = this.find(vQry.id, vQry.str, vQryOpt);
			if (vResult) {
				vResult.or = vQry.or;
				vResult.neg = vQry.neg;
				vResults.push(vResult);
			}
		}
		this.fQueryList = pQryLst;
		this.fStore.set("QueryList", this.xSerialiseObjJs(pQryLst));
		this.fQueryResults = this.consolidateResults(vResults, {returnResultSet:true});
		return this.fQueryResults;
	},
	/* == getLastQueryList =======================================================
	   PUBLIC - Return the last query list.
	   return : last searched array of query objects */
	getLastQueryList: function(){
	if (!this.fQueryList) this.fQueryList = this.xDeserialiseObjJs(this.fStore.get("QueryList"));
		return this.fQueryList;
	},
	/* == getLastQueryResults ====================================================
	   PUBLIC - Return search results for the last query list.
	   return : array of result objects {url,cat,idxCat} or resultSet */
	getLastQueryResults: function(){
		if (this.getLastQueryList() && !this.fQueryResults) this.query(this.fQueryList);
		return this.fQueryResults;
	},
	/* == resetLastQuery =========================================================
	   PUBLIC - Reset the last query.
	   return : true if a reset was needed */
	resetLastQuery: function(){
		if (typeof this.fQueryList == "undefined") return false;
		this.fQueryList = null;
		this.fQueryResults = null;
		this.fStore.set("QueryList", "");
		return true;
	},
	/* == find ===================================================================
	   PUBLIC - Find a string in a given index.
	   pId : index id 
	   pStr : string to search
	   pOpt : optionnal - object containing search options :
	           singleToken : boolean, treat the string as a single token (do not split)
	           matchExact : boolean, search for an exact token match
	           matchStartOnly : boolean, search for for entries beginning with each token
	           cat : comma seperated string of categories to search
	           returnResultSet : boolean, return internal resultSet object
	   return : 1) simple array of result objects {url,cat,idxCat}
	            2) internal resultSet object if pOpt.returnResultSet = true */
	find: function(pId, pStr, pOpt){
		if (!pId) throw new Error("scServices.scSearch.find() must be called with an index ID.");
		this.xLog("find");
		var vIdx = this.xGetIndex(pId);
		if (!vIdx) return null;
		var vOpt = {singleToken:false,matchExact:false,matchStartOnly:false,catMask:null,returnResultSet:false};
		if (typeof pOpt != "undefined"){
			if (typeof pOpt.singleToken != "undefined") vOpt.singleToken = pOpt.singleToken;
			if (typeof pOpt.matchExact != "undefined") vOpt.matchExact = pOpt.matchExact;
			if (typeof pOpt.matchStartOnly != "undefined") vOpt.matchStartOnly = pOpt.matchStartOnly;
			if (typeof pOpt.returnResultSet != "undefined") vOpt.returnResultSet = pOpt.returnResultSet;
			if (typeof pOpt.cat != "undefined") vOpt.catMask = this.xBuildCatMask(vIdx,pOpt.cat);
			else if (typeof pOpt.catMask != "undefined") vOpt.catMask = pOpt.catMask;
		}
		vIdx.fStr = pStr;
		var i=0;
		var vToken = {};
		var vWrdResult = {};
		var vRawResults = [];
		var vTokens = this.xBuildTokens(vIdx,pStr, vOpt.singleToken);
		for (i = 0; i < vTokens.length; i++){
			vToken = vTokens[i];
			vWrdResult = this.xFindWord(vIdx,vToken.wrd,{matchExact:vToken.exact||vOpt.matchExact,matchStartOnly:vToken.start||vOpt.matchStartOnly,catMask:vOpt.catMask});
			if (vWrdResult) {
				vWrdResult.or = vToken.or;
				vWrdResult.neg = vToken.neg;
				vRawResults.push(vWrdResult);
			}
		}
		vIdx.fResults = this.consolidateResults(vRawResults, {returnResultSet:vOpt.returnResultSet});
		return vIdx.fResults;
	},
	/* == getLastSearch ==========================================================
	   PUBLIC - Return the last searched string for a given index.
	   pId : index id 
	   return : last searched string */
	getLastSearch: function(pId){
		if (!pId) throw new Error("scServices.scSearch.getLastSearch() must be called with an index ID.");
		var vIdx = this.xGetIndex(pId);
		if (!vIdx) return null;
		return vIdx.fStr;
	},
	/* == getLastResults =========================================================
	   PUBLIC - Return the last search results for a given index.
	   pId : index id 
	   return : array of result objects {url,cat,idxCat} or resultSet */
	getLastResults: function(pId){
		if (!pId) throw new Error("scServices.scSearch.getLastResults() must be called with an index ID.");
		var vIdx = this.xGetIndex(pId);
		if (!vIdx) return null;
		return vIdx.fResults;
	},
	/* == resetLastSearch ========================================================
	   PUBLIC - Reset the last searched string and results for a given index.
	   pId : index id 
	   return : true if a reset was needed */
	resetLastSearch: function(pId){
		if (!pId) throw new Error("scServices.scSearch.resetLastSearch() must be called with an index ID.");
		var vIdx = this.xGetIndex(pId);
		if (!vIdx) return false;
		if (typeof vIdx.fStr == "undefined") return false;
		vIdx.fStr = null;
		vIdx.fResults = null;
		return true;
	},
	/* == propose ================================================================
	   PUBLIC - Propose a list of possible words from a given index.
	   pId : index id 
	   pWrd : word to look up
	   pOpt : optionnal - propose options :
	           async : boolean - load the index async (false).
	           matchStartOnly : boolean - mtche the start of words only (true)
	           maxNum : int - number of matches over which null is returned (20)
	           filter : resultSet - can be used to filter matches contained in the resultset
	   return : array of proposed strings */
	propose: function(pId, pWrd, pOpt){
		if (!pId) throw new Error("scServices.scSearch.propose() must be called with an index ID.");
		var vOpt = {async:false,matchStartOnly:true,maxNum:20,filter:null};
		if (typeof pOpt != "undefined"){
			if (typeof pOpt.async != "undefined") vOpt.async = pOpt.async;
			if (typeof pOpt.matchStartOnly != "undefined") vOpt.matchStartOnly = pOpt.matchStartOnly;
			if (typeof pOpt.maxNum != "undefined") vOpt.maxNum = pOpt.maxNum;
			if (typeof pOpt.filter != "undefined") vOpt.filter = pOpt.filter;
		}
		var vIdx = this.xGetIndex(pId, pOpt.async);
		if (!vIdx) return null;
		this.xLog("propose");
		var vWrd = this.xFilterWord(vIdx, pWrd);
		var vRet = [];
		if (!vWrd) return vRet;
		if (!vIdx.fSortedWords) this.xSortIndex(vIdx);

		var vRegFind = null;
		if (vOpt.matchStartOnly) vRegFind = new RegExp("\\b"+vWrd);
		else vRegFind = new RegExp(pWrd);

		var sCheckFilter = function(pWord, pFilter){
			if (!pFilter || !pFilter.ctrl || (pFilter.list && pFilter.list.length==0)) return true;
			for (var i = 0; i<pWord.urls.length; i++){
				if (typeof pFilter.ctrl[pWord.urls[i]] != "undefined") return true;
			}
			return false;
		}
		for (var i = 0; i<vIdx.fSortedWords.length	;i++){
			if(vRegFind.test(vIdx.fSortedWords[i].wrd)){
				if (sCheckFilter(vIdx.fSortedWords[i], vOpt.filter)) vRet.push(vIdx.fSortedWords[i]);
				if (vOpt.maxNum>0 && vRet.length==vOpt.maxNum) return null;
			}
		}
		return vRet;
	},
	/* == buildTokens ============================================================
	   PUBLIC - Build an array of search tokens from a given string.
	   pId : index id 
	   pStr : string to search
	   pSingleToken : optionnal - boolean, treat the string as a single token (do not split)
	   return : array of search tokens {wrd:string,neg:boolean,exact:boolean,or:boolean} */
	buildTokens: function(pId, pStr, pSingleToken){
		if (!pId) throw new Error("scServices.scSearch.buildTokens() must be called with an index ID.");
		var vIdx = this.xGetIndex(pId);
		if (!vIdx) return null;
		if (typeof pSingleToken == "undefined") pSingleToken = false;
		return this.xBuildTokens(vIdx, pStr, pSingleToken);
	},
	/* == consolidateResults =====================================================
	   PUBLIC - Parse and consolidate an array of resultSets, concat ORs, seperate all negated results, AND remaining results.
	   pRawResults : Array of result-sets to be consolidated.
	   pOpt : optionnal - consolidating options
	   return : array of consolidated result set. */
	consolidateResults: function(pRawResults,pOpt){
		// Consolidate individual word results (concat ORs and seperate all negated results)
		var vOpt = {returnResultSet:false};
		if (typeof pOpt != "undefined"){
			if (typeof pOpt.returnResultSet != "undefined") vOpt.returnResultSet = pOpt.returnResultSet;
		}
		var vWrdResult = {};
		var vNegResults = [];
		var vWrdResults = [];
		var i=0, j=0, k=0;
		for (i = 0; i < pRawResults.length; i++){
			vWrdResult = pRawResults[i];
			if (vWrdResult.neg) vNegResults.push(vWrdResult);
			else if (vWrdResults.length>0 && vWrdResults[vWrdResults.length-1].or) vWrdResults[vWrdResults.length-1] = this.xConcatResults(vWrdResults[vWrdResults.length-1], vWrdResult);
			else vWrdResults.push(vWrdResult);
		}
		// Calculate final result set
		var vPge;
		var vInAllWords = true;
		var vInNegWords = false;
		var vResults = {ctrl:{},list:[]};
		for (i = 0; i < vWrdResults.length; i++){
			vWrdResult = pRawResults[i];
			for (j = 0; j < vWrdResult.list.length; j++){
				vPge = vWrdResult.list[j];
				vInAllWords = true;
				vInNegWords = false;
				for (k = 0; k < vWrdResults.length; k++){
					if (typeof vWrdResults[k].ctrl[vPge.url] == "undefined") vInAllWords = false;
				}
				for (k = 0; k < vNegResults.length; k++){
					if (typeof vNegResults[k].ctrl[vPge.url] != "undefined") vInNegWords = true;
				}
				if (vInAllWords && !vInNegWords){
					if(typeof vResults.ctrl[vPge.url] != "undefined") {
						this.xMergePageCategories(vResults.list[vResults.ctrl[vPge.url]], vPge);
					} else {
						vResults.ctrl[vPge.url] = vResults.list.length;
						vResults.list.push(vPge);
					}
				}
			}
		}
		return vOpt.returnResultSet ? vResults : vResults.list;
	},
	/* == normalizeString ========================================================
	   PUBLIC - Normalize a string according to a given index's prefs.
	   pId : index id 
	   pStr : string to normalize
	   return : string normalized according to the index's prefs, ready for highlighting searches for example */
	normalizeString: function(pId, pStr){
		if (!pId) throw new Error("scServices.scSearch.normalizeString() must be called with an index ID.");
		var vIdx = this.xGetIndex(pId);
		if (!vIdx) return null;
		var vFilters = vIdx.fParams.filters;
		var vStr;
		for (var i = 0; i < vFilters.length; i++){
			var vFilter = vFilters[i];
			switch (vFilter.type){
			case "filterAccentedLatinLetters":
				vStr = this.xfilterAccentedLatinLetters(pStr, true);
				break;
			}
		}
		return vStr;
	},
	/* == getPages ===============================================================
	   PUBLIC - Retreive an object listing all the pages in a given index.
	   pId : index id 
	   return : page list object */
	getPages: function(pId){
		if (!pId) throw new Error("scServices.scSearch.getPages() must be called with an index ID.");
		var vIdx = this.xGetIndex(pId);
		if (!vIdx) return null;
		return vIdx.fPgeIdx;
	},
	/* == getCategories ==========================================================
	   PUBLIC - Retreive an array of the categories available in a given index.
	   pId : index id 
	   return : array of category names */
	getCategories: function(pId){
		if (!pId) throw new Error("scServices.scSearch.getCategories() must be called with an index ID.");
		var vIdx = this.xGetIndex(pId);
		if (!vIdx) return null;
		return vIdx.fCatIdx;
	},
	/* == isLoadable =============================================================
	   PUBLIC - Returns 'null' if index non existant, 'false' if an exception occured when loading the index and 'true' if the index was loadable.
	   pId : index id 
	   return : boolean */
	isLoadable: function(pId){
		if (!pId) throw new Error("scServices.scSearch.isLoadable() must be called with an index ID.");
		if (!this.fIdxs[pId]) this.xLoadIndex(pId,false);
		var vIdx = this.fIdxs[pId];
		if (!vIdx) return null;
		return !vIdx.fLoadException;
	},
	/* == hasIntersections =======================================================
	   PUBLIC - Determines if a given index has intersections, i.e. if no page is
	            referenced more than once then there are deemed to be no
	            intersections.
	   pId : index id 
	   return : true if any page is referenced more than once */
	hasIntersections: function(pId){
		if (!pId) throw new Error("scServices.scSearch.hasIntersections() must be called with an index ID.");
		var vIdx = this.xGetIndex(pId);
		if (!vIdx) return null;
		if (typeof vIdx.fHasIntersections == "undefined"){
			vIdx.fHasIntersections = false;
			var vCtrl = {};
			var vRawLines = vIdx.fRaw.split("\n");
			for (var i=0; i<vRawLines.length; i++){
				var vRawLine = vRawLines[i].split("\t");
				for (var j = 1; j < vRawLine.length; j++){
					if (vCtrl[vRawLine[j]]){
						vIdx.fHasIntersections = true;
						return vIdx.fHasIntersections;
					}
					vCtrl[vRawLine[j]] = true;
				}
			}
		}
		return vIdx.fHasIntersections;
	},
	/* == xBuildCatMask ===========================================================
	   PRIVATE - Build a category mask for a given index from a comma seperated string of categories
	   pIdx : index 
	   pStr : comma seperated string of categories
	   return : category mask */
	xBuildCatMask: function(pIdx, pCat){
		var vCats = pCat.split(",");
		var vCatMask = "";

		for (var i=0;i<pIdx.fCatIdx.length;i++){
			var vCatIdx = pIdx.fCatIdx[i];
			var vMatch = false;
			for (var k=0;k<vCats.length;k++){
				if (vCatIdx == vCats[k]) vMatch = true;
			}
			vCatMask += vMatch ? "1" : "0";
		}
		return vCatMask;
	},
	/* == xBuildTokens ===========================================================
	   PRIVATE - Build an array of search tokens from a given string (interpret grammar)
	   pIdx : index 
	   pStr : string to search
	   pSingleToken : boolean, treat the string as a single token (do not split)
	   return : array of search tokens */
	xBuildTokens: function(pIdx, pStr, pSingleToken){
		var vRegWords = new RegExp(pIdx.fParams.wordPattern,"gm");
		var vRegOr = new RegExp("^(OR|OU|\\|)$");
		var vCorrectPipe = new RegExp("([^ ])\\|([^ ])","g");
		var vStrs = pSingleToken ? [pStr] : pStr.replace(vCorrectPipe, "$1 | $2").split(" ");
		var i=0, j=0;
		var vStr;
		var vWrd;
		var vWords = [];
		var vToken;
		var vTokens = [];
		for (i = 0; i < vStrs.length; i++){
			vStr = vStrs[i];
			if (vStr.length>0 && !vRegOr.test(vStr)) {
				vWords = vStr.match(vRegWords);
				if (vWords){
					for (j = 0; j < vWords.length; j++){
						if (vWords[j].length>0) {
							vWrd = this.xFilterWord(pIdx, vWords[j]);
							if(vWrd) {
								vToken = {wrd:vWrd};
								vToken.neg = vStr.indexOf("-")==0;
								vToken.exact = vStr.indexOf("+")==0;
								vToken.start = vStr.indexOf("*")==0;
								vToken.or = (i < vStrs.length-2 && vRegOr.test(vStrs[i+1]));
								vTokens.push(vToken);
							}
						}
					}
				}
			}
		}
		return vTokens;
	},
	/* == xFindWord ==============================================================
	   PRIVATE - Find a single word in the given index 
	   pIdx : index 
	   pWrd : string to search
	   pOpt : optionnal - search options
	   return : array of result objects {url,cat,idxCat} */
	xFindWord: function(pIdx, pWrd, pOpt){
		var vMatchExact = pWrd.search(/\D/) < 0 || pOpt.matchExact;
		var vRegFind = null;
		if (vMatchExact) vRegFind = new RegExp("^"+pWrd+"\t[^\\n]*","gm");
		else if (pOpt.matchStartOnly) vRegFind = new RegExp("\\b"+pWrd+"[^\\t]*\t[^\\n]*","gm");
		else vRegFind = new RegExp("^[^\\t]*"+pWrd+"[^\\t]*\t[^\\n]*","gm");
		var vRetList = pIdx.fRaw.match(vRegFind);
		var vResult = {ctrl:{},list:[]};
		var vDiscard = false;
		if (vRetList) {
			for (var i = 0; i < vRetList.length; i++){
				var vRetLine = vRetList[i].trim().split("\t");
				for (var j = 1; j < vRetLine.length; j++){
					var vRetIdx = vRetLine[j];
					var vPgeIdx = null;
					var vPgeCat = "";
					if (vRetIdx.indexOf(".")>0) {
						vPgeIdx = vRetIdx.split(".")[0];
						vPgeCat = vRetIdx.split(".")[1];
					} else {
						vPgeIdx = vRetIdx;
					}
					if (vPgeCat.length>0 && pOpt.catMask && vPgeCat.length == pOpt.catMask.length){
						vDiscard = true;
						for (var k=0; k<vPgeCat.length; k++){
							if (vPgeCat.charCodeAt(k)!=48 && pOpt.catMask.charCodeAt(k)!=48) {
								vDiscard=false;
								break;
							}
						}
					}
					if (!vDiscard){
						var vUrl = pIdx.fPgeIdx[vPgeIdx];
						var vPge = {url:vUrl,cat:vPgeCat,idxCat:{}};
						vPge.idxCat[pIdx.id] = vPgeCat;
						if (typeof vResult.ctrl[vUrl] != "undefined"){
							this.xMergePageCategories(vResult.list[vResult.ctrl[vUrl]], vPge, true);
						} else {
							vResult.list.push(vPge)
							vResult.ctrl[vUrl] = vResult.list.length-1;
						}
					}
				}
			}
		}
		return vResult;
	},
	/* == xFilterWord ============================================================
	   PRIVATE - Filter a single word based on an index's filters
	   pIdx : index 
	   pWrd : string to filter
	   return : filtered word */
	xFilterWord: function(pIdx, pWrd){
		var vFilters = pIdx.fParams.filters;
		var vWrd = pWrd;
		for (var i = 0; i < vFilters.length; i++){
			var vFilter = vFilters[i];
			switch (vFilter.type){
			case "filterAccentedLatinLetters":
				vWrd = this.xfilterAccentedLatinLetters(vWrd);
				break;
			case "lowerCase":
				vWrd = vWrd.toLowerCase();
				break;
			case "ignoreWords":
				if (vFilter.words[vWrd]) return null;
				break;
			case "excludeShortWords":
				if (vFilter.forWords){
					var vRegFilter = new RegExp(vFilter.forWords,"");
					if (vRegFilter.test(vWrd) && vWrd.length < vFilter.min) return null;
				} else if (vWrd.length < vFilter.min) return null;
				break;
			case "cutLongWords":
				if (vFilter.forWords){
					var vRegFilter = new RegExp(vFilter.forWords,"");
					if (vRegFilter.test(vWrd)) vWrd = vWrd.substring(0,vFilter.max);
				} else vWrd = vWrd.substring(0,vFilter.max);
				break;
			}
		}
		return vWrd;
	},
	/* == xGetIndex ==============================================================
	   PRIVATE - Retreive a given index object.
	   pId : index ID
	   pAsync : optionnal - load asynchonusly default false
	   return : index object */
	xGetIndex : function(pId,pAsync){
		if (typeof pAsync == "undefined") pAsync = false;
		if (!this.fIdxs[pId]) this.xLoadIndex(pId,pAsync);
		var vIdx = this.fIdxs[pId];
		if (typeof vIdx != "undefined" && !vIdx.fLoaded) return null;
		else return vIdx;
	},
	/* == xLoadIndex =============================================================
	   PRIVATE - Load an index file : fetch, read and interprate the file
	   pUrl : url to index file
	   pAsync : load asynchonusly
	   return : true if the index is ready */
	xLoadIndex: function(pUrl,pAsync){
		this.xLog("xLoadIndex: url="+pUrl+" Async="+pAsync);
		var vIdx = this.fIdxs[pUrl] = {id:pUrl,fLoaded:false,fLoadException:false};
		var vReq = null;
		if ("XMLHttpRequest" in window && (!this.fIsLocal || !("ActiveXObject" in window))) vReq = new XMLHttpRequest();
		else if ("ActiveXObject" in window) vReq = new ActiveXObject("Microsoft.XMLHTTP");

		function iSetupIndex(pIdx,pRaw) {
			scServices.scSearch.xLog("iSetupIndex");
			try {
				var vRegIdx = new RegExp("^([^\\n]*)\\n([^\\n]*)\\n([^\\n]*)");
				var vStrIdx = vRegIdx.exec(pRaw);
				pIdx.fRaw = pRaw.substring(vStrIdx[1].length + vStrIdx[2].length + vStrIdx[3].length + 3);
				pIdx.fPgeIdx = scServices.scSearch.xDeserialiseObjJs(vStrIdx[1].trim());
				pIdx.fCatIdx = scServices.scSearch.xDeserialiseObjJs(vStrIdx[2].trim());
				pIdx.fParams = scServices.scSearch.xDeserialiseObjJs(vStrIdx[3].trim());
				pIdx.fLoaded = true;
				for (var i=0; i< scServices.scSearch.fListeners.length; i++) try{scServices.scSearch.fListeners[i](pIdx.id)}catch(e){scServices.scSearch.xLog("iSetupIndex : listener error : "+e)};
			} catch(e){
				scServices.scSearch.xLog("ERROR : unable to process search index "+pIdx.id);
				pIdx.fLoadException = true;
			}
			return true;
		};
		if (pAsync) {
			vReq.onreadystatechange = function () {
				if (vReq.readyState != 4) return;
				if (vReq.status != 0 && vReq.status != 200 && vReq.status != 304) {
					alert("ERROR : unable de retreive search index "+pUrl+": " + vReq.status);
					return;
				}
				scServices.scSearch.xLog("xAsyncLoadIndex");
				if (!vIdx.fLoaded) iSetupIndex(vIdx,vReq.responseText);
			}
		}
		vReq.open("GET",pUrl,pAsync);
		try {
			vReq.send();
		} catch(e){
			this.xLog("ERROR : unable to load search index "+vIdx.id);
			vIdx.fLoadException = true;
		}
		if (!pAsync) return iSetupIndex(vIdx,vReq.responseText);
		else return false;
	},

	/* == xSortIndex =============================================================
	   PRIVATE - Sort an index file : add a sorted array of known words
	   pIdx : index */
	xSortIndex : function(pIdx){
		try {
			var vRegFind = new RegExp("^[^\\n]*","gm");
			var vLines = pIdx.fRaw.match(vRegFind);
			pIdx.fSortedWords = [];
			for (var i = 0; i<vLines.length; i++){
				var vLine = vLines[i].split("\t");
				var vUrls = [];
				for (var j = 1; j < vLine.length; j++){
					var vPgeIdx = vLine[j];
					if (vPgeIdx.indexOf(".")>0) {
						vPgeIdx = vPgeIdx.split(".")[0];
					}
					vUrls.push(pIdx.fPgeIdx[vPgeIdx]);
				}
				pIdx.fSortedWords.push({wrd:vLine[0],urls:vUrls,num:vUrls.length});
			}
			pIdx.fSortedWords.sort(function (p1, p2){
				if(scCoLib.isIE) return p1.wrd.localeCompare(p2.wrd||"");
				try{
					return p1.wrd > p2.wrd||"" ? 1 : p1.wrd == p2.wrd ? 0 : -1;
				}catch(e){
					return p1.wrd.localeCompare(p2.wrd||"");
				}
			}
		);
		} catch(e){alert("ERROR : unable de process search index "+pIdx);}
	},
	/* == xConcatResults =========================================================
	   PRIVATE - Concatinate two result objects
	   pRes1 : input result object
	   pRes2 : input result object
	   return : Concatinated result object */
	xConcatResults : function(pRes1, pRes2){
		if (typeof pRes1 == "undefined") return null;
		if (typeof pRes2 == "undefined" || !pRes2.list || pRes2.list.length==0) return pRes1;
		var vResult = pRes1;
		for (var i = 0; i<pRes2.list.length; i++){
			var vPage2 = pRes2.list[i];
			if (typeof vResult.ctrl[vPage2.url] != "undefined"){
				this.xMergePageCategories(vResult.list[vResult.ctrl[vPage2.url]], vPage2);
			} else {
				vResult.list.push(vPage2)
				vResult.ctrl[vPage2.url] = vResult.list.length-1;
			}
		}
		vResult.or = (pRes2.or ? true : false);
		return vResult;
	},
	/* == xMergePageCategories ===================================================
	   PRIVATE - Merge two page category counts and update de first page object
	   pPage1 : input page object
	   pPage2 : input page object
	   pAdd : optionnal - if "true" categories are added not averaged
	   return : Page object */
	xMergePageCategories : function(pPage1, pPage2, pAdd){
		if (typeof pAdd == "undefined") pAdd = false;
		for (var vIdx in pPage2.idxCat){
			var vIdxCat2 = pPage2.idxCat[vIdx];
			if (typeof pPage1.idxCat[vIdx] == "undefined"){
				pPage1.idxCat[vIdx] = pPage2.idxCat[vIdx];
				pPage1.cat = null;
			} else {
				var vIdxCat1 = pPage1.idxCat[vIdx];
				var vCat = "";
				for (var i=0; i<vIdxCat2.length; i++){
					vCat += pAdd ? Math.min(9,scCoLib.toInt(vIdxCat1.charAt(i))+scCoLib.toInt(vIdxCat2.charAt(i))) : Math.round((scCoLib.toInt(vIdxCat1.charAt(i))+scCoLib.toInt(vIdxCat2.charAt(i)))/2);
				}
				pPage1.idxCat[vIdx] = vCat;
				if (typeof pPage1.cat == "string") pPage1.cat = vCat;
			}
		}
		return pPage1;
	},
	/* == xfilterAccentedLatinLetters ============================================
	   PRIVATE - must be synchonized with com.scenari.s.fw.utils.HCharSeqUtil.stringWithoutAccent().
	   pStr : input string
	   pKeepUnknown : optionnal - if true keep all unknown characters (used in highlighting the case of special punctuation)
	   return : filtered string */
	xfilterAccentedLatinLetters : function(pStr, pKeepUnknown){
		if (typeof pKeepUnknown == "undefined") pKeepUnknown = false;
		var vLen = pStr.length;
		var i = 0;
		for (; i < vLen; i++) {
			if (pStr.charCodeAt(i) >= 128) break;
		}
		if (i < vLen) {
			var vBuf = [];
			for (var j = 0; j < i; j++)
				vBuf.push(pStr.charAt(j));
			for (var j = i; j < vLen; j++) {
				var vCh = pStr.charAt(j);
				if (vCh.charCodeAt(0) < 128) {
					vBuf.push(vCh);
					continue;
				}
				switch (vCh) {
				case 'à':
				case 'á':
				case 'â':
				case 'ä':
				case 'ã':
				case 'å':
				case 'ª':
				case '\u0101': // a macron
				case '\u0103': // a breve
				case '\u0105': // a tail
					vBuf.push('a');
					break;
				case 'æ':
					vBuf.push('a');
					vBuf.push('e');
					break;
				case 'ç':
				case '\u010D': // c caron
				case '\u0107': // c acute
					vBuf.push('c');
					break;
				case '\u010F': // d caron
				case '\u0111': // d stroke
					vBuf.push('d');
					break;
				case 'é':
				case 'è':
				case 'ê':
				case 'ë':
				case '\u0113': // e macron
				case '\u0119': // e tail
					vBuf.push('e');
					break;
				case '\u0123': // g cedilla
				case '\u011F': // g breve
					vBuf.push('g');
					break;
				case 'î':
				case 'ï':
				case '\u0129': // i macron
					vBuf.push('i');
					break;
				case '\u013E': // l caron
				case '\u0142': // l stroke
					vBuf.push('l');
					break;
				case 'ñ':
				case '\u0148': // n caron
				case '\u0144': // n acute
					vBuf.push('n');
					break;
				case 'ò':
				case 'ó':
				case 'ô':
				case 'ö':
				case 'õ':
				case 'ø':
					vBuf.push('o');
					break;
				case '\u0153': // oe
					vBuf.push('o');
					vBuf.push('e');
					break;
				case '\u0155': // r acute
					vBuf.push('r');
					break;
				case '\u0161': // s caron
				case '\u0219': // s comma
				case '\u015B': // s acute
				case '\u015F': // s cedilla
					vBuf.push('s');
					break;
				case 'ß':
					vBuf.push('s');
					vBuf.push('s');
					break;
				case '\u021B': // t comma
				case '\u0165': // t caron
					vBuf.push('t');
					break;
				case 'û':
				case 'ù':
				case 'ü':
				case '\u0171': // u double acute
					vBuf.push('u');
					break;
				case 'ý':
				case 'ÿ':
					vBuf.push('y');
					break;
				case '\u017E': // z caron
				case '\u017A': // z acute
				case '\u017C': // z dot above
					vBuf.push('z');
					break;
				case 'À':
				case 'Á':
				case 'Â':
				case 'Ã':
				case 'Ä':
				case 'Å':
				case '\u0100': // A macron
				case '\u0102': // A breve
				case '\u0104': // A tail
					vBuf.push('A');
					break;
				case 'Æ':
					vBuf.push('A');
					vBuf.push('E');
					break;
				case 'Ç':
				case '\u010C': // C caron
				case '\u0106': // C acute
					vBuf.push('C');
					break;
				case '\u010E': // D caron
				case '\u0110': // D stroke
					vBuf.push('D');
					break;
				case 'É':
				case 'È':
				case 'Ê':
				case 'Ë':
				case '\u0112': // E macron
				case '\u0118': // E tail
					vBuf.push('E');
					break;
				case '\u0122': // G cedilla
				case '\u011E': // G breve
					vBuf.push('G');
					break;
				case 'Ì':
				case 'Í':
				case 'Î':
				case 'Ï':
				case '\u012A': // I macron
					vBuf.push('I');
					break;
				case '\u013D': // L caron
				case '\u0141': // L stroke
					vBuf.push('L');
					break;
				case 'Ñ':
				case '\u0147': // N caron
				case '\u0143': // N acute
					vBuf.push('N');
					break;
				case 'Ò':
				case 'Ó':
				case 'Ô':
				case 'Õ':
				case 'Ö':
				case 'Ø':
					vBuf.push('O');
					break;
				case '\u0152': // OE
					vBuf.push('O');
					vBuf.push('E');
					break;
				case '\u0154': // R acute
					vBuf.push('R');
					break;
				case '\u0160': // S caron
				case '\u0218': // S comma
				case '\u015A': // S acute
				case '\u015E': // S cedilla
					vBuf.push('S');
					break;
				case '\u021A': // T comma
				case '\u0164': // T caron
					vBuf.push('T');
					break;
				case 'Ù':
				case 'Ú':
				case 'Û':
				case 'Ü':
				case '\u0170': // U double acute
					vBuf.push('U');
					break;
				case 'Ý':
					vBuf.push('Y');
					break;
				case '\u017D': // Z caron
				case '\u0179': // Z acute
				case '\u017B': // z dot above
					vBuf.push('Z');
					break;
				case '¹':
					vBuf.push('1');
				case '²':
					vBuf.push('2');
				case '³':
					vBuf.push('3');
					break;
				case '\u20A4': // Lira sign
					vBuf.push('L');
					break;
				case '\u20AC': // euro sign
					vBuf.push('E');
					break;
				case '\u00A2': // cent sign
					vBuf.push('c');
					break;
				case '\u00A3': // pound sign
					vBuf.push('P');
					break;
				case '\u00A5': // yen sign
					vBuf.push('Y');
					break;
				default:
					if(pKeepUnknown) vBuf.push(vCh);
				}
			}
			if (vBuf.length == 0) vBuf.push('x');
			return vBuf.join("");
		}
		return pStr;
	},
	/* == xLocalStore ============================================================
	   PRIVATE - Local Storage API (localStorage/userData/cookie)
	   pId : local storage Id*/
	xLocalStore : function (pId){
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
	},
	/* == xSerialiseObjJs ========================================================
	   PRIVATE - Serialize a javascript object
	   pObj : object
	   return : string */
	xSerialiseObjJs : function(pObj){
		var vBuf="";
		iEscapeJs = function(pChar){
			switch(pChar) {
				case '\t' : return "\\t";
				case '\n' : return "\\n";
				case '\r' : return "\\r";
				case '\'' : return "\\\'";
				case '\"' : return "\\\"";
				case '\\' : return "\\\\";
			} 
			return "";
		};
		if(pObj) for (var vKey in pObj){
			var vLbl = (pObj instanceof Array) ? "" : "\'" + vKey + "\':";
			var vObj = pObj[vKey];
			if(vObj != null) {
				vBuf+= (vBuf!="" ? "," : "") + vLbl;
				if(vObj instanceof Array || typeof vObj == "object" || vObj instanceof Object) vBuf+= this.xSerialiseObjJs(vObj);
				else if(typeof vObj == "number") vBuf+= vObj;
				else vBuf+= "\'" + vObj.toString().replace(/[\t\n\r\\'"]/g, iEscapeJs) + "\'";
			}
		}
		if(pObj instanceof Array) return "[" + vBuf + "]";
		else if(typeof pObj == "object" || vObj instanceof Object) return "{" + vBuf + "}";
		else return vBuf;
	},
	/* == xDeserialiseObjJs ======================================================
	   PRIVATE - Deserialize a javascript object
	   pStr : string
	   return : object */
	xDeserialiseObjJs : function(pStr){
		if(!pStr) return null;
		var vVal;
		try{
			eval("vVal="+pStr);
			return vVal;
		}catch(e){
			return null;
		}
	},
	/* == xLog ===================================================================
	   PRIVATE - Log a message to console
	   pStr : input string*/
	xLog : function(pStr){
		try{
			if (this.fDebug) console.log("scServices.scSearch: "+pStr);
		} catch(e){}
	},
	loadSortKey: "0search"
}