/*
The MIT License

Copyright (c) 2011 Jordy Theiller 
work@meow.fr

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
documentation files (the "Software"), to deal in the Software without restriction, including without limitation the 
rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit 
persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the 
Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
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
                            success: function (response) {
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
                    }
                }
                catch (ex) {
                }
                
            }
        }, true).setDefaultPreprocessorPosition('localeLoader', 'last');
    }
});
