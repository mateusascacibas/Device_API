package com.mateusascacibas.device_api.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    void shouldCreateDeviceSuccessfully() {
        DeviceRequestDTO request = new DeviceRequestDTO("Phone", "Samsung", StateEnum.IN_USE);
        Device saved = Device.builder()
                .id(1L)
                .name("Phone")
                .brand("Samsung")
                .state(StateEnum.IN_USE)
                .build();

        when(deviceRepository.save(any(Device.class))).thenReturn(saved);

        DeviceResponseDTO response = deviceService.createDevice(request);

        assertNotNull(response);
        assertEquals("Phone", response.name());
        verify(deviceRepository, times(1)).save(any(Device.class));
    }

    @Test
    void shouldUpdateDeviceSuccessfully() {
        Device existing = Device.builder()
                .id(1L)
                .name("Phone")
                .brand("Samsung")
                .state(StateEnum.AVAILABLE)
                .build();

        Device updated = Device.builder()
                .id(1L)
                .name("Tablet")
                .brand("Samsung")
                .state(StateEnum.AVAILABLE)
                .build();
        
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(deviceRepository.save(any(Device.class))).thenReturn(updated);

        DeviceRequestDTO update = new DeviceRequestDTO("Tablet", "Samsung", StateEnum.AVAILABLE);
        DeviceResponseDTO response = deviceService.updateDevice(1L, update);
        
        assertNotNull(response);
        assertEquals("Tablet", response.name());
    }
    
    @Test
    void shouldThrowWhenUpdatingNameOfInUseDevice() {
        Device existing = Device.builder()
                .id(1L)
                .name("Phone")
                .brand("Samsung")
                .state(StateEnum.IN_USE)
                .build();

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));

        DeviceRequestDTO update = new DeviceRequestDTO("Tablet", "Samsung", StateEnum.IN_USE);

        assertThrows(IllegalStateException.class, () -> deviceService.updateDevice(1L, update));
    }

    @Test
    void shouldDeleteDeviceSuccessfully() {
        Device device = Device.builder()
                .id(1L)
                .name("Phone")
                .brand("Samsung")
                .state(StateEnum.INACTIVE)
                .build();

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        deviceService.deleteDevice(1L);

        verify(deviceRepository, times(1)).delete(device);
    }

    @Test
    void shouldThrowWhenDeletingInUseDevice() {
        Device device = Device.builder()
                .id(1L)
                .name("Phone")
                .brand("Samsung")
                .state(StateEnum.IN_USE)
                .build();

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        assertThrows(IllegalArgumentException.class, () -> deviceService.deleteDevice(1L));
    }

    @Test
    void shouldReturnAllDevices() {
        Device device = Device.builder()
                .id(1L)
                .name("Phone")
                .brand("Samsung")
                .state(StateEnum.IN_USE)
                .build();

        when(deviceRepository.findAll()).thenReturn(List.of(device));

        List<DeviceResponseDTO> result = deviceService.findAllDevices();

        assertEquals(1, result.size());
        assertEquals("Phone", result.get(0).name());
    }

    @Test
    void shouldReturnDeviceById() {
        Device device = Device.builder()
                .id(1L)
                .name("Phone")
                .brand("Samsung")
                .state(StateEnum.IN_USE)
                .build();

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        DeviceResponseDTO result = deviceService.findDeviceByID(1L);

        assertEquals("Phone", result.name());
    }
    
    @Test
    void shouldReturnDevicesByState() {
        StateEnum state = StateEnum.IN_USE;
        Device device1 = Device.builder()
                .id(1L)
                .name("Device A")
                .brand("Brand X")
                .state(state)
                .build();

        Device device2 = Device.builder()
                .id(2L)
                .name("Device B")
                .brand("Brand Y")
                .state(state)
                .build();

        when(deviceRepository.findByState(state)).thenReturn(List.of(device1, device2));

        List<DeviceResponseDTO> result = deviceService.findDevicesByState(state);

        assertEquals(2, result.size());
        assertEquals("Device A", result.get(0).name());
        assertEquals("Device B", result.get(1).name());
    }
    
    @Test
    void shouldReturnDevicesByBrand() {
    	StateEnum state = StateEnum.IN_USE;
    	String brand = "Test";
        Device device1 = Device.builder()
                .id(1L)
                .name("Device A")
                .brand(brand)
                .state(state)
                .build();

        Device device2 = Device.builder()
                .id(2L)
                .name("Device B")
                .brand(brand)
                .state(state)
                .build();

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
}
