package mtaxi;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Management_table")
public class Management {

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long orderId;
    private String status;
    private Long driverId;
    private String location;

    @PostPersist
    public void onPostPersist(){
        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        System.out.println("############### Management onPostPersist status :  "+this.getStatus());
        if(this.getStatus().equals("Ordered")) {
            mtaxi.external.Driver driver = new mtaxi.external.Driver();
            // mappings goes here

            driver.setStatus("Ordered");
            driver.setDriverId(this.getDriverId());
            driver.setOrderId(this.getOrderId());
            driver.setLocation(this.getLocation());

            System.out.println("=============Management-DriverId: " + this.getDriverId() + ":: OrderId : " + this.getOrderId());
            ManagementApplication.applicationContext.getBean(mtaxi.external.DriverService.class).checkOrder(driver);

            System.out.println("End of Management-Ordered");
        }
        else if(this.getStatus().equals("Canceled")) {
            CancelOrderRequested cancelOrderRequested = new CancelOrderRequested();

            cancelOrderRequested.setOrderId(this.getOrderId());
            cancelOrderRequested.setStatus(this.getStatus());
            cancelOrderRequested.setDriverId(this.getDriverId());

            BeanUtils.copyProperties(this, cancelOrderRequested);
            cancelOrderRequested.publishAfterCommit();
        }

    }

    @PostUpdate
    public void onPostUpdate(){

        System.out.println("Management onPostUpdate Status  :  "+this.getStatus());
        if(this.getStatus().equals("Approved")) {
            System.out.println("Management Status : Approved");
            OrderApproved orderApproved = new OrderApproved();

            orderApproved.setOrderId(this.getOrderId());
            orderApproved.setStatus(this.getStatus());
            orderApproved.setDriverId(this.getDriverId());

            BeanUtils.copyProperties(this, orderApproved);
            orderApproved.publishAfterCommit();
        } else if(this.getStatus().equals("Denied")) {
            System.out.println("Management Status : Denied");
            OrderDenied orderDenied = new OrderDenied();

            orderDenied.setOrderId(this.getOrderId());
            orderDenied.setStatus(this.getStatus());
            orderDenied.setDriverId(this.getDriverId());

            BeanUtils.copyProperties(this, orderDenied);
            orderDenied.publishAfterCommit();

        } else if(this.getStatus().equals("Canceled")) {
            System.out.println("Management Status : Canceled");
            CancelOrderRequested cancelOrderRequested = new CancelOrderRequested();

            cancelOrderRequested.setOrderId(this.getOrderId());
            cancelOrderRequested.setStatus(this.getStatus());
            cancelOrderRequested.setDriverId(this.getDriverId());

            BeanUtils.copyProperties(this, cancelOrderRequested);
            cancelOrderRequested.publishAfterCommit();

        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }


}
