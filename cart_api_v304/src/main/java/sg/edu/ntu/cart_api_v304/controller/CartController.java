package sg.edu.ntu.cart_api_v304.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    if (carts.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok().body(carts);
  }

  @PostMapping("/add/{productId}")
  public ResponseEntity<Cart> incrementQuantity(@PathVariable Integer productId, @RequestParam Optional<Integer> quantity) {
    Optional<Cart> cartToUpdateOptional = cartRepository.findByProductId(productId);
    if (!cartToUpdateOptional.isPresent()) {
      Optional<Product> productToAddOptional = productRepository.findById(productId);
      if (!productToAddOptional.isPresent()) {
        return ResponseEntity.badRequest().build();
      } else {
        Product productToAdd = productToAddOptional.get();
        Cart cartToAdd = new Cart();
        cartToAdd.setProduct(productToAdd);
        cartToAdd.setQuantity(quantity.orElseGet(() -> 1));
        cartRepository.save(cartToAdd);
        return ResponseEntity.ok().body(cartToAdd);
      }
    }
    Cart cartToUpdate = cartToUpdateOptional.get();
    cartToUpdate.setQuantity(quantity.orElseGet (() -> cartToUpdate.getQuantity() + 1));
    cartRepository.save(cartToUpdate);
    return ResponseEntity.ok().body(cartToUpdate);
  }

  @PostMapping("/decrement/{productId}")
  public ResponseEntity<Cart> decrementQuantity(@PathVariable Integer productId) {
    Optional<Cart> cartToUpdateOptional = cartRepository.findByProductId(productId);
    if (!cartToUpdateOptional.isPresent()) {
      return ResponseEntity.badRequest().build();
    }
    Cart cartToUpdate = cartToUpdateOptional.get();
    if (cartToUpdate.getQuantity() == 1) {
      cartRepository.deleteById(cartToUpdate.getId());
      return ResponseEntity.ok().build();
    }
    cartToUpdate.setQuantity(cartToUpdate.getQuantity() - 1);
    cartRepository.save(cartToUpdate);
    return ResponseEntity.ok().body(cartToUpdate);
  }

  @PostMapping("/clear")
  public ResponseEntity<List<Cart>> clear() {
    List<Cart> carts = (List<Cart>) cartRepository.findAll();
    if (carts.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }
    cartRepository.deleteAll();
    return ResponseEntity.ok().build();
  }
}
