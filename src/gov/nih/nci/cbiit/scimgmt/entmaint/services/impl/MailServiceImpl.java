package gov.nih.nci.cbiit.scimgmt.entmaint.services.impl;

import gov.nih.nci.cbiit.scimgmt.entmaint.constants.ApplicationConstants;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmDiscrepancyTypesT;
import gov.nih.nci.cbiit.scimgmt.entmaint.hibernate.EmPortfolioRolesVw;
import gov.nih.nci.cbiit.scimgmt.entmaint.security.NciUser;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.AdminService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.I2eAuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.I2ePortfolioService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2AuditService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.Impac2PortfolioService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.LookupService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.MailService;
import gov.nih.nci.cbiit.scimgmt.entmaint.services.UserRoleService;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EmAppUtil;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.EntMaintProperties;
import gov.nih.nci.cbiit.scimgmt.entmaint.utils.PaginatedListImpl;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.AuditSearchVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.DiscrepancyEmailAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.EmAuditsVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioAccountVO;
import gov.nih.nci.cbiit.scimgmt.entmaint.valueObject.PortfolioI2eAccountVO;

import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
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
	protected Impac2AuditService impac2AuditService;
	@Autowired
	protected I2eAuditService i2eAuditService;
	@Autowired
	protected AdminService adminService;
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
	 * @see gov.nih.nci.cbiit.scimgmt.entmaint.service.MailService#getVelocityEngine()
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
					String logTo = null;

					final String env = entMaintProperties.getProperty("ENVIRONMENT", "dev");

					if (env.toLowerCase().startsWith("prod")) {
						helper.setTo(to);
						logTo = Arrays.toString(to);

						if (cc != null) {
							helper.setCc(cc);
						}
						if (bcc != null) {
							helper.setBcc(bcc);
						}
					} else {
						subject = "[" + env.toUpperCase() + "] " + subject;
						subject += " {TO: " + StringUtils.join(to, ',') + "} {CC: " + StringUtils.join(cc, ',') + "}";
						final String[] overrideAddrs = parse(entMaintProperties
								.getProperty("email.bcc"));						
						helper.setTo(overrideAddrs);
						logTo = Arrays.toString(overrideAddrs);
					}

					helper.setText(body, true);
					helper.setSubject(subject);

					helper.setFrom(from);

					log.info("invoking mailSender");
					log.info("Sending mail to " + logTo + " subject: " + subject);
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
	
	/**
	 * Sends Monthly Discrepancy account email to the IC Coordinators
	 */
	@Override
	public void sendDiscrepancyEmail() {
		
		// Check if Audit is active
		EmAuditsVO emAuditsVO = adminService.retrieveCurrentOrLastAuditVO();
		if(ApplicationConstants.AUDIT_STATE_CODE_ENABLED.equals(EmAppUtil.getCurrentAuditState(emAuditsVO))) {
			log.info("=====> Monthly Reminder emails to IC coordinators is turned off. Audit is active (enabled).");
			return;
		}
		
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
			if(StringUtils.equalsIgnoreCase(nciUser.getCurrentUserRole(), "EMADMIN")) {
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
		
		// Get accounts that are excluded from last audit
		HashSet<String> excludedAccounts = null;
		HashSet<String> excludedI2eAccounts = null;
		if(emAuditsVO != null) {
			Long auditId = emAuditsVO.getId();
			excludedAccounts = impac2AuditService.retrieveExcludedFromAuditAccounts(auditId);
			excludedI2eAccounts = i2eAuditService.retrieveExcludedFromAuditAccounts(auditId);
		} else {
			excludedAccounts = new HashSet<String>();
			excludedI2eAccounts = new HashSet<String>();
		}
		
		// Fetch distinct Org regardless of whether IC Coordinator exists and add to the OrgMap.
		List<String> distinctOrgs = impac2PortfolioService.getOrgsWithIcCoordinator();
		for(String org: distinctOrgs) {
			if(!orgMapEmail.containsKey(org)) {
				orgMapEmail.put(org,new ArrayList<String>());
				orgMapName.put(org,new ArrayList<String>());
				orgMapEmail.get(org).add(entMaintProperties.getProperty("email.discrepancy.cc"));
				orgMapName.get(org).add("NCI IMPACII Primary IC Coordinator");
			}
		}
		
		// For each Org, send the email
		for (Map.Entry<String,List<String>> entry : orgMapEmail.entrySet()) {
			
			String[] email = entry.getValue().toArray(new String[entry.getValue().size()]);
			String dear = StringUtils.join(orgMapName.get(entry.getKey()), ", ");
			if (orgMapName.get(entry.getKey()).size() >= 2) {
				StringBuffer sb = new StringBuffer(dear);
		        sb.replace(dear.lastIndexOf(", "), dear.lastIndexOf(", ") + 1, ", and ");
		        dear = sb.toString();
			}
			
			PaginatedListImpl<PortfolioAccountVO> portfolioAccounts = new PaginatedListImpl<PortfolioAccountVO>();	
			PaginatedListImpl<PortfolioI2eAccountVO> portfolioI2eAccounts = new PaginatedListImpl<PortfolioI2eAccountVO>();	
			PaginatedListImpl<PortfolioAccountVO> portfolioInactiveAccounts = new PaginatedListImpl<PortfolioAccountVO>();;
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
			
			//Retrieve accounts that are deactivated due to 120 days in the previous month.
			searchVO.setCategory(ApplicationConstants.PORTFOLIO_CATEGORY_INACTIVE);
			portfolioInactiveAccounts = impac2PortfolioService.searchImpac2Accounts(portfolioInactiveAccounts, searchVO, true);
			// Populate the account list to pass to email template
			List<DiscrepancyEmailAccountVO> accounts = populateDiscrepancyAccounts(portfolioAccounts.getList(), excludedAccounts);
			accounts.addAll(populateI2eDiscrepancyAccounts(portfolioI2eAccounts.getList(), excludedI2eAccounts));
			Collections.sort(accounts);
			
			List<DiscrepancyEmailAccountVO> inActiveAccounts = populateInActiveAccounts(portfolioInactiveAccounts.getList());
			
			if(accounts.size() > 0 ||inActiveAccounts.size() > 0) {
				DateFormat df = new SimpleDateFormat("MMMM yyyy");
				final Map<String, Object> params = new HashMap<String, Object>();
				params.put("accounts", accounts);
				params.put("dear", dear);
				params.put("url", entMaintProperties.getProperty("email.discrepancy.url"));
				params.put("org", (entry.getKey().equalsIgnoreCase("EMADMIN")? "NCI Orgs without IC Coordinators": entry.getKey()));
				params.put("monthYear", df.format(new Date()));
				String cc = entMaintProperties.getProperty("email.discrepancy.cc");
				if (entry.getKey().equalsIgnoreCase("OD OM OGA")) {
					cc = cc.concat(";" + entMaintProperties.getProperty("email.oga"));
				}
				
				//Adding Inactive accounts list to params
				params.put("inActiveAccounts", inActiveAccounts);
				if (portfolioInactiveAccounts.getList() != null && inActiveAccounts.size() > 0) {
					params.put("displayInactiveTable", "Y");
				} else {
					params.put("displayInactiveTable", "N");
				}
				Calendar cal = Calendar.getInstance();
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				cal.set(Calendar.DATE, 1);
				cal.add(Calendar.MONTH, -1);
				params.put("beginDt", dateFormat.format(cal.getTime()));
				cal.add(Calendar.MONTH, 1);
				cal.add(Calendar.DAY_OF_MONTH, -1);
				params.put("endDt", dateFormat.format(cal.getTime()));
				
				send(entMaintProperties.getProperty("email.from"), "discrepancyEmail", email,
					parse(cc), null,
					entMaintProperties.getProperty("email.discrepancy.subject"), params); // Pass list of discrepancy accounts
				
			}
		}
	}

	/**
	 * Populate the account information necessary for discrepancy email table.
	 * 
	 * Filter out the following accounts:
	 * - Last Name Diff Discrepancy
	 * - Accounts that were marked "Excluded from Audit" from the last audit
	 * 
	 * @param list
	 * @return
	 */
	private List<DiscrepancyEmailAccountVO> populateDiscrepancyAccounts(List<PortfolioAccountVO> list, HashSet<String> excluded) {
		
		List<DiscrepancyEmailAccountVO> accounts = new ArrayList<DiscrepancyEmailAccountVO>();
		for (PortfolioAccountVO account : list) {
			DiscrepancyEmailAccountVO entry = new DiscrepancyEmailAccountVO();
			entry.setFullName(account.getFullName());
			entry.setNihNetworkId((account.getNihNetworkId() == null ? "" : account.getNihNetworkId()));
			entry.setNedOrgPath(account.getNedOrgPath());
			entry.setSecondaryOrgText(account.getSecondaryOrgText());
			entry.setCreatedByFullName(account.getCreatedByFullName());
			entry.setCreatedDate((account.getCreatedDate() == null ? "" : new SimpleDateFormat("MM/dd/yyyy")
					.format(account.getCreatedDate())));
			entry.setSystem("IMPAC II");
			StringBuffer sbu = new StringBuffer();
			for(String dis : account.getAccountDiscrepancies()){
				EmDiscrepancyTypesT disVw = (EmDiscrepancyTypesT) lookupService.getListObjectByCode(ApplicationConstants.DISCREPANCY_TYPES_LIST,dis);
				if(disVw.getShortDescrip() != null){
					if(!disVw.getCode().equalsIgnoreCase("LNAMEDIFF")) {
						if (!sbu.toString().isEmpty())
							sbu.append("<br>");
						sbu.append(disVw.getShortDescrip());
					}
				}
			}
			entry.setDiscrepancyText(sbu.toString()) ;
			if (account.getStatusCode() != null ) {
				String accStatus = lookupService.getAppLookupByCode(
						ApplicationConstants.APP_LOOKUP_STATUS_CODE, String.valueOf(account.getStatusCode())).getDescription();
				entry.setAccountStatus(accStatus != null ? accStatus : " ");
			} else {
				entry.setAccountStatus(" ");
			}
			if(!StringUtils.isEmpty(entry.getDiscrepancyText()) && !excluded.contains(account.getImpaciiUserId()))
				accounts.add(entry);
		}
		return accounts;
	}
	
	private List<DiscrepancyEmailAccountVO> populateInActiveAccounts(List<PortfolioAccountVO> list) {
		List<DiscrepancyEmailAccountVO> accounts = new ArrayList<DiscrepancyEmailAccountVO>();
		for (PortfolioAccountVO account : list) {
			DiscrepancyEmailAccountVO entry = new DiscrepancyEmailAccountVO();
			entry.setFullName(account.getFullName());
			entry.setImpaciiUserIdNetworkId(account.getImpaciiUserId() +"<br>"+ account.getNihNetworkId());
			entry.setNedOrgPath(account.getNedOrgPath());
			List<EmPortfolioRolesVw> roles = account.getAccountRoles();
			if(roles == null || roles.size() == 0){
				entry.setApplicationRole("");
			} else {
				String applicationRole = "";
				for (EmPortfolioRolesVw roleVw : roles) {
					applicationRole = applicationRole + roleVw.getRoleName() + "<br>";
				}
				entry.setApplicationRole(applicationRole);
			}
			accounts.add(entry);
		}
		return accounts;
	}

	/**
	 * Populate the account information necessary for discrepancy email table.
	 * 
	 * Filter out the following accounts:
	 * - Last Name Diff Discrepancy
	 * - Accounts that were marked "Excluded from Audit" from the last audit
	 * 
	 * @param list
	 * @return
	 */
	private List<DiscrepancyEmailAccountVO> populateI2eDiscrepancyAccounts(List<PortfolioI2eAccountVO> list, HashSet<String> excluded) {
		
		List<DiscrepancyEmailAccountVO> accounts = new ArrayList<DiscrepancyEmailAccountVO>();
		for (PortfolioI2eAccountVO account : list) {
			DiscrepancyEmailAccountVO entry = new DiscrepancyEmailAccountVO();
			entry.setFullName(account.getFullName());
			entry.setNihNetworkId((account.getNihNetworkId() == null ? "" : account.getNihNetworkId()));
			entry.setNedOrgPath(account.getNedOrgPath());
			entry.setSecondaryOrgText("");
			entry.setCreatedByFullName(account.getLastUpdByFullName());
			entry.setCreatedDate((account.getCreatedDate() == null ? "" : new SimpleDateFormat("MM/dd/yyyy")
					.format(account.getCreatedDate())));
			entry.setSystem("I2E");
			StringBuffer sbu = new StringBuffer();
			for(String dis : account.getAccountDiscrepancies()){
				EmDiscrepancyTypesT disVw = (EmDiscrepancyTypesT) lookupService.getListObjectByCode(ApplicationConstants.DISCREPANCY_TYPES_LIST,dis);
				if(disVw.getShortDescrip() != null){
					if (!sbu.toString().isEmpty())
						sbu.append("<br>");
					sbu.append(disVw.getShortDescrip());
				}
				if(dis.equalsIgnoreCase("I2EONLY")) {
					entry.setSystem("IMPAC II or I2E");
				}
			}
			entry.setDiscrepancyText(sbu.toString());
			
			entry.setAccountStatus(" ");
			
			if(!excluded.contains(entry.getNihNetworkId()))
				accounts.add(entry);
		}
		return accounts;
	}
}
