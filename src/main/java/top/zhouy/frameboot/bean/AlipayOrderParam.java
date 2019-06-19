package top.zhouy.frameboot.bean;

/**
 * @author zhouYan
 * @date 2019/6/18 15:16
 */
public class AlipayOrderParam {

    /**
     * 商户订单号
     */
    private String out_trade_no;
    /**
     * 销售产品码
     */
    private String product_code;
    /**
     * 总金额
     */
    private String total_amount;
    /**
     * 订单标题
     */
    private String subject;
    /**
     * 该笔订单允许的最晚付款时间，逾期将关闭交易
     */
    private String timeout_express;
    /**
     * 公共校验参数
     */
    private String passback_params;

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTimeout_express() {
        return timeout_express;
    }

    public void setTimeout_express(String timeout_express) {
        this.timeout_express = timeout_express;
    }

    public String getPassback_params() {
        return passback_params;
    }

    public void setPassback_params(String passback_params) {
        this.passback_params = passback_params;
    }
}
