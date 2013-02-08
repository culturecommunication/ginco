Ext.define('Thesaurus.ext.KeyMenuItem', {
    extend : 'Ext.menu.Item',
    alias: 'widget.keymenuitem',
    renderTpl: [
        '<tpl if="plain">',
            '{text}',
        '</tpl>',
        '<tpl if="!plain">',
        '<a id="{id}-itemEl" class="' + Ext.baseCSSPrefix + 'menu-item-link keymenuitem-link" href="{href}" <tpl if="hrefTarget">target="{hrefTarget}"</tpl> hidefocus="true" unselectable="on">',
        '<img id="{id}-iconEl" src="{icon}" class="' + Ext.baseCSSPrefix + 'menu-item-icon {iconCls}" />',
        '<span id="{id}-textEl" class="' + Ext.baseCSSPrefix + 'menu-item-text keymenuitem-text">{text}</span>',
                '<span class="' + Ext.baseCSSPrefix + 'menu-item-text keymenuitem-cmdTxt" <tpl if="menu">style="margin-right: 17px;"</tpl> >{cmdTxt}</span>',
                '<tpl if="menu">',
                    '<img id="{id}-arrowEl" src="{blank}" class="' + Ext.baseCSSPrefix + 'menu-item-arrow" />',
                '</tpl>',
            '</a>',
        '</tpl>'
    ],
    beforeRender: function(ct, pos) {
        // Intercept the call to onRender so we can add the
        // keyboard shortcut text to the render data which
        // will be used by the template
        var me = this;
        Ext.applyIf(me.renderData, {
        	cmdTxt:  me.cmdTxt
        });
        me.callParent(arguments);
    }
});

Thesaurus.ext.utils = function(){
	var msgCt;
	function createBox(t, s){
	       return '<div class="msg"><h3>' + t + '</h3><p>' + s + '</p></div>';
	}
	return {
        msg : function(title, format){
            if(!msgCt){
                msgCt = Ext.core.DomHelper.insertFirst(document.body, {id:'msg-div'}, true);
            }
            var s = Ext.String.format.apply(String, Array.prototype.slice.call(arguments, 1));
            var m = Ext.core.DomHelper.append(msgCt, createBox(title, s), true);
            m.hide();
            m.slideIn('t').ghost("t", { delay: 1000, remove: true});
        }
	};
}();