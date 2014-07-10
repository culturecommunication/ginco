package fr.mcc.ginco.dao;

import fr.mcc.ginco.beans.ThesaurusOrganization;

import java.util.List;

/**
 *
 */
public interface IThesaurusOrganizationDAO extends IGenericDAO<ThesaurusOrganization, Integer> {

	/**
	 * Gets list of all organizations that have at lease one thesaurus.
	 *
	 * @return
	 */
	List<ThesaurusOrganization> getFilteredOrganizations();
}
