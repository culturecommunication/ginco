Ext.Loader.setConfig({
    enabled: true
});

Ext.application({
    models: [
    ],
    stores: [
        'AdminTreeStore',
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
