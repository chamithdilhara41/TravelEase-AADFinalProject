package lk.ijse.traveleaseaadfinalproject.service;

import lk.ijse.traveleaseaadfinalproject.dto.PaymentDTO;

import java.util.List;
import java.util.Map;

public interface PaymentService {
    boolean savePayment(PaymentDTO paymentDTO);


    List<Map<String, Object>> getAllPayments();

}
