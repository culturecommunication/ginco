Ext.Loader.setConfig({
    enabled: true
});

Ext.Loader.setLocale({
    enabled: true,
    language: 'fr',
    localizedByDefault: false,
    types: [ 'controller', 'view' ]
});

Ext.application({
    models: [
        'MainTreeModel'
    ],
    stores: [
        'MainTreeStore'
    ],
    views: [
        'HadocViewPort',
        'GlobalTabPanel',
        'LeftPanel',
        'TopToolbar',
        'AProposWin',
        'ThesaurusPanel'
    ],
    autoCreateViewport: true,
    name: 'HadocApp',
    controllers: [
        'TopToolbarController'
    ]
});
