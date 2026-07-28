package com.tordoya.subo.device.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "anonymous_device")
public class AnonymousDevice {
    @Id
    @Column(name = "device_id", length = 128)
    private String deviceId;
}