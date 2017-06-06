package ua.metro.mobileapp.datamodel;

import ua.metro.mobileapp.R;
import android.content.Context;

public class GdsData {

		String gdsKey;
		int gdsId;
		String gdsTitle;
		Double gdsPrice;
		String gdsPriceUah;
		String gdsPriceCoins;
		Double gdsPriceOld;
		String gdsPriceOldUah;
		String gdsPriceOldCoins;
		String gdsUrlImage;
		String gdsDetails1;
		String gdsDetails2;
		String gdsDesc;
		int catalogId;
		String catalogTitle;
		String discountType;
		String discountTitle;
		String discountTitle_;
		String discount;
		String discountImgUrl;
		Boolean expired;
		
		public GdsData(){
		}
		
		public GdsData(int gdsId, String gdsTitle, Double gdsPrice, Double gdsPriceOld, 
						String gdsUrlImage, String gdsDesc){
			this.gdsId = gdsId;
			this.gdsTitle = gdsTitle;
			this.gdsPrice = gdsPrice;
			this.gdsPriceOld = gdsPriceOld;
			this.gdsUrlImage = gdsUrlImage;
			this.gdsDesc = gdsDesc;
		}
		public String getGdsKey() {
			return gdsKey;
		}
		public void setGdsKey(String gdsKey) {
			this.gdsKey = gdsKey;
		}
		
		public int getGdsId() {
			return gdsId;
		}
		public void setGdsId(int gdsId) {
			this.gdsId = gdsId;
		}
		
		public String getGdsTitle() {
			return gdsTitle;
		}
		public void setGdsTitle(String gdsTitle) {
			this.gdsTitle = gdsTitle;
		}
		
		public Double getGdsPrice() {
			return gdsPrice;
		}
		public String getGdsPriceUah() {
			return gdsPriceUah;
		}
		public String getGdsPriceCoins() {
			return gdsPriceCoins;
		}
		public void setGdsPrice(double gdsPrice) {
			int uah;
			int coins;
			
			this.gdsPrice = gdsPrice;
			
			uah = (int) gdsPrice;
			coins = (int) ((gdsPrice - uah)*100);
			
			this.gdsPriceUah = String.valueOf(uah);
			this.gdsPriceCoins = String.format("%02d",coins);
		}
		
		public Double getGdsPriceOld() {
			return gdsPriceOld;
		}
		public String getGdsPriceOldUah() {
			return gdsPriceOldUah;
		}
		public String getGdsPriceOldCoins() {
			return gdsPriceOldCoins;
		}
		public void setGdsPriceOld(double gdsPriceOld) {
			int uah;
			int coins;
			
			this.gdsPriceOld = gdsPriceOld;
			
			uah = (int) gdsPriceOld;
			coins = (int) ((gdsPriceOld - uah)*100);
			
			this.gdsPriceOldUah = String.valueOf(uah);
			this.gdsPriceOldCoins = String.format("%02d",coins);
		}
		
		public String getGdsUrlImage() {
			return gdsUrlImage;
		}
		public void setGdsUrlImage(String gdsUrlImage) {
			this.gdsUrlImage = gdsUrlImage;
		}
		
		public String getGdsDesc() {
			return gdsDesc;
		}
		public void setGdsDesc(String gdsDesc) {
			this.gdsDesc = gdsDesc;
		}

		public String getGdsDetails1() {
			return gdsDetails1;
		}
		public void setGdsDetails1(String gdsDetails1) {
			this.gdsDetails1 = gdsDetails1;
		}
		
		public String getGdsDetails2() {
			return gdsDetails2;
		}
		public void setGdsDetails2(String gdsDetails2) {
			this.gdsDetails2 = gdsDetails2;
		}
	
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
		
		public String getDiscountType() {
			return discountType;
		}
		public void setDiscountType(String discountType) {
			this.discountType = discountType;
		}
		
		public String getDiscount() {
			return discount;
		}
		public void setDiscount(String discount) {
			this.discount = discount;
		}
		
		public String getDiscountTitle() {
			return discountTitle;
		}
		public void setDiscountTitle(String discountTitle) {
			this.discountTitle = discountTitle;
		}
		
		public String getDiscountTitle_(Context mContext) {
			this.discountTitle_ = this.discountTitle;
			if ((getDiscountType()!=null && getDiscountType().length()>0 && getDiscountType().equals(mContext.getString(R.string.discount))) &&
				(getDiscountTitle()!=null && getDiscountTitle().length()>0 && getDiscountTitle().equals(mContext.getString(R.string.percent))) &&
				(getDiscount()!=null && getDiscount().length()>0)){
				this.discountTitle_ = this.discountTitle_.replace("{0}", getDiscount());
			}
			return discountTitle_;
		}
		
		public int getDiscountImgUrl(Context mContext){
			String strImgUrl = mContext.getString(R.string.discount);
			
			//hit и regular зелёным, discount красным, action оранжевым
			if(getDiscountType()!=null && getDiscountType().length()>0){
				  if (getDiscountType().equals(mContext.getString(R.string.hit)))
					  strImgUrl = strImgUrl + mContext.getString(R.string._green);
				  
				  if (getDiscountType().equals(mContext.getString(R.string.regular)))
					  strImgUrl = strImgUrl + mContext.getString(R.string._green);
					  
				  if (getDiscountType().equals(mContext.getString(R.string.discount)))
					  strImgUrl = strImgUrl + mContext.getString(R.string._red);
				  
				  if (getDiscountType().equals(mContext.getString(R.string.action)))
					  strImgUrl = strImgUrl + mContext.getString(R.string._orange);
			}
			
			//хит-цена", "-20%", "2 по цене 1"
			if(getDiscountTitle()!=null && getDiscountTitle().length()>0){
				  if (getDiscountTitle().equals(mContext.getString(R.string.hit_price)) ||
					  getDiscountTitle().equals(mContext.getString(R.string.percent)) ||
					  getDiscountTitle().contains(mContext.getString(R.string._price_of_)))
					  strImgUrl = strImgUrl + mContext.getString(R.string._short);
				  else
					  strImgUrl = strImgUrl + mContext.getString(R.string._long);
			}else
				strImgUrl = strImgUrl + mContext.getString(R.string._long);
			
			int resID = mContext.getResources().getIdentifier(strImgUrl , "drawable", mContext.getPackageName());
			
			return resID;
		}
		
		public Boolean getExpired() {
			return expired;
		}
		public void setExpired(Boolean expired) {
			this.expired = expired;
		}
}