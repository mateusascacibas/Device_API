package com.mateusascacibas.device_api.application.dto;

import java.time.LocalDateTime;

import com.mateusascacibas.device_api.domain.enumerator.StateEnum;

public record DeviceResponseDTO(Long id, String name, String brand, StateEnum state, LocalDateTime creationTime) {

}
