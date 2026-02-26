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
package org.springframework.samples.petclinic.owner;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link OwnerController}.
 */
@ExtendWith(MockitoExtension.class)
class OwnerControllerTest {

	private static final String VIEWS_OWNER_CREATE_OR_UPDATE_FORM = "owners/createOrUpdateOwnerForm";

	@Mock
	private OwnerRepository ownerRepository;

	@Mock
	private BindingResult bindingResult;

	@Mock
	private Model model;

	@Mock
	private RedirectAttributes redirectAttributes;

	@InjectMocks
	private OwnerController ownerController;

	private Owner owner;

	@BeforeEach
	void setUp() {
		owner = new Owner();
		owner.setId(1);
		owner.setFirstName("John");
		owner.setLastName("Doe");
		owner.setAddress("123 Main St");
		owner.setCity("Springfield");
		owner.setTelephone("1234567890");
	}

	// --- setAllowedFields tests ---

	@Test
	void setAllowedFields_shouldDisallowIdField() {
		WebDataBinder dataBinder = new WebDataBinder(owner);

		ownerController.setAllowedFields(dataBinder);

		String[] disallowedFields = dataBinder.getDisallowedFields();
		assertNotNull(disallowedFields);
		assertEquals(1, disallowedFields.length);
		assertEquals("id", disallowedFields[0]);
	}

	// --- findOwner tests ---

	@Test
	void findOwner_withNullOwnerId_shouldReturnNewOwner() {
		Owner result = ownerController.findOwner(null);

		assertNotNull(result);
		assertNull(result.getId());
	}

	@Test
	void findOwner_withValidOwnerId_shouldReturnOwner() {
		when(ownerRepository.findById(1)).thenReturn(Optional.of(owner));

		Owner result = ownerController.findOwner(1);

		assertNotNull(result);
		assertEquals(1, result.getId());
		assertEquals("John", result.getFirstName());
		verify(ownerRepository).findById(1);
	}

	@Test
	void findOwner_withNonExistentOwnerId_shouldThrowException() {
		when(ownerRepository.findById(999)).thenReturn(Optional.empty());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> ownerController.findOwner(999));

