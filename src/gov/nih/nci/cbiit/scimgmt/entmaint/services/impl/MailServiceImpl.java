package gov.nih.nci.cbiit.scimgmt.entmaint.services.impl;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmDiscrepancyTypesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.I2ePortfolioService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2PortfolioService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.MailService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.UserRoleService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.DiscrepancyEmailAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioI2eAccountVO;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.VelocityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Component
public class MailServiceImpl implements MailService {
	private static final Logger log = Logger.getLogger(MailServiceImpl.class);
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private VelocityEngine velocityEngine;
	@Autowired
	private EntMaintProperties entMaintProperties;
	@Autowired
	protected Impac2PortfolioService impac2PortfolioService;
	@Autowired
	protected I2ePortfolioService i2ePortfolioService;
	@Autowired
	private LookupService lookupService;
	@Autowired
    private UserRoleService userRoleService;
	
	private Properties emailTemplates;


	/**
	 * Gets the mail sender.
	 * 
	 * @return the mailSender
	 */
	public JavaMailSender getMailSender() {
		return mailSender;
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cbiit.scimgmt.oar.service.MailService#getVelocityEngine()
	 */
	@Override
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	/**
	 * Parses the source string and returns an array of strings.
	 * 
	 * Separator character is a comma or semicolon.
	 * 
	 * @param source
	 *            the source
	 * @return the string[]
	 */
	private String[] parse(final String source) {
		if (StringUtils.isBlank(source)) {
			return null;
		}
		return source.split("[;,]");
	}

	/**
	 * Send.
	 * 
	 * @param from
	 *            the from
	 * @param template
	 *            the template
	 * @param to
	 *            the to
	 * @param cc
	 *            the cc
	 * @param bcc
	 *            the bcc
	 * @param subject
	 *            the subject
	 * @param params
	 *            the params
	 */
	private void send(final String from, final String template,
			final String to[], final String cc[], final String bcc[],
			final String subj, final Map<String, Object> params) {

		if (template != null) {
			final VelocityContext vc = new VelocityContext(params);

			
			if (to != null) {

				final StringWriter subjectWriter = new StringWriter();

				try {
					final String body = VelocityEngineUtils
							.mergeTemplateIntoString(velocityEngine, emailTemplates.getProperty(template), "UTF-8", params);
					velocityEngine.evaluate(vc, subjectWriter, emailTemplates.getProperty(template), subj);
					log.debug("evaluating email template: " + body);
					log.info("sending message....." + template + " with params..... " + params);

					final MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage(), true,
							"UTF-8");
					String subject = subjectWriter.toString();

					final String env = entMaintProperties.getProperty("ENVIRONMENT", "dev");

					if (env.toLowerCase().startsWith("prod")) {
						helper.setTo(to);

						if (cc != null) {
							helper.setCc(cc);
						}
						if (bcc != null) {
							helper.setBcc(bcc);
						}
					} else {
						subject = "[" + env.toUpperCase() + "] " + subject;
						subject += " {TO: " + StringUtils.join(to, ',') + "} {CC: " + StringUtils.join(cc, ',') + "}";
						final String[] overrideAddrs = StringUtils.split(entMaintProperties
								.getProperty("email.bcc"));						
						helper.setTo(overrideAddrs);
					}

					helper.setText(body, true);
					helper.setSubject(subject);

					helper.setFrom(from);

					log.info("invoking mailSender");
					mailSender.send(helper.getMimeMessage());
					log.info("done invoking mailSender");

				} catch (final VelocityException e) {
					log.error("VelocityException", e);
				} catch (final IOException e) {
					log.error("IOException", e);
				} catch (final MessagingException e) {
					log.error("MessagingException", e);
				} catch (Exception e) {
					log.error("Exception", e);
				}
			} else {
				log.error("required parameter 'to' not found");
			}
		} else {
			log.error("No message with identifier '" + template + "' found");
		}
	}

	public void setEmailTemplates(Properties emailTemplates) {
        this.emailTemplates = emailTemplates;
    }
	
	/**
	 * Sets the mail sender.
	 * 
	 * @param mailSender
	 *            the mailSender to set
	 */
	public void setMailSender(final JavaMailSender mailSender) {
		log.info("setMailSender(" + mailSender + ")");
		this.mailSender = mailSender;
	}
	
