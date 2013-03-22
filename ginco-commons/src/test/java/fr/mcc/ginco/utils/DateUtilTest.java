/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture et de la
 * Communication (2013)
 * <p/>
 * contact.gincoculture_at_gouv.fr
 * <p/>
 * This software is a computer program whose purpose is to provide a thesaurus
 * management solution.
 * <p/>
 * This software is governed by the CeCILL license under French law and abiding
 * by the rules of distribution of free software. You can use, modify and/ or
 * redistribute the software under the terms of the CeCILL license as circulated
 * by CEA, CNRS and INRIA at the following URL "http://www.cecill.info".
 * <p/>
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability.
 * <p/>
 * In this respect, the user's attention is drawn to the risks associated with
 * loading, using, modifying and/or developing or reproducing the software by
 * the user in light of its specific status of free software, that may mean that
 * it is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systemsand/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

package fr.mcc.ginco.utils;
import java.util.Calendar;
/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture et de la
 * Communication (2013)
 * <p/>
 * contact.gincoculture_at_gouv.fr
 * <p/>
 * This software is a computer program whose purpose is to provide a thesaurus
 * management solution.
 * <p/>
 * This software is governed by the CeCILL license under French law and abiding
 * by the rules of distribution of free software. You can use, modify and/ or
 * redistribute the software under the terms of the CeCILL license as circulated
 * by CEA, CNRS and INRIA at the following URL "http://www.cecill.info".
 * <p/>
 * As a counterpart to the access to the source code and rights to copy, modify
 * and redistribute granted by the license, users are provided only with a
 * limited warranty and the software's author, the holder of the economic
 * rights, and the successive licensors have only limited liability.
 * <p/>
 * In this respect, the user's attention is drawn to the risks associated with
 * loading, using, modifying and/or developing or reproducing the software by
 * the user in light of its specific status of free software, that may mean that
 * it is complicated to manipulate, and that also therefore means that it is
 * reserved for developers and experienced professionals having in-depth
 * computer knowledge. Users are therefore encouraged to load and test the
 * software's suitability as regards their requirements in conditions enabling
 * the security of their systemsand/or data to be ensured and, more generally,
 * to use and operate it in the same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.junit.Test;



public class DateUtilTest {

	@Test
	public void testToString() {
		Calendar gCal = GregorianCalendar.getInstance();
		gCal.set(Calendar.DAY_OF_MONTH, 1);
		gCal.set(Calendar.MONTH, 2);
		gCal.set(Calendar.YEAR, 2013);
		gCal.set(Calendar.HOUR_OF_DAY, 22);
		gCal.set(Calendar.MINUTE, 21);
		gCal.set(Calendar.SECOND, 01);

		String actualDate = DateUtil.toString(gCal.getTime());
		Assert.assertEquals("2013-03-01 22:21:01", actualDate);
	}

	@Test
	public void testToStringWithNullValue() {
		String actualDate = DateUtil.toString(null);
		Assert.assertNull(actualDate);
	}

	@Test
	public void testFromString() {
		Calendar gCal = GregorianCalendar.getInstance();
		gCal.set(Calendar.DAY_OF_MONTH, 1);
		gCal.set(Calendar.MONTH, 2);
		gCal.set(Calendar.YEAR, 2013);
		gCal.set(Calendar.HOUR_OF_DAY, 22);
		gCal.set(Calendar.MINUTE, 21);
		gCal.set(Calendar.SECOND, 01);
		gCal.set(Calendar.MILLISECOND, 0);

		Date actualDate = DateUtil.dateFromString("2013-03-01 22:21:01");
		Assert.assertEquals(gCal.getTime(), actualDate);
	}

	@Test
	public void testFromStringWithInvalidFormat() {

		Date actualDate = DateUtil.dateFromString("not:a:date");
		Assert.assertEquals(null, actualDate);
	}

}
