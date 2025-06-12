package com.mateusascacibas.device_api.web.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mateusascacibas.device_api.application.dto.DeviceRequestDTO;
import com.mateusascacibas.device_api.application.dto.DeviceResponseDTO;
import com.mateusascacibas.device_api.application.service.DeviceService;
import com.mateusascacibas.device_api.domain.enumerator.StateEnum;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/devices")
@Tag(name = "Device Controller", description = "Handles all device operations such as creation, retrieval, update, and deletion")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Operation(
        summary = "Create a new device",
        description = "Registers a new device with the given name, brand, and state.",
        responses = {
            @ApiResponse(responseCode = "201", description = "Device successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
        }
    )
    @PostMapping
    public ResponseEntity<DeviceResponseDTO> createDevice(
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Device creation payload",
                required = true,
                content = @Content(
                    examples = @ExampleObject(
                        name = "Example Device",
                        value = "{\"name\":\"Tablet\",\"brand\":\"Apple\",\"state\":\"AVAILABLE\"}"
                    )
                )
            )
            DeviceRequestDTO request) {
        DeviceResponseDTO response = deviceService.createDevice(request);
        URI location = URI.create("/devices/" + response.id());
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Update an existing device", description = "Updates the attributes of an existing device by ID")
    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponseDTO> updateDevice(
            @Parameter(description = "ID of the device to update") @PathVariable Long id,
            @RequestBody DeviceRequestDTO request) {
        DeviceResponseDTO response = deviceService.updateDevice(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Find device by ID", description = "Retrieves a device using its unique ID")
    @GetMapping("/{id}")
    public ResponseEntity<DeviceResponseDTO> findDeviceById(
            @Parameter(description = "ID of the device to retrieve") @PathVariable Long id) {
        DeviceResponseDTO response = deviceService.findDeviceByID(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get all devices", description = "Retrieves a list of all registered devices")
    @GetMapping
    public ResponseEntity<List<DeviceResponseDTO>> findAllDevices() {
        List<DeviceResponseDTO> response = deviceService.findAllDevices();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get devices by brand", description = "Retrieves devices filtered by brand")
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<DeviceResponseDTO>> findDevicesByBrand(
            @Parameter(description = "Brand name to filter devices") @PathVariable String brand) {
        List<DeviceResponseDTO> response = deviceService.findDevicesByBrand(brand);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get devices by state", description = "Retrieves devices filtered by their state")
    @GetMapping("/state/{state}")
    public ResponseEntity<List<DeviceResponseDTO>> findDevicesByState(
            @Parameter(description = "State to filter devices (AVAILABLE, IN_USE, INACTIVE)") @PathVariable StateEnum state) {
        List<DeviceResponseDTO> response = deviceService.findDevicesByState(state);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete a device", description = "Deletes a device by ID if not in use")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDevice(
            @Parameter(description = "ID of the device to delete") @PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
