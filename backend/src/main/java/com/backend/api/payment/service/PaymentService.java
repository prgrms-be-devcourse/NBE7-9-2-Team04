package com.backend.api.payment.service;


import com.backend.api.payment.dto.request.PaymentRequest;
import com.backend.domain.payment.entity.Payment;
import com.backend.domain.payment.entity.PaymentStatus;
import com.backend.domain.payment.repository.PaymentRepository;
import com.backend.domain.user.entity.User;
import com.backend.global.Rq.Rq;
import com.backend.global.exception.ErrorCode;
import com.backend.global.exception.ErrorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    @Value("${toss.payments.secret-key")
    private String secretKey;

    @Value(("${toss.payments.base-url}"))
    private String baseUrl;

    private final PaymentRepository paymentRepository;
    private final Rq rq;

    private final JSONParser parser = new JSONParser();


    //결제 승인
    @Transactional
    public Payment confirmPayment(PaymentRequest request) throws IOException {
        try {
            User user = rq.getUser(); // ✅ 현재 로그인 사용자 정보

            JSONObject body = new JSONObject();
            body.put("paymentKey", request.paymentKey());
            body.put("orderId", request.orderId());
            body.put("amount", request.amount());

            JSONObject response = sendPostRequest("/confirm", body);
            int code = ((Number) response.get("_statusCode")).intValue();

            if (code == 200) {

                Payment payment = Payment.builder()
                        .orderId((String) response.get("orderId"))
                        .paymentKey((String) response.get("paymentKey"))
                        .orderName((String) response.get("orderName"))
                        .method((String) response.get("method"))
                        .totalAmount(((Number) response.get("totalAmount")).longValue())
                        .status(PaymentStatus.DONE)
                        .approvedAt(LocalDateTime.now())
                        .user(user)
                        .build();

                return paymentRepository.save(payment);
            } else {
                throw new ErrorException(ErrorCode.PAYMENT_APPROVE_FAILED);
            }

        } catch (Exception e) {
            throw new ErrorException(ErrorCode.PAYMENT_APPROVE_FAILED);
        }
    }

    public JSONObject geyPaymentByKey(String paymentKey) throws IOException {
        try {
            JSONObject response = sendGetRequest("/" + paymentKey);
            int code = ((Number) response.get("_statusCode")).intValue();

            if (code == 200) {
                return response;
            } else {
                throw new ErrorException(ErrorCode.PAYMENT_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new ErrorException(ErrorCode.PAYMENT_NOT_FOUND);
        }
    }

    public JSONObject getPaymentByOrderId(String orderId) throws IOException {
        try {
            JSONObject response = sendGetRequest("/orders/" + orderId);
            int code = ((Number) response.get("_statusCode")).intValue();

            if (code == 200) {
                return response;
            } else {
                throw new ErrorException(ErrorCode.PAYMENT_NOT_FOUND);
            }
        } catch (Exception e) {
            throw new ErrorException(ErrorCode.PAYMENT_NOT_FOUND);
        }
    }

    //결제 취소 로직 추가해야함



    //POST 요청
    private JSONObject sendPostRequest(String path, JSONObject body) throws IOException, ParseException {
        String urlString = baseUrl + path;
        HttpURLConnection connection = createConnection(urlString, "POST");
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(body.toJSONString().getBytes(StandardCharsets.UTF_8));
        }

        return parseResponse(connection, path);

    }

    //GET 요청
    private JSONObject sendGetRequest(String path) throws IOException, ParseException {
        String urlString = baseUrl + path;
        HttpURLConnection connection = createConnection(urlString, "GET");
        return parseResponse(connection, path);
    }

    //connection 생성
    private HttpURLConnection createConnection(String urlString, String method) throws IOException {

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();


        String encodedAuth = encodeSecretKey(secretKey);

        connection.setRequestProperty("Authorization",  "Basic " + encodedAuth);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod(method);

        return connection;

    }

    //응답 파싱
    private JSONObject parseResponse(HttpURLConnection connection, String path) throws IOException, ParseException {
        int code = connection.getResponseCode();

        boolean isSuccess = code == 200;
        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        JSONObject jsonResponse = (JSONObject) parser.parse(new InputStreamReader(responseStream, StandardCharsets.UTF_8));
        responseStream.close();

        jsonResponse.put("_statusCode", code);

        return jsonResponse;

    }

    //secretKey 인코딩
    private String encodeSecretKey(String secretKey) {
        String raw = secretKey + ":";
        return Base64.getEncoder().encodeToString(raw.getBytes(StandardCharsets.UTF_8));
    }


}
