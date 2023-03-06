package sg.edu.ntu.cart_api_v304.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import sg.edu.ntu.cart_api_v304.entity.Cart;
import sg.edu.ntu.cart_api_v304.entity.Product;
import sg.edu.ntu.cart_api_v304.repository.CartRepository;
import sg.edu.ntu.cart_api_v304.repository.ProductRepository;

@RestController
@RequestMapping("/carts")
public class CartController {

  @Autowired
  CartRepository cartRepository;

  @Autowired
  ProductRepository productRepository;

  @GetMapping
  public ResponseEntity<List<Cart>> findAll() {
    List<Cart> carts = (List<Cart>) cartRepository.findAll();
    if (carts.size() == 0) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().body(carts);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Cart> findById(@PathVariable Integer id) {
    Optional<Cart> cartOptional = cartRepository.findById(id);
    if (cartOptional.isPresent()) {
      return ResponseEntity.ok().body(cartOptional.get());
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping("/add/{productId}")
  public ResponseEntity<Cart> incrementQuantity(@PathVariable Integer productId) {
    Optional<Cart> cartToUpdateOptional = cartRepository.findByProductId(productId);
    if (!cartToUpdateOptional.isPresent()) {
        return ResponseEntity.notFound().build();
    }
    Cart cartToUpdate = cartToUpdateOptional.get();
    cartToUpdate.setQuantity(cartToUpdate.getQuantity() + 1);
    cartRepository.save(cartToUpdate);
    return ResponseEntity.ok().body(cartToUpdate);
  }

  @PostMapping("/decrement/{productId}")
  public ResponseEntity<Cart> decrementQuantity(@PathVariable Integer productId) {
    Optional<Cart> cartToUpdateOptional = cartRepository.findByProductId(productId);
    if (!cartToUpdateOptional.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    Cart cartToUpdate = cartToUpdateOptional.get();
    if (cartToUpdate.getQuantity() <= 0) {
      return ResponseEntity.badRequest().build();
    }
    cartToUpdate.setQuantity(cartToUpdate.getQuantity() - 1);
    cartRepository.save(cartToUpdate);
    return ResponseEntity.ok().body(cartToUpdate);
  }

  @PostMapping("/clear/{productId}")
  public ResponseEntity<Cart> clearQuantity(@PathVariable Integer productId) {
    Optional<Cart> cartToClearOptional = cartRepository.findByProductId(productId);
    if (!cartToClearOptional.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    Cart cartToClear = cartToClearOptional.get();
    // cartRepository.deleteById(cartToClear.getId());
    // return ResponseEntity.ok().build();
    cartToClear.setQuantity(0);
    cartRepository.save(cartToClear);
    return ResponseEntity.ok().body(cartToClear);
  }
}
