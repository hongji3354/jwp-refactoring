package kitchenpos.product.dto;

import java.math.BigDecimal;

import kitchenpos.product.domain.Product;

public class ProductRequest {
	private String name;

	private BigDecimal price;

	public Product toEntity() {
		return new Product(name, price);
	}

	public String getName() {
		return name;
	}

	public BigDecimal getPrice() {
		return price;
	}
}