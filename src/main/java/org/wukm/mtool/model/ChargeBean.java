/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.model
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-8-2
 * Time : 下午6:56
 * ---------------------------------
 */
package org.wukm.mtool.model;

/**
 * Create with IntelliJ IDEA
 * Project name : common
 * Package name : org.wukm.mtool.model
 * Author : Wukunmeng
 * User : wkm
 * Date : 16-8-2
 * Time : 下午6:56
 * ---------------------------------
 * To change this template use File | Settings | File and Code Templates.
 */
public class ChargeBean {

    private String id;

    private String orderNo;

    private String type;

    private Boolean status;

    private Integer amount;

    private Integer settle;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getSettle() {
        return settle;
    }

    public void setSettle(Integer settle) {
        this.settle = settle;
    }
}
