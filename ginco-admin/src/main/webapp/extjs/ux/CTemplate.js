/*!
 * CTemplate
 * Version 1.1
 * Copyright(c) 2011-2013 Skirtle's Den
 * License: http://skirtlesden.com/ux/ctemplate
 */
Ext.define('Ext.ux.CTemplate', {
    extend: 'Ext.XTemplate',

    statics: {
        AUTO_ID: 0
    },

    // May need to be increased if components are included deeper in the data object
    copyDepth: 10,

    // Placeholder element template. Should be changed in tandem with getPlaceholderEl()
    cTpl: '<p id="ctemplate-{0}-{1}"></p>',

    // Flag
    isCTemplate: true,

    constructor: function() {
        var me = this;

        me.callParent(arguments);

        me.id = ++me.statics().AUTO_ID;

        me.reset();
    },

    /* Takes a recursive copy of the values provided, switching out components for placeholder values. The component ids
     * are recorded and injectComponents() uses the ids to find the placeholder elements in the DOM and switch in the
     * components.
     */
    copyValues: function(values, depth) {
        var me = this,
            id,
            copy = {},
            copyDepth = depth || me.copyDepth;

        if (copyDepth === 1) {
            return values;
        }

        if (Ext.isArray(values)) {
            return Ext.Array.map(values, function(value) {
                return me.copyValues(value, copyDepth - 1);
            });
        }

        if (!Ext.isObject(values)) {
            return values;
        }

        // This is the key sleight-of-hand that makes the whole thing work
        if (values.isComponent) {
            id = values.getId();
            me.ids.push(id);
            return Ext.String.format(me.cTpl, id, me.id);
        }

        Ext.Object.each(values, function(key, value) {
            // $comp is a special value for a renderTpl that references the current component
            copy[key] = key === '$comp' ? value : me.copyValues(value, copyDepth - 1);
        });

        return copy;
    },

    // Override
    doInsert: function() {
        var ret = this.callParent(arguments);

        // There's no guarantee this will succeed so we still need polling as well
        this.injectComponents();

        return ret;
    },

    /* We have to resort to polling for component injection as we don't have full control over when the generated HTML
     * will be added to the DOM
     */
    doPolling: function(interval) {
        var me = this;

        me.pollInterval = interval;

        if (me.pollId) {
            clearTimeout(me.pollId);
        }

        me.pollId = Ext.defer(me.injectComponents, interval, me);
    },

    getPlaceholderEl: function(id) {
        return Ext.get('ctemplate-' + id + '-' + this.id);
    },

    /* Attempts to substitute all placeholder elements with the real components. If a component is successfully injected
     * or it has been destroyed then it won't be attempted again. This method is repeatedly invoked by a polling
     * mechanism until no components remain, however relying on the polling is not advised. Instead it is preferable to
     * call this method directly as soon as the generated HTML is inserted into the DOM.
     */
    injectComponents: function() {
        var me = this,
            ids = me.ids,
            index = ids.length - 1,
            id,
            cmp,
            placeholderEl;

        // Iterate backwards because we remove some elements in the loop
        for ( ; index >= 0 ; --index) {
            id = ids[index];
            cmp = Ext.getCmp(id);
            placeholderEl = me.getPlaceholderEl(id);

            if (me.renderComponent(cmp, placeholderEl) || !cmp) {
                // Either we've successfully done the switch or the component has been destroyed
                Ext.Array.splice(ids, index, 1);

                if (placeholderEl) {
                    placeholderEl.remove();
                }
            }
        }

        if (ids.length) {
            // Some components have not been injected. Polling acts both to do deferred injection and as a form of GC
            me.doPolling(me.pollInterval * 1.5);
        }
    },

    // Override
    overwrite: function(el) {
        var dom,
            firstChild,
            ret;

        /* In IE setting the innerHTML will destroy the nodes for the previous content. If we try to reuse components it
         * will fail as their DOM nodes will have been torn apart. We can't defend against external updates to the DOM
         * but we can guard against the case where all updates come through this template.
         */
        if (Ext.isIE) {
            dom = Ext.getDom(el);

            while (firstChild = dom.firstChild) {
                dom.removeChild(firstChild);
            }
        }

        ret = this.callParent(arguments);

        // There's no guarantee this will succeed so we still need polling as well
        this.injectComponents();

        return ret;
    },

    renderComponent: function(cmp, placeholderEl) {
        if (cmp && placeholderEl) {
            var parent = placeholderEl.parent();

            if (cmp.rendered) {
                // Move a component that has been rendered previously
                cmp.getEl().replace(placeholderEl);
            }
            else {
                cmp.render(parent, placeholderEl);
            }

            if (Ext.isIE6) {
                // Some components (mostly form fields) reserve space but fail to show up without a repaint in IE6
                parent.repaint();
            }

            return true;
        }

        return false;
    },

    reset: function() {
        var me = this;

        // The ids of injected components that haven't yet been rendered
        me.ids = [];

        if (me.pollId) {
            clearTimeout(me.pollId);
            me.pollId = null;
        }
    }
}, function(ctemplate) {
    var apply = function() {
        var me = this,
            args = Ext.Array.slice(arguments);

        args[0] = me.copyValues(args[0]);

        // As we're returning an HTML string/array we can't actually complete the injection here
        me.doPolling(10);

        return me.callParent(args);
    };

    // The main override is different depending on whether we're using ExtJS 4.0 or 4.1+
    if (ctemplate.prototype.applyOut) {
        // 4.1+
        ctemplate.override({
            applyOut: apply
        });
    }
    else {
        // 4.0
        ctemplate.override({
            applyTemplate: apply
        });

        ctemplate.createAlias('apply', 'applyTemplate');
    }
});