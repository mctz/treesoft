package org.springframework.base.system.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExportExcelUtils
{
    private static final Logger logger = LoggerFactory.getLogger(ExportExcelUtils.class);
    
    private ExportExcelUtils()
    {
        super();
    }
    
    public static void downloadExcel(HttpServletResponse response, List<Map<String, Object>> list)
    {
        WritableWorkbook book = null;
        response.reset();
        response.setCharacterEncoding("UTF-8");
        OutputStream os = null;
        try
        {
            response.setContentType("application/DOWLOAD");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=data.xls");
            os = response.getOutputStream();
            book = Workbook.createWorkbook(os);
        }
        catch (IOException e)
        {
            logger.error(e.getMessage(), e);
        }
        String value = "";
        try
        {
            WritableSheet sheet = book.createSheet("查询结果", 0);
            String key;
            if (CollectionUtils.isNotEmpty(list))
            {
                Map<String, Object> map4 = list.get(0);
                int z = 0;
                for (Map.Entry<String, Object> vo : map4.entrySet())
                {
                    key = vo.getKey();
                    sheet.addCell(new Label(z, 0, key));
                    z++;
                }
            }
            for (int i = 0; i < list.size(); i++)
            {
                Map<String, Object> map4 = list.get(i);
                int y = 0;
                for (Object vo : map4.entrySet())
                {
                    value = (String)((Map.Entry)vo).getValue();
                    if (((Map.Entry)vo).getValue() == null)
                    {
                        value = "";
                    }
                    else
                    {
                        value = (String)((Map.Entry)vo).getValue();
                    }
                    sheet.addCell(new Label(y, i + 1, value));
                    y++;
                }
            }
            book.write();
            book.close();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        finally
        {
            IOUtils.closeQuietly(os);
        }
    }
}
