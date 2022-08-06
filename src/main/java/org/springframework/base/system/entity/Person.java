package org.springframework.base.system.entity;

import java.io.Serializable;

public class Person implements Serializable {
    private static final long serialVersionUID = -3059966024160185918L;

    private String id;
    /* renamed from: id */
    private String f14id;
    private String createTime;
    private String createdate;
    private String username;
    private String password;
    private String realname;
    private String token;
    private String role;
    private String status;
    private String note;
    private String expiration;
    private String permission;
    private String datascope;
    private String clientId;
    private String lastLoginTime;
    private String lastLoginIp;
    private String failNumber;

    public String getCreatedate()
    {
        return this.createdate;
    }

    public void setCreatedate(String createdate)
    {
        this.createdate = createdate;
    }
    public String getFailNumber() {
        return this.failNumber;
    }

    public void setFailNumber(String failNumber) {
        this.failNumber = failNumber;
    }

    public String getClientId() {
        return this.clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getId() {
        return this.f14id;
    }

    public void setId(String id) {
        this.f14id = id;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRealname() {
        return this.realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getExpiration() {
        return this.expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public String getPermission() {
        return this.permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getDatascope() {
        return this.datascope;
    }

    public void setDatascope(String datascope) {
        this.datascope = datascope;
    }

    public String getLastLoginTime() {
        return this.lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getLastLoginIp() {
        return this.lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }
}