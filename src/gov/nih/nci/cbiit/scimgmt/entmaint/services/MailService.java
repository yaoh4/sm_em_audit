package gov.nih.nci.cbiit.scimgmt.entmaint.services;

import org.apache.velocity.app.VelocityEngine;

public interface MailService {

	/**
	 * Gets the velocity engine.
	 * 
	 * @return the velocity engine
	 */
	public VelocityEngine getVelocityEngine();

	/**
	 * Email IC Coordinators with the discrepancy accounts.
	 * 
	 */
	public void sendDiscrepancyEmail() throws Exception;

}
