package org.springframework.base.system.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.base.system.dbUtils.DBUtil;
import org.springframework.base.system.entity.Attach;
import org.springframework.base.system.utils.DateUtils;
import org.springframework.stereotype.Repository;

@Repository
/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/dao/FileUploadDao.class */
public class FileUploadDao {
    public boolean saveAttach(Attach attach) throws Exception {
        DBUtil db = new DBUtil();
        String sql = "insert into treesoft_attach ( id ,bid ,file_name ,file_ext, file_path , file_size , file_md5 , file_type , file_tag ,status , remark ,sort, create_user , create_time ) values ('" + attach.getId() + "','" + attach.getBid() + "','" + attach.getFileName() + "','" + attach.getFileExt() + "','" + attach.getFilePath() + "'," + attach.getFileSize() + ",'" + attach.getFileMd5() + "','" + attach.getFileType() + "','" + attach.getFileTag() + "'," + attach.getStatus() + ",'" + attach.getRemark() + "'," + attach.getSort() + ",'" + attach.getCreateUser() + "','" + DateUtils.getDateTime() + "' )";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean updateBid(List<String> attachIds, String bid) throws Exception {
        DBUtil db = new DBUtil();
        String ids = "";
        for (int i = 0; i < attachIds.size(); i++) {
            ids = String.valueOf(ids) + "'" + attachIds.get(i) + "',";
        }
        String sql = " update treesoft_attach set bid= '" + bid + "'  where id in (" + ids.substring(0, ids.length() - 1) + ")";
        boolean bl = db.do_update(sql);
        return bl;
    }

    public boolean fileDelete(String fileId) throws Exception {
        DBUtil db = new DBUtil();
        String sql1 = " select id, file_path as filePath from treesoft_attach where id= '" + fileId + "' ";
        List<Map<String, Object>> list = db.executeQuery(sql1);
        boolean bl = false;
        if (list.size() > 0) {
            String filePath = (String) list.get(0).get("filePath");
            File file = new File(filePath);
            file.delete();
            String sql2 = " delete from treesoft_attach where id= '" + fileId + "'";
            bl = db.do_update(sql2);
        }
        return bl;
    }

    public Attach selectAttachById(String id) throws Exception {
        DBUtil db = new DBUtil();
        String sql1 = " select * from treesoft_attach where id= '" + id + "'";
        List<Map<String, Object>> list = db.executeQuery(sql1);
        Map<String, Object> map = list.get(0);
        Attach attach = new Attach();
        attach.setId(map.get("id").toString());
        attach.setFileName(map.get("file_name").toString());
        attach.setFilePath(map.get("file_path").toString());
        attach.setFileTag(map.get("file_tag").toString());
        return attach;
    }

    public List<Attach> selectAttachsByBid(String bid) throws Exception {
        DBUtil db = new DBUtil();
        String sql1 = " select * from treesoft_attach where bid= '" + bid + "' order by create_time ";
        List<Map<String, Object>> list = db.executeQuery(sql1);
        List<Attach> attachList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            Attach attach = new Attach();
            attach.setId(map.get("id").toString());
            attach.setFileName(map.get("file_name").toString());
            attach.setFileTag(map.get("file_tag").toString());
            attach.setFilePath(map.get("file_path").toString());
            attachList.add(attach);
        }
        return attachList;
    }
}
