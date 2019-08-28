package com.nahuo.quicksale.oldermodel;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.List;

public class AuthInfoModel implements Serializable{
    @Expose
    private AIModel AuthInfo;

    public void setAuthInfo(AIModel AuthInfo){
        this.AuthInfo = AuthInfo;
    }
    public AIModel getAuthInfo(){
        return this.AuthInfo;
    }

    public class Images implements Serializable{
        @Expose
        private String Url;
        @Expose
        private int TypeID;

        public void setUrl(String Url){
            this.Url = Url;
        }
        public String getUrl(){
            return this.Url;
        }
        public void setTypeID(int TypeID){
            this.TypeID = TypeID;
        }
        public int getTypeID(){
            return this.TypeID;
        }

    }

    public class AIModel implements Serializable {
        @Expose
        private int UserID;
        @Expose
        private String UserName;
        @Expose
        private String Statu;
        @Expose
        private int StatuID;
        @Expose
        private String Address;
        @Expose
        private String Area;
        @Expose
        private String City;
        @Expose
        private String Mobile;
        @Expose
        private String Province;
        @Expose
        private String RealName;
        @Expose
        private String ShopName;
        @Expose
        private String Street;
        @Expose
        private String Dimension;
        @Expose
        private String Longitude;
        @Expose
        private String AuthResult;
        @Expose
        private String HouseNumber;
        @Expose
        private String ProvinceCode;
        @Expose
        private String CityCode;
        @Expose
        private String AreaCode;
        @Expose
        private String BusinessArea;
        @Expose
        private String MjStyle;

        @Expose
        private List<Images> Images ;

        public void setUserID(int UserID){
            this.UserID = UserID;
        }
        public int getUserID(){
            return this.UserID;
        }
        public void setUserName(String UserName){
            this.UserName = UserName;
        }
        public String getUserName(){
            return this.UserName;
        }
        public void setStatu(String Statu){
            this.Statu = Statu;
        }
        public String getStatu(){
            return this.Statu;
        }
        public void setAddress(String Address){
            this.Address = Address;
        }
        public String getAddress(){
            return this.Address;
        }
        public void setArea(String Area){
            this.Area = Area;
        }
        public String getArea(){
            return this.Area;
        }
        public void setCity(String City){
            this.City = City;
        }
        public String getCity(){
            return this.City;
        }
        public void setMobile(String Mobile){
            this.Mobile = Mobile;
        }
        public String getMobile(){
            return this.Mobile;
        }
        public void setProvince(String Province){
            this.Province = Province;
        }
        public String getProvince(){
            return this.Province;
        }
        public void setRealName(String RealName){
            this.RealName = RealName;
        }
        public String getRealName(){
            return this.RealName;
        }
        public void setShopName(String ShopName){
            this.ShopName = ShopName;
        }
        public String getShopName(){
            return this.ShopName;
        }
        public void setStreet(String Street){
            this.Street = Street;
        }
        public String getStreet(){
            return this.Street;
        }
        public void setDimension(String Dimension){
            this.Dimension = Dimension;
        }
        public String getDimension(){
            return this.Dimension;
        }
        public void setLongitude(String Longitude){
            this.Longitude = Longitude;
        }
        public String getLongitude(){
            return this.Longitude;
        }
        public void setAuthResult(String AuthResult){
            this.AuthResult = AuthResult;
        }
        public String getAuthResult(){
            return this.AuthResult;
        }
        public void setHouseNumber(String HouseNumber){
            this.HouseNumber = HouseNumber;
        }
        public String getHouseNumber(){
            return this.HouseNumber;
        }
        public void setProvinceCode(String ProvinceCode){
            this.ProvinceCode = ProvinceCode;
        }
        public String getProvinceCode(){
            return this.ProvinceCode;
        }
        public void setCityCode(String CityCode){
            this.CityCode = CityCode;
        }
        public String getCityCode(){
            return this.CityCode;
        }
        public void setAreaCode(String AreaCode){
            this.AreaCode = AreaCode;
        }
        public String getAreaCode(){
            return this.AreaCode;
        }
        public void setBusinessArea(String BusinessArea){
            this.BusinessArea = BusinessArea;
        }
        public String getBusinessArea(){
            return this.BusinessArea;
        }
        public void setImages(List<Images> Images){
            this.Images = Images;
        }
        public List<Images> getImages(){
            return this.Images;
        }

        public int getStatuID() {
            return StatuID;
        }

        public void setStatuID(int statuID) {
            StatuID = statuID;
        }

        public String getMjStyle() {
            return MjStyle;
        }

        public void setMjStyle(String mjStyle) {
            MjStyle = mjStyle;
        }
    }
}
