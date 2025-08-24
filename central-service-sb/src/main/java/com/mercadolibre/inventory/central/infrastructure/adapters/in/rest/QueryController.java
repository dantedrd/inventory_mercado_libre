package com.mercadolibre.inventory.central.infrastructure.adapters.in.rest;

import com.mercadolibre.inventory.central.application.usecase.reservation.GetReservationUseCase;
import com.mercadolibre.inventory.central.application.usecase.reservation.GetReservationsUseCase;
import com.mercadolibre.inventory.central.domain.model.ReservationPageable;
import com.mercadolibre.inventory.central.domain.model.ReservationState;
import com.mercadolibre.inventory.shared.models.GenericResponse;
import com.mercadolibre.inventory.shared.models.PageableRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/v1/read")
public class QueryController {
  private final GetReservationsUseCase getReservationsUseCase;
  private final GetReservationUseCase getReservationUseCase;


  public QueryController(GetReservationUseCase getReservationUseCase, GetReservationsUseCase getReservationsUseCase){
      this.getReservationUseCase=getReservationUseCase;
      this.getReservationsUseCase = getReservationsUseCase;

  }

  @GetMapping("/items/{sku}")
  public Object get(@PathVariable String sku){
    return getReservationUseCase.execute(sku);
  }

    @GetMapping("/reservations")
    public ResponseEntity<?> listReservations(
            @RequestParam Optional<String> sku,
            @RequestParam Optional<ReservationState> state,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        PageableRequest pageableRequest=PageableRequest
                .builder()
                .page(page)
                .size(size)
                .sortBy("createdAt")
                .build();

        ReservationPageable reservationPageable=this.getReservationsUseCase.execute(sku,state,pageableRequest);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GenericResponse.of(HttpStatus.OK.value(),"reservaciones encontrados correctamente",reservationPageable));
    }

}
