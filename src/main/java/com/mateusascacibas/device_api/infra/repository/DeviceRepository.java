package com.mateusascacibas.device_api.infra.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mateusascacibas.device_api.domain.enumerator.StateEnum;
import com.mateusascacibas.device_api.domain.model.Device;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long>{

	List<Device> findByBrand(String brand);
	List<Device> findByState(StateEnum state);
}
