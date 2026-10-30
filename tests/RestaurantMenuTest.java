import com.fooddelivery.models.*;
import com.fooddelivery.repository.*;
import com.fooddelivery.services.*;
import com.fooddelivery.cli.*;
import java.time.LocalTime;
import java.util.*;
import java.io.*;
public class RestaurantMenuTest {
 static int count;
 static void check(boolean b){count++;if(!b)throw new AssertionError("Check "+count);}
 static void reject(Runnable r){try{r.run();throw new AssertionError("Expected rejection");}catch(IllegalArgumentException|IllegalStateException expected){count++;}}
 static Restaurant restaurant(String id){return new Restaurant(id,"OWNER","Dosa House","South Indian","Chennai",LocalTime.of(9,0),LocalTime.of(22,0),100);}
 public static void main(String[] args){
  RestaurantRepository rr=new RestaurantRepository();MenuItemRepository mr=new MenuItemRepository();Set<String> active=new HashSet<>();
  RestaurantService rs=new RestaurantService(rr,mr,active::contains);MenuItemService ms=new MenuItemService(mr,rr);
  Restaurant r=restaurant("R1");reject(()->rs.addRestaurant("U","CUSTOMER",r));check(rr.findAll().isEmpty());
  reject(()->rs.addRestaurant("OTHER","RESTAURANT_OWNER",r));rs.addRestaurant("OWNER","RESTAURANT_OWNER",r);
  reject(()->rs.addRestaurant("OWNER","RESTAURANT_OWNER",restaurant("R1")));check(rr.findAll().size()==1);
  check(!r.canAcceptOrders(LocalTime.NOON));rs.activateRestaurant("OWNER","RESTAURANT_OWNER","R1");rs.changeAvailability("OWNER","RESTAURANT_OWNER","R1",true);
  check(r.canAcceptOrders(LocalTime.of(9,0)));check(r.canAcceptOrders(LocalTime.of(21,59)));check(!r.canAcceptOrders(LocalTime.of(22,0)));
  reject(()->rs.updateDetails("OWNER","RESTAURANT_OWNER","R1","Changed","Italian","Delhi"));check(r.getName().equals("Dosa House"));
  rs.updateTimings("OWNER","RESTAURANT_OWNER","R1",LocalTime.of(22,0),LocalTime.of(3,0));check(r.canAcceptOrders(LocalTime.of(23,0)));check(r.canAcceptOrders(LocalTime.of(2,59)));check(!r.canAcceptOrders(LocalTime.of(3,0)));check(!r.canAcceptOrders(LocalTime.NOON));
  reject(()->rs.updateTimings("OWNER","RESTAURANT_OWNER","R1",LocalTime.NOON,LocalTime.NOON));check(r.getOpeningTime().equals(LocalTime.of(22,0)));
  reject(()->new Restaurant("bad","O","Name","Unknown","City",LocalTime.NOON,LocalTime.MIDNIGHT,100));
  reject(()->new Restaurant("bad","O"," ","Italian","City",LocalTime.NOON,LocalTime.MIDNIGHT,100));
  reject(()->new Restaurant("bad","O","Name","Italian","City",LocalTime.NOON,LocalTime.MIDNIGHT,Double.NaN));
  MenuItem item=new MenuItem("M1","R1","Dosa",80,"Veg",true);
  reject(()->ms.addMenuItem("U","CUSTOMER",item));reject(()->ms.addMenuItem("OTHER","RESTAURANT_OWNER",item));
  ms.addMenuItem("OWNER","RESTAURANT_OWNER",item);check(ms.getMenuItems("R1").size()==1);
  reject(()->ms.addMenuItem("OWNER","RESTAURANT_OWNER",new MenuItem("M1","R1","Tea",20,"Veg",true)));
  reject(()->ms.addMenuItem("ADMIN","ADMIN",new MenuItem("M2","missing","Tea",20,"Veg",true)));
  reject(()->new MenuItem("bad","R1","Tea",0,"Veg",true));reject(()->new MenuItem("bad","R1","Tea",Double.POSITIVE_INFINITY,"Veg",true));reject(()->new MenuItem("bad","R1","Tea",20,"other",true));
  Cart wrongRestaurant=new Cart("CHECK");reject(()->wrongRestaurant.addItem("R2",item,1));check(wrongRestaurant.isEmpty());
  CartService carts=new CartService(new CartRepository());carts.addItem("C","R1",item,2);check(carts.calculateTotal("C")==160);
  OrderItemService orders=new OrderItemService(new OrderItemRepository());orders.saveOrderItems("O1",carts.getCart("C"));
  ms.updatePrice("OWNER","RESTAURANT_OWNER","M1",100);check(carts.calculateTotal("C")==200);check(orders.calculateTotal("O1")==160);
  reject(()->ms.updatePrice("OTHER","RESTAURANT_OWNER","M1",999));check(item.getPrice()==100);
  reject(()->ms.updatePrice("OWNER","RESTAURANT_OWNER","M1",Double.NaN));check(item.getPrice()==100);
  ms.changeAvailability("OWNER","RESTAURANT_OWNER","M1",false);check(carts.removeUnavailableItems("C").equals(Arrays.asList("Dosa")));
  item.changeAvailability(true);carts.addItem("C","R1",item,1);
  active.add("R1");reject(()->rs.deleteRestaurant("OWNER","RESTAURANT_OWNER","R1"));check(rs.getRestaurant("R1")==r);check(ms.getMenuItems("R1").size()==1);
  active.clear();rs.deleteRestaurant("OWNER","RESTAURANT_OWNER","R1");check(rr.findById("R1")==null);check(mr.findById("M1")==null);check(!item.isAvailable());check(carts.removeUnavailableItems("C").size()==1);
  rs.addRestaurant("ADMIN","ADMIN",restaurant("R2"));rs.updateDetails("ADMIN","ADMIN","R2","New","Italian","Delhi");check(rs.getRestaurant("R2").getCity().equals("Delhi"));
  MenuItem remove=new MenuItem("M3","R2","Tea",20,"Veg",true);ms.addMenuItem("ADMIN","ADMIN",remove);ms.removeMenuItem("ADMIN","ADMIN","M3");check(!remove.isAvailable());check(ms.getMenuItems("R2").isEmpty());
  ByteArrayOutputStream bytes=new ByteArrayOutputStream();PrintStream out=new PrintStream(bytes);
  new MenuItemCli(ms,new Scanner("bad\n2\nM4\nPizza\nnope\n1\n6\n"),out).showMenu("ADMIN","ADMIN","R2");check(bytes.toString().contains("valid number"));
  new MenuItemCli(ms,new Scanner("2\nM4\nPizza\n120\nVeg\ntrue\n1\n3\nM4\n150\n4\nM4\nfalse\n6\n"),out).showMenu("ADMIN","ADMIN","R2");check(mr.findById("M4").getPrice()==150);check(!mr.findById("M4").isAvailable());check(bytes.toString().contains("Pizza"));
  new RestaurantCli(rs,new Scanner("bad\n1\n6\nR2\n5\nR2\ntrue\n9\n"),out).showMenu("ADMIN","ADMIN");check(rs.getRestaurant("R2").isOpen());check(bytes.toString().contains("New"));
  new RestaurantCli(rs,new Scanner("2\n"),out).showMenu("ADMIN","ADMIN");
  Restaurant draft=restaurant("DRAFT");rs.addRestaurant("ADMIN","ADMIN",draft);
  reject(()->rs.changeAvailability("ADMIN","ADMIN","DRAFT",true));check(!draft.isOpen());
  reject(()->rs.updateDetails("ADMIN","ADMIN","DRAFT","New","bad","Delhi"));check(draft.getName().equals("Dosa House"));
  reject(()->rs.activateRestaurant("OTHER","RESTAURANT_OWNER","DRAFT"));check(!draft.isActivated());
  reject(()->rs.deleteRestaurant("U","CUSTOMER","DRAFT"));check(rr.findById("DRAFT")!=null);
  reject(()->rs.getRestaurant("missing"));reject(()->ms.getMenuItems("missing"));
  reject(()->new RestaurantService(rr,mr,null));reject(()->new MenuItem(" ","R2","Tea",20,"Veg",true));
  List<Restaurant> detached=rr.findAll();detached.clear();check(rr.findById("DRAFT")!=null);
  List<MenuItem> detachedMenu=ms.getMenuItems("R2");detachedMenu.clear();check(ms.getMenuItems("R2").size()==1);
  new RestaurantCli(rs,new Scanner("2\nRCLI\nOWNER\nCafe\nFast Food\nChennai\n09:00\n23:00\n0\n6\nRCLI\n5\nRCLI\ntrue\n4\nRCLI\n08:00\n22:00\n8\nRCLI\n9\n"),out).showMenu("ADMIN","ADMIN");
  check(rs.getRestaurant("RCLI").canAcceptOrders(LocalTime.NOON));check(rs.getRestaurant("RCLI").getMinimumOrderValue()==0);check(rs.getRestaurant("RCLI").getOpeningTime().equals(LocalTime.of(8,0)));
  new RestaurantCli(rs,new Scanner("4\nRCLI\n25:00\n5\nRCLI\nyes\n9\n"),out).showMenu("ADMIN","ADMIN");check(bytes.toString().contains("valid time"));check(bytes.toString().contains("true or false"));
  new MenuItemCli(ms,new Scanner("4\nM4\nyes\n6\n"),out).showMenu("ADMIN","ADMIN","R2");check(!mr.findById("M4").isAvailable());
  new MenuItemCli(ms,new Scanner("5\nM4\n6\n"),out).showMenu("ADMIN","ADMIN","RCLI");check(mr.findById("M4")!=null);
  RestaurantService failsClosed=new RestaurantService(rr,mr,id->{throw new IllegalStateException("Order lookup failed");});
  reject(()->failsClosed.deleteRestaurant("ADMIN","ADMIN","R2"));check(rr.findById("R2")!=null);check(mr.findById("M4")!=null);
  rs.changeAvailability("ADMIN","ADMIN","RCLI",false);check(!rs.getRestaurant("RCLI").canAcceptOrders(LocalTime.NOON));
  System.out.println("PASS: "+count+" restaurant/menu checks");
 }
}
