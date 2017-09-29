package io.proinsight.areaimporter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;

/**
 * 自动从国家统计局官网抓取行政区划代码和名称，重新组合输出格式如下：
行政区划代码,行政区划名称,所属级别,上级行政区划代码,上级行政区划名称,省级行政区划代码,省级行政区划名称,市级行政区划代码,市级行政区划名称,县级行政区划代码,县级行政区划名称
适合于动态更新行政区划库
 * @author yifangyou
 * @date  2017-09-29 14:36:22
 */
public class App 
{
	/**
	 * 国家统计局，行政区域更新列表
	 * */
	String sourceUrl="http://www.stats.gov.cn/tjsj/tjbz/xzqhdm/";
	
    public static void main( String[] args )
    {
    	App app=new App();
    	String lastestUrl=app.getLatestUrl();
        if(lastestUrl==null){
        	System.out.println( "找不到最新行政区域页面URL");
        	return;
        }
    	System.out.println( "lastestUrl=" +lastestUrl);
    	//解析网页获取结构化列表
    	List<AreaBean> res=app.parsePage(lastestUrl);
    	if(res==null){
    		System.out.println( "无法下载行政区域页面");
    		return;
    	}

    	//输出结果
    	String separator=",";
    	for(AreaBean bean:res){
    		StringBuffer sb=new StringBuffer(128);
    		sb.append(bean.getCode()).append(separator);
    		sb.append(bean.getName()).append(separator);
    		sb.append(bean.getLevel().ordinal()).append(separator);
    		sb.append(bean.getParentCode()).append(separator);
    		sb.append(bean.getParentName()).append(separator);
    		sb.append(bean.getProvinceCode()).append(separator);
    		sb.append(bean.getProvinceName()).append(separator);
    		sb.append(bean.getCityCode()).append(separator);
    		sb.append(bean.getCountyCode()).append(separator);
    		sb.append(bean.getCountyName());
    		System.out.println(sb.toString());
    	}
    }
    
    /**
     * 获取行政区域更新列表第一个
     * */
    public String getLatestUrl(){
    	try {
			Document doc = Jsoup.connect(sourceUrl).get();
			Element latestUrl = doc.select(".center_list_contlist a[href]").first();
			if(latestUrl!=null){
				//获得绝对路径
				return latestUrl.attr("abs:href");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 解析省市区树状结构，转化为列表
     * */
    public List<AreaBean> parsePage(String pageUrl){
    
    	try {
			Document doc = Jsoup.connect(pageUrl).get();
			Elements lines = doc.select(".TRS_PreAppend .MsoNormal");
			if(lines!=null){
				List<AreaBean> result=new ArrayList<AreaBean>();
				Map<Integer,AreaBean> beanMap=new HashMap<Integer,AreaBean>();
				
				for(int i=0;i<lines.size();i++){
					Element line=lines.get(i);
					//去掉全角空格
					char nbsp = 0x00a0; 
					String lineText=line.text().replaceAll("\u3000","").replace(nbsp, ' ');  
					
					String[]fields=lineText.split("\\s+");
                    if(fields.length!=2){
                    	System.out.println("错误行["+lineText+"]");
                    	continue;
                    }
                    String s=StringUtils.trim(fields[0]);
//                    System.out.println(this.bytes2HexString(s.getBytes()));
                    Integer code=Integer.parseInt(StringUtils.trim(fields[0]));
                    String nameStr=StringUtils.trim(fields[1]);
                    //编码不为6位或者名字为空
                    if(code<100000||code>=1000000||nameStr.length()==0){
                    	System.out.println("错误编码["+lineText+"]");
                    	continue;
                    }
                    AreaBean bean=new AreaBean();
                    beanMap.put(code, bean);
                    result.add(bean);
                    bean.setName(nameStr);
                    bean.setCode(code,beanMap);
//					System.out.println(JSON.toJSONString(bean));
				}
				return result;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return null;
    }
    
    public String bytes2HexString(byte[] b) {  
        StringBuffer result = new StringBuffer();  
        String hex;  
        for (int i = 0; i < b.length; i++) {  
            hex = Integer.toHexString(b[i] & 0xFF);  
            if (hex.length() == 1) {  
                hex = '0' + hex;  
            }  
            result.append(hex.toUpperCase());  
        }  
        return result.toString();  
    }  
}
