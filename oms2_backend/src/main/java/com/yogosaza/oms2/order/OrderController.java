package com.yogosaza.oms2.order;

import com.yogosaza.oms2.exception.CommonException;
import com.yogosaza.oms2.logging.LogInputOutput;
import com.yogosaza.oms2.order.dto.OrderRequestDto;
import com.yogosaza.oms2.order.dto.OrderResponseDto;
import com.yogosaza.oms2.order.dto.OrderUpdateDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @LogInputOutput
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody OrderRequestDto dto) {
        orderService.create(dto);

        return ResponseEntity.ok("ok");
    }

    @LogInputOutput
    @GetMapping("/findById")
    public ResponseEntity<?> findById(@RequestParam Integer id) {
        OrderResponseDto result = orderService.findByOrderId(id);

        return ResponseEntity.ok(result);
    }

    @LogInputOutput
    @PutMapping("")
    public ResponseEntity<?> update(@RequestBody OrderUpdateDto dto) {
        OrderResponseDto result = orderService.update(dto);

        return ResponseEntity.ok(result);
    }

    @LogInputOutput
    @DeleteMapping("/hard")
    public ResponseEntity<?> hardDelete(@RequestParam Integer id) {
        orderService.hardDelete(id);

        return ResponseEntity.ok("ok");
    }

    @LogInputOutput
    @DeleteMapping("/soft")
    public ResponseEntity<?> softDelete(@RequestParam Integer id) {
        orderService.softDelete(id);

        return ResponseEntity.ok("ok");
    }

    @LogInputOutput
    @PatchMapping("/restore")
    public ResponseEntity<?> restore(@RequestParam Integer id) {
        orderService.restore(id);

        return ResponseEntity.ok("ok");
    }
    
}
