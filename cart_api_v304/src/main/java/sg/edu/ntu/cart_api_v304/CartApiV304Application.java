package sg.edu.ntu.cart_api_v304;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import sg.edu.ntu.cart_api_v304.helper.MinimumPayableCheckHelper;

@SpringBootApplication
public class CartApiV304Application {

	@Value("${MIN_PURCHASE}")
	float minimumPurchase;

	public static void main(String[] args) {
		SpringApplication.run(CartApiV304Application.class, args);
	}

	@Bean
	public MinimumPayableCheckHelper minimumPayableCheckHelper() {
		return new MinimumPayableCheckHelper(minimumPurchase);
	}
}
