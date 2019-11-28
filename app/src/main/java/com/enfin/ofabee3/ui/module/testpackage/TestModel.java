package com.enfin.ofabee3.ui.module.testpackage;

public class TestModel implements TestContract.Model {

    private String data;

    @Override
    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String getData() {
        String msg = "H! \n";
        return msg + data;
    }
}
