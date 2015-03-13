package gov.nih.nci.cbiit.scimgmt.entmaint.actions;
      
import org.apache.log4j.Logger;



@SuppressWarnings("serial")
public class HomeAction extends BaseAction {

    /*
     * (non-Javadoc)
     * @see com.opensymphony.xwork2.ActionSupport#execute()
     */
	
	static Logger logger = Logger.getLogger(HomeAction.class);
	
	
	
    public String execute() throws Exception {
        String forward = SUCCESS;

        return forward;
    }
}
