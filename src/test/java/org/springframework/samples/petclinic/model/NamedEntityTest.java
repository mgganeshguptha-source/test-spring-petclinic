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
package org.springframework.samples.petclinic.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for {@link NamedEntity}.
 */
class NamedEntityTest {

	private NamedEntity namedEntity;

	@BeforeEach
	void setUp() {
		namedEntity = new NamedEntity();
	}

	@Test
	void toStringShouldReturnNameWhenNameIsSet() {
		String expectedName = "TestName";
		namedEntity.setName(expectedName);

		String result = namedEntity.toString();

		assertEquals(expectedName, result);
	}

	@Test
	void toStringShouldReturnNullLiteralWhenNameIsNull() {
		// name is null by default

		String result = namedEntity.toString();

		assertEquals("<null>", result);
	}

	@Test
	void toStringShouldReturnEmptyStringWhenNameIsEmpty() {
		namedEntity.setName("");

		String result = namedEntity.toString();

		assertEquals("", result);
	}

	@Test
	void toStringShouldReturnNameWithWhitespaceWhenNameContainsWhitespace() {
		String nameWithSpaces = "  Name With Spaces  ";
		namedEntity.setName(nameWithSpaces);

		String result = namedEntity.toString();

		assertEquals(nameWithSpaces, result);
	}

}
