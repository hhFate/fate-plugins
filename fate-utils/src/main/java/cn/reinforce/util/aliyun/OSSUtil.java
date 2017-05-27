package cn.reinforce.util.aliyun;

import cn.reinforce.util.MD5;
import cn.reinforce.util.commons.Constants;
import com.aliyun.oss.model.AbortMultipartUploadRequest;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.BucketReferer;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.CompleteMultipartUploadResult;
import com.aliyun.oss.model.CopyObjectResult;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.ListMultipartUploadsRequest;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.ListPartsRequest;
import com.aliyun.oss.model.MultipartUploadListing;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.PartListing;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * OSS文件存储的工具类
 *
 * @author Fate
 * @create 2017/3/20
 */
public class OSSUtil {

    private final static Logger LOG = Logger.getLogger(OSSUtil.class);

    // 设置每块为 200K
    private static final long PARTSIZE1 = 1024L * 200;

    // 设置每块为 1M
    private static final long PARTSIZE2 = 1024L * 1024;

    // 设置每块为 2M
    private static final long PARTSIZE3 = 1024L * 1024 * 2;

    // 设置每块为 5M
    private static final long PARTSIZE4 = 1024L * 1024 * 5;

    // 设置每块为 10M
    private static final long PARTSIZE5 = 1024L * 1024 * 20;

    /**
     * 小文件上传
     *
     * @param bucketName
     * @param clientFile
     * @param folder
     * @return
     */
    public static PutObjectResult simpleUpload(String bucketName,
                                               MultipartFile clientFile, String folder, String fileName) {
        fileName = folder + fileName;
        PutObjectResult result = null;
        try {
            InputStream content = clientFile.getInputStream();
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(clientFile.getSize());
            meta.setContentType(clientFile.getContentType());
            result = Aliyun.INSTANCE.getOSSClient().putObject(bucketName, fileName, content, meta);// 会自动关闭流？
        } catch (IOException e) {
            LOG.error(e);
        }
        return result;
    }

    public static PutObjectResult simpleUpload(String bucketName, File file, String folder, String fileName) {
        fileName = folder + fileName;
        PutObjectResult result = null;
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(file.getTotalSpace());
        result = Aliyun.INSTANCE.getOSSClient().putObject(bucketName, fileName, file, meta);// 会自动关闭流？
        return result;
    }


    public static PutObjectResult byteUpload(String bucketName, byte[] file, String folder, String fileName) {
        fileName = folder + fileName;
        PutObjectResult result = null;
        fileName = folder + fileName;
        result = Aliyun.INSTANCE.getOSSClient().putObject(bucketName, fileName, new ByteArrayInputStream(file));
        return result;
    }


