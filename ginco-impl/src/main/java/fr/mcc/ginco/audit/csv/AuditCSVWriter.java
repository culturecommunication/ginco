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
package fr.mcc.ginco.audit.csv;

import java.io.BufferedWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fr.mcc.ginco.utils.LabelUtil;

@Service("auditCSVWriter")
public class AuditCSVWriter {

	private static final String COMMA = ",";
	
	private Logger logger  = LoggerFactory.getLogger(AuditCSVWriter.class);

	
	public void writeJournalLine(JournalLine line, BufferedWriter out) throws IOException {
		logger.debug(line.toString());
		out.write(line.toString());
		out.newLine();
	}
	
	public void writeHeader(BufferedWriter out) throws IOException {
		String header = "";
		header += LabelUtil.getResourceLabel("log-journal.headers.event-type") + COMMA;
		header += LabelUtil.getResourceLabel("log-journal.headers.date") + COMMA;
		header += LabelUtil.getResourceLabel("log-journal.headers.author") + COMMA;
		header += LabelUtil.getResourceLabel("log-journal.headers.conceptId") + COMMA;
		header += LabelUtil.getResourceLabel("log-journal.headers.termId") + COMMA;
		header += LabelUtil.getResourceLabel("log-journal.headers.role") + COMMA;

		header += LabelUtil.getResourceLabel("log-journal.headers.status") + COMMA;
		header += LabelUtil.getResourceLabel("log-journal.headers.oldlexicalvalue") + COMMA;
		header += LabelUtil.getResourceLabel("log-journal.headers.newlexicalvalue") + COMMA;
		header += LabelUtil.getResourceLabel("log-journal.headers.oldParent") + COMMA;
		header += LabelUtil.getResourceLabel("log-journal.headers.newparent");
		
		out.write(header);
		out.newLine();		
	}

	
}
