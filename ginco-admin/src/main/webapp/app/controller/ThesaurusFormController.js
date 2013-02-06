Ext.define('HadocApp.controller.ThesaurusFormController', {
    extend: 'Ext.app.Controller',

    views: [
        'ThesaurusPanel'
    ],

    models: [
        'ThesaurusModel'
    ],

    loadPanel : function(theForm)
    {
        var model = this.getThesaurusModelModel();
        var id = theForm.up('thesaurusPanel').thesaurusId;
        if( id != '' ) {
            model.load(id, {
                success:function(model){
                    theForm.loadRecord(model);
                }
            });
        }
    },

    init: function(application) {
        this.control({ 'thesaurusPanel form' : {
            afterrender : this.loadPanel
        }
        });
    }
});
