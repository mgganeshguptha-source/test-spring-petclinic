/*
 * Copyright 2012-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.system;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for {@link HealthController}.
 */
@WebMvcTest(HealthController.class)
class HealthControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private VetRepository vetRepository;

	@Test
	void healthShouldReturnStatusUpWhenDatabaseIsHealthy() throws Exception {
		when(vetRepository.findAll()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/v1/health"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.status").value("UP"))
			.andExpect(jsonPath("$.timestamp").exists())
			.andExpect(jsonPath("$.components.database.status").value("UP"))
			.andExpect(jsonPath("$.components.application.status").value("UP"));
	}

	@Test
	void healthShouldReturnStatusDownWhenDatabaseFails() throws Exception {
		when(vetRepository.findAll()).thenThrow(new DataAccessResourceFailureException("Connection refused"));

		mockMvc.perform(get("/api/v1/health"))
			.andExpect(status().isServiceUnavailable())
			.andExpect(jsonPath("$.status").value("DOWN"))
			.andExpect(jsonPath("$.components.database.status").value("DOWN"))
			.andExpect(jsonPath("$.components.database.details").exists())
			.andExpect(jsonPath("$.components.application.status").value("UP"));
	}

	@Test
	void healthResponseShouldContainAllComponents() throws Exception {
		when(vetRepository.findAll()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/api/v1/health"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.components").isMap())
			.andExpect(jsonPath("$.components.database").exists())
			.andExpect(jsonPath("$.components.application").exists());
	}

}
