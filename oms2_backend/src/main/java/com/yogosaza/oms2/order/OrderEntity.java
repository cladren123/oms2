package com.yogosaza.oms2.order;

import com.yogosaza.oms2.enums.ErrorCode;
import com.yogosaza.oms2.exception.CommonException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "oms2_orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price")
    private Integer price;

    // 결제 완료, 배송중, 배송완료, 반품, 교환
    @Column(name = "status")
    private String status;

    @Column(name = "created_date_time", updatable = false)
    private LocalDateTime createdDateTime;

    // 삭제 여부
    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    // 삭제
    public void delete() throws CommonException {
        if (isDeleted) {
            throw new CommonException(ErrorCode.ORDER_ALREADY_DELETED);
        }
        isDeleted = true;
    }

    // 복구
    public void restore() throws CommonException {
        if (!isDeleted) {
            throw new CommonException(ErrorCode.ORDER_NOT_DELETED);
        }
        isDeleted = false;
    }

}
