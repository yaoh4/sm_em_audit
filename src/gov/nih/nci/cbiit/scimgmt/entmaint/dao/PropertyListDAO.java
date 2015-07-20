package gov.nih.nci.cbiit.scimgmt.entmaint.dao;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.exceptions.HibernateDAOException;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PropertyListDAO  {

	@Autowired
	private EntMaintProperties properties;
	static Logger logger = Logger.getLogger(PropertyListDAO.class);
	
	@Autowired
	private SessionFactory sessionFactory;

	public List retrieve(String listName) {
           //     logger.debug("In retrieve "+listName);
		Session session = null;
		List result = null;
		try {
			session = sessionFactory.getCurrentSession();
			Criteria criteria = buildCriteria(session, listName);

			logger.info("criteria " + criteria.toString());
			result = criteria.list();
		} catch (Exception e) {
			throw new HibernateDAOException(e.getMessage());

		}
		return result;

	}

	private Criteria buildCriteria(Session session, String listName) {
		Criteria aCriteria = null;
		Class clazz = null;
          //      logger.info("list name "+listName);
		String className =
				properties.getProperty(listName + ".hibernatemappingclass");
		if(StringUtils.startsWithIgnoreCase(listName, ApplicationConstants.GLOBAL_LOOKUP_LIST)) {
			className = properties.getProperty("appLookup.hibernatemappingclass");
		}
		//logger.info("Criteria class name " + className);
		try {
			clazz = Class.forName(className);

			aCriteria = session.createCriteria(clazz);
			
			if(StringUtils.startsWithIgnoreCase(listName, ApplicationConstants.GLOBAL_LOOKUP_LIST)) {
				aCriteria.add(Restrictions.eq("applicationName", "EM"));
				aCriteria.addOrder(Order.asc("orderNum"));
				aCriteria.add(Restrictions.eq("active", true));
				String discriminatorCamel = StringUtils.removeStartIgnoreCase(listName, "appLookup");
				String discriminatorLower = discriminatorCamel.replaceAll("([^_A-Z])([A-Z])", "$1_$2");
				String discriminator = StringUtils.upperCase(discriminatorLower);
				aCriteria.add(Restrictions.eq("discriminator", discriminator));
			}

		} catch (ClassNotFoundException e) {

			throw new HibernateDAOException("Unable to create class " +
											className + ";  " + e.toString());
		} catch (Exception e) {
			throw new HibernateDAOException(e.toString());
		}
		return aCriteria;
	}

}
