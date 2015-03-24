package gov.nih.nci.cbiit.scimgmt.entmaint.utils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;

public class PaginatedListImpl<T> implements PaginatedList {

	/** default page size */
	private static int DEFAULT_PAGE_SIZE = 10;

	/** current page index, starts at 0 */
	private int index;

	/** number of results per page */
	private int pageSize;

	/** total results (records, not pages) */
	private int fullListSize;

	/** list of results in the current page */
	private List list;

	/** default sorting order */
	private SortOrderEnum sortDirection = SortOrderEnum.ASCENDING;

	/** sort criteria (sorting property name) */
	private String sortCriterion;

	public PaginatedListImpl() {
	}

	/**
	 * Factory-style constructor. Initializes properties with a request
	 * attributes
	 */
	public PaginatedListImpl(HttpServletRequest request) {
		sortCriterion = request.getParameter("sort");
		sortDirection = "desc".equals(request.getParameter("dir")) ? SortOrderEnum.DESCENDING
				: SortOrderEnum.ASCENDING;
		pageSize = DEFAULT_PAGE_SIZE;
		String page = request.getParameter("page");
		index = page == null ? 0 : Integer.parseInt(page) - 1;
		String size = request.getParameter("size");
		fullListSize = size == null ? 0 : Integer.parseInt(size);
	}

	@Override
	public int getFullListSize() {
		return fullListSize;
	}

	@Override
	public List<T> getList() {
		return list;
	}

	@Override
	public int getObjectsPerPage() {
		return pageSize;
	}

	@Override
	public int getPageNumber() {
		return index + 1;
	}

	@Override
	public String getSearchId() {
		// unimplemented
		return null;
	}

	@Override
	public String getSortCriterion() {
		return sortCriterion;
	}

	@Override
	public SortOrderEnum getSortDirection() {
		return sortDirection;
	}

	/**
	 * @return A readable description of this instance state for logs
	 */
	public String toString() {
		return "PageResponse { index = " + index + ", pageSize = " + pageSize + ", total = " + fullListSize + " }";
	}

	public int getFirstRecordIndex() {
		return index * pageSize;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setList(List<T> results) {
		this.list = results;
	}

	public void setTotal(int total) {
		this.fullListSize = total;
	}

	public int getTotalPages() {
		return (int) Math.ceil(((double) fullListSize) / pageSize);
	}

	public String getSqlSortDirection() {
		return SortOrderEnum.DESCENDING.equals(sortDirection) ? "desc" : "asc";
	}

	public void setSortCriterion(String sortCriterion) {
		this.sortCriterion = sortCriterion;
	}

	public void setSortDirection(SortOrderEnum sortDirection) {
		this.sortDirection = sortDirection;
	}
}
