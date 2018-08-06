package com.app.model;

import java.util.List;

import org.litepal.crud.DataSupport;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/**
 * 单据
 * @author anqiansong
 * 商品盘点数据
 */
public class ProdCheckDataInfo extends DataSupport implements Parcelable{

	private int id;
	
	/**盘点单据编码，由服务端产生。值为空，则做新增。值非空，则做更新*/
	private String doc_num;
	/**货位编码*/
	private String loc_id;//货位
	private String shelf_code;//货架
	private String aera_code;//区域
	/**楼层*/
	private String floor;
	/**货位下的总商品数*/
	private int total_qty;//扫描数
	/**备注*/
	private String remark;
	/**标签*/
	private String mark_type;
	/**商品明细数据*/
	
	
	private String create_user;
	private String create_time;
	private String precheck_qty;//预点数
	
	private int status;//0:未上传；1：已上传
	private int prodPreChcekData_id;//货架ID
	private ProdPreChcekData prodPreChcekData;//货架
	private boolean IsImportedToErp;
	
	
	private String pddbh;//not null,--主建、盘点单号
	private String zdrbh;//not null,--制单人编号，能提供最好，不能提供默认几个字符
	private String zdsj;//not null,--制单时间
	private String pdck;//null,--盘点仓库，能提供最好
	private String pdbz;//null,--备注
	private String dcbj;//not null default '0',--导出标记，0未导出，1已导出
	private List<ProdCheckDtl>  prodcheckdtl;
	private String pdkw;//货架号
    
	public String getPdkw() {
		return pdkw;
	}
	public void setPdkw(String pdkw) {
		this.pdkw = pdkw;
	}
	
	public String getPddbh() {
		return pddbh;
	}
	public void setPddbh(String pddbh) {
		this.pddbh = pddbh;
	}
	public String getZdrbh() {
		return zdrbh;
	}
	public void setZdrbh(String zdrbh) {
		this.zdrbh = zdrbh;
	}
	public String getZdsj() {
		return zdsj;
	}
	public void setZdsj(String zdsj) {
		this.zdsj = zdsj;
	}
	public String getPdck() {
		return pdck;
	}
	public void setPdck(String pdck) {
		this.pdck = pdck;
	}
	public String getPdbz() {
		return pdbz;
	}
	public void setPdbz(String pdbz) {
		this.pdbz = pdbz;
	}
	public String getDcbj() {
		return dcbj;
	}
	public void setDcbj(String dcbj) {
		this.dcbj = dcbj;
	}
	public String getDoc_num() {
		return doc_num;
	}
	public void setDoc_num(String doc_num) {
		this.doc_num = doc_num;
	}
	public String getLoc_id() {
		return loc_id;
	}
	public void setLoc_id(String loc_id) {
		this.loc_id = loc_id;
	}
	public String getShelf_code() {
		return shelf_code;
	}
	public void setShelf_code(String shelf_code) {
		this.shelf_code = shelf_code;
	}
	public String getAera_code() {
		return aera_code;
	}
	public void setAera_code(String aera_code) {
		this.aera_code = aera_code;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public int getTotal_qty() {
		return total_qty;
	}
	public void setTotal_qty(int total_qty) {
		this.total_qty = total_qty;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getMark_type() {
		return mark_type;
	}
	public void setMark_type(String mark_type) {
		this.mark_type = mark_type;
	}
	public List<ProdCheckDtl> getProdcheckdtl() {
		return prodcheckdtl;
	}
	public void setProdcheckdtl(List<ProdCheckDtl> prodcheckdtl) {
		this.prodcheckdtl = prodcheckdtl;
	}
	public String getCreate_user() {
		return create_user;
	}
	public void setCreate_user(String create_user) {
		this.create_user = create_user;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getPrecheck_qty() {
		return precheck_qty;
	}
	public void setPrecheck_qty(String precheck_qty) {
		this.precheck_qty = precheck_qty;
	}
	public boolean isIsImportedToErp() {
		return IsImportedToErp;
	}
	public void setIsImportedToErp(boolean isImportedToErp) {
		IsImportedToErp = isImportedToErp;
	}
	
	public int getProdPreChcekData_id() {
		return prodPreChcekData_id;
	}
	public void setProdPreChcekData_id(int prodPreChcekData_id) {
		this.prodPreChcekData_id = prodPreChcekData_id;
	}
	
	public ProdPreChcekData getProdPreChcekData() {
		return prodPreChcekData;
	}
	public void setProdPreChcekData(ProdPreChcekData prodPreChcekData) {
		this.prodPreChcekData = prodPreChcekData;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
//	public List<ProdCheckDtl> getProdCheckDtls() {  
//        return DataSupport.where("prodCheckData_id = ?", String.valueOf(id)).find(ProdCheckDtl.class);  
//    }  
	
	
	public static Parcelable.Creator<ProdCheckDataInfo> getCreator()
    {
        return CREATOR;
    }

    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        // TODO Auto-generated method stub
        dest.writeInt(id);
    	dest.writeString(doc_num);
//    	dest.writeString(LocID);
    	dest.writeString(floor);
    	dest.writeString(aera_code);
    	dest.writeInt(total_qty);
    	dest.writeString(remark);
    	dest.writeString(mark_type);
    	dest.writeString(create_user);
    	dest.writeString(create_time);
    	dest.writeInt(status);
    	dest.writeInt(prodPreChcekData_id);
    	dest.writeList(prodcheckdtl); 
    	dest.writeString(shelf_code);
    	dest.writeString(precheck_qty);
    }

    public static final Parcelable.Creator<ProdCheckDataInfo> CREATOR = new Creator<ProdCheckDataInfo>()
    {
        public ProdCheckDataInfo createFromParcel(Parcel source)
        {
        	ProdCheckDataInfo instance = new ProdCheckDataInfo();
        	instance.id = source.readInt();
            instance.doc_num = source.readString();
//            instance.LocID = source.readString();
            instance.floor = source.readString();
            instance.aera_code = source.readString();
            instance.total_qty = source.readInt();
            instance.remark = source.readString();
            instance.mark_type = source.readString();
            instance.create_user = source.readString();
            instance.create_time = source.readString();
            instance.status = source.readInt();
            instance.prodPreChcekData_id = source.readInt();
            instance.prodcheckdtl = source.readArrayList(ProdCheckDtl.class.getClassLoader());
            instance.shelf_code = source.readString();
            instance.precheck_qty = source.readString();
            return instance;
        }

        public ProdCheckDataInfo[] newArray(int size)
        {
            // TODO Auto-generated method stub
            return new ProdCheckDataInfo[size];
        }
    };
    
}
