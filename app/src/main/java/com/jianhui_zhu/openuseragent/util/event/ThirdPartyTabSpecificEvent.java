package com.jianhui_zhu.openuseragent.util.event;

/**
 * Created by jianhuizhu on 2016-05-03.
 */
public class ThirdPartyTabSpecificEvent {
    public ThirdPartyTabSpecificEvent(String domain, boolean status) {
        this.domain = domain;
        this.status = status;
    }

    private String domain;
    private boolean status;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
