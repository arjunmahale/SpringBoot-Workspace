package com.erp.Controller;

import java.util.*;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.razorpay.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private static final String RAZORPAY_KEY_ID = "rzp_test_RJ0D2xD5yve2fO";  // ✅ your real key ID
    private static final String RAZORPAY_KEY_SECRET = "kzRvZCF47X8iBnpXfsyAZzXX"; // ✅ your real secret

    @PostMapping("/create-order")
    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> req) {
        Map<String, Object> response = new HashMap<>();

        try {
            RazorpayClient client = new RazorpayClient(RAZORPAY_KEY_ID, RAZORPAY_KEY_SECRET);

            // Amount must be in paise (so multiply by 100 if you pass in rupees)
            int amount = (int) req.get("amount");

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_" + UUID.randomUUID());

            Order order = client.orders.create(orderRequest);

            response.put("orderId", order.get("id"));
            response.put("amount", order.get("amount"));
            response.put("currency", order.get("currency"));
            response.put("razorpayKey", RAZORPAY_KEY_ID); // ✅ correct key passed to frontend

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}
