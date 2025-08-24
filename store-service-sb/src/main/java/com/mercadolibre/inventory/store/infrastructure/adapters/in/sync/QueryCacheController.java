package com.mercadolibre.inventory.store.infrastructure.adapters.in.sync;

import com.mercadolibre.inventory.shared.models.GenericResponse;
import com.mercadolibre.inventory.store.application.usecase.inventorycache.GetInventoryCacheUseCase;
import com.mercadolibre.inventory.store.application.usecase.inventorycache.ListInventoryCacheUseCase;
import com.mercadolibre.inventory.store.domain.model.InventoryCache;
import com.mercadolibre.inventory.store.domain.model.InventoryCachePageable;
import com.mercadolibre.inventory.shared.models.PageableRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/v1/inventory-cache")
public class QueryCacheController {
    private final GetInventoryCacheUseCase getInventoryCacheUseCase;
    private final ListInventoryCacheUseCase listInventoryCacheUseCase;

    public QueryCacheController(GetInventoryCacheUseCase getInventoryCacheUseCase, ListInventoryCacheUseCase listInventoryCacheUseCase) {
        this.getInventoryCacheUseCase = getInventoryCacheUseCase;
        this.listInventoryCacheUseCase = listInventoryCacheUseCase;
    }

    @GetMapping("/{sku}")
    public ResponseEntity<?> getBySku(@PathVariable String sku) {
        Optional<InventoryCache> cache = getInventoryCacheUseCase.execute(sku);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GenericResponse.of(HttpStatus.OK.value(),"Inventario encontrado correctamente",cache));

    }


    @GetMapping("/paged")
    public ResponseEntity<?> listPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "sku") String sortBy
    ) {
        PageableRequest pageableRequest = PageableRequest.builder()
                .page(page)
                .size(size)
                .sortBy(sortBy)
                .build();
        InventoryCachePageable result = listInventoryCacheUseCase.execute(pageableRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GenericResponse.of(HttpStatus.OK.value(),"Inventarios encontrados correctamente",result));
    }
}
