//package com.nhat.demoSpringbooRestApi.controllers;
//
//import com.paypal.api.payments.*;
//import com.paypal.base.rest.APIContext;
//import com.paypal.base.rest.PayPalRESTException;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.*;
//
//@RestController
//@RequestMapping("/api/paypal")
//public class PaymentController {
//
//    @Autowired
//    private APIContext apiContext;
//
//    @PostMapping("/create-payment")
//    public Map<String, Object> createPayment() {
//        Map<String, Object> response = new HashMap<>();
//
//        // Tạo đối tượng thanh toán
//        Amount amount = new Amount();
//        amount.setCurrency("USD");
//        amount.setTotal("1.00"); // ví dụ: 1 đô la
//
//        Transaction transaction = new Transaction();
//        transaction.setDescription("Description about the transaction");
//        transaction.setAmount(amount);
//
//        List<Transaction> transactions = new ArrayList<>();
//        transactions.add(transaction);
//
//        Payer payer = new Payer();
//        payer.setPaymentMethod("paypal");
//
//        Payment payment = new Payment();
//        payment.setIntent("sale");
//        payment.setPayer(payer);
//        payment.setTransactions(transactions);
//
//        RedirectUrls redirectUrls = new RedirectUrls();
//        redirectUrls.setCancelUrl("http://localhost:3000/cancel");
//        redirectUrls.setReturnUrl("http://localhost:8080/api/paypal/complete-payment");
//        payment.setRedirectUrls(redirectUrls);
//
//        try {
//            Payment createdPayment = payment.create(apiContext);
//            Iterator links = createdPayment.getLinks().iterator();
//            while (links.hasNext()) {
//                Links link = (Links) links.next();
//                if (link.getRel().equalsIgnoreCase("approval_url")) {
//                    response.put("approval_url", link.getHref());
//                }
//            }
//            response.put("paymentId", createdPayment.getId());
//        } catch (PayPalRESTException e) {
//            System.err.println(e.getDetails());
//        }
//
//        return response;
//    }
//
//    @GetMapping("/complete-payment")
//    public String completePayment(HttpServletRequest req) {
//        String paymentId = req.getParameter("paymentId");
//        String payerId = req.getParameter("PayerID");
//
//        if (paymentId == null || payerId == null) {
//            return "redirect:/error";
//        } else {
//            try {
//                Payment payment = new Payment();
//                payment.setId(paymentId);
//
//                PaymentExecution paymentExecution = new PaymentExecution();
//                paymentExecution.setPayerId(payerId);
//
//                Payment executedPayment = payment.execute(apiContext, paymentExecution);
//
//                if (executedPayment.getState().equals("approved")) {
//                    return "redirect:/success";
//                } else {
//                    return "redirect:/error";
//                }
//            } catch (PayPalRESTException e) {
//                System.err.println(e.getDetails());
//                return "redirect:/error";
//            }
//        }
//    }
//}
