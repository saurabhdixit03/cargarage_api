package com.example.cargarage.repository.common;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.cargarage.model.common.FaceliftKit;

@Repository
public interface FaceliftkitRepositoy extends JpaRepository<FaceliftKit, Long> {
    List<FaceliftKit> findByCarModel(String carModel);
}
