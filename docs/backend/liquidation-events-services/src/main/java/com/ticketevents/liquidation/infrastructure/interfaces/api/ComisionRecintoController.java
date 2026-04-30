package com.ticketevents.liquidation.infrastructure.interfaces.api;

import com.ticketevents.liquidation.application.usecase.ConsultarComisionRecintoUseCase;
import com.ticketevents.liquidation.application.usecase.ConsultarComisionRecintoResult;
import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.ComisionResponse;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/recintos")
public class ComisionRecintoController {

    private final ConsultarComisionRecintoUseCase useCase;

    public ComisionRecintoController(ConsultarComisionRecintoUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/{id}/comision")
    public ResponseEntity<ComisionResponse> consultar(@PathVariable("id") @Positive Long id) {
        ConsultarComisionRecintoResult result = useCase.ejecutar(id);
        ComisionResponse response = new ComisionResponse(
                result.configurada(),
                result.mensaje(),
                result.tipoComision(),
                result.valorComision()
        );
        return ResponseEntity.ok(response);
    }
}
