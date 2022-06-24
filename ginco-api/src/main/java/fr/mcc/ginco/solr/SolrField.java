/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture
 * et de la Communication (2013)
 *
 * contact.gincoculture_at_gouv.fr
 *
 * This software is a computer program whose purpose is to provide a thesaurus
 * management solution.
 *
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software. You can use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty and the software's author, the holder of the
 * economic rights, and the successive licensors have only limited liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading, using, modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and, more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

package fr.mcc.ginco.solr;

import fr.mcc.ginco.exceptions.TechnicalException;

import java.lang.reflect.Field;

public final class SolrField {
	private SolrField() {
	}

	public static final Integer MAX_SIZE = 32766;
	public static final Integer CUTOFF_SIZE = 8192;

	public static final String LEXICALVALUE = "lexicalValue";
	public static final String LEXICALVALUE_STR = "lexicalValue_str";
	public static final String IDENTIFIER = "identifier";
	public static final String THESAURUSID = "thesaurusId";
	public static final String THESAURUSTITLE = "thesaurusTitle";
	public static final String TYPE = "type";
	public static final String NOTES = "notes";
	public static final String CREATED = "created";
	public static final String MODIFIED = "modified";
	public static final String STATUS = "status";
	public static final String EXT_TYPE = "ext_type";
	public static final String LANGUAGE = "language";
	public static final String TEXT = "text";
	public static final String CONCEPTID = "conceptId";

	public static String getCheckedValue(String fieldName) {
		for (Field f : SolrField.class.getFields()) {
			try {
				if (fieldName.equals(f.get(null))) {
					return fieldName;
				}
			} catch (IllegalArgumentException e) {
				throw new TechnicalException("Wrong argument", e);
			} catch (IllegalAccessException e) {
				throw new TechnicalException("Access violation during argument access", e);
			}
		}
		return null;
	}
}