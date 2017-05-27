import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.Bucket;

import java.util.List;

/**
 * @author Fate
 * @create 2017/5/23
 */
public class OSSTest {

    public static void main(String[] args) {
        String endpoint = "http://oss.aliyuncs.com";
// accessKey请登录https://ak-console.aliyun.com/#/查看
        String accessKeyId = "BSDvNTpEiJC4alTw";
        String accessKeySecret = "mSDWbTzur3PRZyz4eFCeVQRoEvY1BB";

//        Aliyun aliyun = Aliyun.INSTANCE;
//
//        aliyun.init(accessKeyId, accessKeySecret);
//        aliyun.initOSS(endpoint, "", "", "");

// 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
// 列举bucket
        List<Bucket> buckets = ossClient.listBuckets();
        for (Bucket bucket : buckets) {
            System.out.println(" - " + bucket.getName());
        }
// 关闭client
        ossClient.shutdown();
    }
}
