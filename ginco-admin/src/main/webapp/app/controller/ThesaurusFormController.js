Ext.define('HadocApp.controller.ThesaurusFormController', {
    extend: 'Ext.app.Controller',

    views: [
        'ThesaurusPanel'
    ],

    models: [
        'ThesaurusModel'
    ],
    stores : ['MainTreeStore'],

    loadPanel : function(theForm)
    {
        var model = this.getThesaurusModelModel();
        var id = theForm.up('thesaurusPanel').thesaurusId;
        if( id != '' ) {
        	theForm.getEl().mask("Chargemenet");
            model.load(id, {
                success:function(model){
                	theForm.up('thesaurusPanel').setTitle(model.data.title);
                	theForm.setTitle(model.data.title);
                    theForm.loadRecord(model);
                    theForm.getEl().unmask();
                }
            });
        } else {
            model = Ext.create('HadocApp.model.ThesaurusModel');
            model.id = -1;
            theForm.loadRecord(model);
        }
    },
    updateTreeView: function()
    {
    	var MainTreeStore = this.getMainTreeStoreStore();
    	MainTreeStore.load();
    },

    saveForm : function(theButton)
    {
    	var me = this;
        var theForm = theButton.up('form');
        if (theForm.getForm().isValid())
        {
        	theForm.getEl().mask("Chargement");
	        theForm.getForm().updateRecord();
	        var updatedModel = theForm.getForm().getRecord();
	        updatedModel.save({
	        	success: function(record, operation)
	            {
	        		theForm.loadRecord(record);
	        		 theForm.getEl().unmask();
	        		 Thesaurus.ext.utils.msg('Succès', 'Le thesaurus a été enregistré!');
	        		 me.updateTreeView();
	            }
	        });
        }
    },
    init: function(application) {
        this.control(
            {
                'thesaurusPanel form' :
                {
                    afterrender : this.loadPanel
                },
                'thesaurusPanel button[cls=save]' :
                {
                    click : this.saveForm
                }

            });
    }
});
