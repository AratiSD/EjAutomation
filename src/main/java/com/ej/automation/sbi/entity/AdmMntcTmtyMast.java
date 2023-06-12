package com.ej.automation.sbi.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name="ADM_MNTC_TMTY_MAST")
public class AdmMntcTmtyMast implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="AMT_DEVC_TYPE")
    private String amtDevcType;

	@Column(name="AMT_DEVC_DESC")
    private String amtDevcDesc;

    @Column(name="ATD_DELV_CHID")
    private String atdDelvChid;

    public String getAmtDevcType() {
        return amtDevcType;
    }

    public void setAmtDevcType(final String amtDevcType) {
        this.amtDevcType = amtDevcType;
    }

    public String getAmtDevcDesc() {
        return amtDevcDesc;
    }

    public void setAmtDevcDesc(final String amtDevcDesc) {
        this.amtDevcDesc = amtDevcDesc;
    }

    public String getAtdDelvChid() {
        return atdDelvChid;
    }

    public void setAtdDelvChid(final String atdDelvChid) {
        this.atdDelvChid = atdDelvChid;
    }

}
