package com.mercadolibre.inventory.store.infrastructure.adapters.in.sync;

import com.mercadolibre.inventory.shared.commands.CancelCommand;
import com.mercadolibre.inventory.shared.commands.CommitCommand;
import com.mercadolibre.inventory.shared.commands.ReserveCommand;
import com.mercadolibre.inventory.shared.models.GenericResponse;
import com.mercadolibre.inventory.store.application.usecase.SendCancelCommandUseCase;
import com.mercadolibre.inventory.store.application.usecase.SendCommitCommandUseCase;
import com.mercadolibre.inventory.store.application.usecase.SendReserveCommandUseCase;
import com.mercadolibre.inventory.store.domain.model.Cancel;
import com.mercadolibre.inventory.store.domain.model.Commit;
import com.mercadolibre.inventory.store.domain.model.Reserve;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/v1/commands")
public class CommandController {
    private final SendReserveCommandUseCase reserve;
    private final SendCommitCommandUseCase commit;
    private final SendCancelCommandUseCase cancel;

    public CommandController(SendReserveCommandUseCase r, SendCommitCommandUseCase c, SendCancelCommandUseCase k){
        this.reserve=r;
        this.commit=c;
        this.cancel=k;
    }

    @Operation(
            summary = "send reserve command",
            description = "send reserve command to central service",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "reserve send successfully",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Body invalid"
                    )
            }
    )
    @PostMapping("/reserve") public ResponseEntity<?> reserve(@Valid @RequestBody ReserveCommand req){
        reserve.execute(new Reserve(req.getSku(), req.getSiteId(), req.getReservationId(), req.getQty()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GenericResponse.of(HttpStatus.OK.value(),"Mensaje enviado correctamente",null));
    }

    @Operation(
            summary = "send reserve command",
            description = "send reserve command to central service",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "reserve send successfully",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Body invalid"
                    )
            }
    )
    @PostMapping("/commit")  public ResponseEntity<?> commit(@Valid @RequestBody CommitCommand req){
        commit.execute(new Commit(req.getSku(), req.getReservationId()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GenericResponse.of(HttpStatus.OK.value(),"Mensaje enviado correctamente",null));

    }

    @Operation(
            summary = "send reserve command",
            description = "send reserve command to central service",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "reserve send successfully",
                            content = @Content(schema = @Schema(implementation = ResponseEntity.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Body invalid"
                    )
            }
    )
    @PostMapping("/cancel")  public ResponseEntity<?> cancel(@Valid @RequestBody CancelCommand req){
        cancel.execute(new Cancel(req.getSku(), req.getReservationId(), req.getReason()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(GenericResponse.of(HttpStatus.OK.value(),"Mensaje enviado correctamente",null));

    }
}
