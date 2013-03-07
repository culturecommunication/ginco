Ext.define('GincoApp.controller.SandboxPanelController',
		{
			extend : 'Ext.app.Controller',

			models : [ 'ThesaurusModel' ],

			onGridRender : function(theGrid) {
				var thePanel = theGrid.up('sandboxPanel');
				var theStore = theGrid.getStore();
				theStore.getProxy().setExtraParam('idThesaurus',
						thePanel.thesaurusData.id);
				theStore.load();
				thePanel.setTitle(thePanel.title + ' : '
						+ thePanel.thesaurusData.title);
				
				var thesaurusId= thePanel.thesaurusData.id;
				var thesaurusModel= this.getThesaurusModelModel();
				thesaurusModel.load(thesaurusId, {
					success : function(model) {
						thePanel.thesaurusData = model.data;
					}
				});
			},

			onNodeDblClick : function(theGrid, record, item, index, e, eOpts) {
				var thePanel = theGrid.up('sandboxPanel');
				this.openThesaurusTermTab(record,thePanel.thesaurusData);
			},
			openThesaurusTermTab : function(aRecord, aThesaurusData) {				
				Thesaurus.ext.tabs.openTermTab(aRecord.data.identifier, aThesaurusData);			
			},
			refreshSandBoxList : function(thesaurusData)
			{
				var sandBoxTabs = Ext.ComponentQuery.query('topTabs sandboxPanel');
				Ext.Array.each(sandBoxTabs, function(sandBox, index, array) {
					if (sandBox.thesaurusData.id ==  thesaurusData.id) {
						var sandBoxGrid= sandBox.down("gridpanel");
						sandBoxGrid.getStore().load();
					}
				});
			},

			init : function() {
				this.application.on({
					termdeleted : this.refreshSandBoxList,
					termupdated : this.refreshSandBoxList,
                    conceptupdated : this.refreshSandBoxList,
                    conceptdeleted : this.refreshSandBoxList,
			        scope: this
				});
				this.control({
					'sandboxPanel gridpanel' : {
						render : this.onGridRender,
						itemdblclick : this.onNodeDblClick
					}
				});

			}
		});