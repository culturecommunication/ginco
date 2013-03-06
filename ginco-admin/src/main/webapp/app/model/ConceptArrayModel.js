Ext.define('GincoApp.model.ConceptArrayModel', {
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
            name: 'title',
            type: 'string'
        },
        {
            name: 'thesaurusId',
            type: 'string'
        },
        {
            name : 'superOrdinateConceptId',
            type : 'string'
        },
        {
            name : 'superOrdinateConceptTitle',
            type : 'string'
        }
    ],
    
    idProperty : 'identifier',
    
    proxy : {
		api : {
			//create : 'services/ui/thesaurusconceptservice/updateConcept',
			//update : 'services/ui/thesaurusconceptservice/updateConcept',
			//read :   'services/ui/thesaurusconceptservice/getConcept',
			//destroy: 'services/ui/thesaurusconceptservice/destroyTerm'
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