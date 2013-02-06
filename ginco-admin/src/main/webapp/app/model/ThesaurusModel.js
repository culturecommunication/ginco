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
            type: 'date',
            dateFormat: 'Y-m-d'
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
            type: 'date',
            dateFormat: 'Y-m-d'
        },
        {
            name: 'format',
            type: 'string'
        },
        {
            name: 'type',
            type: 'string'
        },
        {
            name: 'creator',
            type: 'string'
        }
    ],

    hasMany: 'Language',

    proxy: {
        type: 'rest',
        url: 'services/ui/thesaurusservice/getVocabulary',
        reader: {
            type: 'json'
        }
    }
});