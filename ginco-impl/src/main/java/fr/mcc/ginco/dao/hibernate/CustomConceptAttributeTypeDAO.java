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
package fr.mcc.ginco.dao.hibernate;

import fr.mcc.ginco.beans.CustomConceptAttributeType;
import fr.mcc.ginco.beans.Thesaurus;
import fr.mcc.ginco.dao.ICustomConceptAttributeTypeDAO;
import fr.mcc.ginco.exceptions.BusinessException;
import org.hibernate.Criteria;
import org.hibernate.FlushMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 */
@Repository("customConceptAttributeTypeDAO")
public class CustomConceptAttributeTypeDAO extends
        GenericHibernateDAO<CustomConceptAttributeType, Integer> implements ICustomConceptAttributeTypeDAO {

    @Override
    public List<CustomConceptAttributeType> getAttributesByThesaurus(Thesaurus thesaurus) throws BusinessException {
        Criteria criteria = getCurrentSession().createCriteria(CustomConceptAttributeType.class)
                .add(Restrictions.eq("thesaurus.identifier", thesaurus.getIdentifier()));
        return (List <CustomConceptAttributeType>) criteria.list();
    }
    
    @Override
    public CustomConceptAttributeType getAttributeByCode(Thesaurus thesaurus, String code)
    {
        Criteria criteria = getCurrentSession().createCriteria(CustomConceptAttributeType.class)
                .add(Restrictions.eq("thesaurus.identifier", thesaurus.getIdentifier()))
                .add(Restrictions.eq("code", code));
        List objList = criteria.list();
        if (objList.size()>0) {
        	return (CustomConceptAttributeType)objList.get(0);
        }
        return null;
        
    }
    
    @Override
    public CustomConceptAttributeType getAttributeByValue(Thesaurus thesaurus, String value)
    {
        Criteria criteria = getCurrentSession().createCriteria(CustomConceptAttributeType.class)
                .add(Restrictions.eq("thesaurus.identifier", thesaurus.getIdentifier()))
                .add(Restrictions.eq("value", value));
        List objList = criteria.list();
        if (objList.size()>0) {
        	return (CustomConceptAttributeType)objList.get(0);
        }
        return null;        
    }
    
    @Override
    public CustomConceptAttributeType update(CustomConceptAttributeType conceptAttributeType)
    {
    	getCurrentSession().setFlushMode(FlushMode.MANUAL);
    	CustomConceptAttributeType existingAttrByCode = this.getAttributeByCode(conceptAttributeType.getThesaurus(), conceptAttributeType.getCode());
    	CustomConceptAttributeType existingAttrByValue = this.getAttributeByValue(conceptAttributeType.getThesaurus(), conceptAttributeType.getValue());
    	boolean isUniqueCode = true;
    	boolean isUniqueValue = true;
    	if (existingAttrByCode!=null && existingAttrByCode.getIdentifier()!=conceptAttributeType.getIdentifier()) {
    		isUniqueCode = false;
    	}
    	if (existingAttrByValue!=null && existingAttrByValue.getIdentifier()!=conceptAttributeType.getIdentifier()) {
    		isUniqueValue = false;
    	}
    	if (isUniqueValue && isUniqueCode) {
	    	getCurrentSession().saveOrUpdate(conceptAttributeType);
			getCurrentSession().flush();
	    } else {
			if (!isUniqueValue) {
				throw new BusinessException(
						"Already existing custom attribute with value : "
								+ conceptAttributeType.getValue(),
						"already-existing-custom-attribute-value",
						new Object[] { conceptAttributeType.getValue() });
			} else {
				throw new BusinessException(
						"Already existing custom attribute with code : "
								+ conceptAttributeType.getCode(),
						"already-existing-custom-attribute-code",
						new Object[] { conceptAttributeType.getCode() });
			}
	    }
    	return conceptAttributeType;
    }

    public CustomConceptAttributeTypeDAO() {
        super(CustomConceptAttributeType.class);
    }
}