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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mcc.ginco.beans.Language;

/**
 * utility class to get objects labels
 *
 */
public final class LabelUtil {

	private static Logger logger = LoggerFactory.getLogger(LabelUtil.class);

	private static final String LANGUAGE_SEPARATOR = "@";

	private LabelUtil() {
	}

	/**
	 * Return a standard label depending on the language value and the default
	 * language defined
	 *
	 * @param lexical
	 *            value
	 * @parma language
	 * @param defaultLang
	 * @return the term lexical value + @ language if the language is not the
	 *         specified default language
	 */
	public static String getLocalizedLabel(String lexicalValue,
			Language language, String defaultLang) {
		String resultLabel = lexicalValue;
		if (!defaultLang.equals(language.getId())) {
			resultLabel += LANGUAGE_SEPARATOR + language.getId();
		}
		return resultLabel;
	}

	public static String getResourceLabel(String key) {
		try{
			ResourceBundle res = ResourceBundle.getBundle("labels",
				new EncodedControl("UTF-8"));
			return res.getString(key);
		} catch (MissingResourceException me) {
			logger.warn("Unable to find ResourceBUndle", me);
			return key;
		}
	}

	public static String getDefaultLabel(String key) {
		try{
			ResourceBundle res = ResourceBundle.getBundle("default",
				new EncodedControl("UTF-8"));
			return res.getString(key);
		} catch (MissingResourceException me) {
			logger.warn("Unable to find ResourceBUndle", me);
			return key;
		}
	}

}
