package cn.reinforce.util;

import cn.reinforce.util.entity.GeetestLib;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * 极验验证的二次验证
 * @author hhFate
 * @create 2016-08-17
 * @since 1.0.0
 */
public class GeetestUtil {

    private static Logger LOG = Logger.getLogger(GeetestUtil.class);

    public static int validate(String captchaId, String privateKey, HttpServletRequest request){

        GeetestLib gtSdk = new GeetestLib(captchaId, privateKey);

        String challenge = request.getParameter(GeetestLib.fn_geetest_challenge);
        String validate = request.getParameter(GeetestLib.fn_geetest_validate);
        String seccode = request.getParameter(GeetestLib.fn_geetest_seccode);

        //从session中获取gt-server状态
        int gt_server_status_code = (Integer) request.getSession().getAttribute(gtSdk.gtServerStatusSessionKey);

        int gtResult = 0;

        //从session中获取userid
        String userid = (String)request.getSession().getAttribute("userid");

        if (gt_server_status_code == 1) {
            //gt-server正常，向gt-server进行二次验证
            gtResult = gtSdk.enhencedValidateRequest(challenge, validate, seccode, userid);
        } else {
            // gt-server非正常情况下，进行failback模式验证
            LOG.info("failback:use your own server captcha validate");
            gtResult = gtSdk.failbackValidateRequest(challenge, validate, seccode);
        }
        return gtResult;
    }
}
