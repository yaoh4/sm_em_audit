/**
 * 
 */
package gov.nih.nci.cbiit.scimgmt.entmaint.helper;

import javax.servlet.http.HttpServletRequest;

import org.displaytag.tags.TableTagParameters;
import org.displaytag.util.ParamEncoder;

/**
 * @author menons2
 *
 */
public class DisplayTagHelper {

	/**
	 * 
	 */
	public DisplayTagHelper() {
		// TODO Auto-generated constructor stub
	}
	
	
	 public static boolean isExportRequest(HttpServletRequest request, 
             String tableId) {

		 String exportType = request.getParameter(
			new ParamEncoder(tableId).encodeParameterName(TableTagParameters.PARAMETER_EXPORTTYPE));
		 
		 return (exportType != null);
	 }

}
