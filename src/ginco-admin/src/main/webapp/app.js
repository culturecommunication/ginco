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
    ],
    stores: [
    ],
    views: [
        'HadocViewPort',
        'GlobalTabPanel',
        'LeftPanel',
        'TopToolbar',
        'AProposWin'
    ],
    autoCreateViewport: true,
    name: 'HadocApp',
    controllers: [
        'TopToolbarController'
    ]
});
