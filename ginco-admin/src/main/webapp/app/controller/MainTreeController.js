Ext.define('HadocApp.controller.MainTreeController', {
    extend: 'Ext.app.Controller',

    views: [
        'LeftPanel'
    ],

    onNodeDblClick: function(tree, record, item, index, e, eOpts) {
        if(record.data.type == "THESAURUS") {
            var topTabs = Ext.ComponentQuery.query('topTabs')[0];
            var tabExists = false;
            topTabs.items.each(function (element, index, array) {
                if(element.thesaurusId == record.data.id) {
                    tabExists = element;
                }
            });

            if(!tabExists) {
                var ThesaurusPanel = Ext.create('HadocApp.view.ThesaurusPanel');
                ThesaurusPanel.thesaurusId = record.data.id;

                var tab = topTabs.add(ThesaurusPanel);
                topTabs.setActiveTab(tab);
                tab.show();
            } else {
                topTabs.setActiveTab(tabExists);
            }
        }
        return false;
    },

    init: function(application) {
        this.control({
            "#mainTreeView": {
                beforeitemdblclick: this.onNodeDblClick
            }
        });
    }

});
