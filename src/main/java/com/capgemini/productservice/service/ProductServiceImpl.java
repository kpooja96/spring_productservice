package com.capgemini.productservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.capgemini.productservice.model.Product;
import com.capgemini.productservice.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

	private final ProductRepository productRepository;

	public ProductServiceImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public boolean delete(Integer id) {
		// TODO Auto-generated method stub
		return false;
	}

	public Optional<Product> findById(Integer id) {
		return ProductRepository.findById(id);
	}

	public List<Product> findAll() {
		return ProductRepository.findAll();
	}

	public boolean update(Product product) {
		return ProductRepository.update(product);
	}

	public Product save(Product product) {
		product.setVersion(1);
		return ProductRepository.save(product);
	}

	public ProductRepository getProductRepository() {
		return productRepository;
	}

}
