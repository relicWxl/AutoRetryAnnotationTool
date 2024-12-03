//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.relic.retry.pojo.dto;

import java.util.List;

public class PageResult<T> {
    private PageQuery query;
    private boolean hasNextPage;
    private List<T> dataList;

    public static <T> PageResultBuilder<T> builder() {
        return new PageResultBuilder();
    }

    public PageQuery getQuery() {
        return this.query;
    }

    public boolean isHasNextPage() {
        return this.hasNextPage;
    }

    public List<T> getDataList() {
        return this.dataList;
    }

    public void setQuery(PageQuery query) {
        this.query = query;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof PageResult)) {
            return false;
        } else {
            PageResult<?> other = (PageResult)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label39: {
                    Object this$query = this.getQuery();
                    Object other$query = other.getQuery();
                    if (this$query == null) {
                        if (other$query == null) {
                            break label39;
                        }
                    } else if (this$query.equals(other$query)) {
                        break label39;
                    }

                    return false;
                }

                if (this.isHasNextPage() != other.isHasNextPage()) {
                    return false;
                } else {
                    Object this$dataList = this.getDataList();
                    Object other$dataList = other.getDataList();
                    if (this$dataList == null) {
                        if (other$dataList != null) {
                            return false;
                        }
                    } else if (!this$dataList.equals(other$dataList)) {
                        return false;
                    }

                    return true;
                }
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof PageResult;
    }



    public String toString() {
        return "PageResult(query=" + this.getQuery() + ", hasNextPage=" + this.isHasNextPage() + ", dataList=" + this.getDataList() + ")";
    }

    public PageResult() {
    }

    public PageResult(PageQuery query, boolean hasNextPage, List<T> dataList) {
        this.query = query;
        this.hasNextPage = hasNextPage;
        this.dataList = dataList;
    }

    public static class PageResultBuilder<T> {
        private PageQuery query;
        private boolean hasNextPage;
        private List<T> dataList;

        PageResultBuilder() {
        }

        public PageResultBuilder<T> query(PageQuery query) {
            this.query = query;
            return this;
        }

        public PageResultBuilder<T> hasNextPage(boolean hasNextPage) {
            this.hasNextPage = hasNextPage;
            return this;
        }

        public PageResultBuilder<T> dataList(List<T> dataList) {
            this.dataList = dataList;
            return this;
        }

        public PageResult<T> build() {
            return new PageResult(this.query, this.hasNextPage, this.dataList);
        }

        public String toString() {
            return "PageResult.PageResultBuilder(query=" + this.query + ", hasNextPage=" + this.hasNextPage + ", dataList=" + this.dataList + ")";
        }
    }
}
