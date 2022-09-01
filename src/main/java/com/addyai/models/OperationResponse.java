package com.addyai.models;

public class OperationResponse {
    private long id;

    private String name;

    private boolean operationSuccessful;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOperationSuccessful() {
        return operationSuccessful;
    }

    public void setOperationSuccessful(boolean operationSuccessful) {
        this.operationSuccessful = operationSuccessful;
    }
}
