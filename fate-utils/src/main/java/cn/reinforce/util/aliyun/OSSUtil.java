package cn.reinforce.util.aliyun;

import cn.reinforce.util.MD5;
import cn.reinforce.util.commons.Constants;
import com.aliyun.oss.model.AbortMultipartUploadRequest;
import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.BucketInfo;
import com.aliyun.oss.model.BucketReferer;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.CompleteMultipartUploadResult;
import com.aliyun.oss.model.CopyObjectResult;
import com.aliyun.oss.model.CreateBucketRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.InitiateMultipartUploadRequest;
import com.aliyun.oss.model.InitiateMultipartUploadResult;
import com.aliyun.oss.model.ListMultipartUploadsRequest;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.ListPartsRequest;
import com.aliyun.oss.model.MultipartUploadListing;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.PartListing;
import com.aliyun.oss.model.PartSummary;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.model.StorageClass;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
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

    // 设置每块为 500K
    private static final long PART_SIZE1 = 1024L * 500;

    // 设置每块为 1M
    private static final long PART_SIZE2 = 1024L * 1024;

    // 设置每块为 2M
    private static final long PART_SIZE3 = 1024L * 1024 * 2;

    // 设置每块为 5M
    private static final long PART_SIZE4 = 1024L * 1024 * 5;

    // 设置每块为 10M
    private static final long PART_SIZE5 = 1024L * 1024 * 20;

    /**
     * 列举所有bucket
     *
     * @return
     */
    public static List<Bucket> listBuckets() {
        return Aliyun.INSTANCE.getOSSClient().listBuckets();
    }

    /**
     * 创建bucket并设置bucket权限和存储类型
     *
     * @param bucketName
     * @param storageClass        存储类型
     * @param cannedAccessControl 权限控制
     */
    public static void createBucket(String bucketName, StorageClass storageClass, CannedAccessControlList cannedAccessControl) {
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        createBucketRequest.setCannedACL(cannedAccessControl);
        createBucketRequest.setStorageClass(storageClass);
        Aliyun.INSTANCE.getOSSClient().createBucket(createBucketRequest);
    }

    public static boolean doesBucketExist(String bucketName) {
        return Aliyun.INSTANCE.getOSSClient().doesBucketExist(bucketName);
    }

    public static BucketInfo getBucketInfo(String bucketName) {
        return Aliyun.INSTANCE.getOSSClient().getBucketInfo(bucketName);
    }

    /**
     * 小文件上传,页面传到后台的文件
     *
     * @param bucketName
     * @param clientFile
     * @param folder
     * @return
     */
    public static PutObjectResult simpleUpload(String bucketName, MultipartFile clientFile, String folder, String filename) {
        String key = folder + filename;
        PutObjectResult result = null;
        try {
            InputStream content = clientFile.getInputStream();
            ObjectMetadata meta = new ObjectMetadata();
            meta.setContentLength(clientFile.getSize());
//            meta.setContentType(clientFile.getContentType());
            result = Aliyun.INSTANCE.getOSSClient().putObject(bucketName, key, content, meta);// 会自动关闭流？
        } catch (IOException e) {
            LOG.error(e);
        }
        return result;
    }

    /**
     * 服务器本地文件上传
     *
     * @param bucketName
     * @param file
     * @param folder
     * @param filename
     * @return
     */
    public static PutObjectResult simpleUpload(String bucketName, File file, String folder, String filename) {
        String key = folder + filename;
        PutObjectResult result = null;
        ObjectMetadata meta = new ObjectMetadata();
//        meta.setContentType(Files.probeContentType(filename));
        meta.setContentLength(file.getTotalSpace());
        result = Aliyun.INSTANCE.getOSSClient().putObject(bucketName, key, file, meta);// 会自动关闭流？
        return result;
    }

    /**
     * 直接上传文件流
     * @param bucketName
     * @param is
     * @param folder
     * @param filename
     * @return
     */
    public static PutObjectResult simpleUpload(String bucketName, InputStream is, String folder, String filename) {
        String key = folder + filename;
        PutObjectResult result = null;
        ObjectMetadata meta = new ObjectMetadata();
//        meta.setContentLength(is.available());
        result = Aliyun.INSTANCE.getOSSClient().putObject(bucketName, key, is, meta);// 会自动关闭流？
        return result;
    }

    /**
     * 文件下载
     *
     * @param bucketName
     * @param os
     * @param folder
     * @param filename
     * @return
     * @throws IOException
     */
    public static void download(String bucketName, OutputStream os, String folder, String filename) throws IOException {
        String key = folder + filename;
        OSSObject ossObject = Aliyun.INSTANCE.getOSSClient().getObject(bucketName, key);
        InputStream content = ossObject.getObjectContent();
        if (content != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                writer.write(line);
            }
            content.close();
        }
    }

    /**
     * 字节流上传
     *
     * @param bucketName
     * @param file
     * @param folder
     * @param filename
     * @return
     */
    public static PutObjectResult byteUpload(String bucketName, byte[] file, String folder, String filename) {
        filename = folder + filename;
        PutObjectResult result = null;
        filename = folder + filename;
        result = Aliyun.INSTANCE.getOSSClient().putObject(bucketName, filename, new ByteArrayInputStream(file));
        return result;
    }

    /**
     * 上传网上资源到OSS
     *
     * @param url
     * @param bucketName
     * @param folder
     * @return
     */
    public static PutObjectResult uploadOnlineFileToOSS(String url, String bucketName, String folder) throws IOException {
        String filename = url.substring(url.lastIndexOf("/") + 1);
        filename = folder + filename;
        PutObjectResult result = null;
        InputStream content = new URL(url).openStream();
        result = Aliyun.INSTANCE.getOSSClient().putObject(bucketName, filename, content);
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

    public static void multipartUpload(final String bucketName, MultipartFile partFile, String folder, String filename) throws IOException {
        long partSize;
        if (partFile.getSize() <= PART_SIZE2) {
            partSize = PART_SIZE1;
        } else if (partFile.getSize() <= PART_SIZE2 * 50) {
            partSize = PART_SIZE2;
        } else if (partFile.getSize() <= PART_SIZE3 * 50) {
            partSize = PART_SIZE3;
        } else if (partFile.getSize() <= PART_SIZE4 * 100) {
            partSize = PART_SIZE4;
        } else {
            partSize = PART_SIZE5;
        }
        InputStream content;
        String key = folder + filename;

        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentLength(partFile.getSize());
        meta.setContentType(partFile.getContentType());

        // 开始Multipart Upload
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, key, meta);
        InitiateMultipartUploadResult result = Aliyun.INSTANCE.getOSSClient().initiateMultipartUpload(request);
        String uploadId = result.getUploadId();

        System.out.println(uploadId);

        // 计算分块数目
        int partCount = (int) (partFile.getSize() / partSize);
        if (partFile.getSize() % partSize != 0) {
            partCount++;
        }
        // 新建一个List保存每个分块上传后的ETag和PartNumber
        List<PartETag> partETags = new ArrayList<PartETag>();
        LOG.info("开始上传");

        for (int i = 0; i < partCount; i++) {
            // 获取文件流
            InputStream is = partFile.getInputStream();

            // 跳到每个分块的开头
            long skipBytes = partSize * i;
            is.skip(skipBytes);

            // 计算每个分块的大小
            long size = partSize < partFile.getSize() - skipBytes ? partSize : partFile.getSize() - skipBytes;

            String oraginalTag = MD5.getMd5ByFile(is, skipBytes, size);
            // 创建UploadPartRequest，上传分块
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(key);
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setInputStream(is);
            uploadPartRequest.setPartSize(size);
            uploadPartRequest.setPartNumber(i + 1);
            UploadPartResult uploadPartResult = Aliyun.INSTANCE.getOSSClient().uploadPart(uploadPartRequest);
            if (oraginalTag.equals(uploadPartResult.getPartETag().getETag())) {
                // 将返回的PartETag保存到List中。
                LOG.info(uploadPartResult.getPartETag().getETag());
                partETags.add(uploadPartResult.getPartETag());
            } else {
                i--;
            }
            // 关闭文件
            is.close();
        }
        completeMultipartUpload(bucketName, key, partETags, uploadId);
    }

    /**
     * 完成上传
     *
     * @param bucketName
     * @param filename
     * @param partETags
     * @param uploadId
     * @return
     */

    public static void completeMultipartUpload(String bucketName, String filename, List<PartETag> partETags, String uploadId) {
        CompleteMultipartUploadRequest completeMultipartUploadRequest = new CompleteMultipartUploadRequest(bucketName, filename, uploadId, partETags);

        // 完成分块上传
        CompleteMultipartUploadResult completeMultipartUploadResult = Aliyun.INSTANCE.getOSSClient().completeMultipartUpload(completeMultipartUploadRequest);

        // 打印Object的ETag
        System.out.println(completeMultipartUploadResult.getETag());
    }

    /**
     * 取消上传
     *
     * @param filename
     * @param uploadId
     */

    public static void abortMultipartUpload(String filename, String uploadId) {
        AbortMultipartUploadRequest abortMultipartUploadRequest = new AbortMultipartUploadRequest(Aliyun.INSTANCE.getOssBucket(), filename, uploadId);

        // 取消分块上传
        Aliyun.INSTANCE.getOSSClient().abortMultipartUpload(abortMultipartUploadRequest);
    }

    /**
     * 列举分片
     * @param key
     * @param uploadId
     * @return
     */
    public static List<PartSummary> listParts(String key, String uploadId) {
        ListPartsRequest listPartsRequest = new ListPartsRequest(Aliyun.INSTANCE.getOssBucket(), key, uploadId);
        // 每页100个分片
//        listPartsRequest.setMaxParts(100);
        PartListing partListing;
        List<PartSummary> parts = new ArrayList<>();
        do {
            partListing = Aliyun.INSTANCE.getOSSClient().listParts(listPartsRequest);
            parts.addAll(partListing.getParts());
            listPartsRequest.setPartNumberMarker(partListing.getNextPartNumberMarker());
        } while (partListing.isTruncated());
        return parts;
    }


    public static String generatePresignedUrl(String key, Date expiration) {
        String url = Aliyun.INSTANCE.getOSSClient().generatePresignedUrl(Aliyun.INSTANCE.getOssBucket(), key, expiration).toString();
        if (Aliyun.INSTANCE.getOssUrl() != null){
            url = url.replace(Aliyun.INSTANCE.getOssBucket() + "." + Aliyun.INSTANCE.getOssEndpoint(), Aliyun.INSTANCE.getOssUrl());
        }
        return url;
    }

    /**
     * 删除文件
     *
     * @param key
     * @throws UnsupportedEncodingException
     */
    public static void deleteObject(String key) throws UnsupportedEncodingException {
        // 删除Object
        Aliyun.INSTANCE.getOSSClient().deleteObject(Aliyun.INSTANCE.getOssBucket(), key);
    }

    /**
     * 新建文件夹
     * @param folderName
     * @param parentFolder
     */
    public static void createFolder(String folderName, String parentFolder) {
        String keySuffixWithSlash = parentFolder + folderName + "/";
        Aliyun.INSTANCE.getOSSClient().putObject(Aliyun.INSTANCE.getOssBucket(), keySuffixWithSlash, new ByteArrayInputStream(new byte[0]));
    }

    /**
     * 列举上传事件
     * @return
     */
    public static MultipartUploadListing listMultipartUploads() {
        ListMultipartUploadsRequest listMultipartUploadsRequest = new ListMultipartUploadsRequest(Aliyun.INSTANCE.getOssBucket());
        return Aliyun.INSTANCE.getOSSClient().listMultipartUploads(listMultipartUploadsRequest);
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
        List<String> refererList = new ArrayList<>();
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
