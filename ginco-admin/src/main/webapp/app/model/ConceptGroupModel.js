Ext.define('GincoApp.model.ConceptGroupModel', {
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
            name: 'label',
            type: 'string'
        },
        {
            name: 'thesaurusId',
            type: 'string'
        },
        {
            name: 'groupConceptLabelId',
            type: 'integer'
        },
        {
            name: 'type',
            type: 'string'
        },
        {
            name: 'language',
            type: 'string'
        },
        {
            name : 'concepts',
            type : 'array_of_string'
        }
    ],
    
    idProperty : 'identifier',
    
    proxy : {
		api : {
			//create : 'services/ui/thesaurusarrayservice/updateGroup',
			//update : 'services/ui/thesaurusarrayservice/updateGroup',
			read :   'services/ui/thesaurusconceptgroupservice/getConceptGroup'
			//destroy: 'services/ui/thesaurusarrayservice/destroyGroup'
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