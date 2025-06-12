package com.mateusascacibas.device_api.application.dto;

import com.mateusascacibas.device_api.domain.enumerator.StateEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DeviceRequestDTO(@NotBlank(message = "Name cannot be blank") String name, @NotBlank(message = "Brand cannot be blank") String brand, @NotNull StateEnum state) {
}
