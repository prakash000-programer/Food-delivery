import com.fooddelivery.models.*;
import com.fooddelivery.repository.*;
import com.fooddelivery.services.*;
import com.fooddelivery.cli.*;
import java.util.*;
import java.io.*;
public class CartOrderIntegrationTest {
 static int checks;
 static void check(boolean condition){checks++;if(!condition)throw new AssertionError("Check "+checks);}
 static void rejects(Runnable action){try{action.run();throw new AssertionError("Expected rejection");}catch(IllegalArgumentException | IllegalStateException expected){checks++;}}
 public static void main(String[] args){
  MenuItem dosa=menu("M1","Dosa",80,true), tea=menu("M2","Tea",20,true);
  CartService service=new CartService(new CartRepository());
  Cart cart=service.getCart("U1");
  check(cart==service.getCart("U1"));check(cart!=service.getCart("U2"));check(cart.calculateTotal()==0);
  service.addItem("U1","R1",dosa,2);service.addItem("U1","R1",dosa,3);
  check(cart.getQuantity("M1")==5);check(cart.calculateTotal()==400);
  rejects(()->service.addItem("U1","R2",tea,1));rejects(()->service.addItem("U1","R1",dosa,6));
  rejects(()->service.addItem("U1","R1",tea,0));rejects(()->service.addItem("U1","R1",tea,-1));
  rejects(()->service.addItem("U1","R1",dosa,Integer.MAX_VALUE));check(cart.getQuantity("M1")==5);
  tea.changeAvailability(false);rejects(()->service.addItem("U1","R1",tea,1));tea.changeAvailability(true);
  service.addItem("U1","R1",tea,1);tea.changeAvailability(false);
  check(service.removeUnavailableItems("U1").equals(Arrays.asList("Tea")));check(cart.getItems().size()==1);
  OrderItemService orders=new OrderItemService(new OrderItemRepository());
  List<OrderItem> saved=orders.saveOrderItems("O1",cart);check(saved.get(0).calculateSubtotal()==400);
  dosa.updatePrice(100);check(saved.get(0).getPriceAtOrder()==80);check(cart.calculateTotal()==500);
  check(orders.calculateTotal("O1")==400);check(orders.getOrderItems("O1").get(0).getQuantity()==5);
  rejects(()->orders.saveOrderItems("O1",cart));
  try{cart.getItems().clear();throw new AssertionError();}catch(UnsupportedOperationException expected){checks++;}
  service.removeItem("U1","M1");check(cart.isEmpty());check(cart.getRestaurantId()==null);
  service.addItem("U1","R2",teaAvailable(),1);service.clearCart("U1");check(cart.calculateTotal()==0);
  rejects(()->orders.saveOrderItems("O2",cart));rejects(()->new OrderItem("",1,10));
  rejects(()->new OrderItem("Dosa",0,10));rejects(()->new OrderItem("Dosa",1,Double.NaN));
  ByteArrayOutputStream bytes=new ByteArrayOutputStream();PrintStream out=new PrintStream(bytes);
  new CartCli(service,new Scanner("bad\n2\nM1\n2\n1\n4\n3\nM1\n5\n6\n"),out).showMenu("U3","R1",Arrays.asList(dosa));
  check(service.getCart("U3").isEmpty());check(bytes.toString().contains("Dosa"));check(bytes.toString().contains("200.00"));
  new OrderItemCli(orders,out).displayOrderItems("O1");check(bytes.toString().contains("400.00"));
  CartRepository repo=new CartRepository();
  rejects(()->repo.getOrCreateCart(" "));check(repo.findByCustomerId("missing")==null);
  repo.getOrCreateCart("X");repo.removeByCustomerId("X");check(repo.findByCustomerId("X")==null);
  Cart ten=service.getCart("TEN");service.addItem("TEN","R1",dosa,10);check(ten.getQuantity("M1")==10);
  rejects(()->service.addItem("TEN","R1",dosa,1));check(ten.getQuantity("M1")==10);
  dosa.changeAvailability(false);rejects(()->orders.saveOrderItems("O3",ten));check(orders.getOrderItems("O3").isEmpty());
  check(service.removeUnavailableItems("TEN").equals(Arrays.asList("Dosa")));check(ten.isEmpty());check(ten.getRestaurantId()==null);
  dosa.changeAvailability(true);
  rejects(()->service.addItem("TEN","",dosa,1));rejects(()->service.addItem("TEN","R1",null,1));
  rejects(()->service.addItem("TEN","R1",menu("bad","Bad",Double.POSITIVE_INFINITY,true),1));
  new CartCli(service,new Scanner("2\nM1\noops\n2\nmissing\n6\n"),out).showMenu("U4","R1",Arrays.asList(dosa));
  check(service.getCart("U4").isEmpty());check(bytes.toString().contains("whole number"));check(bytes.toString().contains("not found"));
  new CartCli(service,new Scanner("2\n"),out).showMenu("U4","R1",Arrays.asList(dosa));
  orders.removeOrderItems("O1");check(orders.getOrderItems("O1").isEmpty());
  OrderItemRepository orderRepo=new OrderItemRepository();List<OrderItem> original=new ArrayList<>();original.add(new OrderItem("Tea",1,20));
  orderRepo.save("SNAP",original);original.clear();check(orderRepo.findByOrderId("SNAP").size()==1);
  try{orderRepo.findByOrderId("SNAP").clear();throw new AssertionError();}catch(UnsupportedOperationException expected){checks++;}
  System.out.println("PASS: "+checks+" checks");
 }
 static MenuItem menu(String id,String name,double price,boolean available){return new MenuItem(id,"R1",name,price,"Veg",available);}
 static MenuItem teaAvailable(){return new MenuItem("M2","R2","Tea",20,"Veg",true);}
}