    public static PutObjectResult uplaodOnlineFileToOSS(String url, String bucketName, String folder) {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        fileName = folder + fileName;
        PutObjectResult result = null;
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet get = new HttpGet(url);
            get.addHeader("Content-Type", "text/html;charset=UTF-8");
            get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
            CloseableHttpResponse response = httpclient.execute(get);
            HttpEntity responseEntity = response.getEntity();

            InputStream content = responseEntity.getContent();
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(responseEntity.getContentLength());
            result = Aliyun.INSTANCE.getOSSClient().putObject(bucketName, fileName, content, meta);
        } catch (IOException e) {
            LOG.error(e);
        }
        return result;
    }


    public static PutObjectResult headIconUpload(String bucketName,
                                                 File file, String folder, String fileName) {
        fileName = folder + fileName;
        PutObjectResult result = null;
        InputStream content = null;
        try {
            content = new FileInputStream(file);
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(file.length());
            result = Aliyun.INSTANCE.getOSSClient().putObject(bucketName, fileName, content, meta);// 会自动关闭流？
            content.close();
        } catch (IOException e) {
            LOG.error(e);
        } finally {
            if (content != null) {
                try {
                    content.close();
                } catch (IOException e) {
                    LOG.error(e);
                }
            }
        }
        return result;
    }

    /**
     * 分块上传
     *
     * @param bucketName
     * @param partFile
     * @param folder
     * @return
     */

    public static Map<String, Object> multipartUpload(final String bucketName,
                                                      MultipartFile partFile, String folder, String filename) {
        ExecutorService pool = Executors.newCachedThreadPool();
        long partSize;
        if (partFile.getSize() <= PARTSIZE2)
            partSize = PARTSIZE1;
        else if (partFile.getSize() <= PARTSIZE2 * 50)
            partSize = PARTSIZE2;
        else if (partFile.getSize() <= PARTSIZE3 * 50)
            partSize = PARTSIZE3;
        else if (partFile.getSize() <= PARTSIZE4 * 100)
            partSize = PARTSIZE4;
        else
            partSize = PARTSIZE5;
        InputStream content;
        final String fileName = folder + filename;
        Map<String, Object> map = new HashMap<>();

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(partFile.getSize());
        meta.setContentType(partFile.getContentType());

        // 开始Multipart Upload
        InitiateMultipartUploadRequest initiateMultipartUploadRequest = new InitiateMultipartUploadRequest(
                bucketName, fileName, meta);
        InitiateMultipartUploadResult initiateMultipartUploadResult = Aliyun.INSTANCE.getOSSClient()
                .initiateMultipartUpload(initiateMultipartUploadRequest);
        final String uploadId = initiateMultipartUploadResult.getUploadId();

        // 计算分块数目
        int partCount = (int) (partFile.getSize() / partSize);
        if (partFile.getSize() % partSize != 0) {
            partCount++;
        }
        // List<ProgressEntity> list = (List<ProgressEntity>)
        // session.getAttribute("progressList");
        // int i;
        // for(i=0;i<list.size();i++){
        // if(list.get(i).getFileName().contains(partFile.getOriginalFilename())){
        // break;
        // }
        // }
        final ProgressEntity progressEntity = new ProgressEntity();
        progressEntity.setBucketName(bucketName);
        progressEntity.setFileName(fileName);
        progressEntity.setFilePath(folder);
        progressEntity.setUploadId(uploadId);
        progressEntity.setPartsAll(partCount);
        // 新建一个List保存每个分块上传后的ETag和PartNumber
        final List<PartETag> partETags = new ArrayList<PartETag>();
        Runnable runnable = () -> {
            Tag:
            while (true) {
                try {
                    if (progressEntity.getState() != ProgressEntity.upload_state_complete) {
                        ListPartsRequest listPartsRequest = new ListPartsRequest(
                                progressEntity.getBucketName(),
                                progressEntity.getFileName(),
                                progressEntity.getUploadId());
                        if (listPartsRequest != null) {
                            progressEntity
                                    .setState(ProgressEntity.upload_state_OSS_uploading);
                            // 获取上传的所有Part信息
                            PartListing partListing = Aliyun.INSTANCE.getOSSClient()
                                    .listParts(listPartsRequest);

                            progressEntity.setPartsRead(partListing
                                    .getParts().size());
                            if (partListing.getParts().size() == progressEntity
                                    .getPartsAll()) {
                                progressEntity
                                        .setState(ProgressEntity.upload_state_complete);
                                completeMultipartUpload(
                                        progressEntity.getBucketName(),
                                        fileName, partETags,
                                        progressEntity.getUploadId());
                                break Tag;
                            }
                        }
                    }
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    LOG.error(e);
                } catch (Exception e) {
                    LOG.error(e);
                }
            }

        };

        pool.execute(runnable);
        LOG.info("开始上传");

        try {
            for (int i = 0; i < partCount; i++) {
                // 获取文件流
                FileInputStream fis = (FileInputStream) partFile
                        .getInputStream();

                // 跳到每个分块的开头
                long skipBytes = partSize * i;
                fis.skip(skipBytes);

                // 计算每个分块的大小
                long size = partSize < partFile.getSize() - skipBytes ? partSize
                        : partFile.getSize() - skipBytes;

                String oraginalTag = MD5.getMd5ByFile(fis, skipBytes, size);
                // 创建UploadPartRequest，上传分块
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setKey(fileName);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setInputStream(fis);
                uploadPartRequest.setPartSize(size);
                uploadPartRequest.setPartNumber(i + 1);
                UploadPartResult uploadPartResult = Aliyun.INSTANCE.getOSSClient().uploadPart(uploadPartRequest);
                if (uploadPartResult.getPartETag().getETag().equals(oraginalTag)) {
                    // 将返回的PartETag保存到List中。
                    LOG.info(uploadPartResult.getPartETag().getETag());
                    partETags.add(uploadPartResult.getPartETag());
                } else {
                    i--;
                }
                // 关闭文件
                fis.close();
            }
            map.put(Constants.SUCCESS, true);
        } catch (IOException e) {
            LOG.error(e);
            abortMultipartUpload(fileName, uploadId);
            map.put(Constants.SUCCESS, false);
            map.put(Constants.MSG, "分块上传失败");
        }

        return map;
    }

    /**
     * 完成上传
     *
     * @param bucketName
     * @param fileName
     * @param partETags
     * @param uploadId
     * @return
     */

    public static boolean completeMultipartUpload(String bucketName, String fileName,
                                                  List<PartETag> partETags, String uploadId) {
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(
                bucketName, fileName, uploadId, partETags);

        // 完成分块上传
        CompleteMultipartUploadResult completeMultipartUploadResult = Aliyun.INSTANCE.getOSSClient()
                .completeMultipartUpload(completeMultipartUploadRequest);

//		FileEntity fileEntity = fileEntityDao.loadByUploadId(uploadId);
//		fileEntity.setState(ProgressEntity.upload_state_complete);
//		fileEntityDao.update(fileEntity);
        // 打印Object的ETag
        System.out.println(completeMultipartUploadResult.getETag());

//		completeMultipartUploadResult.getETag()

        return true;
    }

    /**
     * 取消上传
     *
     * @param fileName
     * @param uploadId
     */

    public static void abortMultipartUpload(String fileName,
                                            String uploadId) {
        AbortMultipartUploadRequest abortMultipartUploadRequest = new AbortMultipartUploadRequest(Aliyun.INSTANCE.getOssBucket(), fileName, uploadId);

        // 取消分块上传
        Aliyun.INSTANCE.getOSSClient().abortMultipartUpload(abortMultipartUploadRequest);
    }


    public static String generatePresignedUrl(String key, Date expiration) {
        String url = Aliyun.INSTANCE.getOSSClient().generatePresignedUrl(Aliyun.INSTANCE.getOssBucket(), key, expiration).toString();
        if (Aliyun.INSTANCE.getOssUrl() != null)
            url = url.replace(Aliyun.INSTANCE.getOssBucket() + "." + Aliyun.INSTANCE.getOssEndpoint(), Aliyun.INSTANCE.getOssUrl());
        return url;
    }


    public static void deleteObject(String key) throws UnsupportedEncodingException {
        key = java.net.URLDecoder.decode(key, "utf-8");
        key = key.replace("|", "/").replace("*", "+");
        String fileName = key.substring(key.lastIndexOf("/") + 1);
        String filePath = key.substring(0, key.lastIndexOf("/") + 1);
//			if(!key.endsWith("/")){
//			FileEntity fileEntity = fileEntityDao.loadByFileNameAndPath(fileName, filePath);
//			if(fileEntity!=null)
//				fileEntityDao.delete(fileEntity);
//			}
        // 删除Object
        Aliyun.INSTANCE.getOSSClient().deleteObject(Aliyun.INSTANCE.getOssBucket(), key);

    }


    public static boolean newFolder(String folderName, String curFolder) {
        String key = curFolder + folderName + "/";
        ByteArrayInputStream in = null;
        try {
            ObjectMetadata meta = new ObjectMetadata();
            byte[] buffer = new byte[0];
            in = new ByteArrayInputStream(buffer);
            meta.setContentLength(0);
            Aliyun.INSTANCE.getOSSClient().putObject(Aliyun.INSTANCE.getOssBucket(), key, in, meta);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {

            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return true;
    }


    public static MultipartUploadListing listMultipartUploads() {
        ListMultipartUploadsRequest listMultipartUploadsRequest = new ListMultipartUploadsRequest(Aliyun.INSTANCE.getOssBucket());
        return Aliyun.INSTANCE.getOSSClient().listMultipartUploads(listMultipartUploadsRequest);
    }


    public static List<Bucket> listBuckets() {
        return Aliyun.INSTANCE.getOSSClient().listBuckets();
    }


    public static ObjectListing getList(String dir) {
//		if(dir.equals(""))
//			dir="/";
        // 构造ListObjectsRequest请求
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest(Aliyun.INSTANCE.getOssBucket());

        // "/" 为文件夹的分隔符
        listObjectsRequest.setDelimiter("/");

        // 列出fun目录下的所有文件和文件夹
        listObjectsRequest.setPrefix(dir);

        ObjectListing listing = Aliyun.INSTANCE.getOSSClient().listObjects(listObjectsRequest);
//		for (OSSObjectSummary objectSummary : listing.getObjectSummaries()) {
//	        System.out.println(objectSummary.getKey());
//	    }
        return listing;
    }


    public static int count(String dir) {
        ObjectListing list = getList(dir);
        return list.getObjectSummaries().size();
    }


    public static PartListing listParts(String key, String uploadId) {
        ListPartsRequest listPartsRequest = new ListPartsRequest(Aliyun.INSTANCE.getOssBucket(), key, uploadId);
        return Aliyun.INSTANCE.getOSSClient().listParts(listPartsRequest);
    }

    /**
     * 将所有OSS中的文件下载到本地，切换存储模式的时候用
     */

    public static void downloadAll() {
        ObjectListing objectListing = getList("/");
        for (OSSObjectSummary objectSummary : objectListing.getObjectSummaries()) {
            // 新建GetObjectRequest
            GetObjectRequest getObjectRequest = new GetObjectRequest(Aliyun.INSTANCE.getOssBucket(), objectSummary.getKey());

            // 下载Object到文件
            ObjectMetadata objectMetadata = Aliyun.INSTANCE.getOSSClient().getObject(getObjectRequest, new File("D:/" + objectSummary.getKey()));
        }

    }


    public static void copyObject(String srcKey, String destKey) {
        // 拷贝Object
        CopyObjectResult result = Aliyun.INSTANCE.getOSSClient().copyObject(Aliyun.INSTANCE.getOssBucket(), srcKey, Aliyun.INSTANCE.getOssBucket(), destKey);

        // 打印结果
        System.out.println("ETag: " + result.getETag() + " LastModified: " + result.getLastModified());
    }


    public static void setReferer(String referer, boolean allowEmptyReferer) {
        List<String> refererList = new ArrayList<String>();
        String[] referers = referer.split("\n");
        for (String s : referers) {
            refererList.add(s);
        }
        BucketReferer bucketReferer = new BucketReferer(allowEmptyReferer, refererList);
        Aliyun.INSTANCE.getOSSClient().setBucketReferer(Aliyun.INSTANCE.getOssBucket(), bucketReferer);
    }


    public static List<String> getReferer() {
        if (Aliyun.INSTANCE.getOSSClient() != null) {
            BucketReferer bucketReferer = Aliyun.INSTANCE.getOSSClient().getBucketReferer(Aliyun.INSTANCE.getOssBucket());
            return bucketReferer.getRefererList();
        }
        return null;
    }


    public static boolean isAllowEmptyReferer() {
        if (Aliyun.INSTANCE.getOSSClient() != null) {
            BucketReferer bucketReferer = Aliyun.INSTANCE.getOSSClient().getBucketReferer(Aliyun.INSTANCE.getOssBucket());
            return bucketReferer.isAllowEmptyReferer();
        }
        return false;
    }
}
