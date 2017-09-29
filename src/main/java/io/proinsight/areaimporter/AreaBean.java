package io.proinsight.areaimporter;

import java.util.Map;
/**
 * 自动从国家统计局官网抓取行政区划代码和名称，重新组合输出格式如下：
行政区划代码,行政区划名称,所属级别,上级行政区划代码,上级行政区划名称,省级行政区划代码,省级行政区划名称,市级行政区划代码,市级行政区划名称,县级行政区划代码,县级行政区划名称
适合于动态更新行政区划库
 * @author yifangyou
 * @date  2017-09-29 14:36:22
 */
public class AreaBean {
	/**
	 * 行政区域编码
	 * */
	Integer code;
	
	/**
	 * 行政区域名字
	 * */
	String name;
	
	/**
	 * 上一级行政区域编码
	 * */
	Integer parentCode=-1;
	
	/**
	 * 上一级行政区域名字
	 * */
	String parentName="";
	
	/**
	 * 行政区域级别
	 * */
	AreaLevel level=AreaLevel.LEVEL_UNKNOWN;
	
	
	/**
	 * 省级行政区域编码
	 * */
	Integer provinceCode=-1;
	
	/**
	 * 省级行政区域名字
	 * */
	String provinceName="";
	
	
	/**
	 * 省级行政区域编码
	 * */
	Integer cityCode=-1;
	
	/**
	 * 省级行政区域名字
	 * */
	String cityName="";
	
	
	/**
	 * 县级行政区域编码
	 * */
	Integer countyCode=-1;
	
	/**
	 * 县级行政区域名字
	 * */
	String countyName="";

	/**
	 * @return the code
	 */
	public Integer getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(Integer code,Map<Integer,AreaBean> beanMap) {
		this.code = code;
		switch(this.getLevel()){
			case LEVEL_PROVINCE:{
				//中国
				this.parentCode=100000;
				this.setParentName(this.getNameByCode(beanMap, this.parentCode));
				this.provinceCode=this.toProvinceCode(code);
				this.setProvinceName(this.getNameByCode(beanMap, this.provinceCode));
			}
			break;
			case LEVEL_CITY:{
				this.parentCode=this.toProvinceCode(code);
				this.setParentName(this.getNameByCode(beanMap, this.parentCode));
				this.provinceCode=this.toProvinceCode(code);
				this.setProvinceName(this.getNameByCode(beanMap, this.provinceCode));
				this.cityCode=this.toCityCode(code);
				this.setCityName(this.getNameByCode(beanMap, this.cityCode));
			}
			break;
			case LEVEL_COUNTY:{
				this.parentCode=this.toCityCode(code);
				this.setParentName(this.getNameByCode(beanMap, this.parentCode));
				this.provinceCode=this.toProvinceCode(code);
				this.setProvinceName(this.getNameByCode(beanMap, this.provinceCode));
				this.cityCode=this.toCityCode(code);
				this.setCityName(this.getNameByCode(beanMap, this.cityCode));
				this.countyCode=this.code;
				this.setCountyName(this.getNameByCode(beanMap, this.countyCode));
			}
			break;
		}
		
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the parentCode
	 */
	public Integer getParentCode() {
		return parentCode;
	}

	/**
	 * @param parentCode the parentCode to set
	 */
	public void setParentCode(Integer parentCode) {
		this.parentCode = parentCode;
	}

	/**
	 * @return the level
	 */
	public AreaLevel getLevel() {
		if(code<100000){
			return AreaLevel.LEVEL_UNKNOWN;
		}
		//省一级
		if(code % 10000 == 0){
			return AreaLevel.LEVEL_PROVINCE;
		}
		//市一级
		if(code % 100 == 0){
			return AreaLevel.LEVEL_CITY;
		}
		//县一级
		return AreaLevel.LEVEL_COUNTY;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(AreaLevel level) {
		this.level = level;
	}

	/**
	 * @return the provinceCode
	 */
	public Integer getProvinceCode() {
		return provinceCode;
	}

	/**
	 * @param provinceCode the provinceCode to set
	 */
	public void setProvinceCode(Integer provinceCode) {
		this.provinceCode = provinceCode;
	}

	/**
	 * @return the provinceName
	 */
	public String getProvinceName() {
		return provinceName;
	}

	/**
	 * @param provinceName the provinceName to set
	 */
	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	/**
	 * @return the cityCode
	 */
	public Integer getCityCode() {
		return cityCode;
	}

	/**
	 * @param cityCode the cityCode to set
	 */
	public void setCityCode(Integer cityCode) {
		this.cityCode = cityCode;
	}

	/**
	 * @return the cityName
	 */
	public String getCityName() {
		return cityName;
	}

	/**
	 * @param cityName the cityName to set
	 */
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * @return the countyCode
	 */
	public Integer getCountyCode() {
		return countyCode;
	}

	/**
	 * @param countyCode the countyCode to set
	 */
	public void setCountyCode(Integer countyCode) {
		this.countyCode = countyCode;
	}

	/**
	 * @return the countyName
	 */
	public String getCountyName() {
		return countyName;
	}

	/**
	 * @param countyName the countyName to set
	 */
	public void setCountyName(String countyName) {
		this.countyName = countyName;
	}
	
	
	public Integer toProvinceCode(Integer code){
		return code/10000*10000;
	}
	
	public Integer toCityCode(Integer code){
		return code/100*100;
	}

	/**
	 * @return the parentName
	 */
	public String getParentName() {
		return parentName;
	}

	
	public String getNameByCode(Map<Integer,AreaBean> beanMap,Integer code) {
		AreaBean bean=beanMap.get(code);
		if(bean!=null){
			return bean.getName();
		}else{
			return "";
		}
	}

	/**
	 * @param parentName the parentName to set
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
}
