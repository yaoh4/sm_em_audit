package gov.nih.nci.cbiit.scimgmt.entmaint.utils;

import gov.nih.nci.cbiit.scimgmt.entmaint.services.MailService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The Class AutomatedSendReminderMailTask.
 */
@Component
public class AutomatedSendEmailTask {

	@Autowired
	protected MailService mailService;
	@Autowired
	private EntMaintProperties entMaintProperties;
	
	private static final Logger log = Logger.getLogger(AutomatedSendEmailTask.class);

	/**
	 * @return the mailService
	 */
	public MailService getMailService() {
		return mailService;
	}

	/**
	 * @param mailService the mailService to set
	 */
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	/**
	 * Send emails to IC coordinator for discrepancy accounts
	 */
	public void sendDiscrepancyEmail() {
		if (entMaintProperties.getProperty("email.discrepancy.sendflag", "Y").equalsIgnoreCase("N")) {
			log.info("=====> Monthly Reminder emails to IC coordinators is turned off. Property Name: email.discrepancy.sendflag");
			return;
		}
		log.info("=====> Sending Monthly Reminder emails to IC coordinators");
		try {
			//Send email			
			mailService.sendDiscrepancyEmail();
		} catch (Exception e) {
			log.error("=====> Exception occurred while Sending Monthly Reminder emails to IC coordinators", e);
		}
	}
}
