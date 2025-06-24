package com.plexushr.model;

public class Designation {
    private int designationId;
    private String designationName;

    public Designation() {}

    public Designation(int designationId, String designationName) {
        this.designationId = designationId;
        this.designationName = designationName;
    }

    public int getDesignationId() {
        return designationId;
    }

    public void setDesignationId(int designationId) {
        this.designationId = designationId;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    @Override
    public String toString() {
        return "Designation{" +
               "designationId=" + designationId +
               ", designationName='" + designationName + '\'' +
               '}';
    }
}