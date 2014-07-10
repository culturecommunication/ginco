/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture
 * et de la Communication (2013)
 * <p/>
 * contact.gincoculture_at_gouv.fr
 * <p/>
 * This software is a computer program whose purpose is to provide a thesaurus
 * management solution.
 * <p/>
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software. You can use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * <p/>
 * As a counterpart to the access to the source code and rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty and the software's author, the holder of the
 * economic rights, and the successive licensors have only limited liability.
 * <p/>
 * In this respect, the user's attention is drawn to the risks associated
 * with loading, using, modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systemsand/or
 * data to be ensured and, more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

Ext.apply(Ext.Loader, {
    
    locale: {
        enabled: false,
        language: null,
        path: 'locale',
        localizedByDefault: false,
        extLocalePath: false,
        i18nServer: false,
        types: []
    },
    
    setLocale: function (config) {
        Ext.apply(this.locale, config);
        this.initLocaleManager();
    },
    
    // Returns the type of the class (controller, view...) if it's a localized class, otherwise, false.
    isLocalizedClass: function (className, data) {
        if (!this.locale.enabled || data.localized === false) {
            return false;
        }
        if (!this.locale.localizedByDefault && !data.localized) {
            return false;
        }
        if (className.indexOf(".locale." + this.locale.language) !== -1) {
            return false;    
        }
        for (var c in this.locale.types) {
            var currentType = this.locale.types[c];
            if (className.indexOf(currentType) !== -1) {
                return currentType;
            }
        }
        return false;
    },
    
    // TODO
    // This function is called too many times, it would be nice to find
    // a way to optimize the caller.
    applyLocaleForExtJS: function () {
        if (!!Ext.Loader.locale.extLocale) {
            try {
                eval(Ext.Loader.locale.extLocale);
            } catch (ex) {
            }
        }
    },

    initLocaleManager: function () {
        
        if (!this.locale.enabled) {
            return;
        }

        if (!!Ext.Loader.locale.extLocalePath) {
            Ext.onReady(function () {
                Ext.Loader.locale.isReady = true;
                var url = Ext.Loader.locale.extLocalePath + "/ext-lang-" + Ext.Loader.locale.language + ".js";
                Ext.Ajax.request({
                    async: false,
                    disableCaching : false,
                    url: url,
                    proxy: {
                        type: 'json'
                    },
                    success: function (response) {
                        Ext.Loader.locale.extLocale = response.responseText;
                    }
                });
            });
        }

        // LOCALE LOADER
        Ext.Class.registerPreprocessor('localeLoader', function (cls, data, callback) {
            var scope = Ext.Loader;
            var className = data.$className;
            var type = scope.isLocalizedClass(className, data);

            if (!type && !!Ext.Loader.locale.extLocalePath && !!Ext.Loader.locale.isReady) {
                scope.applyLocaleForExtJS();
            }
            if (type) {
                var dependencies = data.requires = data.requires || [];
                var appName = className.substring(0, className.indexOf("." + type));
                var dependency = appName + ".locale." + scope.locale.language + "." + className.substring(appName.length + 1);
                try
                {
                    // Previously used "dependencies.push(dependency)" and another preprocessor
                    // to apply the locale file. Everything is done in the same one now.
                    var useServer = !!scope.locale.i18nServer;
                    var url = scope.getPath(dependency);
                    var url = useServer ? ("http://" + scope.locale.i18nServer + "/" + url.substring(url.indexOf("/locale/") + "/locale/".length)) : url;

                    // JSONP
                    if (useServer) {
                        alert(url);
                        Ext.data.JsonP({
                            async: false,
                            url: url,
                            callback: function (response) {
                                alert("JSONP RESPONSE");
                                eval(response.responseText);
                                var localeClass = Ext.create(dependency);
                                var localeProperties = {};
                                for (var member in localeClass) {
                                    if (member[0] === 'x') {
                                        localeProperties[member] = localeClass[member];
                                    }
                                };
                                Ext.apply(data, localeProperties);
                            },
                            failure: function () {
                            }
                        });
                    } else {
                        Ext.Ajax.request({
                            async: false,
                            url: url,
                            proxy: {
                                type: 'json',
                                url: url
                            },
                            disableCaching : false,
                            success: function (response) {
                                eval(response.responseText);
                                var localeClass = Ext.create(dependency);
                                var localeProperties = {};
                                for (var member in localeClass) {
                                    if (member.charAt(0) === 'x') {
                                        localeProperties[member] = localeClass[member];
                                    }
                                }
                                Ext.apply(data, localeProperties);
                            },
                            failure: function () {
                            }
                        });
                    }
                }
                catch (ex) {
                }
                
            }
        }, true,'last');
    }
});
