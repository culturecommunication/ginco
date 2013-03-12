Ext.define('GincoApp.model.ThesaurusModel', {
	extend : 'Ext.data.Model',

	fields : [ {
		name : 'id',
		type : 'string'
	}, {
		name : 'contributor',
		type : 'string'
	}, {
		name : 'coverage',
		type : 'string'
	}, {
		name : 'date',
		type : 'string'
	}, {
		name : 'description',
		type : 'string'
	}, {
		name : 'publisher',
		type : 'string'
	}, {
		name : 'relation',
		type : 'string'
	}, {
		name : 'rights',
		type : 'string'
	}, {
		name : 'source',
		type : 'string'
	}, {
		name : 'subject',
		type : 'string'
	}, {
		name : 'title',
		type : 'string'
	}, {
		name : 'created',
		type : 'string'
	}, {
		name : 'defaultTopConcept',
		type : 'boolean'
	}, {
		name : 'format',
		type : 'int'
	}, {
		name : 'type',
		type : 'int'
	}, {
		name : 'creatorName',
		type : 'string'
	}, {
		name : 'creatorHomepage',
		type : 'string'
	}, {
		name : 'languages',
		type : 'array_of_string'
	} ],

	proxy : {
		api : {
			create : 'services/ui/thesaurusservice/updateVocabulary',
			read : 'services/ui/thesaurusservice/getVocabulary',
			update : 'services/ui/thesaurusservice/updateVocabulary',
            destroy : 'services/ui/thesaurusservice/destroyVocabulary'
		},
		type : 'ajax',
		url : 'services/ui/thesaurusservice/getVocabulary',
		reader : {
			type : 'json',
			idProperty : 'id'
		},
		writer : {
			type : 'json',
			idProperty : 'id'
		}
	}
});