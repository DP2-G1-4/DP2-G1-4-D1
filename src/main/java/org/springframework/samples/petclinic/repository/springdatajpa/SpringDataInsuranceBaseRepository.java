/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.repository.springdatajpa;


import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.InsuranceBase;
import org.springframework.samples.petclinic.repository.InsuranceBaseRepository;

public interface SpringDataInsuranceBaseRepository extends InsuranceBaseRepository, Repository<InsuranceBase, Integer> {

	@Query("SELECT insurance_base FROM InsuranceBase insurance_base WHERE insurance_base.id =?1")
	InsuranceBase findById(@Param("id") int id);
	
	@Query("SELECT insurance_base FROM InsuranceBase insurance_base")
	List<InsuranceBase> findAll();
	
	@Query("SELECT insurance_base FROM InsuranceBase insurance_base WHERE insurance_base.petType.id =?1")
	Collection<InsuranceBase> findByPetTypeId(@Param("id") int id);
	
}
