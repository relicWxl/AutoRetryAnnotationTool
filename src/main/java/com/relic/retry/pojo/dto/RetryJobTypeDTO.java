//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.relic.retry.pojo.dto;

import java.util.List;

public class RetryJobTypeDTO {
    private String className;
    private String methodName;
    private List<String> paramClassNameList;

    public static RetryJobTypeDTOBuilder builder() {
        return new RetryJobTypeDTOBuilder();
    }

    public String getClassName() {
        return this.className;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public List<String> getParamClassNameList() {
        return this.paramClassNameList;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public void setParamClassNameList(List<String> paramClassNameList) {
        this.paramClassNameList = paramClassNameList;
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof RetryJobTypeDTO)) {
            return false;
        } else {
            RetryJobTypeDTO other = (RetryJobTypeDTO)o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                label47: {
                    Object this$className = this.getClassName();
                    Object other$className = other.getClassName();
                    if (this$className == null) {
                        if (other$className == null) {
                            break label47;
                        }
                    } else if (this$className.equals(other$className)) {
                        break label47;
                    }

                    return false;
                }

                Object this$methodName = this.getMethodName();
                Object other$methodName = other.getMethodName();
                if (this$methodName == null) {
                    if (other$methodName != null) {
                        return false;
                    }
                } else if (!this$methodName.equals(other$methodName)) {
                    return false;
                }

                Object this$paramClassNameList = this.getParamClassNameList();
                Object other$paramClassNameList = other.getParamClassNameList();
                if (this$paramClassNameList == null) {
                    if (other$paramClassNameList != null) {
                        return false;
                    }
                } else if (!this$paramClassNameList.equals(other$paramClassNameList)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof RetryJobTypeDTO;
    }


    public String toString() {
        return "RetryJobTypeDTO(className=" + this.getClassName() + ", methodName=" + this.getMethodName() + ", paramClassNameList=" + this.getParamClassNameList() + ")";
    }

    public RetryJobTypeDTO() {
    }

    public RetryJobTypeDTO(String className, String methodName, List<String> paramClassNameList) {
        this.className = className;
        this.methodName = methodName;
        this.paramClassNameList = paramClassNameList;
    }

    public static class RetryJobTypeDTOBuilder {
        private String className;
        private String methodName;
        private List<String> paramClassNameList;

        RetryJobTypeDTOBuilder() {
        }

        public RetryJobTypeDTOBuilder className(String className) {
            this.className = className;
            return this;
        }

        public RetryJobTypeDTOBuilder methodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public RetryJobTypeDTOBuilder paramClassNameList(List<String> paramClassNameList) {
            this.paramClassNameList = paramClassNameList;
            return this;
        }

        public RetryJobTypeDTO build() {
            return new RetryJobTypeDTO(this.className, this.methodName, this.paramClassNameList);
        }

        public String toString() {
            return "RetryJobTypeDTO.RetryJobTypeDTOBuilder(className=" + this.className + ", methodName=" + this.methodName + ", paramClassNameList=" + this.paramClassNameList + ")";
        }
    }
}
