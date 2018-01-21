package com.xnx3.version;

import com.xnx3.BaseVO;

/**
 * 版本更新提示,用于当前发布的系统，检测到有新版本时，会在总管理后台提示新版本
 * @author 管雷鸣
 *
 */
public class VersionVO extends BaseVO {
	private boolean findNewVersion;	//发现有新版本
	private String newVersion;		//新版本的版本号，不包含前面的v，如 3.7.1.20180121
	private String previewUrl;			//预览的url，弹出提示框后，点击查看新版本时，跳转到的网址
	
	public VersionVO() {
		findNewVersion = false;
	}
	
	public boolean isFindNewVersion() {
		return findNewVersion;
	}
	public void setFindNewVersion(boolean findNewVersion) {
		this.findNewVersion = findNewVersion;
	}
	public String getNewVersion() {
		return newVersion;
	}
	public void setNewVersion(String newVersion) {
		this.newVersion = newVersion;
	}
	public String getPreviewUrl() {
		return previewUrl;
	}
	public void setPreviewUrl(String previewUrl) {
		this.previewUrl = previewUrl;
	}
	@Override
	public String toString() {
		return "VersionVO [findNewVersion=" + findNewVersion + ", newVersion="
				+ newVersion + ", previewUrl=" + previewUrl + "]";
	}
	
}
