package org.springframework.base.system.persistence;

import com.google.common.collect.Lists;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/* loaded from: spring-tx-4.1.3.RELEASE.jar:org/springframework/base/system/persistence/Page.class */
public class Page<T> {
    public static final String ASC = "asc";
    public static final String DESC = "desc";
    protected int pageNo;
    protected int pageSize;
    protected String orderBy;
    protected String order;
    protected boolean autoCount;
    protected List<T> result;
    protected long totalCount;
    protected String columns;
    protected String primaryKey;
    protected String tableName;
    private String executeTime;
    private String operator;

    public Page() {
        this.pageNo = 1;
        this.pageSize = -1;
        this.orderBy = null;
        this.order = null;
        this.autoCount = true;
        this.result = Lists.newArrayList();
        this.totalCount = -1L;
    }

    public Page(int pageSize) {
        this.pageNo = 1;
        this.pageSize = -1;
        this.orderBy = null;
        this.order = null;
        this.autoCount = true;
        this.result = Lists.newArrayList();
        this.totalCount = -1L;
        this.pageSize = pageSize;
    }

    public Page(int pageNo, int pageSize, String orderBy, String order) {
        this.pageNo = 1;
        this.pageSize = -1;
        this.orderBy = null;
        this.order = null;
        this.autoCount = true;
        this.result = Lists.newArrayList();
        this.totalCount = -1L;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.orderBy = orderBy;
        this.order = order;
    }

    public String getExecuteTime() {
        return this.executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }

    public String getColumns() {
        return this.columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public String getPrimaryKey() {
        return this.primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public String getTableName() {
        return this.tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getPageNo() {
        return this.pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
        if (pageNo < 1) {
            this.pageNo = 1;
        }
    }

    public Page<T> pageNo(int thePageNo) {
        setPageNo(thePageNo);
        return this;
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Page<T> pageSize(int thePageSize) {
        setPageSize(thePageSize);
        return this;
    }

    public int getFirst() {
        return ((this.pageNo - 1) * this.pageSize) + 1;
    }

    public String getOrderBy() {
        return this.orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Page<T> orderBy(String theOrderBy) {
        setOrderBy(theOrderBy);
        return this;
    }

    public String getOrder() {
        return this.order;
    }

    public void setOrder(String order) {
        String lowcaseOrder = StringUtils.lowerCase(order);
        String[] orders = StringUtils.split(lowcaseOrder, ',');
        for (String orderStr : orders) {
            if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr)) {
                throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
            }
        }
        this.order = lowcaseOrder;
    }

    public Page<T> order(String theOrder) {
        setOrder(theOrder);
        return this;
    }

    public boolean isOrderBySetted() {
        return StringUtils.isNotBlank(this.orderBy) && StringUtils.isNotBlank(this.order);
    }

    public boolean isAutoCount() {
        return this.autoCount;
    }

    public void setAutoCount(boolean autoCount) {
        this.autoCount = autoCount;
    }

    public Page<T> autoCount(boolean theAutoCount) {
        setAutoCount(theAutoCount);
        return this;
    }

    public List<T> getResult() {
        return this.result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public long getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    /* JADX WARN: Type inference failed for: r0v12, types: [long] */
    /* JADX WARN: Type inference failed for: r0v5, types: [long] */
    public long getTotalPages() {
        if (this.totalCount < 0) {
            return -1L;
        }
        long c = this.totalCount / this.pageSize;
        if (this.totalCount % this.pageSize > 0) {
            c++;
        }
        return c;
    }

    public boolean isHasNext() {
        return ((long) (this.pageNo + 1)) <= getTotalPages();
    }

    public int getNextPage() {
        if (isHasNext()) {
            return this.pageNo + 1;
        }
        return this.pageNo;
    }

    public boolean isHasPre() {
        return this.pageNo - 1 >= 1;
    }

    public int getPrePage() {
        if (isHasPre()) {
            return this.pageNo - 1;
        }
        return this.pageNo;
    }

    public String getOperator() {
        return this.operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
}
