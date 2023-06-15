package com.henriqueardina.dscommerce.services.exceptions;

public class ResourceNotFoundExcepetion extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundExcepetion(String msg) {
		super(msg);
	}
}
