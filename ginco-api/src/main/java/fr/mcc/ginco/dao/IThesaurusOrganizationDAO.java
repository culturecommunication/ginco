package fr.mcc.ginco.dao;

import fr.mcc.ginco.beans.ThesaurusOrganization;

import java.util.List;

/**
 *
 */
public interface IThesaurusOrganizationDAO extends IGenericDAO<ThesaurusOrganization, Integer> {

	/**
	 * Gets list of all organization's names that have at lease one thesaurus.
	 *
	 * @return
	 */
	List<ThesaurusOrganization> getFilteredOrganizationNames();
}
