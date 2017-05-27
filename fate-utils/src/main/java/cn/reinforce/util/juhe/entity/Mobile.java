package cn.reinforce.util.juhe.entity;

import com.google.gson.annotations.Expose;

/**
 * 手机号码归属地返回实体
 * @author Fate
 * @create 2017/3/15
 */
public class Mobile {

    /**
     * 省份
     */
    @Expose
    private String province;

    /**
     * 城市
     */
    @Expose
    private String city;

    /**
     * 区号
     */
    @Expose
    private int areacode;

    /**
     * 邮编
     */
    @Expose
    private int zip;

    /**
     * 运营商
     */
    @Expose
    private String company;

    /**
     * 卡类型
     */
    @Expose
    private String card;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getAreacode() {
        return areacode;
    }

    public void setAreacode(int areacode) {
        this.areacode = areacode;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
