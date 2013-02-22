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
package fr.mcc.ginco.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.ResourceBundle.Control;

public class EncodedControl extends Control {
	   private String encoding;  
		  
	    public EncodedControl(String encoding)  
	    {  
	        this.encoding = encoding;  
	    }  
	  
	    @Override  
	    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)  
	           throws IllegalAccessException, InstantiationException, IOException  
	    {  
	        if (!format.equals("java.properties"))  
	        {  
	            return super.newBundle(baseName, locale, format, loader, reload);  
	        }  
	        String bundleName = toBundleName(baseName, locale);  
	        ResourceBundle bundle = null;  
	        // code below copied from Sun's/Oracle's code; that's their indentation, not mine ;)  
	        final String resourceName = toResourceName(bundleName, "properties");  
	        final ClassLoader classLoader = loader;  
	        final boolean reloadFlag = reload;  
	        InputStream stream = null;  
	        try {  
	            stream = AccessController.doPrivileged(  
	            new PrivilegedExceptionAction<InputStream>() {  
	                public InputStream run() throws IOException {  
	                InputStream is = null;  
	                if (reloadFlag) {  
	                    URL url = classLoader.getResource(resourceName);  
	                    if (url != null) {  
	                    URLConnection connection = url.openConnection();  
	                    if (connection != null) {  
	                        // Disable caches to get fresh data for  
	                        // reloading.  
	                        connection.setUseCaches(false);  
	                        is = connection.getInputStream();  
	                    }  
	                    }  
	                } else {  
	                    is = classLoader.getResourceAsStream(resourceName);  
	                }  
	                return is;  
	                }  
	            });  
	        } catch (PrivilegedActionException e) {  
	            throw (IOException) e.getException();  
	        }  
	        if (stream != null) {  
	            try {  
	            Reader reader = new InputStreamReader(stream, encoding);  
	            bundle = new PropertyResourceBundle(reader);  
	// END CHANGE  
	            } finally {  
	            stream.close();  
	            }  
	        }  
	        // and to finish it off  
	        return bundle;  
	    }  
}
