package com.mateusascacibas.device_api.application.mapper;

import com.mateusascacibas.device_api.application.dto.DeviceRequestDTO;
import com.mateusascacibas.device_api.application.dto.DeviceResponseDTO;
import com.mateusascacibas.device_api.domain.model.Device;

public class DeviceMapper {
	
	public static Device toEntity(DeviceRequestDTO dto) {
		Device device = new Device();
		device.setName(dto.name());
		device.setBrand(dto.brand());
		device.setState(dto.state());
		return device;
	}

	public static DeviceResponseDTO toDTO(Device device) {
		return new DeviceResponseDTO(device.getId(), device.getName(), device.getBrand(), device.getState(),
				device.getCreationTime());
	}
}
