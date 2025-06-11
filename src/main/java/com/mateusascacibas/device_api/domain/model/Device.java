package com.mateusascacibas.device_api.domain.model;

import java.time.LocalDateTime;

import com.mateusascacibas.device_api.domain.enumerator.StateEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "devices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Device {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String brand;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private StateEnum state;
	
	@Column(name = "creation_time", nullable = false, updatable = false)
	private LocalDateTime creationTime;
	
	@PrePersist
    public void prePersist() {
        this.creationTime = LocalDateTime.now();
    }	
}
