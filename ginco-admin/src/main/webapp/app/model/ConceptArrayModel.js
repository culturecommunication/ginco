Ext.define('GincoApp.model.ConceptArrayModel', {
    extend: 'Ext.data.Model',

    fields: [
        {
            name: 'identifier',
            type: 'string'
        },
        {
            name: 'ordered',
            type: 'boolean'
        },
        {
            name: 'notation',
            type: 'string'
        },
        {
            name: 'thesaurusId',
            type: 'string'
        }
    ],
    associations: [
                   {type: 'hasMany', model: 'GincoApp.model.ThesaurusConceptReducedModel',    name: 'concepts'},
                   {type: 'hasMany', model: 'GincoApp.model.NodeLabelModel',    name: 'nodeLabelViewList'}
  ],
    
    idProperty : 'identifier',
    
    proxy : {
		api : {
			//create : 'services/ui/thesaurusconceptservice/updateConcept',
			//update : 'services/ui/thesaurusconceptservice/updateConcept',
			read :   'services/ui/thesaurusarrayservice/getArray',
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