package com.mateusascacibas.device_api.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.mateusascacibas.device_api.application.dto.DeviceRequestDTO;
import com.mateusascacibas.device_api.application.dto.DeviceResponseDTO;
import com.mateusascacibas.device_api.application.exception.DeviceNotFoundException;
import com.mateusascacibas.device_api.domain.enumerator.StateEnum;
import com.mateusascacibas.device_api.domain.model.Device;
import com.mateusascacibas.device_api.infra.repository.DeviceRepository;

class DeviceServiceTest {

	@Mock
	private DeviceRepository deviceRepository;

	@InjectMocks
	private DeviceService deviceService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	private Device createDevice(String name, String brand, StateEnum state) {
		Device device = new Device();
		device.setName(name);
		device.setBrand(brand);
		device.setState(state);
		return device;
	}

	@Test
	void shouldCreateDeviceSuccessfully() {
		DeviceRequestDTO request = new DeviceRequestDTO("Phone", "Samsung", StateEnum.IN_USE);
		Device saved = createDevice("Phone", "Samsung", StateEnum.IN_USE);

		when(deviceRepository.save(any(Device.class))).thenReturn(saved);

		DeviceResponseDTO response = deviceService.createDevice(request);

		assertNotNull(response);
		assertEquals("Phone", response.name());
		verify(deviceRepository, times(1)).save(any(Device.class));
	}

