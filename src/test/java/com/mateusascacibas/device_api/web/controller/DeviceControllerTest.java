package com.mateusascacibas.device_api.web.controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mateusascacibas.device_api.application.dto.DeviceRequestDTO;
import com.mateusascacibas.device_api.application.dto.DeviceResponseDTO;
import com.mateusascacibas.device_api.application.service.DeviceService;
import com.mateusascacibas.device_api.domain.enumerator.StateEnum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

class DeviceControllerTest {

	private MockMvc mockMvc;

	@Mock
	private DeviceService deviceService;

	@InjectMocks
	private DeviceController deviceController;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(deviceController).build();
	}

	@Test
	void shouldCreateDeviceSuccessfully() throws Exception {
		DeviceRequestDTO request = new DeviceRequestDTO("Tablet", "Apple", StateEnum.AVAILABLE);
		DeviceResponseDTO response = new DeviceResponseDTO(1L, "Tablet", "Apple", StateEnum.AVAILABLE,
				LocalDateTime.now());

		when(deviceService.createDevice(request)).thenReturn(response);

		mockMvc.perform(post("/devices").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated())
				.andExpect(header().string("Location", "/devices/1")).andExpect(jsonPath("$.id").value(1));
	}

	@Test
	void shouldUpdateDeviceSuccessfully() throws Exception {
		DeviceRequestDTO request = new DeviceRequestDTO("Tablet", "Apple", StateEnum.AVAILABLE);
		DeviceResponseDTO response = new DeviceResponseDTO(1L, "Tablet", "Apple", StateEnum.AVAILABLE,
				LocalDateTime.now());

		when(deviceService.updateDevice(1L, request)).thenReturn(response);

		mockMvc.perform(put("/devices/1").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Tablet"));
	}

	@Test
	void shouldFindDeviceByIdSuccessfully() throws Exception {
		DeviceResponseDTO response = new DeviceResponseDTO(1L, "Tablet", "Apple", StateEnum.AVAILABLE,
				LocalDateTime.now());

		when(deviceService.findDeviceByID(1L)).thenReturn(response);

		mockMvc.perform(get("/devices/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1));
	}

	@Test
	void shouldReturnAllDevices() throws Exception {
		List<DeviceResponseDTO> devices = List.of(
				new DeviceResponseDTO(1L, "Tablet", "Apple", StateEnum.AVAILABLE, LocalDateTime.now()),
				new DeviceResponseDTO(2L, "Phone", "Samsung", StateEnum.IN_USE, LocalDateTime.now()));

		when(deviceService.findAllDevices()).thenReturn(devices);

		mockMvc.perform(get("/devices")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2));
	}

	@Test
	void shouldReturnDevicesByBrand() throws Exception {
		List<DeviceResponseDTO> devices = List
				.of(new DeviceResponseDTO(1L, "Tablet", "Apple", StateEnum.AVAILABLE, LocalDateTime.now()));

		when(deviceService.findDevicesByBrand("Apple")).thenReturn(devices);

		mockMvc.perform(get("/devices/brand/Apple")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].brand").value("Apple"));
	}

	@Test
	void shouldReturnDevicesByState() throws Exception {
		List<DeviceResponseDTO> devices = List
				.of(new DeviceResponseDTO(1L, "Tablet", "Apple", StateEnum.AVAILABLE, LocalDateTime.now()));

		when(deviceService.findDevicesByState(StateEnum.AVAILABLE)).thenReturn(devices);

		mockMvc.perform(get("/devices/state/AVAILABLE")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].state").value("AVAILABLE"));
	}

	@Test
	void shouldDeleteDeviceSuccessfully() throws Exception {
		doNothing().when(deviceService).deleteDevice(1L);

		mockMvc.perform(delete("/devices/1")).andExpect(status().isNoContent());
	}
}
