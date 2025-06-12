package com.mateusascacibas.device_api.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.mateusascacibas.device_api.domain.enumerator.StateEnum;

class DeviceTest {

    @Test
    void shouldCreateDevice() {
        Device device = new Device();
        device.setName("Table");
        device.setBrand("Apple");
        device.setState(StateEnum.INACTIVE);

        assertNull(device.getCreationTime());

        device.prePersist();

        assertNotNull(device.getCreationTime());
        assertTrue(device.getCreationTime().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}
