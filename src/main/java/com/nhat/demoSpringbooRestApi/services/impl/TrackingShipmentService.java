package com.nhat.demoSpringbooRestApi.services.impl;

import com.nhat.demoSpringbooRestApi.repositories.OrderRepository;
import com.trackingmore.TrackingMore;
import com.trackingmore.exception.TrackingMoreException;
import com.trackingmore.model.TrackingMoreResponse;
import com.trackingmore.model.courier.Courier;
import com.trackingmore.model.courier.DetectParams;
import com.trackingmore.model.tracking.CreateTrackingParams;
import com.trackingmore.model.tracking.GetTrackingResultsParams;
import com.trackingmore.model.tracking.Tracking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TrackingShipmentService {

    @Value("${trackingMore.apiKey}")
    private String apiKey;

//    private final WebClient trackingMoreWebClient = WebClient.create("https://api.trackingmore.com/v4/trackings");

    @Autowired
    private OrderRepository orderRepository;

    public void getAllCouriers() {
        try {
            TrackingMore trackingMore = new TrackingMore(apiKey);
            TrackingMoreResponse<List<Courier>> result = trackingMore.couriers.getAllCouriers();
            System.out.println(result.getMeta().getCode());
            List<Courier> couriers = result.getData();
            for (Courier courier : couriers) {
                String courierName = courier.getCourierName();
                String courierCode = courier.getCourierCode();
                System.out.println(courierName + "---" + courierCode);
            }
        } catch (TrackingMoreException e) {
            System.err.println("error：" + e.getMessage());
        } catch (IOException e) {
            System.err.println("error：" + e.getMessage());
        }
    }

    public void detectCouriers(String trackingNumber) {
        try {
            TrackingMore trackingMore = new TrackingMore(apiKey);
            DetectParams detectParams = new DetectParams();
            detectParams.setTrackingNumber(trackingNumber);
            TrackingMoreResponse<List<Courier>> result = trackingMore.couriers.detect(detectParams);
            System.out.println(result.getMeta().getCode());
            List<Courier> couriers = result.getData();
            for (Courier courier : couriers) {
                String courierName = courier.getCourierName();
                String courierCode = courier.getCourierCode();
                System.out.println(courierName + "---" + courierCode);
            }
        } catch (TrackingMoreException e) {
            System.err.println("error：" + e.getMessage());
        } catch (IOException e) {
            System.err.println("error：" + e.getMessage());
        }
    }

    public void createTracking(String trackingNumber, String courierCode) {
        try {
            TrackingMore trackingMore = new TrackingMore(apiKey);
            CreateTrackingParams createTrackingParams = new CreateTrackingParams();
            createTrackingParams.setTrackingNumber(trackingNumber);
            createTrackingParams.setCourierCode(courierCode);
            TrackingMoreResponse<Tracking> result = trackingMore.trackings.CreateTracking(createTrackingParams);
            System.out.println(result.getMeta().getCode());
            if (result.getData() != null) {
                Tracking trackings = result.getData();
                System.out.println(trackings);
                System.out.println(trackings.getTrackingNumber());
            }
        } catch (TrackingMoreException e) {
            System.err.println("error：" + e.getMessage());
        } catch (IOException e) {
            System.err.println("error：" + e.getMessage());
        }
    }

    public Tracking getTrackingByTrackingNumber(String trackingNumber1) {
        try {
            TrackingMore trackingMore = new TrackingMore(apiKey);
            GetTrackingResultsParams trackingParams = new GetTrackingResultsParams();
//            trackingParams.setTrackingNumbers("92612903029511573030094537,92612903029511573030094531");
            trackingParams.setTrackingNumbers(trackingNumber1);
//            trackingParams.setCourierCode("usps");
//            trackingParams.setCreatedDateMin("2023-08-23T06:00:00+00:00");
//            trackingParams.setCreatedDateMax("2023-09-18T06:00:00+00:00");
            TrackingMoreResponse<List<Tracking>> result = trackingMore.trackings.GetTrackingResults(trackingParams);
            System.out.println(result.getMeta().getCode());
            List<Tracking> trackings = result.getData();
//            for (Tracking tracking : trackings) {
//                String trackingNumber = tracking.getTrackingNumber();
//                String courierCode = tracking.getCourierCode();
//
//                System.out.println("Tracking Number: " + trackingNumber);
//                System.out.println("Courier Code: " + courierCode);
//            }
            return trackings.get(0);
        } catch (TrackingMoreException e) {
            System.err.println("error：" + e.getMessage());
        } catch (IOException e) {
            System.err.println("error：" + e.getMessage());
        }
        return null;
    }

    //    public void updateOrderStatusFromTrackingMore(String trackingNumber) {
//        String response = trackingMoreWebClient.get()
//                .uri("/get?tracking_numbers={trackingNumber}", trackingNumber)
//                .header("Tracking-Api-Key", apiKey)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        // Phân tích dữ liệu từ response và cập nhật
//        JSONObject jsonObject = new JSONObject(response);
//        String status = jsonObject.getJSONObject("data").getString("status");
//
//        Order order = orderRepository.findByTrackingNumber(trackingNumber).orElse(null);
//        if (order != null) {
//            order.setOrderStatus(status);
//            orderRepository.save(order);
//        }
//    }
//
//    public void createTracker(String trackingNumber , String courierCode) {
//        TrackingOrderRequestDTO trackingOrderRequestDTO = new TrackingOrderRequestDTO();
//        trackingOrderRequestDTO.setTracking_number(trackingNumber);
//        trackingOrderRequestDTO.setCourier_code(courierCode);
//        try {
//            String response = trackingMoreWebClient.post()
//                    .uri("/create")
////                    .header("Authorization", "Bearer " + apiKey)
//                    .header("Tracking-Api-Key", apiKey)
//                    .body(Mono.just(trackingOrderRequestDTO),TrackingOrderRequestDTO.class)
//                    .retrieve()
//                    .bodyToMono(String.class)
//                    .block();
//
//            // Parse và xử lý dữ liệu từ response ở đây
//            // Ví dụ:
//            JSONObject jsonObject = new JSONObject(response);
//            String status = jsonObject.getJSONObject("data").getString("delivery_status");
//        } catch (Exception e) {
//            e.printStackTrace();
//            // Xử lý các ngoại lệ phát sinh ở đây
//        }
//    }

}