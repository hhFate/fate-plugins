package cn.reinforce.utils.juhe;

import cn.reinforce.utils.GsonUtil;
import cn.reinforce.utils.HttpClientUtil;
import cn.reinforce.utils.juhe.entity.IP;
import cn.reinforce.utils.juhe.entity.JuheResponse;
import cn.reinforce.utils.juhe.entity.Sms;
import cn.reinforce.utils.juhe.entity.Weather;
import com.google.gson.Gson;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 聚合数据工具类
 *
 * @author hhFate
 * @create 2016-09-06
 * @since 1.0.0
 */
public class JHUtils {

    private static Logger LOG = Logger.getLogger(JHUtils.class);

    private JHUtils() {
    }

    /**
     * 短信发送
     *
     * @param mobile     手机号
     * @param code       验证码
     * @param timeout    超时时间，分钟
     * @param templeteId 短信模版id
     * @return 错误码
     */
    public static JuheResponse sendSms(String mobile, String code, int timeout, String key, int templeteId) {
        List<NameValuePair> pair = new ArrayList<>();
        pair.add(new BasicNameValuePair("mobile", mobile));
        pair.add(new BasicNameValuePair("tpl_id", Integer.toString(templeteId)));
        pair.add(new BasicNameValuePair("tpl_value", "#code#=" + code + "&#hour#=" + timeout));
        pair.add(new BasicNameValuePair("key", key));

        String result = HttpClientUtil.post("http://v.juhe.cn/sms/send", pair).getResult();
        Gson gson = new Gson();

        JuheResponse response = gson.fromJson(result, JuheResponse.class);

        Sms sms = gson.fromJson(gson.toJson(response.getResult()), Sms.class);

        response.setSms(sms);

        return response;
    }

    /**
     * 评论提醒通知
     *
     * @param mobile     手机号
     * @param title
     * @param content
     * @param key
     * @param templeteId 短信模版id
     * @return 错误码
     */
    public static Sms sendCommentSms(String mobile, String title, String content, String key, int templeteId) {
        List<NameValuePair> pair = new ArrayList<>();
        pair.add(new BasicNameValuePair("mobile", mobile));
        pair.add(new BasicNameValuePair("tpl_id", Integer.toString(templeteId)));
        pair.add(new BasicNameValuePair("tpl_value", "#title#=" + title + "&#content#=" + content));
        pair.add(new BasicNameValuePair("key", key));

        String result = HttpClientUtil.post("http://v.juhe.cn/sms/send", pair).getResult();
        Gson gson = new Gson();
        JuheResponse response = gson.fromJson(result, JuheResponse.class);

        Sms sms = gson.fromJson(gson.toJson(response.getResult()), Sms.class);

        response.setSms(sms);
        return sms;
    }

    /**
     * 友链申请提醒
     *
     * @param mobile
     * @param site
     * @param key
     * @param templeteId
     * @return
     */
    public static Sms sendFriendLinkSms(String mobile, String site, String key, int templeteId) {
        List<NameValuePair> pair = new ArrayList<>();
        pair.add(new BasicNameValuePair("mobile", mobile));
        pair.add(new BasicNameValuePair("tpl_id", Integer.toString(templeteId)));
        pair.add(new BasicNameValuePair("tpl_value", "#site#=" + site));
        pair.add(new BasicNameValuePair("key", key));

        String result = HttpClientUtil.post("http://v.juhe.cn/sms/send", pair).getResult();
        Gson gson = new Gson();
        JuheResponse response = gson.fromJson(result, JuheResponse.class);

        Sms sms = gson.fromJson(gson.toJson(response.getResult()), Sms.class);

        response.setSms(sms);
        return sms;
    }


    /**
     * 全国天气查询
     *
     * @param ip
     * @return
     * @throws JSONException
     */
    public static Weather weather(String ip, String key) {
        List<NameValuePair> pair = new ArrayList<>();
        pair.add(new BasicNameValuePair("ip", ip));
        pair.add(new BasicNameValuePair("key", key));
        String result = HttpClientUtil.post("http://v.juhe.cn/weather/ip", pair).getResult();
        Gson gson = new Gson();
        JuheResponse response = gson.fromJson(result, JuheResponse.class);

        Weather weather = gson.fromJson(gson.toJson(response.getResult()), Weather.class);

        response.setWeather(weather);
        return weather;
    }

    /**
     * 天气查询，weather.com
     *
     * @param ip
     * @return
     * @throws JSONException
     */
//    public static Map<String, Object> weather2(String ip, String key) {
//        Map<String, Object> map = new HashMap<>();
//
//        String city = ip(ip).getIp().getArea();
//        city = "苏州市";
//        try {
//            city = URLEncoder.encode(city, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//        String result = HttpClientUtil.get("http://op.juhe.cn/onebox/weather/query?cityname=" + city + "&key="+key).getResult();
//        System.out.println(result);
//        Gson gson = GsonUtil.getGson();
//        JuheResponse response = gson.fromJson(result, JuheResponse.class);
//
//        System.out.println(response.getResult());
//
//        return map;
//    }
    public static JuheResponse ip(String ip, String key) {
        String result = HttpClientUtil.get("http://apis.juhe.cn/ip/ip2addr?ip=" + ip + "&key=" + key).getResult();
        Gson gson = GsonUtil.getGson();
        JuheResponse response = gson.fromJson(result, JuheResponse.class);

        IP ip1 = gson.fromJson(gson.toJson(response.getResult()), IP.class);
        System.out.println(ip1);
        response.setIp(ip1);
        return response;
    }

    /**
     * 获取手机号的运营商
     * @param mobile
     * @return
     */
    public static JuheResponse getMobile(String mobile) {
        String result = HttpClientUtil.get("http://apis.juhe.cn/mobile/get?phone=" + mobile + "&key=c8836f697b27f0f0ad9ded9cdba95a19").getResult();
        Gson gson = GsonUtil.getGson();
        JuheResponse response = gson.fromJson(result, JuheResponse.class);

        System.out.println(response.getResult());
        return response;
    }

    public static void main(String[] args) {
        ip("117.82.202.59", "acd6918ff5a3b87ed1119c0df7d61408");
    }
}
