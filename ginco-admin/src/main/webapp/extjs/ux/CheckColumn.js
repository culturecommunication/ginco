/**
 * @class Ext.ux.CheckColumn
 * @extends Ext.grid.column.Column
 * A Header subclass which renders a checkbox in each column cell which toggles the truthiness of the associated data field on click.
 *
 * Example usage:
 * 
 *    // create the grid
 *    var grid = Ext.create('Ext.grid.Panel', {
 *        ...
 *        columns: [{
 *           text: 'Foo',
 *           ...
 *        },{
 *           xtype: 'checkcolumn',
 *           text: 'Indoor?',
 *           dataIndex: 'indoor',
 *           width: 55
 *        }]
 *        ...
 *    });
 *
 * In addition to toggling a Boolean value within the record data, this
 * class adds or removes a css class <tt>'x-grid-checked'</tt> on the td
 * based on whether or not it is checked to alter the background image used
 * for a column.
 */
Ext.define('Ext.ux.CheckColumn', {
    extend: 'Ext.grid.column.Column',
    alias: 'widget.checkcolumn',

    /**
     * @cfg {Boolean} [stopSelection=true]
     * Prevent grid selection upon mousedown.
     */
    stopSelection: true,

    tdCls: Ext.baseCSSPrefix + 'grid-cell-checkcolumn',

    constructor: function(cfg) {
        var me = this;
        me.tpl = ['<tpl for=".">','placeholder','</tpl>']; // shouldn't
        // ever be used. Ignore it.
        var tplChecked = Ext.create('Ext.XTemplate', [
          '<tpl for=".">',
          '<div class="x-field x-form-item x-field-default x-form-cb-checked">',
          '<div class="x-form-item-body x-form-cb-wrap" role="presentation">',
          '<input class="x-form-field x-form-checkbox" type="button" autocomplete="off" aria-checked="true" aria-invalid="false" role="checkbox" data-errorqtip="">',
          '</div>',
          '</div>',
          '</tpl>'
        ]);
        var tplUnchecked = Ext.create('Ext.XTemplate', [
          '<tpl for=".">',
          '<div class="x-field x-form-item x-field-default">',
          '<div class="x-form-item-body x-form-cb-wrap" role="presentation">',
          '<input class="x-form-field x-form-checkbox" type="button" autocomplete="off" aria-checked="false" aria-invalid="false" role="checkbox" data-errorqtip="">',
          '</div>',
          '</div>',
          '</tpl>'
        ]);

        me.callParent(arguments);
        me.renderer = function(value, p, record) {
          var data = Ext.apply({}, record.data, record.getAssociatedData());
          if (data[me.dataIndex]) {
            return tplChecked.apply(data);
          } else {
            return tplUnchecked.apply(data);
          }
        };

      },
        processEvent : function(type, view, cell, recordIndex, cellIndex, e){
          var me = this;
          if (type == 'click') {
            var rec = view.getStore().getAt(recordIndex);
            rec.set(this.dataIndex, (rec.get(this.dataIndex))?false:true);
          } else if (type == 'mousedown') {
            return false;
          }
          return me.callParent(arguments);
        }
});
