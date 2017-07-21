package cn.reinforce.util.aliyun;

import com.aliyun.opensearch.OpenSearchClient;
import com.aliyun.opensearch.SearcherClient;
import com.aliyun.opensearch.sdk.dependencies.com.google.common.collect.Lists;
import com.aliyun.opensearch.sdk.generated.OpenSearch;
import com.aliyun.opensearch.sdk.generated.commons.OpenSearchClientException;
import com.aliyun.opensearch.sdk.generated.commons.OpenSearchException;
import com.aliyun.opensearch.sdk.generated.search.Config;
import com.aliyun.opensearch.sdk.generated.search.Order;
import com.aliyun.opensearch.sdk.generated.search.SearchFormat;
import com.aliyun.opensearch.sdk.generated.search.SearchParams;
import com.aliyun.opensearch.sdk.generated.search.Sort;
import com.aliyun.opensearch.sdk.generated.search.SortField;
import com.aliyun.opensearch.sdk.generated.search.Suggest;
import com.aliyun.opensearch.sdk.generated.search.general.SearchResult;
import com.aliyun.opensearch.search.SearchParamsBuilder;
import com.aliyun.opensearch.search.SearchResultDebug;
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
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于阿里云产品的操作，单例模式
 *
 * @author Fate
 * @create 2017/3/20
 */
public enum Aliyun {
    INSTANCE;

    private final Logger LOG = Logger.getLogger(Aliyun.class);

    private String accessKeyId;
    private String accessKeySecret;

    //OSS部分
    private String ossUrl;
    private String ossBucket;
    private String ossRegion;
    private int ossNet;
    private String ossEndpoint;
    private OSSClient ossClient;


    // open search
    private String appName;
    private OpenSearch openSearch;
    private OpenSearchClient openSearchClient;
    private SearcherClient searcherClient;
    private Suggest suggest;

    //sms
    private IAcsClient client;

    /**
     * 初始化Key和Secret
     *
     * @param accessKeyId
     * @param accessKeySecret
     */
    public void init(String accessKeyId, String accessKeySecret) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
    }

    /**
     * 初始化OSS配置
     *
     * @param endpoint    OSS地址
     * @param ossUrl      CName的URL
     * @param ossBucket   bucket
     */
    public void initOSS(String ossRegion, int ossNet, String ossUrl, String ossBucket) {
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(5000);
        conf.setMaxErrorRetry(10);
//        conf.setSupportCname(true);
        this.ossBucket = ossBucket;
        this.ossUrl = ossUrl;
        this.ossNet = ossNet;
        this.ossEndpoint = ossRegion + (ossNet==2?"-internal":"") + ".aliyuncs.com";
        ossClient = new OSSClient("http://" + ossEndpoint, accessKeyId, accessKeySecret, conf);
    }

    /**
     * 初始化OpenSearch
     *
     * @param endpoint URL
     * @param appName  应用名称
     */
    public void initOpenSearch(String endpoint, String appName) {
        Map<String, Object> opts = new HashMap<>();
        this.appName = appName;
        // 这里的host需要根据访问应用详情页中提供的的API入口来确定

        openSearch = new OpenSearch(accessKeyId, accessKeySecret, endpoint);
        openSearchClient = new OpenSearchClient(openSearch);
        String suggestName = "nana";
        suggest = new Suggest(suggestName);

    }

    public void initSms() {
        try {
            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", "Sms", "sms.aliyuncs.com");
            client = new DefaultAcsClient(profile);
        } catch (ClientException e) {
            e.printStackTrace();
        }
    }

    public OSSClient getOSSClient() {
        return ossClient;
    }

    public OpenSearchClient getOpenSearchClient() {
        return openSearchClient;
    }

    /**
     * 搜索
     *
     * @param keyword  关键字
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    public String search(String keyword, int pageNum, int pageSize) throws OpenSearchClientException, OpenSearchException {
        //创建SearcherClient对象，并以OpenSearchClient对象作为构造参数
        searcherClient = new SearcherClient(openSearchClient);
        //定义Config对象，用于设定config子句参数，指定应用名，分页，数据返回格式等等
        Config config = new Config(Lists.newArrayList(appName));

        config.setStart((pageNum - 1) * pageSize);
        config.setHits(pageSize);
        //设置返回格式为fulljson格式
        config.setSearchFormat(SearchFormat.JSON);
        // 创建参数对象
        SearchParams searchParams = new SearchParams(config);
        // 指定搜索的关键词，这里要指定在哪个索引上搜索，如果不指定的话默认在使用“default”索引（索引字段名称是您在您的数据结构中的“索引字段列表”中对应字段。），若需多个索引组合查询，需要在setQuery处合并，否则若设置多个setQuery子句，则后面的子句会替换前面子句
        searchParams.setQuery("default:'" + keyword + "'");
        //设置查询过滤条件
//        searchParams.setFilter("id>0");
        //创建sort对象，并设置二维排序
        Sort sort = new Sort();
        //降序
        sort.addToSortFields(new SortField("publish_date", Order.DECREASE));
        //若id相同则以RANK相关性算分升序
//        sort.addToSortFields(new SortField("RANK", Order.INCREASE));
        //执行查询语句返回数据对象
        SearchResult searchResult = searcherClient.execute(searchParams);
        //以字符串返回查询数据
        String result = searchResult.getResult();

        return result;
    }

    /**
     * 获取OpenSearch的Suggest
     *
     * @param query 关键字
     * @return
     */
    public Object suggest(String query) {
        //定义Config对象，用于设定config子句参数，分页或数据返回格式，指定应用名等等
        Config config = new Config(Lists.newArrayList(appName));
        config.setStart(0);
        config.setHits(8);
        //设置返回格式为json，3.1.2 sdk只支持返回xml和json格式，暂不支持返回fulljson类型，3.1.3版本支持fulljson
        config.setSearchFormat(SearchFormat.JSON);
        // 设置查询子句，若需多个索引组合查询，需要setQuery处合并，否则若设置多个setQuery后面的会替换前面查询
        SearchParams searchParams = new SearchParams(config);
        searchParams.setSuggest(suggest);//searchParams对象添加下拉对象
        searchParams.setQuery(query);//在控制台中下拉列表中如果能搜出"nihao"，这里也能够搜出
        // SearchParams的工具类，提供了更为便捷的操作
        SearchParamsBuilder paramsBuilder = SearchParamsBuilder.create(searchParams);
        // 执行返回查询结果
        SearchResult searchResult;
        try {
            searchResult = searcherClient.execute(paramsBuilder);
            String result = searchResult.getResult();
            JSONObject obj = new JSONObject(result);
            //判断执行返回是否成功，如果报错执行下面代码，否者输出正确查询内容
            if (obj.has("errors")) {
                JSONArray jsonarr = obj.getJSONArray("errors");
                System.out.println("error code:" + jsonarr.getJSONObject(0).get("code"));
                System.out.println("error message:" + jsonarr.getJSONObject(0).get("message"));
            } else {
                // 输出查询结果
                System.out.println("output:" + obj.toString());
                //个别用户可能需要debug请求地址信息
                SearchResultDebug srd = searcherClient.executeDebug(searchParams);
                System.out.println(srd.getRequestUrl().toString());
            }
        } catch (OpenSearchException e) {
            e.printStackTrace();
        } catch (OpenSearchClientException e) {
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

    public String getOssRegion() {
        return ossRegion;
    }

    public int getOssNet() {
        return ossNet;
    }

    public IAcsClient getClient() {
        return client;
    }
}
