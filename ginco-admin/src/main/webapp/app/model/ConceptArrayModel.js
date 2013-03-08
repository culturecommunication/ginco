Ext.define('GincoApp.model.ConceptArrayModel', {
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
            name: 'language',
            type: 'string'
        },
        {
            name: 'thesaurusId',
            type: 'string'
        }
    ],
    associations: [
                   {
                       type: 'hasMany',
                       model: 'GincoApp.model.ThesaurusConceptReducedModel',
                       name: 'superOrdinateConcept',
                       associationKey: 'superOrdinateConcept',
                       instanceName: 'superOrdinateConcept'
                   },
                   {type: 'hasMany', model: 'GincoApp.model.ThesaurusConceptReducedModel',    name: 'concepts'}
  ],
    
    idProperty : 'identifier',
    
    proxy : {
		api : {
			//create : 'services/ui/thesaurusconceptservice/updateConcept',
			update : 'services/ui/thesaurusarrayservice/updateArray',
			read :   'services/ui/thesaurusarrayservice/getArray'
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