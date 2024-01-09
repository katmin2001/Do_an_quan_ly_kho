package com.kenzy.manage.do_an_quan_ly_kho.service;

import com.kenzy.manage.do_an_quan_ly_kho.entity.CustomerEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.OrderDetailEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.OrderEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.ProductEntity;
import com.kenzy.manage.do_an_quan_ly_kho.entity.constant.OrderStatus;
import com.kenzy.manage.do_an_quan_ly_kho.repository.ProductRepository;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

@Service
public class EmailService {
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private ProductRepository productRepository;
    public void sendMail(OrderEntity order, CustomerEntity customer, List<OrderDetailEntity> orderDetailList){
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setFrom(new InternetAddress("anh.trannguyenduc@vti.com.vn","Shop"));
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            String orderStatus = "";
            for (OrderStatus status:OrderStatus.values()){
                if(status.getType() == order.getOrderStatus()){
                    orderStatus = status.name();
                }
            }
//            String contextMail = "Thank you for your order. Please confirm your information order!"
//                    +"\nName: "+customer.getName()
//                    +"\nAddress: "+customer.getAddress()
//                    +"\nPhone: "+customer.getPhone()
//                    +"\nTotal Amount: "+order.getTotalAmount() + " VNĐ"
//                    +"\nStatus: "+orderStatus
//                    +"\nOrder Date: "+order.getOrderDate();
            String contextMail = "<html>"
                    + "<head>"
                    + "<style>"
                    + "table {"
                    + "    width: 100%;"
                    + "    border-collapse: collapse;"
                    + "}"
                    + "th, td {"
                    + "    border: 1px solid #ddd;"
                    + "    padding: 8px;"
                    + "    text-align: left;"
                    + "}"
                    + "th {"
                    + "    background-color: #f2f2f2;"
                    + "}"
                    + "</style>"
                    + "</head>"
                    + "<body>"
                    + "<h2>Thank you for your order. Please confirm your information order!</h2>"
                    + "<p><strong>Order Code:</strong> " + order.getCode() + "</p>"
                    + "<p><strong>Name:</strong> " + customer.getName() + "</p>"
                    + "<p><strong>Address:</strong> " + customer.getAddress() + "</p>"
                    + "<p><strong>Phone:</strong> " + customer.getPhone() + "</p>"
                    + "<p><strong>Total Amount:</strong> " + order.getTotalAmount() + " VNĐ</p>"
                    + "<p><strong>Status:</strong> " + orderStatus + "</p>"
                    + "<p><strong>Order Date:</strong> " + order.getOrderDate() + "</p>"
                    + "<table border='1'>"
                    + "<tr>"
                    + "<th>Product Name</th>"
                    + "<th>Quantity</th>"
                    + "<th>Price Unit</th>"
                    + "<th>Total Price</th>"
                    + "</tr>";
            for (OrderDetailEntity orderDetail : orderDetailList) {
                ProductEntity product = productRepository.findById(orderDetail.getProductId()).orElse(null);
                if(product == null){
                    throw new NullPointerException("Not found product!");
                }
                contextMail += "<tr>"
                        + "<td>" + product.getProductName() + "</td>"
                        + "<td>" + orderDetail.getQuantity() + "</td>"
                        + "<td>" + product.getPrice() + " VNĐ</td>"
                        + "<td>" + orderDetail.getTotalPrice() + " VNĐ</td>"
                        + "</tr>";
            }
            contextMail += "</table>"
                    + "</body>"
                    + "</html>";
            helper.setTo(customer.getEmail());
            helper.setSubject("Email confirm order by " + customer.getName());
            helper.setText(contextMail, true);
            javaMailSender.send(message);
        }catch (MessagingException e){
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
