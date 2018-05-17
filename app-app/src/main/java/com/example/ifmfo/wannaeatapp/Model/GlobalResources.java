package com.example.ifmfo.wannaeatapp.Model;

import android.app.Application;
import android.support.design.widget.NavigationView;
import android.widget.Toast;

import com.example.ifmfo.wannaeatapp.Activities.FullShoppingBasketActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalResources extends Application{

    private static GlobalResources instance;
    private Map<Product, Integer> shopping_basket_productsAdded; //Producto - Cantidad
    private List <Restaurant> foundRestaurnts;  //Todos los restaurantes de la app
    private List <CommensalPerHour> capacitiesPerHourOfRestaurant;
    private int shopping_basket_numberOfProductsAdded;
    private double shopping_basket_totalPrice;
    private double shopping_basket_totalPrice_with_discount;
    private boolean user_isLogged;
    private User user;
    private String session_currentAddress;
    private String session_addressEntered;
    private NavigationView navigationView;
    private Boolean wellcomeMessageShowed;
    private Coupon couponApplied;
    private Product productCouponApplied;
    private Double discountApplied;
    private Boolean isBookingForToday;
    private String reservationTime;
    private int number_of_commensals;
    private String client_commentary;
    private Booking last_single_booking_visited;

    public GlobalResources() {
        shopping_basket_numberOfProductsAdded = 0;
        shopping_basket_productsAdded = new HashMap<>();
        shopping_basket_totalPrice = 0;
        user_isLogged = false;
        user = null;
        wellcomeMessageShowed = false;
        foundRestaurnts = new ArrayList<>();
        capacitiesPerHourOfRestaurant = new ArrayList<>();
        isBookingForToday = true;
    }

    public static synchronized GlobalResources getInstance(){
        if(instance == null){
            instance = new GlobalResources();
        }
        return instance;
    }

    //Cesta compra
    public List <Product> shopping_basket_getAllDiferentsProducts(){
        List <Product> allDiferentsProducts = new ArrayList<>();
        for (Map.Entry<Product, Integer> entry : shopping_basket_productsAdded.entrySet()){
            allDiferentsProducts.add(entry.getKey());
        }
        return allDiferentsProducts;
    }

    public int shopping_basket_getAmountOfDiferentsProduct(){
        return shopping_basket_productsAdded.size();
    }

    public void shopping_basket_addProduct(Product product){
        if(shopping_basket_productsAdded.containsKey(product)){
            shopping_basket_productsAdded.put(product, shopping_basket_productsAdded.get(product) + 1);
        }else{
            shopping_basket_productsAdded.put(product, 1);
        }
        if(getCouponApplied() != null){
            FullShoppingBasketActivity.removeCouponApplied();
            FullShoppingBasketActivity.getCouponDesplegable().setSelection(0);
        }
        shopping_basket_totalPrice += product.getPrice();
        shopping_basket_numberOfProductsAdded++;
    }

    public void shopping_basket_emptyShoppingCart(){
        shopping_basket_productsAdded.clear();
        shopping_basket_totalPrice = 0;
        shopping_basket_numberOfProductsAdded = 0;
        if(getCouponApplied() != null){
            FullShoppingBasketActivity.removeCouponApplied();
        }
    }

    public void shopping_basket_removeProduct(Product product){
        if(shopping_basket_productsAdded.containsKey(product)){
            if(shopping_basket_productsAdded.get(product) > 1){
                shopping_basket_productsAdded.put(product, shopping_basket_productsAdded.get(product) - 1);
            }else{
                shopping_basket_productsAdded.remove(product);
            }
            if(getCouponApplied() != null){
                FullShoppingBasketActivity.removeCouponApplied();
                FullShoppingBasketActivity.getCouponDesplegable().setSelection(0);
            }
            shopping_basket_totalPrice -= product.getPrice();
            shopping_basket_numberOfProductsAdded--;
        }
    }

    public Product CheapestProductOfCategory(String category){
        List <Product> sameCategoryProducts = new ArrayList<>();
        //Primero cojo todos productos que sean de esa categoria
        for (Map.Entry<Product, Integer> entry : shopping_basket_productsAdded.entrySet()) {
            if(category.equals(entry.getKey().getCategory())){
                sameCategoryProducts.add(entry.getKey());
            }
        }
        if(sameCategoryProducts.size() == 0){
            return null;
        }
        //Ahora calculo el de menor valor
        Product cheapestProduct = sameCategoryProducts.get(0);
        for(Product product: sameCategoryProducts){
            if (product.getPrice() < cheapestProduct.getPrice()) {
                cheapestProduct = product;
            }
        }
        return cheapestProduct;
    }

    //Si se trata de añadir un producto de un restaurante diferente a los productos ya añadidos, se
    //mostrará un mensaje, si acepta se vacía la "cesta" y se añade el nuevo producto, si deniega,
    //se cancela el añadir el producto seleccionado.
    public boolean shopping_basket_canAddProduct(Product product){
        for (Map.Entry<Product, Integer> entry : shopping_basket_productsAdded.entrySet()) {
            if(product.getId_restaurant() != entry.getKey().getId_restaurant()){
                return false;
            }
        }
        return true;
    }

    public int shopping_basket_getNumberOfProductsAdded() {
        return shopping_basket_numberOfProductsAdded;
    }

    public double shopping_basket_getTotalPrice() {
        return shopping_basket_totalPrice;
    }

    public int shopping_basket_getAmountOfAProduct(Product product){
        for (Map.Entry<Product, Integer> entry : shopping_basket_productsAdded.entrySet()) {
            if(product.getId() == entry.getKey().getId()){
                return entry.getValue();
            }
        }
        return 0;
    }

    public int getCurrentRestaurantIdOfBooking(){
        Map.Entry <Product, Integer> entry = shopping_basket_productsAdded.entrySet().iterator().next();
        return entry.getKey().getId_restaurant();
    }

    public String getCurrentRestaurantNameOfBooking(){
        int id = getCurrentRestaurantIdOfBooking();
        for(Restaurant restaurant: getFoundRestaurnts()){
            if (id == restaurant.getId()){
                return restaurant.getName();
            }
        }
        return "";
    }

    public Double getShopping_basket_totalPrice_with_discount() {
        return shopping_basket_totalPrice_with_discount;
    }

    public void setShopping_basket_totalPrice_with_discount(Double shopping_basket_totalPrice_with_discount) {
        this.shopping_basket_totalPrice_with_discount = shopping_basket_totalPrice_with_discount;
    }

    public Product getProductCouponApplied() {
        return productCouponApplied;
    }

    public void setProductCouponApplied(Product productCouponApplied) {
        this.productCouponApplied = productCouponApplied;
    }

    public Double getDiscountApplied() {
        return discountApplied;
    }

    public void setDiscountApplied(Double discountApplied) {
        this.discountApplied = discountApplied;
    }

    public Coupon getCouponApplied() {
        return couponApplied;
    }

    public void setCouponApplied(Coupon couponApplied) {
        this.couponApplied = couponApplied;
    }

    public int getIndexOfCouponApplied(Coupon coupon, String [] applicableCoupons){
        for (int i=0; i< applicableCoupons.length; i++){
            if(coupon.getDescription().equals(applicableCoupons[i])){
                return i;
            }
        }
        return -1;
    }

    //Sesión
    public String getSession_currentAddress() {
        return session_currentAddress;
    }

    public void setSession_currentAddress(String session_currentAddress) {
        this.session_currentAddress = session_currentAddress;
    }

    public String getSession_addressEntered() {
        return session_addressEntered;
    }

    public void setSession_addressEntered(String session_addressEntered) {
        this.session_addressEntered = session_addressEntered;
    }

    //Usuario
    public boolean getUser_isLogged() {
        return user_isLogged;
    }

    public void setUser_isLogged(boolean user_isLogged) {
        this.user_isLogged = user_isLogged;
    }

    public User getUserLogged() {
        return user;
    }

    public void user_logIn(User user){
        this.user = user;
        setUser_isLogged(true);
    }

    public void user_logOut(){
        this.user = null;
        setUser_isLogged(false);
        setWellcomeMessageTo(false);
    }

    public Coupon getCouponWithThisDescription(String description){
        for(Coupon coupon : user.getUserCoupons()){
            if(coupon.getDescription().equals(description)){
                return coupon;
            }
        }
        return null;
    }

    //Restaurantes

    public List<Restaurant> getFoundRestaurnts() {
        return foundRestaurnts;
    }

    public void addFoundRestaurant(Restaurant restaurant){
        getFoundRestaurnts().add(restaurant);
    }

    public String getNameOfThisRestaurant(int id_restaurant){
        return getFoundRestaurnts().get(id_restaurant - 1).getName();
    }

    public void clearFoundRestaurants(){
        getFoundRestaurnts().clear();
    }

    //Capacidades por hora de restaurante

    public List<CommensalPerHour> getCapacitiesOfRestaurant(){
        return capacitiesPerHourOfRestaurant;
    }

    public void addCapacityPerHour(CommensalPerHour commensalPerHour){
        getCapacitiesOfRestaurant().add(commensalPerHour);
    }

    public void emptyCapacitiesOfCurrentRestaurant(){
        getCapacitiesOfRestaurant().clear();
    }

    public void orderOpenHoursOfCapacities(){
        //Sort by hour
        Collections.sort(getCapacitiesOfRestaurant(), new Comparator<CommensalPerHour>() {
            @Override
            public int compare(CommensalPerHour p1, CommensalPerHour p2) {
                return p1.getHour().compareTo(p2.getHour());
            }
        });
    }

    //Reserva
    public Boolean getBookingForToday() {
        return isBookingForToday;
    }

    public void setBookingForToday(Boolean bookingForToday) {
        isBookingForToday = bookingForToday;
    }

    public String getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(String reservationTime) {
        this.reservationTime = reservationTime;
    }

    public int getNumber_of_commensals() {
        return number_of_commensals;
    }

    public void setNumber_of_commensals(int number_of_commensals) {
        this.number_of_commensals = number_of_commensals;
    }

    public String getClient_commentary() {
        return client_commentary;
    }

    public void setClient_commentary(String client_commentary) {
        this.client_commentary = client_commentary;
    }

    //Elementos globales
    public NavigationView getNavigationView() {
        return navigationView;
    }

    public void setNavigationView(NavigationView navigationView) {
        this.navigationView = navigationView;
    }

    public void setWellcomeMessageTo(Boolean status){
        this.wellcomeMessageShowed = status;
    }

    public Boolean wellcomeMessageIsShowed(){
        return this.wellcomeMessageShowed;
    }

    public String generateProductsToString() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<Product, Integer> entry : shopping_basket_productsAdded.entrySet()){
            result.append(entry.getKey().getId()).append(":").append(entry.getValue().toString()).append(",");
        }
        return result.toString().replace(" ", "_");
    }

    public Booking getLast_single_booking_visited() {
        return last_single_booking_visited;
    }

    public void setLast_single_booking_visited(Booking last_single_booking_visited) {
        this.last_single_booking_visited = last_single_booking_visited;
    }

    public void updateStateKeepLogged(int id_booking) {
        for(Booking booking: getUserLogged().getUserBookings()){
            if(booking.getId() == id_booking){
                booking.setCanrate(false);
            }
        }
    }
}
