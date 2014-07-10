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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Util desined to deal with Date/String/Timestamp convertions.
 */
public final class DateUtil {

	//DateUtil is an utility class => declaring its constructor as private
	private DateUtil() {
	}

	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat iso8601formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

	/**
	 * Converts Date to String.
	 *
	 * @param date object to convert.
	 * @return String in format yyyy-MM-dd HH:mm:ss.
	 */
	public static String toString(Date date) {
		if (date != null) {
			return formatter.format(date);
		}
		return null;
	}

	public static String toISO8601String(Date date) {
		if (date != null) {
			return iso8601formatter.format(date);
		}
		return null;
	}

	/**
	 * Parse String to Date object.
	 *
	 * @param date String in format yyyy-MM-dd HH:mm:ss.
	 * @return Date object or {@code null} if wrong format.
	 */
	public static Date dateFromString(String date) {
		try {
			return formatter.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * Get current time.
	 *
	 * @return Date object.
	 */
	public static Date nowDate() {
		return GregorianCalendar.getInstance().getTime();
	}
}
