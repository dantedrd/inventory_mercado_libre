package com.mercadolibre.inventory.central.infrastructure.adapters.in.rest;

import com.mercadolibre.inventory.shared.commands.CancelCommand;
import com.mercadolibre.inventory.shared.commands.CommitCommand;
import com.mercadolibre.inventory.shared.commands.ReserveCommand;
import com.mercadolibre.inventory.central.application.usecase.CancelUseCase;
import com.mercadolibre.inventory.central.application.usecase.CommitUseCase;
import com.mercadolibre.inventory.central.application.usecase.ReserveUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/v1/commands")
public class CommandController {
  private final ReserveUseCase reserve;
  private final CommitUseCase commit;
  private final CancelUseCase cancel;

  public CommandController(ReserveUseCase reserveUseCase, CommitUseCase commitUseCase, CancelUseCase cancelUseCase){
    this.reserve = reserveUseCase;
    this.commit = commitUseCase;
    this.cancel = cancelUseCase;
  }

    @Operation(
            summary = "reserve item",
            description = "reserve item",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "reserve  successfully",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Body invalid"
                    )
            }
    )
  @PostMapping("/reserve")
  public ResponseEntity<?> reserve(@Valid @RequestBody ReserveCommand c){
    reserve.execute(c.getSku(), c.getSiteId(), c.getReservationId(), c.getQty());
    return ResponseEntity.accepted().build();
  }

    @Operation(
            summary = "commit item",
            description = "commit item",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "commit  successfully",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Body invalid"
                    )
            }
    )
  @PostMapping("/commit")
  public ResponseEntity<?> commit(@Valid  @RequestBody CommitCommand c){
    commit.execute(c.getSku(), c.getReservationId());
    return ResponseEntity.accepted().build();
  }


    @Operation(
            summary = "cancel item",
            description = "cancel item",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "cancel  successfully",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Body invalid"
                    )
            }
    )
  @PostMapping("/cancel")
  public ResponseEntity<?> cancel(@Valid @RequestBody CancelCommand c){
    cancel.execute(c.getSku(), c.getReservationId(), c.getReason());
    return ResponseEntity.accepted().build();
  }
}
