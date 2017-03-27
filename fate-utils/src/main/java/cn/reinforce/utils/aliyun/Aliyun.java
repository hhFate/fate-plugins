package cn.reinforce.utils.aliyun;

import com.aliyun.opensearch.CloudsearchClient;
import com.aliyun.opensearch.CloudsearchSearch;
import com.aliyun.opensearch.CloudsearchSuggest;
import com.aliyun.opensearch.object.KeyTypeEnum;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于阿里云产品的操作，单例模式
 * @author Fate
 * @create 2017/3/20
 */
public enum  Aliyun {
    INSTANCE;

    private final Logger LOG = Logger.getLogger(Aliyun.class);
    
    private String accessKeyId;
    private String accessKeySecret;

    //OSS部分
    private String ossUrl;
    private String ossBucket;
    private String ossEndpoint;
    private OSSClient ossClient;


    // open search
    private String appName;
    private CloudsearchClient openSearchClient;
    private CloudsearchSearch search;
    private CloudsearchSuggest suggest;

    //sms
    private IAcsClient client;

    /**
     * 初始化Key和Secret
     * @param accessKeyId
     * @param accessKeySecret
     */
    public void init(String accessKeyId, String accessKeySecret) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
    }

    /**
     * 初始化OSS配置
     * @param endpoint OSS地址
     * @param ossUrl CName的URL
     * @param ossBucket bucket
     * @param ossEndpoint
     */
    public void initOSS(String endpoint, String ossUrl, String ossBucket, String ossEndpoint) {
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(5000);
        conf.setMaxErrorRetry(10);
        this.ossBucket = ossBucket;
        this.ossUrl = ossUrl;
        this.ossEndpoint = ossEndpoint;
        ossClient = new OSSClient("http://" + endpoint, accessKeyId, accessKeySecret, conf);
    }

    /**
     * 初始化OpenSearch
     * @param endpoint URL
     * @param appName 应用名称
     */
    public void initOpenSearch(String endpoint, String appName) {
        Map<String, Object> opts = new HashMap<>();
        this.appName = appName;
        // 这里的host需要根据访问应用详情页中提供的的API入口来确定
        try {
            openSearchClient = new CloudsearchClient(accessKeyId, accessKeySecret, endpoint, opts, KeyTypeEnum.ALIYUN);
            search = new CloudsearchSearch(openSearchClient);
            String suggestName = "nana";
            suggest = new CloudsearchSuggest(appName, suggestName, openSearchClient);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void initSms() {
        try {
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms",  "sms.aliyuncs.com");
            client = new DefaultAcsClient(profile);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public OSSClient getOSSClient() {
        return ossClient;
    }

    public CloudsearchClient getOpenSearchClient() {
        return openSearchClient;
    }

    /**
     * 搜索
     * @param keyword 关键字
     * @param pageNum 页码
     * @param pageSize 每页条数
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String search(String keyword, int pageNum, int pageSize)
            throws ClientProtocolException, IOException {
        search.clear();
        // 添加指定搜索的应用：
        search.addIndex(appName);
        // 指定搜索的关键词，这里要指定在哪个索引上搜索，如果不指定的话默认在使用“default”索引（索引字段名称是您在您的数据结构中的“索引到”字段。）
        search.setQueryString("default:'" + keyword + "'");
        // 分页
        search.addCustomConfig("start", (pageNum - 1) * pageSize);
        search.addCustomConfig("hit", pageSize);
        // 指定搜索返回的格式。
        search.setFormat("json");
        // 设定过滤条件
        // search.addFilter("price>10");
        // 设定排序方式 + 表示正序 - 表示降序
        search.addSort("publish_date", "-");
        // 返回搜索结果
        return search.search();
    }

    /**
     * 获取OpenSearch的Suggest
     * @param query 关键字
     * @return
     */
    public Object suggest(String query){
        try {
            suggest.setHit(10);
            suggest.setQuery(query);
            String result = suggest.search();

            JSONObject jsonResult = new JSONObject(result);
            List<String> suggestions = new ArrayList<String>();

            if (!jsonResult.has("errors")) {
                JSONArray itemsJsonArray = (JSONArray) jsonResult.get("suggestions");
                for (int i = 0; i < itemsJsonArray.length(); i++){
                    JSONObject item = (JSONObject) itemsJsonArray.get(i);
                    suggestions.add(item.getString("suggestion"));
                }
                Map<String,Object> ret = new HashMap<String,Object>();
                ret.put("result",suggestions);
                ret.put("status","OK");
                return ret;
            } else {
                LOG.info("获取搜索候选词失败");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public String getOssUrl() {
        return ossUrl;
    }

    public String getOssBucket() {
        return ossBucket;
    }

    public String getOssEndpoint() {
        return ossEndpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public IAcsClient getClient() {
        return client;
    }
}
