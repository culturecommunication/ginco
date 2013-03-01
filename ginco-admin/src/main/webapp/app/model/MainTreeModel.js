Ext.define('GincoApp.model.MainTreeModel', {
    extend: 'Ext.data.Model',

    fields: [
        {
            name: 'title',
            type: 'string'
        },
        {
            name: 'type',
            type: 'string'
        },
        {
            name: 'thesaurusId',
            type: 'string'
        }
    ]
});