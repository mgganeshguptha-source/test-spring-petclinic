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

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Health check controller providing application health status including database
 * connectivity.
 *
 * @author PetClinic Team
 */
@RestController
@RequestMapping("/api/v1")
public class HealthController {

	private static final Logger LOG = LoggerFactory.getLogger(HealthController.class);

	private static final String STATUS_UP = "UP";

	private static final String STATUS_DOWN = "DOWN";

	private final VetRepository vetRepository;

	public HealthController(VetRepository vetRepository) {
		this.vetRepository = vetRepository;
	}

	/**
	 * Health check endpoint returning application health status including database
	 * connectivity.
	 * @return health response with component statuses
	 */
	@GetMapping("/health")
	public ResponseEntity<HealthResponse> health() {
		Map<String, HealthResponse.ComponentHealth> components = new LinkedHashMap<>();

		// Check database connectivity
		HealthResponse.ComponentHealth dbHealth = checkDatabaseHealth();
		components.put("database", dbHealth);

		// Check application status
		components.put("application", new HealthResponse.ComponentHealth(STATUS_UP, "Application is running"));

		// Determine overall status
		String overallStatus = components.values().stream().allMatch(c -> STATUS_UP.equals(c.getStatus())) ? STATUS_UP
				: STATUS_DOWN;

		HealthResponse response = new HealthResponse(overallStatus, Instant.now(), components);

		if (STATUS_DOWN.equals(overallStatus)) {
			return ResponseEntity.status(503).body(response);
		}

		return ResponseEntity.ok(response);
	}

	private HealthResponse.ComponentHealth checkDatabaseHealth() {
		try {
			// Perform a simple query to verify database connectivity
			vetRepository.findAll();
			return new HealthResponse.ComponentHealth(STATUS_UP, "Database connection is healthy");
		}
		catch (Exception ex) {
			LOG.error("Database health check failed", ex);
			return new HealthResponse.ComponentHealth(STATUS_DOWN, "Database connection failed: " + ex.getMessage());
		}
	}

}
