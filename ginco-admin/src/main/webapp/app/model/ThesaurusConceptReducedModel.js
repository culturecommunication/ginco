Ext.define('GincoApp.model.ThesaurusConceptReducedModel', {
    extend: 'Ext.data.Model',

    fields: [
        {
            name: 'identifier',
            type: 'string'
        },
        {
            name: 'label',
            type: 'string'
        },
        {
            name: 'lang',
            type: 'string'
        }
    ],
   idProperty : 'identifier',

});