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
            name: 'nodeLabelId',
            type: 'int'
        },
        {
            name: 'thesaurusId',
            type: 'string'
        },
        {
            name: 'superOrdinateId',
            type: 'string'
        },
        {
            name: 'superOrdinateLabel',
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
			create : 'services/ui/thesaurusarrayservice/updateArray',
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