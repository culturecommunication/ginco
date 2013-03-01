Ext.define('GincoApp.model.NoteTypeModel', {
    extend: 'Ext.data.Model',

    fields: [
             	{
                    name: 'id',
                    type: 'string'
                },
                {
                    name: 'label',
                    type: 'string'
                },
                {
                    name: 'isTerm',
                    type: 'boolean'
                },
                {
                    name: 'isConcept',
                    type: 'boolean'
                }
    ],
    idProperty : 'id',
    
    proxy : {
		api : {
			create : 'services/ui/thesaurustermservice/updateNoteType',
			update : 'services/ui/thesaurustermservice/updateNoteType',
			read :   'services/ui/thesaurustermservice/getNoteType',
			destroy: 'services/ui/thesaurustermservice/destroyNoteType'
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