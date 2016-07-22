package cn.reinforce.utils.chat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 推送
 * 
 * @author Fate
 *
 */
public class ChatPushView {

	/**
	 * 接收人的手机号
	 */
	private List<String> mobile = new ArrayList<String>();
	
	/**
	 * 推送通知的内容
	 */
	@Expose
	private String Content;

	/**
	 * 推送时间
	 */
	@Expose
	private Date pushTime = new Date();

	/**
	 * 操作人姓名
	 */
	@Expose
	private String username;
	
	/**
	 * 操作人头像
	 */
	@Expose
	private String headIcon;

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public Date getPushTime() {
		return pushTime;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHeadIcon() {
		return headIcon;
	}

	public void setHeadIcon(String headIcon) {
		this.headIcon = headIcon;
	}

	public List<String> getMobile() {
		return mobile;
	}

	public void setMobile(List<String> mobile) {
		this.mobile = mobile;
	}


}
