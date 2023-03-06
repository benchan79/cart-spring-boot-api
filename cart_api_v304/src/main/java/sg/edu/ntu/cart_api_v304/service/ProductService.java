package sg.edu.ntu.cart_api_v304.service;

import org.springframework.stereotype.Service;

import sg.edu.ntu.cart_api_v304.entity.Product;

@Service
public class ProductService {
  
  public Product create() {
    Product p = new Product();
    p.setId(1);
    p.setName("Mock Name");
    p.setDescription("Mock Desc");
    p.setPrice(10f);
    return p;
  }
}
