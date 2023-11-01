package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.services.OrderService;
import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalService {

    private APIContext apiContext;

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.mode}")
    private String mode;

    @Autowired
    OrderService orderService;

    @PostConstruct
    public void init() {
        apiContext = new APIContext(clientId, clientSecret, mode);
    }

    public Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {

        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }


    public void confirmPayment(String paymentId, String payerId , int orderId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        Payment confirmedPayment = payment.execute(apiContext, paymentExecute);

        if ("approved".equals(confirmedPayment.getState().toLowerCase())) {
            orderService.updatePaymentStatus(orderId,"PAID"); // giả định rằng bạn sử dụng paymentId như ID đơn hàng
        }
    }

}

//code REactJS
//import React, { useState, useEffect } from 'react';
//        import { PayPalScriptProvider, PayPalButtons } from "@paypal/react-paypal-js";
//        import axios from 'axios';
//
//        const PaymentComponent = () => {
//        const [orderID, setOrderID] = useState(null);
//        const [isPaid, setIsPaid] = useState(false);
//
//        const handleCreateOrder = (data, actions) => {
//        return axios.post('/api/orders/create-payment', {
//        // Điền thông tin cho yêu cầu tại đây
//        // Ví dụ:
//        total: 100.00,
//        currency: 'USD',
//        method: 'paypal',
//        intent: 'sale',
//        description: 'Test Payment',
//        cancelUrl: 'http://yourwebsite.com/cancel',
//        successUrl: 'http://yourwebsite.com/success'
//        }).then(response => {
//        return response.data;  // Trả về order ID từ response
//        });
//        }
//
//        const handleApprove = (data, actions) => {
//        // Gọi API để xác nhận việc thanh toán
//        axios.post('/api/orders/confirm-payment', {
//        paymentId: data.orderID,
//        payerId: data.payerID
//        }).then(response => {
//        setIsPaid(true);
//        // Bạn cũng có thể thêm các xử lý khác sau khi thanh toán thành công
//        }).catch(error => {
//        console.error('Thanh toán bị lỗi:', error);
//        // Xử lý lỗi tại đây
//        });
//        }
//
//        return (
//<div>
//            {!isPaid ? (
//<PayPalScriptProvider options={{ "client-id": "YOUR_PAYPAL_CLIENT_ID" }}>
//<PayPalButtons
//                        createOrder={handleCreateOrder}
//                                onApprove={handleApprove}
//                                />
//</PayPalScriptProvider>
//        ) : (
//<div>Thanh toán thành công. Cảm ơn bạn!</div>
//        )}
//</div>
//        );
//        }
//
//        export default PaymentComponent;

