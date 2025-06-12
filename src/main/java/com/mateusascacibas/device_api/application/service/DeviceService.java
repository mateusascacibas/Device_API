package com.mateusascacibas.device_api.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mateusascacibas.device_api.application.dto.DeviceRequestDTO;
import com.mateusascacibas.device_api.application.dto.DeviceResponseDTO;
import com.mateusascacibas.device_api.application.exception.DeviceNotFoundException;
import com.mateusascacibas.device_api.application.mapper.DeviceMapper;
import com.mateusascacibas.device_api.domain.enumerator.StateEnum;
import com.mateusascacibas.device_api.domain.model.Device;
import com.mateusascacibas.device_api.infra.repository.DeviceRepository;

@Service
public class DeviceService {

	@Autowired
	private DeviceRepository deviceRepository;

	public DeviceResponseDTO createDevice(DeviceRequestDTO requestDTO) {
		Device device = DeviceMapper.toEntity(requestDTO);
		Device saved = deviceRepository.save(device);
		return DeviceMapper.toDTO(saved);
	}

	public DeviceResponseDTO updateDevice(Long id, DeviceRequestDTO requestDTO) {
		Device device = deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));

		validateUpdate(requestDTO, device);

		if (requestDTO.state() != null) {
			device.setState(requestDTO.state());
		}

		if (!StateEnum.IN_USE.equals(device.getState())) {
			if (isNotBlank(requestDTO.name())) {
				device.setName(requestDTO.name());
			}
			if (isNotBlank(requestDTO.brand())) {
				device.setBrand(requestDTO.brand());
			}
		}

		Device updated = deviceRepository.save(device);
		return DeviceMapper.toDTO(updated);
	}

	public DeviceResponseDTO findDeviceByID(Long id) {
		Device device = deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));
		return DeviceMapper.toDTO(device);
	}

	public List<DeviceResponseDTO> findAllDevices() {
		List<Device> devices = deviceRepository.findAll();
		return devices.stream().map(DeviceMapper::toDTO).toList();
	}

	public List<DeviceResponseDTO> findDevicesByBrand(String brand) {
		List<Device> devices = deviceRepository.findByBrand(brand);
		return devices.stream().map(DeviceMapper::toDTO).toList();
	}

	public List<DeviceResponseDTO> findDevicesByState(StateEnum state) {
		List<Device> devices = deviceRepository.findByState(state);
		List<DeviceResponseDTO> returnDevices = devices.stream().map(DeviceMapper::toDTO).toList();
		return returnDevices;
	}

	public void deleteDevice(Long id) {
		Device device = deviceRepository.findById(id).orElseThrow(() -> new DeviceNotFoundException(id));
		if (StateEnum.IN_USE.equals(device.getState())) {
			throw new IllegalArgumentException("Cannot delete device when state is IN_USE");
		}
		deviceRepository.delete(device);
	}

	private void validateUpdate(DeviceRequestDTO requestDTO, Device device) {
	    String newName = requestDTO.name() != null ? requestDTO.name() : device.getName();
	    String newBrand = requestDTO.brand() != null ? requestDTO.brand() : device.getBrand();

	    if (StateEnum.IN_USE.equals(device.getState())) {
	        boolean nameChanged = !newName.equals(device.getName());
	        boolean brandChanged = !newBrand.equals(device.getBrand());

	        if (nameChanged || brandChanged) {
	            throw new IllegalStateException("Cannot update name or brand when device is IN_USE");
	        }
	    }

	    if (requestDTO.name() != null && requestDTO.name().isBlank()) {
	        throw new IllegalArgumentException("Device name cannot be blank");
	    }

	    if (requestDTO.brand() != null && requestDTO.brand().isBlank()) {
	        throw new IllegalArgumentException("Device brand cannot be blank");
	    }
	}



	private boolean isNotBlank(String value) {
		return value != null && !value.trim().isEmpty();
	}

}
