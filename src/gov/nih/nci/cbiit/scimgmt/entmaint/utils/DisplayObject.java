package gov.nih.nci.cbiit.scimgmt.entmaint.utils;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("Display")
public class DisplayObject {
	@XStreamImplicit(itemFieldName = "Tab")
	private List<Tab> tabs = new ArrayList<Tab>();

	/**
	 * @return the tabs
	 */
	public List<Tab> getTabs() {
		return tabs;
	}

	/**
	 * @param tabs the tabs to set
	 */
	public void setTabs(List<Tab> tabs) {
		this.tabs = tabs;
	}
	
	
}
