package com.example.quantity.service;

import com.example.quantity.dto.OperationRequestDTO;
import com.example.quantity.dto.QuantityDTO;
import com.example.quantity.dto.QuantityOperationResultDTO;
import com.example.quantity.entity.QuantityMeasurementEntity;
import java.util.List;

public interface IQuantityMeasurementService {
    QuantityOperationResultDTO convert(QuantityDTO source, String targetUnit, String userEmail);

    QuantityOperationResultDTO compare(QuantityDTO firstQuantity, QuantityDTO secondQuantity, String userEmail);

    QuantityOperationResultDTO add(QuantityDTO firstQuantity, QuantityDTO secondQuantity, String resultUnit, String userEmail);

    QuantityOperationResultDTO subtract(QuantityDTO firstQuantity, QuantityDTO secondQuantity, String resultUnit, String userEmail);

    QuantityOperationResultDTO multiply(QuantityDTO firstQuantity, QuantityDTO secondQuantity, String resultUnit, String userEmail);

    QuantityOperationResultDTO divide(QuantityDTO firstQuantity, QuantityDTO secondQuantity, String resultUnit, String userEmail);

    QuantityOperationResultDTO operate(OperationRequestDTO request, String userEmail);

    List<QuantityMeasurementEntity> getMeasurementHistory(String userEmail);

    List<QuantityMeasurementEntity> getMeasurementHistoryByOperation(String operationType, String userEmail);
}