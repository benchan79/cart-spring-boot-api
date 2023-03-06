package sg.edu.ntu.cart_api_v304.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.ResponseBody;
// import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import sg.edu.ntu.cart_api_v304.entity.Product;
import sg.edu.ntu.cart_api_v304.repository.ProductRepository;
import sg.edu.ntu.cart_api_v304.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

  @Autowired
  ProductService service;

  @Autowired
  ProductRepository productRepository;

  @GetMapping
  public ResponseEntity<List<Product>> findAll() {
    List<Product> products = (List<Product>) productRepository.findAll();
    if (products.size() == 0)
      return ResponseEntity.notFound().build();
    return ResponseEntity.ok().body(products);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> findById(@PathVariable Integer id) {
    Optional<Product> productOptional = productRepository.findById(id);
    if (productOptional.isPresent()) {
      return ResponseEntity.ok().body(productOptional.get());
    }
    return ResponseEntity.notFound().build();
  }

  @PostMapping
  public ResponseEntity<Product> create(@RequestBody Product product) {
    validateNewProduct(product);
    try {
      Product newProduct = productRepository.save(product);
      return ResponseEntity.created(null).body(newProduct);
    } catch (IllegalArgumentException iae) {
      iae.printStackTrace();
      return ResponseEntity.badRequest().build();
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.internalServerError().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Product> update(@RequestBody Product product, @PathVariable int id) {
    Optional<Product> productToUpdateOptional = productRepository.findById(id);
    if(!productToUpdateOptional.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    try {
      Product productToUpdate = productToUpdateOptional.get();
      if (product.getName() != null) {
        productToUpdate.setName(product.getName());
      }
      if (product.getDescription() != null) {
        productToUpdate.setDescription(product.getDescription());
      }
      if (product.getPrice() != null) {
        validatePrice(product.getPrice());
        productToUpdate.setPrice(product.getPrice());
      }
      Product updatedProduct = productRepository.save(productToUpdate);
      return ResponseEntity.ok().body(updatedProduct);
    } catch (Exception e) {
      e.printStackTrace();
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Product> delete(@PathVariable int id) {
    Optional<Product> productToDeleteOptional = productRepository.findById(id);
    if (!productToDeleteOptional.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    Product productToDelete = productToDeleteOptional.get();
    productRepository.delete(productToDelete);
    return ResponseEntity.ok().build();
  }

  private void validateNewProduct(Product product) {
    if (ObjectUtils.isEmpty(product.getName())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Product Name");
    }

    if (ObjectUtils.isEmpty(product.getPrice())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No Product Price");
    }

    validatePrice(product.getPrice());

    Optional<Product> existingProduct = productRepository.findProductsByNameAndDescriptionAndPrice(product.getName(),
        product.getDescription(), product.getPrice());
    if (existingProduct.isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Product exists already.");
    }
  }

  private void validatePrice(Float price) {
    if (price <= 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price cannot be zero or negative");
    }
  }  
}
