package ua.metro.mobileapp.datamodel;

public class CatalogData {
	
	int catalogId;
	String catalogTitle;
	String catalogDateEnd;
	String catalogImage;
	
	public int getCatalogId() {
		return catalogId;
	}
	public void setCatalogId(int catalogId) {
		this.catalogId = catalogId;
	}
	
	public String getCatalogTitle() {
		return catalogTitle;
	}
	public void setCatalogTitle(String catalogTitle) {
		this.catalogTitle = catalogTitle;
	}
	
	public String getCatalogDateEnd() {
		return catalogDateEnd;
	}
	public void setCatalogDateEnd(String catalogDateEnd) {
		this.catalogDateEnd = catalogDateEnd;
	}
	
	public String getCatalogUrlImage() {
		return catalogImage;
	}
	public void setCatalogUrlImage(String catalogImage) {
		this.catalogImage = catalogImage;
	}
}
