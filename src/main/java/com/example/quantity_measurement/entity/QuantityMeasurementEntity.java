package com.example.quantity_measurement.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "quantity_measurement") // ✅ FIXED TABLE NAME
public class QuantityMeasurementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "operation_type", nullable = false, length = 32)
    private String operationType;

    @Column(name = "first_operand_value")
    private Double firstOperandValue;

    @Column(name = "first_measurement_type", length = 32)
    private String firstMeasurementType;

    @Column(name = "first_unit", length = 32)
    private String firstUnit;

    @Column(name = "second_operand_value")
    private Double secondOperandValue;

    @Column(name = "second_measurement_type", length = 32)
    private String secondMeasurementType;

    @Column(name = "second_unit", length = 32)
    private String secondUnit;

    @Column(name = "result_operand_value")
    private Double resultOperandValue;

    @Column(name = "result_measurement_type", length = 32)
    private String resultMeasurementType;

    @Column(name = "result_unit", length = 32)
    private String resultUnit;

    @Column(name = "comparison_result")
    private Boolean comparisonResult;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    @Column(name = "successful", nullable = false)
    private Boolean successful;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public String getOperationType() {
        return operationType;
    }

    public Double getFirstOperandValue() {
        return firstOperandValue;
    }

    public String getFirstMeasurementType() {
        return firstMeasurementType;
    }

    public String getFirstUnit() {
        return firstUnit;
    }

    public Double getSecondOperandValue() {
        return secondOperandValue;
    }

    public String getSecondMeasurementType() {
        return secondMeasurementType;
    }

    public String getSecondUnit() {
        return secondUnit;
    }

    public Double getResultOperandValue() {
        return resultOperandValue;
    }

    public String getResultMeasurementType() {
        return resultMeasurementType;
    }

    public String getResultUnit() {
        return resultUnit;
    }

    public Boolean getComparisonResult() {
        return comparisonResult;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Boolean getSuccessful() {
        return successful;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public void setFirstOperandValue(Double firstOperandValue) {
        this.firstOperandValue = firstOperandValue;
    }

    public void setFirstMeasurementType(String firstMeasurementType) {
        this.firstMeasurementType = firstMeasurementType;
    }

    public void setFirstUnit(String firstUnit) {
        this.firstUnit = firstUnit;
    }

    public void setSecondOperandValue(Double secondOperandValue) {
        this.secondOperandValue = secondOperandValue;
    }

    public void setSecondMeasurementType(String secondMeasurementType) {
        this.secondMeasurementType = secondMeasurementType;
    }

    public void setSecondUnit(String secondUnit) {
        this.secondUnit = secondUnit;
    }

    public void setResultOperandValue(Double resultOperandValue) {
        this.resultOperandValue = resultOperandValue;
    }

    public void setResultMeasurementType(String resultMeasurementType) {
        this.resultMeasurementType = resultMeasurementType;
    }

    public void setResultUnit(String resultUnit) {
        this.resultUnit = resultUnit;
    }

    public void setComparisonResult(Boolean comparisonResult) {
        this.comparisonResult = comparisonResult;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public void setSuccessful(Boolean successful) {
        this.successful = successful;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}