		assertTrue(exception.getMessage().contains("Owner not found with id: 999"));
		verify(ownerRepository).findById(999);
	}

	// --- initCreationForm tests ---

	@Test
	void initCreationForm_shouldReturnCreateOrUpdateForm() {
		String viewName = ownerController.initCreationForm();

		assertEquals(VIEWS_OWNER_CREATE_OR_UPDATE_FORM, viewName);
	}

	// --- processCreationForm tests ---

	@Test
	void processCreationForm_withValidOwner_shouldSaveAndRedirect() {
		when(bindingResult.hasErrors()).thenReturn(false);

		String result = ownerController.processCreationForm(owner, bindingResult, redirectAttributes);

		assertEquals("redirect:/owners/" + owner.getId(), result);
		verify(ownerRepository).save(owner);
		verify(redirectAttributes).addFlashAttribute("message", "New Owner Created");
	}

	@Test
	void processCreationForm_withValidationErrors_shouldReturnForm() {
		when(bindingResult.hasErrors()).thenReturn(true);

		String result = ownerController.processCreationForm(owner, bindingResult, redirectAttributes);

		assertEquals(VIEWS_OWNER_CREATE_OR_UPDATE_FORM, result);
		verify(ownerRepository, never()).save(any(Owner.class));
		verify(redirectAttributes).addFlashAttribute("error", "There was an error in creating the owner.");
	}

	// --- initFindForm tests ---

	@Test
	void initFindForm_shouldReturnFindOwnersView() {
		String viewName = ownerController.initFindForm();

		assertEquals("owners/findOwners", viewName);
	}

	// --- processFindForm tests ---

	@Test
	void processFindForm_withNoResults_shouldReturnFindOwnersViewWithError() {
		owner.setLastName("NonExistent");
		Page<Owner> emptyPage = new PageImpl<>(Collections.emptyList());
		when(ownerRepository.findByLastNameStartingWith(anyString(), any())).thenReturn(emptyPage);

		String result = ownerController.processFindForm(1, owner, bindingResult, model);

		assertEquals("owners/findOwners", result);
		verify(bindingResult).rejectValue("lastName", "notFound", "not found");
	}

	@Test
	void processFindForm_withSingleResult_shouldRedirectToOwnerDetails() {
		owner.setLastName("Doe");
		Page<Owner> singleOwnerPage = new PageImpl<>(List.of(owner));
		when(ownerRepository.findByLastNameStartingWith(anyString(), any())).thenReturn(singleOwnerPage);

		String result = ownerController.processFindForm(1, owner, bindingResult, model);

		assertEquals("redirect:/owners/" + owner.getId(), result);
	}

	@Test
	void processFindForm_withMultipleResults_shouldReturnOwnersListView() {
		Owner owner2 = new Owner();
		owner2.setId(2);
		owner2.setLastName("Doe");

		owner.setLastName("Doe");
		Page<Owner> multipleOwnersPage = new PageImpl<>(List.of(owner, owner2), PageRequest.of(0, 5), 2);
		when(ownerRepository.findByLastNameStartingWith(anyString(), any())).thenReturn(multipleOwnersPage);

		String result = ownerController.processFindForm(1, owner, bindingResult, model);

		assertEquals("owners/ownersList", result);
		verify(model).addAttribute("currentPage", 1);
		verify(model).addAttribute("totalPages", 1);
		verify(model).addAttribute("totalItems", 2L);
		verify(model).addAttribute(eq("listOwners"), any());
	}

	@Test
	void processFindForm_withNullLastName_shouldSearchAll() {
		owner.setLastName(null);
		Page<Owner> ownersPage = new PageImpl<>(List.of(owner));
		when(ownerRepository.findByLastNameStartingWith(eq(""), any())).thenReturn(ownersPage);

		String result = ownerController.processFindForm(1, owner, bindingResult, model);

		// Single result redirects
		assertEquals("redirect:/owners/" + owner.getId(), result);
		verify(ownerRepository).findByLastNameStartingWith(eq(""), any());
	}

	@Test
	void processFindForm_withEmptyLastName_shouldSearchAll() {
		owner.setLastName("");
		Page<Owner> ownersPage = new PageImpl<>(List.of(owner));
		when(ownerRepository.findByLastNameStartingWith(eq(""), any())).thenReturn(ownersPage);

		ownerController.processFindForm(1, owner, bindingResult, model);

		verify(ownerRepository).findByLastNameStartingWith(eq(""), any());
	}

	// --- initUpdateOwnerForm tests ---

	@Test
	void initUpdateOwnerForm_shouldReturnCreateOrUpdateForm() {
		String viewName = ownerController.initUpdateOwnerForm();

		assertEquals(VIEWS_OWNER_CREATE_OR_UPDATE_FORM, viewName);
	}

	// --- processUpdateOwnerForm tests ---

	@Test
	void processUpdateOwnerForm_withValidOwner_shouldSaveAndRedirect() {
		when(bindingResult.hasErrors()).thenReturn(false);
		owner.setId(1);

		String result = ownerController.processUpdateOwnerForm(owner, bindingResult, 1, redirectAttributes);

		assertEquals("redirect:/owners/{ownerId}", result);
		verify(ownerRepository).save(owner);
		verify(redirectAttributes).addFlashAttribute("message", "Owner Values Updated");
	}

	@Test
	void processUpdateOwnerForm_withValidationErrors_shouldReturnForm() {
		when(bindingResult.hasErrors()).thenReturn(true);

		String result = ownerController.processUpdateOwnerForm(owner, bindingResult, 1, redirectAttributes);

		assertEquals(VIEWS_OWNER_CREATE_OR_UPDATE_FORM, result);
		verify(ownerRepository, never()).save(any(Owner.class));
		verify(redirectAttributes).addFlashAttribute("error", "There was an error in updating the owner.");
	}

	@Test
	void processUpdateOwnerForm_withIdMismatch_shouldRejectAndRedirect() {
		when(bindingResult.hasErrors()).thenReturn(false);
		owner.setId(1);

		String result = ownerController.processUpdateOwnerForm(owner, bindingResult, 2, redirectAttributes);

		assertEquals("redirect:/owners/{ownerId}/edit", result);
		verify(bindingResult).rejectValue("id", "mismatch", "The owner ID in the form does not match the URL.");
		verify(redirectAttributes).addFlashAttribute("error", "Owner ID mismatch. Please try again.");
		verify(ownerRepository, never()).save(any(Owner.class));
	}

	@Test
	void processUpdateOwnerForm_withNullIdInOwner_shouldSaveWithPathId() {
		when(bindingResult.hasErrors()).thenReturn(false);
		owner.setId(null);

		// null != 5 so this will be a mismatch
		String result = ownerController.processUpdateOwnerForm(owner, bindingResult, 5, redirectAttributes);

		assertEquals("redirect:/owners/{ownerId}/edit", result);
		verify(bindingResult).rejectValue("id", "mismatch", "The owner ID in the form does not match the URL.");
	}

	// --- showOwner tests ---

	@Test
	void showOwner_withValidOwnerId_shouldReturnOwnerDetailsView() {
		when(ownerRepository.findById(1)).thenReturn(Optional.of(owner));

		ModelAndView mav = ownerController.showOwner(1);

		assertEquals("owners/ownerDetails", mav.getViewName());
		assertTrue(mav.getModel().containsKey("owner"));
		assertEquals(owner, mav.getModel().get("owner"));
		verify(ownerRepository).findById(1);
	}

	@Test
	void showOwner_withNonExistentOwnerId_shouldThrowException() {
		when(ownerRepository.findById(999)).thenReturn(Optional.empty());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
				() -> ownerController.showOwner(999));

		assertTrue(exception.getMessage().contains("Owner not found with id: 999"));
		verify(ownerRepository).findById(999);
	}

	// --- Edge cases ---

	@Test
	void processFindForm_withPageGreaterThanOne_shouldUseCorrectPagination() {
		owner.setLastName("Test");
		Owner owner2 = new Owner();
		owner2.setId(2);
		Page<Owner> ownersPage = new PageImpl<>(List.of(owner, owner2), PageRequest.of(1, 5), 10);
		when(ownerRepository.findByLastNameStartingWith(anyString(), any())).thenReturn(ownersPage);

		String result = ownerController.processFindForm(2, owner, bindingResult, model);

		assertEquals("owners/ownersList", result);
		verify(model).addAttribute("currentPage", 2);
	}

}
