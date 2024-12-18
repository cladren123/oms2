package com.yogosaza.oms2.order;

import com.yogosaza.oms2.enums.ErrorCode;
import com.yogosaza.oms2.exception.CommonException;
import com.yogosaza.oms2.logging.LogInputOutput;
import com.yogosaza.oms2.order.dto.OrderRequestDto;
import com.yogosaza.oms2.order.dto.OrderResponseDto;
import com.yogosaza.oms2.order.dto.OrderUpdateDto;
import com.yogosaza.oms2.order.enums.OrderStatus;
import com.yogosaza.oms2.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @LogInputOutput
    public void create(OrderRequestDto dto) {
        OrderEntity order = OrderEntity.builder()
                .userId(dto.getUserId())
                .productId(dto.getProductId())
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .status(OrderStatus.PAYMENT_COMPLETE.getStatus())
                .createdDateTime(LocalDateTime.now())
                .build();

        orderRepository.save(order);
    }

    @LogInputOutput
    @Transactional(readOnly = true)
    public OrderResponseDto findByOrderId(Integer id) {
         OrderEntity order = orderRepository.findById(id)
                 .orElseThrow(() -> new CommonException(ErrorCode.ORDER_NOT_FOUND));

         return OrderResponseDto.builder()
                 .id(order.getId())
                 .userId(order.getUserId())
                 .productId(order.getProductId())
                 .quantity(order.getQuantity())
                 .price(order.getPrice())
                 .status(order.getStatus())
                 .createdDateTime(Util.seoulTime(order.getCreatedDateTime()))
                 .isDeleted(order.isDeleted())
                 .build();
    }

    public OrderResponseDto update(OrderUpdateDto dto) {
        OrderEntity order = orderRepository.findById(dto.getId())
                .orElseThrow(() -> new CommonException(ErrorCode.ORDER_NOT_FOUND));

        order.setQuantity(dto.getQuantity());
        order.setPrice(dto.getPrice());
        order.setStatus(dto.getStatus());
        orderRepository.save(order);

        return OrderResponseDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .status(order.getStatus())
                .createdDateTime(Util.seoulTime(order.getCreatedDateTime()))
                .isDeleted(order.isDeleted())
                .build();
    }

    // 하드 삭제 (데이터 삭제)
    public void hardDelete(Integer id)  {
        if (!orderRepository.existsById(id)) {
            throw new CommonException(ErrorCode.ORDER_NOT_FOUND);
        }
        orderRepository.deleteById(id);
    }

    // 소프트 삭제 (상태 변경)
    public void softDelete(Integer id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.ORDER_NOT_FOUND));
        order.delete();
        orderRepository.save(order);
    }

    // 복구
    public void restore(Integer id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new CommonException(ErrorCode.ORDER_NOT_FOUND));
        order.restore();
        orderRepository.save(order);
    }
}
