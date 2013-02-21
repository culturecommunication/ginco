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
            name: 'topconcept',
            type: 'boolean'
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
			create : 'services/ui/thesaurusconceptservice/updateConcept',
			update : 'services/ui/thesaurusconceptservice/updateConcept',
			read :   'services/ui/thesaurusconceptservice/getConcept',
			destroy: 'services/ui/thesaurusconceptservice/destroyTerm'
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