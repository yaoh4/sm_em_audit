package gov.nih.nci.cbiit.scimgmt.entmaint.actions.decorator;

import java.text.SimpleDateFormat;
import java.util.Date;

import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioI2eAccountVO;

import org.apache.commons.lang3.StringUtils;
import org.displaytag.decorator.TableDecorator;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * This class is responsible for decorating the rows of I2E portfolio accounts search results table.
 * @author Jim Zhou
 *
 */
@Configurable
public class I2ePortfolioSearchResultExportDecorator extends TableDecorator{
	
	/**
	 * Get the full name.
	 * @return fullName
	 */
	public String getFullName() {
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		return portfolioVO.getFullName();
	}
	
	public String getI2eCreatedDate(){
		PortfolioI2eAccountVO portfolioVO = (PortfolioI2eAccountVO)getCurrentRowObject();
		Date createDate = portfolioVO.getCreatedDate();
		if(createDate == null){
			return "";
		}
		return new SimpleDateFormat("MM/dd/yyyy").format(createDate);
	}
	
}
