package com.mateusascacibas.device_api.web.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mateusascacibas.device_api.application.dto.DeviceRequestDTO;
import com.mateusascacibas.device_api.application.dto.DeviceResponseDTO;
import com.mateusascacibas.device_api.application.service.DeviceService;
import com.mateusascacibas.device_api.domain.enumerator.StateEnum;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/devices")
public class DeviceController {

	@Autowired
	private DeviceService deviceService;

	@PostMapping
	public ResponseEntity<DeviceResponseDTO> createDevice(@Valid @RequestBody DeviceRequestDTO request) {
		DeviceResponseDTO response = deviceService.createDevice(request);
		URI location = URI.create("/devices/" + response.id());
		return ResponseEntity.created(location).body(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<DeviceResponseDTO> updateDevice(@PathVariable Long id,
			@RequestBody DeviceRequestDTO request) {
		DeviceResponseDTO response = deviceService.updateDevice(id, request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<DeviceResponseDTO> findDeviceById(@PathVariable Long id) {
		DeviceResponseDTO response = deviceService.findDeviceByID(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping
	public ResponseEntity<List<DeviceResponseDTO>> findAllDevices() {
		List<DeviceResponseDTO> response = deviceService.findAllDevices();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/brand/{brand}")
	public ResponseEntity<List<DeviceResponseDTO>> findDevicesByBrand(@PathVariable String brand) {
		List<DeviceResponseDTO> response = deviceService.findDevicesByBrand(brand);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/state/{state}")
	public ResponseEntity<List<DeviceResponseDTO>> findDevicesByState(@PathVariable StateEnum state) {
		List<DeviceResponseDTO> response = deviceService.findDevicesByState(state);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteDevice(@PathVariable Long id) {
		deviceService.deleteDevice(id);
		return ResponseEntity.noContent().build();
	}
}
