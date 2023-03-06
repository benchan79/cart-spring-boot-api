package sg.edu.ntu.cart_api_v304.helper;

public class MinimumPayableCheckHelper {
  float minimumPayable;

  public MinimumPayableCheckHelper(float minimumPayable) {
    this.minimumPayable = minimumPayable;
  }
  
  public boolean hasMinimumPayable(float payable) throws Exception {
    if (payable < 0) throw new Exception("Invalid Payable");
    return payable > this.minimumPayable;
  }
}
