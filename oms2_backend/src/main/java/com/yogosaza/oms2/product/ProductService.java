package com.yogosaza.oms2.product;

import com.yogosaza.oms2.exception.CommonException;
import com.yogosaza.oms2.product.dto.ProductRequestDto;
import com.yogosaza.oms2.product.dto.ProductResponseDto;
import com.yogosaza.oms2.product.dto.ProductUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public void create(ProductRequestDto dto) throws CommonException {
        ProductEntity product = ProductEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .quantity(dto.getQuantity())
                .build();

        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public ProductResponseDto findByProductId(Integer id) throws CommonException {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new CommonException("PRODUCT_NOT_FOUND", "해당 id의 상품이 없습니다."));

        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .quantity(product.getQuantity())
                .build();
    }

    public void update(ProductUpdateDto dto) throws CommonException {
        ProductEntity product = productRepository.findById(dto.getId())
                .orElseThrow(() -> new CommonException("PRODUCT_NOT_FOUND", "해당 id의 상품이 없습니다."));

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setQuantity(dto.getQuantity());
    }




}
