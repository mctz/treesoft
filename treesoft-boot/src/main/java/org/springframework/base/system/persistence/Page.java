package org.springframework.base.system.persistence;


import java.util.List;

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

    }

    public Page(int pageSize) {

    }

    public Page(int pageNo, int pageSize, String orderBy, String order) {

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
    }

    public Page<T> pageNo(int thePageNo) {
        return this;
    }

    public int getPageSize() {
        return 0;
    }

    public void setPageSize(int pageSize) {
    }

    public Page<T> pageSize(int thePageSize) {
        return this;
    }

    public int getFirst() {
        return 0;
    }

    public String getOrderBy() {
        return "";
    }

    public void setOrderBy(String orderBy) {
    }

    public Page<T> orderBy(String theOrderBy) {
        return this;
    }

    public String getOrder() {
        return "";
    }

    public void setOrder(String order) {
    }

    public Page<T> order(String theOrder) {
        return this;
    }

    public boolean isOrderBySetted() {
        return false;
    }

    public boolean isAutoCount() {
        return false;
    }

    public void setAutoCount(boolean autoCount) {
    }

    public Page<T> autoCount(boolean theAutoCount) {
        return this;
    }

    public List<T> getResult() {
        return null;
    }

    public void setResult(List<T> result) {
    }

    public long getTotalCount() {
        return 0;
    }

    public void setTotalCount(long totalCount) {
    }

    public long getTotalPages() {
        return 0;
    }

    public boolean isHasNext() {
        return false;
    }

    public int getNextPage() {
        return 0;
    }

    public boolean isHasPre() {
        return false;
    }

    public int getPrePage() {
        return 0;
    }

    public String getOperator() {
        return "";
    }

    public void setOperator(String operator) {
    }
}
