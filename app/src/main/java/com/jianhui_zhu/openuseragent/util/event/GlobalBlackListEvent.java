package com.jianhui_zhu.openuseragent.util.event;

import java.util.Set;

/**
 * Created by jianhuizhu on 2016-05-03.
 */
public class GlobalBlackListEvent {
    public static final int CLEAR = 0;
    public static final int ADD = 1;
    public static final int REMOVE = 2;
    private Set domains;
    private int action;

    public Set getDomains() {
        return domains;
    }

    public void setDomains(Set domains) {
        this.domains = domains;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public GlobalBlackListEvent(Set domains, int action) {
        this.domains = domains;
        this.action = action;
    }
}
