package com.fly.bos.domain.base;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 * @description: 运输配送信息
 */
@Entity
@Table(name = "T_TRANSIT_INFO")
public class TransitInfo {
	@Id
	@GeneratedValue
	@Column(name = "C_ID")
	private Integer id;

	@OneToOne
	@JoinColumn(name = "C_WAYBILL_ID")
	private WayBill wayBill;

	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "C_TRANSIT_INFO_ID")
	@OrderColumn(name = "C_IN_OUT_INDEX")
	private List<InOutStorageInfo> inOutStorageInfos = new ArrayList<InOutStorageInfo>();

	@OneToOne
	@JoinColumn(name = "C_DELIVERY_INFO_ID")
	private DeliveryInfo deliveryInfo;

	@OneToOne
	@JoinColumn(name = "C_SIGN_INFO_ID")
	private SignInfo signInfo;

	@Column(name = "C_STATUS")
	// 出入库中转、到达网点、开始配置、正常签收、异常
	private String status;

	// 到达网点的地址
	@Column(name = "C_OUTLET_ADDRESS")
	private String outletAddress;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public WayBill getWayBill() {
		return wayBill;
	}

	public void setWayBill(WayBill wayBill) {
		this.wayBill = wayBill;
	}

	public List<InOutStorageInfo> getInOutStorageInfos() {
		return inOutStorageInfos;
	}

	public void setInOutStorageInfos(List<InOutStorageInfo> inOutStorageInfos) {
		this.inOutStorageInfos = inOutStorageInfos;
	}

	public DeliveryInfo getDeliveryInfo() {
		return deliveryInfo;
	}

	public void setDeliveryInfo(DeliveryInfo deliveryInfo) {
		this.deliveryInfo = deliveryInfo;
	}

	public SignInfo getSignInfo() {
		return signInfo;
	}

	public void setSignInfo(SignInfo signInfo) {
		this.signInfo = signInfo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOutletAddress() {
		return outletAddress;
	}

	public void setOutletAddress(String outletAddress) {
		this.outletAddress = outletAddress;
	}

	// get方法，存放物流信息
	public String getTransferInfo(){
		StringBuffer buffer = new StringBuffer();
		// 存放出入库
		if(inOutStorageInfos!=null && inOutStorageInfos.size()>0){
			for(InOutStorageInfo inOutStorageInfo:inOutStorageInfos){
				buffer.append(inOutStorageInfo.getDescription()+"<br>");
			}
		}
		// 存放配送
		if(deliveryInfo!=null){
			buffer.append(deliveryInfo.getDescription()+"<br>");
		}
		// 签收
		if(signInfo!=null){
			buffer.append(signInfo.getDescription()+"<br>");
		}
		return buffer.toString();
	}
}
