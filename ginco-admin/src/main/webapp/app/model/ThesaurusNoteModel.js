Ext.define('GincoApp.model.ThesaurusNoteModel', {
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
            name: 'language',
            type: 'string'
        },
        {
            name: 'type',
            type: 'string'
        },
        {
            name: 'source',
            type: 'string'
        },
        {
            name: 'created',
            type: 'string'
        },
        {
            name: 'modified',
            type: 'string'
        }
    ],
    idProperty : 'identifier'

});