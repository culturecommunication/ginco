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
package fr.mcc.ginco.tests.audit.csv;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import fr.mcc.ginco.audit.csv.JournalEventsEnum;
import fr.mcc.ginco.audit.csv.JournalLine;
import fr.mcc.ginco.utils.DateUtil;

public class JournalLineTest {
	
	@Test
	public void testToString() throws IOException {
		
		JournalLine line = new JournalLine();
		line.setAuthorId("unknown.author");
		line.setConceptId("http://fakeconcept");
		line.setEventDate(DateUtil.dateFromString("2013-01-20 20:30:00"));
		line.setEventType(JournalEventsEnum.THESAURUSCONCEPT_CREATED);
		Set<String> newGenericTerms = new LinkedHashSet<String>();
		newGenericTerms.add("http://fakeconcept1");
		newGenericTerms.add("http://fakeconcept2");
		line.setNewGenericTerm(newGenericTerms);
		line.setNewLexicalValue("New term lexical Value");
		Set<String> oldGenericTerms = new LinkedHashSet<String>();
		oldGenericTerms.add("http://fakeconcept1");
		line.setOldGenericTerm(oldGenericTerms);
		line.setOldLexicalValue("Old term lexical value");
		line.setStatus(0);
		line.setTermId("http://faketerm");
		line.setTermRole("TP");				

		Assert.assertEquals("log-journal.thesaurus-concept-created-event,2013-01-20 20:30:00,unknown.author,http://fakeconcept,http://faketerm,TP,concept-status[0],Old term lexical value,New term lexical Value,http://fakeconcept1,http://fakeconcept1|http://fakeconcept2", line.toString());
		
	}
	
	@Test
	public void testCompareTo() throws IOException {
		
		JournalLine line1 = new JournalLine();
		line1.setEventDate(DateUtil.dateFromString("2013-01-20 20:30:00"));
		line1.setEventType(JournalEventsEnum.THESAURUSCONCEPT_CREATED);
		line1.setRevisionNumber(1);
		
		JournalLine line11 = new JournalLine();
		line11.setEventDate(DateUtil.dateFromString("2013-01-20 20:30:00"));
		line11.setEventType(JournalEventsEnum.THESAURUSCONCEPT_CREATED);
		line11.setRevisionNumber(2);
		
		JournalLine line2 = new JournalLine();
		line2.setEventDate(DateUtil.dateFromString("2013-01-21 20:30:00"));
		line2.setEventType(JournalEventsEnum.THESAURUSCONCEPT_CREATED);
		line2.setRevisionNumber(1);
		
		JournalLine line3 = new JournalLine();
		line3.setEventDate(DateUtil.dateFromString("2013-01-21 20:30:00"));
		line3.setEventType(JournalEventsEnum.THESAURUSTERM_LINKED_TO_CONCEPT);
		line3.setRevisionNumber(1);
		
		
		int revisionTest = line1.compareTo(line11);
		Assert.assertTrue(revisionTest<0);
		
		int dateTest = line1.compareTo(line2);
		Assert.assertTrue(dateTest<0);

		int priorityTest = line1.compareTo(line3);
		Assert.assertTrue(priorityTest<0);
		
	}
}
