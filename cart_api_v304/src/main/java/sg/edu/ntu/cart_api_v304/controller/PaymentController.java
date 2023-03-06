package sg.edu.ntu.cart_api_v304.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import sg.edu.ntu.cart_api_v304.helper.MinimumPayableCheckHelper;

import java.text.DecimalFormat;

@RestController
public class PaymentController {

  @Value("${MIN_PURCHASE}")
  float minimumPurchase;

  final DecimalFormat df = new DecimalFormat("0.00");

  @Autowired
  MinimumPayableCheckHelper helper;

  Logger logger = LogManager.getLogger(this.getClass());

  @RequestMapping(value="/payment", method=RequestMethod.POST, produces="application/json", consumes="application/json")
  public ResponseEntity<ResponseMessage> pay(@RequestParam float payable) {
      logger.info("POST /payment?payable={}", payable);
      if(payable > minimumPurchase){            
          return ResponseEntity.ok(new ResponseMessage("Payment successful"));
      }else{
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Payable fail because minimum payable is $"+df.format(minimumPurchase)));
      }
          
  }

  @RequestMapping(value="/payment_2", method=RequestMethod.POST, produces="application/json", consumes="application/json")
  public ResponseEntity<ResponseMessage> payMethodTwo(@RequestParam float payable) throws Exception {
      logger.info("POST /payment_2?payable={}", payable);
      try{
          if(helper.hasMinimumPayable(payable))
              return ResponseEntity.ok(new ResponseMessage("Payment successful"));
          else
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Payable fail because minimum payable is $"+df.format(minimumPurchase)));
      }catch(Exception e){
          logger.error("POST /payable_2?payable={} encountered error", payable, e);
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Invalid input - payable cannot be lesser than $0.00"));            
      }
      
  }
}

class ResponseMessage {
  String message;

  public ResponseMessage(String message){
      this.message = message;
  }

  public String getMessage() {
      return message;
  }

  public void setMessage(String message) {
      this.message = message;
  }

}