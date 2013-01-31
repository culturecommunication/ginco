Ext.define('HadocApp.view.Viewport', {
    extend: 'HadocApp.view.HadocViewPort',
    renderTo: Ext.getBody(),
    requires: [
        'HadocApp.view.HadocViewPort',
        'HadocApp.view.AProposWin'
    ]
});