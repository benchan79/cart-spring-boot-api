package sg.edu.ntu.cart_api_v304.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import sg.edu.ntu.cart_api_v304.entity.Cart;

public interface CartRepository extends CrudRepository<Cart, Integer> {
  Optional<Cart> findByProductId(Integer productId);
}
