Ext.define('HadocApp.model.MainTreeModel', {
    extend: 'Ext.data.Model',

    fields: [
        {
            name: 'title',
            type: 'string'
        },
        {
            name: 'type',
            type: 'string'
        }
    ]
});