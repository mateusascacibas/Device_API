package com.mateusascacibas.device_api.application.mapper;

import com.mateusascacibas.device_api.application.dto.DeviceRequestDTO;
import com.mateusascacibas.device_api.application.dto.DeviceResponseDTO;
import com.mateusascacibas.device_api.domain.model.Device;

public class DeviceMapper {

	public static Device toEntity(DeviceRequestDTO dto) {
		return Device.builder()
				.name(dto.name())
				.brand(dto.brand())
				.state(dto.state())
				.build();
	}
	
	public static DeviceResponseDTO toDTO(Device device) {
		return new DeviceResponseDTO(device.getId(), device.getName(), device.getBrand(), device.getState(), device.getCreationTime());
	}
}