	/**
	 * Sets the velocity engine.
	 * 
	 * @param velocityEngine
	 *            the new velocity engine
	 */
	public void setVelocityEngine(final VelocityEngine velocityEngine) {
		log.info("setVelocityEngine(" + velocityEngine + ")");
		this.velocityEngine = velocityEngine;
	}
	
	@Override
	public void sendDiscrepancyEmail() {
		
		// Retrieve list of IC coordinator network ID
		List<String> icCoordinators = userRoleService.retrieveIcCoordinators();
		Map<String,List<String>> orgMapEmail = new HashMap<String, List<String>>();
		Map<String,List<String>> orgMapName = new HashMap<String, List<String>>();
		
		// Add EMADMIN
		String [] admin = parse(entMaintProperties.getProperty("EMADMIN"));
		icCoordinators.addAll(Arrays.asList(admin));
		
		// For each IC coordinator, get the org
		for (String icCoordinator: icCoordinators) {
			
			NciUser nciUser = userRoleService.getNCIUser(icCoordinator);  
			// Load user EM roles
			try {
				userRoleService.loadPersonInfo(nciUser);
			} catch (Exception e) {
				continue;
			}
			if(nciUser.getCurrentUserRole().equalsIgnoreCase("EMADMIN")) {
				if(!orgMapEmail.containsKey("EMADMIN")) {
					orgMapEmail.put("EMADMIN",new ArrayList<String>());
					orgMapName.put("EMADMIN",new ArrayList<String>());
				}
				orgMapEmail.get("EMADMIN").add(nciUser.getEmail());
				orgMapName.get("EMADMIN").add(nciUser.getFirstLastName());
			}
			else {
				if(!orgMapEmail.containsKey(nciUser.getOrgPath())) {
					orgMapEmail.put(nciUser.getOrgPath(),new ArrayList<String>());
					orgMapName.put(nciUser.getOrgPath(),new ArrayList<String>());
				}
				orgMapEmail.get(nciUser.getOrgPath()).add(nciUser.getEmail());
				orgMapName.get(nciUser.getOrgPath()).add(nciUser.getFirstLastName());
			}
		}
		
		// For each Org, send the email
		for (Map.Entry<String,List<String>> entry : orgMapEmail.entrySet()) {
			
			String[] email = entry.getValue().toArray(new String[entry.getValue().size()]);
			String dear = StringUtils.join(orgMapName.get(entry.getKey()), ", ");
			if (orgMapName.get(entry.getKey()).size() >= 3) {
				StringBuffer sb = new StringBuffer(dear);
		        sb.replace(dear.lastIndexOf(", "), dear.lastIndexOf(", ") + 1, ", and ");
		        dear = sb.toString();
			}
			
			PaginatedListImpl<PortfolioAccountVO> portfolioAccounts = new PaginatedListImpl<PortfolioAccountVO>();	
			PaginatedListImpl<PortfolioI2eAccountVO> portfolioI2eAccounts = new PaginatedListImpl<PortfolioI2eAccountVO>();	
			AuditSearchVO searchVO = new AuditSearchVO();
			if(entry.getKey().equalsIgnoreCase("EMADMIN")) {
				searchVO.setExcludeNCIOrgs(true);
			}
			else {
				searchVO.setOrganization(entry.getKey());
			}
			searchVO.setCategory(ApplicationConstants.PORTFOLIO_CATEGORY_DISCREPANCY);
		
			// Retrieve list of discrepancy accounts for IMPAC II
			portfolioAccounts = impac2PortfolioService.searchImpac2Accounts(portfolioAccounts, searchVO , true);
		
			// Retrieve list of discrepancy accounts for I2E
			searchVO.setCategory(ApplicationConstants.I2E_PORTFOLIO_CATEGORY_DISCREPANCY);
			portfolioI2eAccounts = i2ePortfolioService.searchI2eAccounts(portfolioI2eAccounts, searchVO, true);
		
			// Populate the account list to pass to email template
			List<DiscrepancyEmailAccountVO> accounts = populateDiscrepancyAccounts(portfolioAccounts.getList());
			accounts.addAll(populateI2eDiscrepancyAccounts(portfolioI2eAccounts.getList()));

			if(accounts.size() > 0) {
				DateFormat df = new SimpleDateFormat("MM/yyyy");
				final Map<String, Object> params = new HashMap<String, Object>();
				params.put("accounts", accounts);
				params.put("dear", dear);
				params.put("url", entMaintProperties.getProperty("email.discrepancy.url"));
				params.put("org", (entry.getKey().equalsIgnoreCase("EMADMIN")? "NCI Orgs without IC Coordinators": entry.getKey()));
				params.put("monthYear", df.format(new Date()));
				send(entMaintProperties.getProperty("email.from"), "discrepancyEmail", email,
					parse(entMaintProperties.getProperty("email.cc")), null,
					entMaintProperties.getProperty("email.discrepancy.subject"), params); // Pass list of discrepancy accounts
			}
		}
	}


