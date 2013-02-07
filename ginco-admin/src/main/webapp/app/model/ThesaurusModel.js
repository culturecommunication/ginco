Ext.define('HadocApp.model.ThesaurusModel', {
    extend: 'Ext.data.Model',

    fields: [
        {
            name: 'identifier',
            type: 'string'
        },
        {
            name: 'contributor',
            type: 'string'
        },
        {
            name: 'coverage',
            type: 'string'
        },
        {
            name: 'date',
            type: 'date'
        },
        {
            name: 'description',
            type: 'string'
        },
        {
            name: 'publisher',
            type: 'string'
        },
        {
            name: 'relation',
            type: 'string'
        },
        {
            name: 'rights',
            type: 'string'
        },
        {
            name: 'source',
            type: 'string'
        },
        {
            name: 'subject',
            type: 'string'
        },
        {
            name: 'title',
            type: 'string'
        },
        {
            name: 'created',
            type: 'date'
        },
        {
            name: 'format',
            type: 'int'
        },
        {
            name: 'type',
            type: 'int'
        },
        {
            name: 'creator',
            type: 'string'
        },
        {
            name: 'languages',
            type: 'array_of_string'
        }
    ],

    proxy: {
        type: 'rest',
        url: 'services/ui/thesaurusservice/getVocabulary',
        reader: {
            type: 'json'
        }
    }
});