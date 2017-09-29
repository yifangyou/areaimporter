package io.proinsight.areaimporter;
/**
 * 自动从国家统计局官网抓取行政区划代码和名称，重新组合输出格式如下：
行政区划代码,行政区划名称,所属级别,上级行政区划代码,上级行政区划名称,省级行政区划代码,省级行政区划名称,市级行政区划代码,市级行政区划名称,县级行政区划代码,县级行政区划名称
适合于动态更新行政区划库
 * @author yifangyou
 * @date  2017-09-29 14:36:22
 */
public enum AreaLevel {
	LEVEL_UNKNOWN,  //0
	LEVEL_PROVINCE, //1
	LEVEL_CITY, //2
	LEVEL_COUNTY, //3
}
