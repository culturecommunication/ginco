Ext.define('HadocApp.model.ThesaurusTermModel', {
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
            name: 'created',
            type: 'string'
        },
        {
            name: 'modified',
            type: 'string'
        },
        {
            name: 'source',
            type: 'string'
        },
        {
            name: 'prefered',
            type: 'boolean'
        },
        {
            name: 'status',
            type: 'int'
        },
        {
            name: 'role',
            type: 'int'
        },
        {
            name: 'conceptId',
            type: 'string'
        },
        {
            name: 'thesaurusId',
            type: 'string'
        },
        {
            name: 'language',
            type: 'string'
        }
    ]
});