package com.henriqueardina.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.henriqueardina.dscommerce.dto.ProductDTO;
import com.henriqueardina.dscommerce.entities.Product;
import com.henriqueardina.dscommerce.repositories.ProductRepository;
import com.henriqueardina.dscommerce.services.exceptions.DataBaseException;
import com.henriqueardina.dscommerce.services.exceptions.ResourceNotFoundExcepetion;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	@Autowired
	private ProductRepository repository;
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		

		Product product = repository.findById(id).orElseThrow(() -> new ResourceNotFoundExcepetion("Recurso não encontrado"));
		return new ProductDTO(product);

	}
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(Pageable pageable) {
		Page<Product> result = repository.findAll(pageable);
		return result.map(x -> new ProductDTO(x));
	}
	
	@Transactional
	public ProductDTO insert(ProductDTO dto) {
			Product entity = new Product();
			copyDtoToEntuty(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity);
	}
	
	@Transactional
	public ProductDTO update(Long id,ProductDTO dto) {
		try {
			Product entity = repository.getReferenceById(id);
			copyDtoToEntuty(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity);
		}
		catch(EntityNotFoundException e) {
			throw new ResourceNotFoundExcepetion("Recurso não encontrado");
		}
	}

	private void copyDtoToEntuty(ProductDTO dto, Product entity) {
		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		
	}
	
	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundExcepetion("Recurso não encontrado");
		}
		try {
	        	repository.deleteById(id);    		
		}
	    	catch (DataIntegrityViolationException e) {
	        	throw new DataBaseException("Falha de integridade referencial");
	   	}
	}
}
