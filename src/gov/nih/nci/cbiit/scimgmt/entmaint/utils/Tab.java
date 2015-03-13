package gov.nih.nci.cbiit.scimgmt.entmaint.utils;

import java.io.ObjectStreamException;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Tab")
public class Tab {
	@XStreamAlias("type")
	private String type;
	@XStreamAlias("columnName")
	private String columnName;
	@XStreamAlias("property")
	private String property;
	@XStreamAlias("sort")
	private String sort;
	@XStreamAlias("display")
	private String display;
	@XStreamAlias("export")
	private String export;
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * @return the columnName
	 */
	public String getColumnName() {
		return columnName;
	}
	
	/**
	 * @param columnName the columnName to set
	 */
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	/**
	 * @return the property
	 */
	public String getProperty() {
		return property;
	}
	
	/**
	 * @param property the property to set
	 */
	public void setProperty(String property) {
		this.property = property;
	}
	
	/**
	 * @return the sort
	 */
	public String getSort() {
		return sort;
	}
	
	/**
	 * @param sort the sort to set
	 */
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	/**
	 * @return the display
	 */
	public String getDisplay() {
		return display;
	}
	
	/**
	 * @param display the display to set
	 */
	public void setDisplay(String display) {
		this.display = display;
	}
	
	/**
	 * @return the export
	 */
	public String getExport() {
		return export;
	}
	
	/**
	 * @param export the export to set
	 */
	public void setExport(String export) {
		this.export = export;
	}
	
	
	/**
	 * Set default values. This method is called during the deserialization
	 * process and here we can check if a field value is null. If it is,
	 * then it means that the tag for that field is not present.
	 *  
	 * @return
	 * @throws ObjectStreamException
	 */
	public Object readResolve() throws ObjectStreamException {
		if(display == null) {
			display = "true";
		}
		
		if(export == null) {
			export = "true";
		}
		
		return this;
	}
	
}
