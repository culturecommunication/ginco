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
            name: 'source',
            type: 'string'
        },
        {
            name: 'type',
            type: 'string'
        }
    ],
    idProperty : 'identifier',
    
    proxy : {
		api : {
			create : 'services/ui/thesaurusnoteservice/updateNote',
			update : 'services/ui/thesaurusnoteservice/updateNote',
			read :   'services/ui/thesaurusnoteservice/getThesaurusNote',
			destroy: 'services/ui/thesaurusnoteservice/destroyNote'
		},
		type : 'ajax',
		reader : {
			type : 'json',
			messageProperty: 'message'
		},
		writer : {
			type : 'json'
		}
	}
    
});