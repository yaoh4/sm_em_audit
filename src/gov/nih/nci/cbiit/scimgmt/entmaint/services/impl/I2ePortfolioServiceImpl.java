package gov.nih.nci.cbiit.scimgmt.entmaint.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nih.nci.cbiit.scimgmt.entmaint.dao.I2ePortfolioDAO;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioI2eVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.I2ePortfolioService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.DBResult;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioI2eAccountVO;

/**
 * @author dinhys
 *
 */
@Component
public class I2ePortfolioServiceImpl implements I2ePortfolioService {

	static Logger log = Logger.getLogger(I2ePortfolioServiceImpl.class);

	@Autowired
	private I2ePortfolioDAO i2ePortfolioDAO;
	@Autowired
	private LookupService lookupService;


	/* (non-Javadoc)
	 * @see gov.nih.nci.cbiit.scimgmt.entmaint.services.I2ePortfolioService#searchI2eAccounts(gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl, gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO, java.lang.Boolean)
	 */
	@Override
	public PaginatedListImpl<PortfolioI2eAccountVO> searchI2eAccounts(PaginatedListImpl paginatedList, AuditSearchVO searchVO, Boolean all) {
		paginatedList = i2ePortfolioDAO.searchI2eAccounts(paginatedList, searchVO, all);
		List<EmPortfolioI2eVw> portfolioList = paginatedList.getList();

		List<PortfolioI2eAccountVO> list = new ArrayList<PortfolioI2eAccountVO>();
		for (final EmPortfolioI2eVw account : portfolioList) {
			list.add(populatePortfolioAccountVO(account));
		}
		paginatedList.setList(list);

		return paginatedList;
	}
	
	/* (non-Javadoc)
	 * @see gov.nih.nci.cbiit.scimgmt.entmaint.services.I2ePortfolioService#saveNotes(java.lang.Integer, java.lang.String, java.util.Date)
	 */
	@Override
	public DBResult saveNotes(Long npnId, String notes, Date date) {
		return i2ePortfolioDAO.saveNotes(npnId, notes, date);
	}
	/**
	 * 
	 * @param id
	 * @param category
	 * @return
	 */
	public String getPortfolioNoteById(Long id){
		
		return i2ePortfolioDAO.getPortfolioNoteById(id);
	}
	
	/**
	 * Convert to PortfolioAccountVO object
	 * 
	 * @param emPortfolioVw
	 * @return
	 */
	private PortfolioI2eAccountVO populatePortfolioAccountVO(EmPortfolioI2eVw emPortfolioVw) {
		final PortfolioI2eAccountVO portfolioAccountVO = new PortfolioI2eAccountVO();
		try {
			ConvertUtils.register(new BigDecimalConverter(null),
									java.math.BigDecimal.class);
			ConvertUtils.register(new LongConverter(null),
									java.lang.Long.class);
			ConvertUtils.register(new IntegerConverter(null),
									java.lang.Integer.class);
			ConvertUtils.register(new ShortConverter(null),
									java.lang.Short.class);
			ConvertUtils.register(new DateConverter(null),java.util.Date.class);
			BeanUtils.copyProperties(portfolioAccountVO, emPortfolioVw);

		} catch (final Exception e) {
			log.error("Error occured creating notification transfer object",
							e);
		}
		return portfolioAccountVO;
	}
	
}
