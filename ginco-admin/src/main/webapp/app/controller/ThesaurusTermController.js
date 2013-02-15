Ext.define('GincoApp.controller.ThesaurusTermController', {
	extend:'Ext.app.Controller',
	
	views : [ 'SandBoxPanel' ],
	
	onGridRender : function(theGrid){
		var thePanel = theGrid.up('sandboxPanel');
		console.log('GridRendered');
		var theStore= theGrid.getStore();
		theStore.getProxy().setExtraParam('idThesaurus',thePanel.thesaurusData.id);
		theStore.load();
	},
	
    init:function(){
         this.control({
        	 'sandboxPanel gridpanel' : {
 				render : this.onGridRender
 			}
         });

       
    }
});