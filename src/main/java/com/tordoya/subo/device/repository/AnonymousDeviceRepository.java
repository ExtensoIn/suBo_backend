package com.tordoya.subo.device.repository;

import com.tordoya.subo.device.model.AnonymousDevice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnonymousDeviceRepository extends JpaRepository<AnonymousDevice, String> {
}