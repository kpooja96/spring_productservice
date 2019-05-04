package com.capgemini.productservice.repository;

import java.util.List;
import java.util.Optional;

import com.capgemini.productservice.model.Product;

public interface ProductRepository {
	public static Optional<Product> findById(Integer id) {
		return null;
	}

	public static List<Product> findAll() {
		return null;
	}

	public static boolean update(Product product) {
		return false;
	}

	public static Product save(Product product) {
		return null;
	}

	public static boolean delete(Integer id) {
		return false;
	}

}
