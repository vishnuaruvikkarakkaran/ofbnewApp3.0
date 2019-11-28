package com.enfin.ofabee3.data.remote.model.payment.request;

public class PaymentRequestModel {

    /**
     * item_id : 46
     * item_type : 1
     * razorpay_signature :
     * razorpay_payment_id :
     * razorpay_order_id :
     */

    private String item_id;
    private String item_type;
    private String razorpay_signature;
    private String razorpay_payment_id;
    private String razorpay_order_id;

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getRazorpay_signature() {
        return razorpay_signature;
    }

    public void setRazorpay_signature(String razorpay_signature) {
        this.razorpay_signature = razorpay_signature;
    }

    public String getRazorpay_payment_id() {
        return razorpay_payment_id;
    }

    public void setRazorpay_payment_id(String razorpay_payment_id) {
        this.razorpay_payment_id = razorpay_payment_id;
    }

    public String getRazorpay_order_id() {
        return razorpay_order_id;
    }

    public void setRazorpay_order_id(String razorpay_order_id) {
        this.razorpay_order_id = razorpay_order_id;
    }
}
