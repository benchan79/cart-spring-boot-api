package sg.edu.ntu.cart_api_v304.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import sg.edu.ntu.cart_api_v304.entity.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {
  Optional<Product> findProductsByNameAndDescriptionAndPrice(String name, String description, Float price);
}
