package fr.mcc.ginco.solr;

import java.util.List;

import fr.mcc.ginco.beans.ThesaurusConcept;
import fr.mcc.ginco.exceptions.TechnicalException;

public interface IConceptIndexerService {
	/**
     * Takes an {@link ThesaurusConcept} and adds it to index
     *
     * @param Updated/created {@link ThesaurusConcept} to save to index
     * @throws TechnicalException
     */
	public void addConcept(ThesaurusConcept thesaurusConcept) throws TechnicalException;

	/**
     * Takes a list of {@link ThesaurusConcept} and adds it to index
     *
     * @param List of updated/created (@link Note) to save to index
     * @throws TechnicalException
     */
	public void addConcepts(List<ThesaurusConcept> thesaurusConcepts) throws TechnicalException;

	/**
     * Remove {@link ThesaurusConcept} from search index
     * @param {@link ThesaurusConcept}
     * @throws TechnicalException
     */
	public void removeConcept(ThesaurusConcept thesaurusConcept) throws TechnicalException;
}