	private List<DiscrepancyEmailAccountVO> populateDiscrepancyAccounts(List<PortfolioAccountVO> list) {
		
		List<DiscrepancyEmailAccountVO> accounts = new ArrayList<DiscrepancyEmailAccountVO>();
		for (PortfolioAccountVO account : list) {
			DiscrepancyEmailAccountVO entry = new DiscrepancyEmailAccountVO();
			entry.setFullName(account.getFullName());
			entry.setImpaciiUserIdNetworkId((account.getImpaciiUserId() == null ? "" : account.getImpaciiUserId()
					+ "/")
					+ (account.getNihNetworkId() == null ? "" : account.getNihNetworkId()));
			entry.setNedOrgPath(account.getNedOrgPath());
			entry.setSecondaryOrgText(account.getSecondaryOrgText());
			entry.setCreatedByFullName(account.getCreatedByFullName());
			entry.setCreatedDate((account.getCreatedDate() == null ? "" : new SimpleDateFormat("MM/dd/yyyy")
					.format(account.getCreatedDate())));
			StringBuffer sbu = new StringBuffer();
			for(String dis : account.getAccountDiscrepancies()){
				EmDiscrepancyTypesT disVw = (EmDiscrepancyTypesT) lookupService.getListObjectByCode(ApplicationConstants.DISCREPANCY_TYPES_LIST,dis);
				if(disVw.getShortDescrip() != null){
					if(!disVw.getCode().equalsIgnoreCase("LNAMEDIFF")) {
						if (!sbu.toString().isEmpty())
							sbu.append(",");
						sbu.append(disVw.getShortDescrip());
					}
				}
			}
			
			entry.setDiscrepancyText(sbu.toString());
			if(!StringUtils.isEmpty(entry.getDiscrepancyText()))
				accounts.add(entry);
		}
		return accounts;
	}

	private List<DiscrepancyEmailAccountVO> populateI2eDiscrepancyAccounts(List<PortfolioI2eAccountVO> list) {
		
		List<DiscrepancyEmailAccountVO> accounts = new ArrayList<DiscrepancyEmailAccountVO>();
		for (PortfolioI2eAccountVO account : list) {
			DiscrepancyEmailAccountVO entry = new DiscrepancyEmailAccountVO();
			entry.setFullName(account.getFullName());
			entry.setImpaciiUserIdNetworkId((account.getNihNetworkId() == null ? "" : account.getNihNetworkId()));
			entry.setNedOrgPath(account.getNedOrgPath());
			entry.setSecondaryOrgText("");
			entry.setCreatedByFullName(account.getLastUpdByFullName());
			entry.setCreatedDate((account.getCreatedDate() == null ? "" : new SimpleDateFormat("MM/dd/yyyy")
					.format(account.getCreatedDate())));
			StringBuffer sbu = new StringBuffer();
			for(String dis : account.getAccountDiscrepancies()){
				EmDiscrepancyTypesT disVw = (EmDiscrepancyTypesT) lookupService.getListObjectByCode(ApplicationConstants.DISCREPANCY_TYPES_LIST,dis);
				if(disVw.getShortDescrip() != null){
					if (!sbu.toString().isEmpty())
						sbu.append(",");
					sbu.append(disVw.getShortDescrip());
				}
			}
			entry.setDiscrepancyText(sbu.toString());
			accounts.add(entry);
		}
		return accounts;
	}

}
