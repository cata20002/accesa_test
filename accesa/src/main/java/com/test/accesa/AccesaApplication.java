package com.test.accesa;

import com.test.accesa.entity.Product;
import com.test.accesa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

import static com.test.accesa.service.ParsingService.parseCsvToProducts;

@SpringBootApplication
public class AccesaApplication {

	@Autowired
	private ProductRepository productRepository;

	public static void main(String[] args) {

		SpringApplication.run(AccesaApplication.class, args);

	}

}
