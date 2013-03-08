Ext.define('GincoApp.model.NodeLabelModel', {
    extend: 'Ext.data.Model',

    fields: [
        {
            name: 'identifier',
            type: 'string'
        },
        {
            name: 'lexicalValue',
            type: 'string'
        },
        {
            name: 'modified',
            type: 'string'
        },
        {
            name: 'created',
            type: 'string'
        },
        {
            name: 'language',
            type: 'string'
        },
        {
            name: 'thesaurusArrayId',
            type: 'string'
        }
    ],

    idProperty : 'identifier'
    
});