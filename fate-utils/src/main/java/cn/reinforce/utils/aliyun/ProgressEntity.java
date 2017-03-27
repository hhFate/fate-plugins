package cn.reinforce.utils.aliyun;

import cn.reinforce.utils.Tools;
import com.aliyun.oss.model.PartETag;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于记录文件上传时的进度
 * @author 幻幻(Fate)
 *
 */
public class ProgressEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6147738404322517735L;

	public final static int upload_state_creating = 0;

	public final static int upload_state_service_uploading = 1;
	
	public final static int upload_state_OSS_uploading =2;

	public final static int upload_state_complete = 3;

	public final static int upload_state_suspend = 4;

	public final static int upload_state_error = 5;

	private String bucketName;
	private String filePath;
	private String fileName;
	private long pBytesRead;
	private long size;
	private int state;
	private int partsRead;
	private int partsAll;
	private String percentDone;
	private String uploadId;
	private List<PartETag> partETags;
	private DecimalFormat df = new DecimalFormat("#.##");

	public ProgressEntity(){
		super();
		partETags = new ArrayList<PartETag>();
	}
	
	public ProgressEntity(String bucketName, String filePath, String fileName,
                          int partsAll, String uploadId, long size) {
		super();
		this.partsAll = partsAll;
		this.uploadId = uploadId;
		this.fileName = fileName;
		this.bucketName = bucketName;
		this.size = size;
		this.filePath = filePath;
		partETags = new ArrayList<PartETag>();
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getPartsRead() {
		return partsRead;
	}

	public void setPartsRead(int partsRead) {
		this.partsRead = partsRead;
		percentDone = df.format(100.00 * partsRead / partsAll);
	}

	public int getPartsAll() {
		return partsAll;
	}

	public void setPartsAll(int partsAll) {
		this.partsAll = partsAll;
	}

	public String getPercentDone() {
		return percentDone;
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public List<PartETag> getPartETags() {
		return partETags;
	}

	public String getSize() {
		return Tools.sizeConvert(size);
	}

	public void setSize(long size) {
		this.size = size;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public long getpBytesRead() {
		return pBytesRead;
	}

	public void setpBytesRead(long pBytesRead) {
		this.pBytesRead = pBytesRead;
		percentDone = df.format(100.00 * pBytesRead / size);
	}

	@Override
	public String toString() {
		return "ProgressEntity [bucketName=" + bucketName + ", fileName="
				+ fileName + ", partsRead=" + partsRead + ", partsAll="
				+ partsAll + ", percentDone=" + percentDone + ", uploadId="
				+ uploadId + ", partETags=" + partETags + "]";
	}

}
