package org.springframework.base.system.entity;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/entity/WeaverOA.class */
public class WeaverOA {
    private String id;
    private String requestId;
    private String ITdatabase;
    private String ITreason;
    private String lastname;
    private List<MultipartFile> attachments;

    public String getITdatabase() {
        return this.ITdatabase;
    }

    public void setITdatabase(String iTdatabase) {
        this.ITdatabase = iTdatabase;
    }

    public String getITreason() {
        return this.ITreason;
    }

    public void setITreason(String iTreason) {
        this.ITreason = iTreason;
    }

    public String getLastname() {
        return this.lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public List<MultipartFile> getAttachments() {
        return this.attachments;
    }

    public void setAttachments(List<MultipartFile> attachments) {
        this.attachments = attachments;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}
