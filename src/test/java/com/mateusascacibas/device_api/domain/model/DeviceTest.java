package com.mateusascacibas.device_api.domain.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.mateusascacibas.device_api.domain.enumerator.StateEnum;
import com.mateusascacibas.device_api.domain.model.Device;

public class DeviceTest {

	@Test
	void shouldCreateDevice() {
		Device device = Device.builder()
				.name("Table")
				.brand("Apple")
				.state(StateEnum.INACTIVE)
				.build();
		
		assertNull(device.getCreationTime());
		device.prePersist();
		assertNotNull(device.getCreationTime());
		assertTrue(device.getCreationTime().isBefore(LocalDateTime.now().plusSeconds(1)));
	}
}
