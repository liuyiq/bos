package xin.liuyiq.bos.domain.take_delivery;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import freemarker.template.SimpleDate;
import xin.liuyiq.bos.domain.constants.Contants;

/**
 * @description:促销信息实体类
 */
@Entity
@Table(name = "T_PROMOTION")
public class Promotion implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "C_ID")
	private Integer id;
	@Column(name = "C_TITLE")
	private String title; // 宣传概要(标题)
	@Column(name = "C_TITLE_IMG")
	private String titleImg; // 宣传图片
	@Column(name = "C_ACTIVE_SCOPE")
	private String activeScope;// 活动范围
	@Column(name = "C_START_DATE")
	private Date startDate; // 发布时间
	@Column(name = "C_END_DATE")
	private Date endDate; // 失效时间
	@Column(name = "C_UPDATE_TIME")
	private Date updateTime; // 更新时间
	@Column(name = "C_UPDATE_UNIT")
	private String updateUnit; // 更新单位
	@Column(name = "C_UPDATE_USER")
	private String updateUser;// 更新人 后续与后台用户关联
	@Column(name = "C_STATUS")
	private String status = "1"; // 状态 可取值：1.进行中 2. 已结束
	@Column(name = "C_DESCRIPTION")
	private String description; // 宣传内容(活动描述信息)
	
	@Transient
	private String statusStr; // 状态码的返回值
	@Transient
	private String startDateStr; // 发布时间的返回值
	@Transient
	private String endDateStr; // 失效时间的返回值
	
	
	public String getStartDateStr() {
		
		return new SimpleDateFormat("yyyy-MM-dd").format(startDate);
	}

	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	public String getEndDateStr() {
		return new SimpleDateFormat("yyyy-MM-dd").format(endDate);
	}

	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}

	public String getStatusStr() {
		if(status.equals("1")){
			return "进行中";
		}
		return "已结束";
	}

	public void setStatusStr(String statusStr) {
		this.statusStr = statusStr;
	}
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleImg() {
		if(titleImg.startsWith("http")){
			return titleImg;
		}
		return Contants.BOS_MANAGEMENT_HOST+titleImg;
	}

	public void setTitleImg(String titleImg) {
		this.titleImg = titleImg;
	}

	public String getActiveScope() {
		return activeScope;
	}

	public void setActiveScope(String activeScope) {
		this.activeScope = activeScope;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUnit() {
		return updateUnit;
	}

	public void setUpdateUnit(String updateUnit) {
		this.updateUnit = updateUnit;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
