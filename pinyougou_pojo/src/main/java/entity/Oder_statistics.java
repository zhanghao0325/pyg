package entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class Oder_statistics implements Serializable {
    long id;
    String username;
    long orderTotal;
    double payment;

    @Override
    public String toString() {
        return "Oder_statistics{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", orderTotal=" + orderTotal +
                ", payment=" + payment +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(long orderTotal) {
        this.orderTotal = orderTotal;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }
}
