Ext.define('GincoApp.model.SimpleConceptModel', {
    extend: 'Ext.data.Model',

    fields: [
        {
            name: 'identifier',
            type: 'string'
        }
    ],
    idProperty : 'identifier'   
});