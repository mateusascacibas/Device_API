package com.mateusascacibas.device_api.application.dto;

import com.mateusascacibas.device_api.domain.enumerator.StateEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeviceRequestDTO(@NotBlank String name, @NotBlank String brand, @NotNull StateEnum state) {
}
