package com.mateusascacibas.device_api.application.exception;

public class DeviceNotFoundException extends RuntimeException{

	public DeviceNotFoundException(Long id) {
		super("Device not found with id: " + id);
	}
}
