package top.zhouy.frameboot.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zhouy.frameboot.bean.AlipayOrderParam;
import top.zhouy.frameboot.config.AliPayConfiguration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付宝
 * @author zhouYan
 * @date 2019/6/18 15:11
 */
@Api(description = "pay")
@RestController
@RequestMapping("/pay")
public class PayController {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AliPayConfiguration aliPayConfiguration;

    @ApiOperation(value = "alipayUrl")
    @PostMapping("/alipayUrl")
    public String doPost(@ApiParam @RequestParam String outTradeNo,@ApiParam @RequestParam String productCode,@ApiParam @RequestParam String productName,@ApiParam @RequestParam String totalAmount) throws ServletException, IOException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(aliPayConfiguration.getGatewayUrl(), aliPayConfiguration.getAppID(), aliPayConfiguration.getMerchantPrivateKey(), aliPayConfiguration.getFormat(), aliPayConfiguration.getCharset(), aliPayConfiguration.getAlipayPublicKey(), aliPayConfiguration.getSignType());
        //创建API对应的request
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();

        //在公共参数中设置回跳和通知地址
        alipayRequest.setNotifyUrl(aliPayConfiguration.getNotifyUrl());
        //alipayRequest.setReturnUrl(aliPayConfig.getReturnUrl());

        AlipayOrderParam alipayOrderParam = new AlipayOrderParam();
        //唯一标识
        alipayOrderParam.setOut_trade_no(outTradeNo);
        alipayOrderParam.setProduct_code("FAST_INSTANT_TRADE_PAY");
        alipayOrderParam.setSubject(productName);
        alipayOrderParam.setTotal_amount(totalAmount);

        //alipayOrderParam.setTimeout_express(time_express);
        //alipayOrderParam.setPassback_params(urlEncodeOrderNum);
        //填充业务参数
        alipayRequest.setBizContent(JSON.toJSONString(alipayOrderParam));
        String payUrl = "";
        try {
        	//这里使用GET的方式，这样就能生成支付链接
            payUrl = alipayClient.pageExecute(alipayRequest, "GET").getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return payUrl;
    }

    /**
     * 支付宝支付异步回调
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "alipayNotify")
    @RequestMapping("/alipayNotify")
    public void alipayNotify(HttpServletRequest request, HttpServletResponse response) {
        System.err.println("支付宝进入异步通知");
        String resultFail = "fail";
        String resultSuccess = "success";

        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            /*try {
                valueStr = new String(valueStr.getBytes("ISO-8859-1"), "GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/
            params.put(name, valueStr);
        }

        log.info("params={}", params);

        //调用SDK验证签名
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, aliPayConfiguration.getAlipayPublicKey(), aliPayConfiguration.getCharset(), aliPayConfiguration.getSignType());
        } catch (AlipayApiException e) {
            log.error("【支付宝异步通知】支付宝回调通知失败 e={} params={}", e, params);
            responseBody(response, resultFail);
            return;
            //e.printStackTrace();
        }
        if (!signVerified) {
            log.error("【支付宝异步通知】验证签名错误 params={} ", params);
            responseBody(response, resultFail);
            return;
        }

        BigDecimal trade_price = new BigDecimal(request.getParameter("total_amount"));
        //商户订单号
        String out_trade_no = params.get("out_trade_no");
        //支付宝交易号
        String trade_no = params.get("trade_no");
        //交易状态
        String trade_status = params.get("trade_status");

        // 支付成功修改订单状态
        if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
            //业务处理，主要是更新订单状态
            responseBody(response, resultSuccess);
            return;
        }
        log.error("【支付宝异步通知】支付宝状态错误 params={} ", params);
        responseBody(response, resultFail);
        return;
    }

    private void responseBody(HttpServletResponse response, String contentBody) {
        try {
            response.setContentType("type=text/html;charset=UTF-8");
            String s = contentBody;
            response.getWriter().write(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    /**
     * 支付宝支付异步回调
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "returnUrl")
    @RequestMapping("/returnUrl")
    public String returnUrl(HttpServletRequest request, HttpServletResponse response) {
        Map requestParams = request.getParameterMap();
        log.info("支付宝return");
        return "已经支付成功";
    }
}
