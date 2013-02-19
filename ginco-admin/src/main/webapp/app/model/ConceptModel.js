Ext.define('GincoApp.model.ConceptModel', {
    extend: 'Ext.data.Model',

    fields: [
        {
            name: 'identifier',
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
            name: 'thesaurusId',
            type: 'string'
        }
    ],
    hasMany: [
              'Terms',
              {model: 'GincoApp.model.ThesaurusTermModel', name: 'terms'}
    ],
    idProperty : 'identifier',
    
    proxy : {
		api : {
			create : 'services/ui/thesaurustermservice/updateConcept',
			update : 'services/ui/thesaurustermservice/updateConcept',
			read :   'services/ui/thesaurustermservice/getConcept',
			destroy: 'services/ui/thesaurustermservice/destroyTerm'
		},
		type : 'ajax',
		reader : {
			type : 'json'
		},
		writer : {
			type : 'json'
		}
	}
    
});