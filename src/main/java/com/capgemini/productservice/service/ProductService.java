package com.capgemini.productservice.service;

import java.util.Optional;

import com.capgemini.productservice.model.Product;

public interface ProductService {

	public static Optional<Product> findById(Integer id) {
		return null;
	}

	public static Iterable<Product> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public static Product save(Product product) {
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean delete(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean update(Product p) {
		// TODO Auto-generated method stub
		return false;
	}

	boolean delete(Integer id);

}
