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
        debugger;
        var model = this.getThesaurusModelModel();
        var id = theForm.up('thesaurusPanel').thesaurusId;
        model.load(id, {
            success:function(model){
                console.log(model);
//                debugger;
                theForm.loadRecord(model);
            }
        });
    },

    init: function(application) {
        this.control({ 'thesaurusPanel form' : {
            afterrender : this.loadPanel
        }
        });
    }
});