	@Test
	void shouldUpdateDeviceSuccessfully() {
		Device existing = createDevice("Phone", "Samsung", StateEnum.AVAILABLE);
		Device updated = createDevice("Tablet", "Samsung", StateEnum.AVAILABLE);

		when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));
		when(deviceRepository.save(any(Device.class))).thenReturn(updated);

		DeviceRequestDTO update = new DeviceRequestDTO("Tablet", "Samsung", StateEnum.AVAILABLE);
		DeviceResponseDTO response = deviceService.updateDevice(1L, update);

		assertNotNull(response);
		assertEquals("Tablet", response.name());
	}

	@Test
	void shouldThrowWhenUpdatingNameOfInUseDevice() {
		Device existing = createDevice("Phone", "Samsung", StateEnum.IN_USE);

		when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));

		DeviceRequestDTO update = new DeviceRequestDTO("Tablet", "Samsung", StateEnum.IN_USE);

		assertThrows(IllegalStateException.class, () -> deviceService.updateDevice(1L, update));
	}

	@Test
	void shouldDeleteDeviceSuccessfully() {
		Device device = createDevice("Phone", "Samsung", StateEnum.INACTIVE);

		when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

		deviceService.deleteDevice(1L);

		verify(deviceRepository, times(1)).delete(device);
	}

	@Test
	void shouldThrowWhenDeletingInUseDevice() {
		Device device = createDevice("Phone", "Samsung", StateEnum.IN_USE);

		when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

		assertThrows(IllegalArgumentException.class, () -> deviceService.deleteDevice(1L));
	}

	@Test
	void shouldReturnAllDevices() {
		Device device = createDevice("Phone", "Samsung", StateEnum.IN_USE);

		when(deviceRepository.findAll()).thenReturn(List.of(device));

		List<DeviceResponseDTO> result = deviceService.findAllDevices();

		assertEquals(1, result.size());
		assertEquals("Phone", result.get(0).name());
	}

	@Test
	void shouldReturnDeviceById() {
		Device device = createDevice("Phone", "Samsung", StateEnum.IN_USE);

		when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

		DeviceResponseDTO result = deviceService.findDeviceByID(1L);

		assertEquals("Phone", result.name());
	}

	@Test
	void shouldReturnDevicesByState() {
		StateEnum state = StateEnum.IN_USE;
		Device device1 = createDevice("Device A", "Brand X", state);
		Device device2 = createDevice("Device B", "Brand Y", state);

		when(deviceRepository.findByState(state)).thenReturn(List.of(device1, device2));

		List<DeviceResponseDTO> result = deviceService.findDevicesByState(state);

		assertEquals(2, result.size());
		assertEquals("Device A", result.get(0).name());
		assertEquals("Device B", result.get(1).name());
	}

	@Test
	void shouldReturnDevicesByBrand() {
		String brand = "Test";
		Device device1 = createDevice("Device A", brand, StateEnum.IN_USE);
		Device device2 = createDevice("Device B", brand, StateEnum.IN_USE);

		when(deviceRepository.findByBrand(brand)).thenReturn(List.of(device1, device2));

		List<DeviceResponseDTO> result = deviceService.findDevicesByBrand(brand);

		assertEquals(2, result.size());
		assertEquals("Device A", result.get(0).name());
		assertEquals("Device B", result.get(1).name());
	}

	@Test
	void shouldThrowWhenDeviceNotFound() {
		when(deviceRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(DeviceNotFoundException.class, () -> deviceService.findDeviceByID(1L));
	}

	@Test
	void shouldThrowWhenNameIsBlank() {
		Device existing = createDevice("Phone", "Samsung", StateEnum.AVAILABLE);

		when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));

		DeviceRequestDTO update = new DeviceRequestDTO("   ", "Samsung", StateEnum.AVAILABLE);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			deviceService.updateDevice(1L, update);
		});

		assertEquals("Device name cannot be blank", exception.getMessage());
	}

	@Test
	void shouldThrowWhenBrandIsBlank() {
		Device existing = createDevice("Phone", "Samsung", StateEnum.AVAILABLE);

		when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));

		DeviceRequestDTO update = new DeviceRequestDTO("Phone", "  ", StateEnum.AVAILABLE);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			deviceService.updateDevice(1L, update);
		});

		assertEquals("Device brand cannot be blank", exception.getMessage());
	}

	@Test
	void shouldNotThrowWhenNameAndBrandAreNullInInUseDevice() {
		Device existing = createDevice("Phone", "Samsung", StateEnum.IN_USE);

		when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));
		when(deviceRepository.save(any(Device.class))).thenReturn(existing);

		DeviceRequestDTO update = new DeviceRequestDTO(null, null, StateEnum.IN_USE);

		DeviceResponseDTO response = deviceService.updateDevice(1L, update);

		assertEquals("Phone", response.name());
		assertEquals("Samsung", response.brand());
	}

	@Test
	void shouldThrowWhenOnlyBrandChangesInInUseDevice() {
		Device existing = createDevice("Phone", "Samsung", StateEnum.IN_USE);
		when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));

		DeviceRequestDTO update = new DeviceRequestDTO(null, "Apple", StateEnum.IN_USE);

		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
			deviceService.updateDevice(1L, update);
		});

		assertEquals("Cannot update name or brand when device is IN_USE", exception.getMessage());
	}

	@Test
	void shouldThrowWhenOnlyNameChangesInInUseDevice() {
		Device existing = createDevice("Phone", "Samsung", StateEnum.IN_USE);
		when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));

		DeviceRequestDTO update = new DeviceRequestDTO("Tablet", null, StateEnum.IN_USE);

		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
			deviceService.updateDevice(1L, update);
		});

		assertEquals("Cannot update name or brand when device is IN_USE", exception.getMessage());
	}

	@Test
	void shouldReturnAccurateDtoFromEntity() {
		Device device = createDevice("Phone", "Samsung", StateEnum.IN_USE);
		when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

		DeviceResponseDTO expected = new DeviceResponseDTO(1L, "Phone", "Samsung", StateEnum.IN_USE,
				device.getCreationTime());
		DeviceResponseDTO actual = deviceService.findDeviceByID(1L);

		assertEquals(expected.name(), actual.name());
		assertEquals(expected.brand(), actual.brand());
		assertEquals(expected.state(), actual.state());
	}
	
	@Test
	void shouldIgnoreStateWhenNullInUpdate() {
	    Device existing = createDevice("Phone", "Samsung", StateEnum.AVAILABLE);
	    when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));
	    when(deviceRepository.save(any(Device.class))).thenReturn(existing);

	    DeviceRequestDTO update = new DeviceRequestDTO("Phone", "Samsung", null); // state null

	    DeviceResponseDTO response = deviceService.updateDevice(1L, update);

	    assertEquals(StateEnum.AVAILABLE, response.state());
	}


}