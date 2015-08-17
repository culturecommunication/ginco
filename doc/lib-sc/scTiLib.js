


var scTiLib = {
	fStep : 50,
	fProcess : -1,
	fCurr : null,
	
	addTaskNow : function(pTask) {
		if(this.fCurr == null) {
			this.fCurr = pTask;
		} else {
			var vLast = this.fCurr;
			if(vLast === pTask) { this.xRun(); return;}
			while(vLast._ParalTask) {
				vLast = vLast._ParalTask;
				if(vLast === pTask) { this.xRun(); return;}
			}
			vLast._ParalTask = pTask;
		}
		this.xRun();
	},
	
	addTaskAfterAll : function(pTask) {
		if(this.fCurr==null) {
			this.fCurr = pTask;
		} else {
			var vLast = this.fCurr;
			while(vLast._NextTask) vLast = vLast._NextTask;
			vLast._NextTask = pTask;
		}
		this.xRun();
	},
	
	stopAll : function() {
		window.clearTimeout(this.fProcess);
		this.fProcess = -1;
		var vT = this.fCurr;
		while(vT && vT._ParalTask) {
			var vNext = vT._ParalTask;
			delete vT._ParalTask;
			vT = vNext;
		}
		var vT = this.fCurr;
		while(vT && vT._NextTask) {
			var vNext = vT._NextTask;
			delete vT._NextTask;
			vT = vNext;
		}
		this.fCurr = null;
	},
	
	xRun : function() {
		if(this.fProcess==-1) this.fProcess = window.setTimeout(scTiExecProcess, 0);
	}
}


function scTiExecProcess () {
	var vTask = scTiLib.fCurr;
	var vPrevTask = null;
	while(vTask) {
		var vAlive = false;
		try{ vAlive = vTask.execTask(); } catch(e){}
		if( ! vAlive) {

			if(vPrevTask) {

				vPrevTask._ParalTask = vTask._ParalTask;
				delete vTask._ParalTask;
			} else {

				if(vTask._ParalTask) {

					scTiLib.fCurr = vTask._ParalTask;
					scTiLib.fCurr._NextTask = vTask._NextTask;
					delete vTask._ParalTask;
					delete vTask._NextTask;
					vPrevTask = null;
					vTask = scTiLib.fCurr;
					continue;
				} else {

					scTiLib.fCurr = vTask._NextTask;
					delete vTask._NextTask;
					break;
				}
			}
		}
		vPrevTask = vTask;
		vTask = vTask._ParalTask;
	}
	if(scTiLib.fCurr) scTiLib.fProcess = window.setTimeout(scTiExecProcess, scTiLib.fStep);
	else scTiLib.fProcess = -1;
}



 



function ScTiGroupTask(pTaskArrray){
	this.fTasks = pTaskArrray;
}
ScTiGroupTask.prototype.execTask = function() {
	var vAlive = false;
	if(this.fTasks) for (var i = 0; i < this.fTasks.length; i++) {
		var vTask = this.fTasks[i];
		if(vTask != null) {
			var vTaskAlive = false;
			try { vTaskAlive = vTask.execTask(); } catch(e){}
			vAlive |= vTaskAlive;
		}
	}
	return vAlive;
}
ScTiGroupTask.prototype.addTask = function(pTask) {
	if(this.fTasks==null) this.fTasks = [pTask];
	else this.fTasks[this.fTasks.length] = pTask;
}


function ScTiSequenceTask(pTaskArrray){
	this.fTasks = pTaskArrray;
}
ScTiSequenceTask.prototype.execTask = function() {
	var vAlive = false;
	if(this.fTasks) for (var i = 0; i < this.fTasks.length; i++) {
		var vTask = this.fTasks[i];
		if(vTask != null) {
			var vTaskAlive = false;
			try { vTaskAlive = vTask.execTask(); } catch(e){}
			vAlive = vTaskAlive || i < this.fTasks.length - 1;
			break;
		}
	}
	return vAlive;
}
ScTiSequenceTask.prototype.addTask = ScTiGroupTask.prototype.addTask;



function ScTiWaitTask(pNbCycles){
	this.fNbCycles = pNbCycles;
	this.fCyclesDone = 0;
}
ScTiWaitTask.prototype.execTask = function() {
	this.fCyclesDone++;
	return (this.fCyclesDone>=this.fNbCycles);
}

