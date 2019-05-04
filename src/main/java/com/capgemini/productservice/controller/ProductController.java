package com.capgemini.productservice.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.productservice.model.Product;
import com.capgemini.productservice.service.ProductService;

@RestController
public class ProductController {

	private static final Logger logger = LogManager.getLogger(ProductController.class);

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@GetMapping("/product/{id}")

	public ResponseEntity<?> getProduct(@PathVariable Integer id) {
		return ProductService.findById(id).map(product -> {

			try {
				return ResponseEntity.ok().eTag(Integer.toString(product.getVersion()))
						.location(new URI("/product/" + product.getId())).body(product);
			} catch (URISyntaxException e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}).orElse(ResponseEntity.notFound().build());

	}

	@GetMapping("/products")

	public Iterable<Product> getProducts() {
		return ProductService.findAll();
	}

	@PostMapping
	public ResponseEntity<Product> createProduct(@RequestBody Product product) {
		logger.info("Creating new product with name: {}, quantity: {}", product.getName(), product.getQuantity());

		Product newProduct = ProductService.save(product);
		try {
			// Build a created response
			return ResponseEntity.created(new URI("/product/" + newProduct.getId()))
					.eTag(Integer.toString(newProduct.getVersion())).body(newProduct);
		} catch (URISyntaxException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	public ResponseEntity<?> updateProduct(@RequestBody Product product, @PathVariable Integer id,
			@RequestHeader("If-Match") Integer ifMatch) {
		logger.info("Updating product with id: {}, name: {}, quantity: {}", id, product.getName(),
				product.getQuantity());

		Optional<Product> existingProduct = ProductService.findById(id);

		return existingProduct.map(p -> {

			logger.info("Product with ID: " + id + " has a version of " + p.getVersion() + ". Update is for If-Match: "
					+ ifMatch);

			if (!p.getVersion().equals(ifMatch)) {
				return ResponseEntity.status(HttpStatus.CONFLICT).build();
			}

			p.setName(product.getName());
			p.setQuantity(product.getQuantity());
			p.setVersion(p.getVersion() + 1);

			logger.info("Updating product with ID: " + p.getId() + " -> name=" + p.getName() + ", quantity="
					+ p.getQuantity() + ", version=" + p.getVersion());

			try {
				// Update the product and return an ok response
				if (ProductService.update(p)) {
					return ResponseEntity.ok().location(new URI("/product/" + p.getId()))
							.eTag(Integer.toString(p.getVersion())).body(p);
				} else {
					return ResponseEntity.notFound().build();
				}
			} catch (URISyntaxException e) {

				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}

		}).orElse(ResponseEntity.notFound().build());
	}

	@DeleteMapping
	public ResponseEntity<?> deleteProduct(@PathVariable Integer id) {

		logger.info("Deleting product with ID {}", id);
		Optional<Product> existingProduct = ProductService.findById(id);

		return existingProduct.map(p -> {

			if (ProductService.delete(p.getId())) {
				return ResponseEntity.ok().build();
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		}).orElse(ResponseEntity.notFound().build());
	}

}
