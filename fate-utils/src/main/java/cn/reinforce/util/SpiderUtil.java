package cn.reinforce.util;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 搜索引擎工具
 * @author 幻幻Fate
 * @create 2016-07-28
 * @since 1.0.0
 */
public class SpiderUtil {
    public static final String[] spider_key = {
            "googlebot",
            "mediapartners-google",
            "feedfetcher-Google",
            "ia_archiver",
            "iaarchiver",
            "sqworm",
            "baiduspider",
            "msnbot",
            "yodaobot",
            "yahoo! slurp;",
            "yahoo! slurp china;",
            "yahoo",
            "iaskspider",
            "sogou spider",
            "sogou web spider",
            "sogou push spider",
            "sogou orion spider",
            "sogou-test-spider",
            "sogou+head+spider",
            "sohu",
            "sohu-search",
            "Sosospider",
            "Sosoimagespider",
            "JikeSpider",
            "360spider",
            "qihoobot",
            "tomato bot",
            "bingbot",
            "youdaobot",
            "askjeeves/reoma",
            "manbot",
            "robozilla",
            "MJ12bot",
            "HuaweiSymantecSpider",
            "Scooter",
            "Infoseek",
            "ArchitextSpider",
            "Grabber",
            "Fast",
            "ArchitextSpider",
            "Gulliver",
            "Lycos",
            "YisouSpider",
            "YYSpider",
            "360JK",//360监控
            "Baidu-YunGuanCe-Bot",//百度云观测
            "Baidu-YunGuanCe-SLABot",
            "Alibaba",//阿里云云盾对机子的安全扫描
            "AhrefsBot",
            "Renren Share Slurp",//人人分享
            "SinaWeiboBot",//新浪微博分享
            "masscan"//Masscan 扫描器

    };
    public static final String[] spider_name = {
            "Google",
            "Google Adsense",
            "Google",
            "Alexa",
            "Alexa",
            "AOL",
            "Baidu",
            "MSN",
            "Yodao",
            "Yahoo!",
            "Yahoo! China",
            "Yahoo!",
            "Iask",
            "Sogou",
            "Sogou",
            "Sogou",
            "Sogou",
            "Sogou",
            "Sogou",
            "Sohu",
            "Sohu",
            "SoSo",
            "SoSo",
            "Jike",
            "360",
            "360",
            "Tomato",
            "Bing",
            "Yodao",
            "Ask",
            "Bing",
            "Dmoz",
            "MJ12",
            "HuaweiSymantec",
            "AltaVista",
            "Infoseek",
            "Excite",
            "DirectHit",
            "Fast",
            "WebCrawler",
            "NorthernLight",
            "Lycos",
            "宜搜搜索",
            "YYSpider"
    };

    /**
     * 判断是否是蜘蛛来访
     * @param request
     * @return
     */
    public static Map<String, Object> getSpider(HttpServletRequest request){
        Map<String, Object> map = new HashMap<>();
        String agent = request.getHeader("User-Agent");
        if(StringUtils.isEmpty(agent)){
            map.put("isSpider", false);
            map.put("isScanner", false);
            map.put("spiderName","模拟访问");
            return map;
        }
        int i = 0;
        String spiderName = null;
        for(i=0;i<spider_key.length;i++){
            if(agent.trim().toLowerCase().contains(spider_key[i].toLowerCase()))
                break;
        }
        map.put("isSpider", false);
        map.put("isScanner", false);
        if(i<spider_name.length){
            map.put("isSpider", true);
            spiderName = spider_name[i];
        }else if(i<spider_key.length){
            map.put("isScanner", true);
            spiderName = spider_key[i];
        }
        map.put("spiderName",spiderName);
        return map;
    }
}
