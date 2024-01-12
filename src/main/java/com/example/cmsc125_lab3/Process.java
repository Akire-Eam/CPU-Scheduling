package com.example.cmsc125_lab3;

public class Process {
    private int pid;
    private int at;
    private int bt;
    private int pn;
    private int ct;
    private int rt;
    private int wt;
    private int tat;
    private boolean usedState;
    private int firstExecutionTime;
    private int origBt;


    public Process (int pid, int pn, int at, int bt, boolean usedState){
        this.pid = pid;
        this.at = at;
        this.bt = bt;
        this.pn = pn;
        this.usedState = usedState;
        this.origBt = bt;

    }
    // Copy constructor
    public Process(Process other) {
        this.pid = other.pid;
        this.at = other.at;
        this.bt = other.bt;
        this.pn = other.pn;
        this.usedState = other.usedState;
    }
    public int getOrigBt() {
        return origBt;
    }

    public void setOrigBt(int origBt) {
        this.origBt = origBt;
    }
    public int getFirstExecutionTime() {
        return firstExecutionTime;
    }

    public void setFirstExecutionTime(int firstExecutionTime) {
        this.firstExecutionTime = firstExecutionTime;
    }
    public int getCt() {
        return ct;
    }

    public void setCt(int ct) {
        this.ct = ct;
    }

    public int getRt() {
        return rt;
    }

    public void setRt(int rt) {
        this.rt = rt;
    }

    public int getWt() {
        return wt;
    }

    public void setWt(int wt) {
        this.wt = wt;
    }


    public int getTat() {
        return tat;
    }

    public void setTat(int tat) {
        this.tat = tat;
    }

    public boolean isUsed() {
        return usedState;
    }

    public void setUsedState(boolean usedState) {
        this.usedState = usedState;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setAt(int at) {
        this.at = at;
    }

    public void setBt(int bt) {
        this.bt = bt;
    }

    public void decrementBt(int bt){
        this.bt -= bt;
    }

    public void setPn(int pn) {
        this.pn = pn;
    }

    public int getPid() {
        return pid;
    }

    public int getAt() {
        return at;
    }

    public int getBt() {
        return bt;
    }

    public int getPn() {
        return pn;
    }
}

