package com.example.quantity.repository;

import com.example.quantity.entity.QuantityMeasurementEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuantityMeasurementRepository extends JpaRepository<QuantityMeasurementEntity, Long> {
    List<QuantityMeasurementEntity> findAllByOrderByCreatedAtAsc();

    List<QuantityMeasurementEntity> findByUserEmailOrderByCreatedAtAsc(String userEmail);

    List<QuantityMeasurementEntity> findByOperationTypeOrderByCreatedAtAsc(String operationType);
}