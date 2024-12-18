package com.yogosaza.oms2.product;

import com.yogosaza.oms2.exception.CommonException;
import com.yogosaza.oms2.logging.LogInputOutput;
import com.yogosaza.oms2.product.dto.ProductRequestDto;
import com.yogosaza.oms2.product.dto.ProductResponseDto;
import com.yogosaza.oms2.product.dto.ProductUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @LogInputOutput
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProductRequestDto dto) throws CommonException {
        productService.create(dto);

        return ResponseEntity.ok("ok");
    }

    @LogInputOutput
    @GetMapping("/find")
    public ResponseEntity<?> findById(@RequestParam Integer id) throws CommonException {
        ProductResponseDto result = productService.findByProductId(id);

        return ResponseEntity.ok(result);
    }

    @LogInputOutput
    @PutMapping("/update")
    public ResponseEntity<?> update(@RequestBody ProductUpdateDto dto) throws CommonException {
        productService.update(dto);

        return ResponseEntity.ok("ok");
    }

}
