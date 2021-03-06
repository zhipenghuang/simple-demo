package com.hzp.test.entity;

import java.io.Serializable;
import java.util.Date;

public class WechatGroup implements Serializable {
    /**
     * 字段: wechat_group.id<br/>
     * 主键: 自动增长<br/>
     * 必填: true<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * 字段: wechat_group.group<br/>
     * 必填: true<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 
     *
     * @mbggenerated
     */
    private String group;

    /**
     * 字段: wechat_group.wechats<br/>
     * 必填: true<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 
     *
     * @mbggenerated
     */
    private String wechats;

    /**
     * 字段: wechat_group.update_time<br/>
     * 必填: false<br/>
     * 缺省: CURRENT_TIMESTAMP<br/>
     * 长度: 19<br/>
     * 说明: 
     *
     * @mbggenerated
     */
    private Date updateTime;

    /**
     * 字段: wechat_group.create_time<br/>
     * 必填: false<br/>
     * 缺省: CURRENT_TIMESTAMP<br/>
     * 长度: 19<br/>
     * 说明: 
     *
     * @mbggenerated
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table wechat_group
     *
     * @mbggenerated
     */
    private static final long serialVersionUID = 1L;

    /**
     * @return wechat_group.id: 
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * 字段: wechat_group.id<br/>
     * 主键: 自动增长<br/>
     * 必填: true<br/>
     * 缺省: <br/>
     * 长度: 10<br/>
     * 说明: 
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return wechat_group.group: 
     *
     * @mbggenerated
     */
    public String getGroup() {
        return group;
    }

    /**
     * 字段: wechat_group.group<br/>
     * 必填: true<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 
     *
     * @mbggenerated
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * @return wechat_group.wechats: 
     *
     * @mbggenerated
     */
    public String getWechats() {
        return wechats;
    }

    /**
     * 字段: wechat_group.wechats<br/>
     * 必填: true<br/>
     * 缺省: <br/>
     * 长度: 255<br/>
     * 说明: 
     *
     * @mbggenerated
     */
    public void setWechats(String wechats) {
        this.wechats = wechats;
    }

    /**
     * @return wechat_group.update_time: 
     *
     * @mbggenerated
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 字段: wechat_group.update_time<br/>
     * 必填: false<br/>
     * 缺省: CURRENT_TIMESTAMP<br/>
     * 长度: 19<br/>
     * 说明: 
     *
     * @mbggenerated
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return wechat_group.create_time: 
     *
     * @mbggenerated
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 字段: wechat_group.create_time<br/>
     * 必填: false<br/>
     * 缺省: CURRENT_TIMESTAMP<br/>
     * 长度: 19<br/>
     * 说明: 
     *
     * @mbggenerated
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wechat_group
     *
     * @mbggenerated
     */
    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        WechatGroup other = (WechatGroup) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGroup() == null ? other.getGroup() == null : this.getGroup().equals(other.getGroup()))
            && (this.getWechats() == null ? other.getWechats() == null : this.getWechats().equals(other.getWechats()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()));
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wechat_group
     *
     * @mbggenerated
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGroup() == null) ? 0 : getGroup().hashCode());
        result = prime * result + ((getWechats() == null) ? 0 : getWechats().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        return result;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table wechat_group
     *
     * @mbggenerated
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", group=").append(group);
        sb.append(", wechats=").append(wechats);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}