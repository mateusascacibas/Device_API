package com.mateusascacibas.device_api.domain.enumerator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.mateusascacibas.device_api.domain.enumerator.StateEnum;

class StateEnumTest {

	@Test
	void shouldReturnEnumByName() {
		StateEnum inactive = StateEnum.valueOf("INACTIVE");
		assertEquals(StateEnum.INACTIVE, inactive);
	}
	
	@Test
	void shouldListAllEnumValues() {
		StateEnum[] values = StateEnum.values();
		assertTrue(values.length > 0);
		assertTrue(List.of(values).contains(StateEnum.INACTIVE));
	}
}
