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

    private Integer userId;

    private Integer productId;

    private Integer quantity;

    private Integer price;

    // 결제 완료, 배송중, 배송완료, 반품, 교환
    private String status;

    private LocalDateTime createdDateTime;

    // 삭제 여부
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
