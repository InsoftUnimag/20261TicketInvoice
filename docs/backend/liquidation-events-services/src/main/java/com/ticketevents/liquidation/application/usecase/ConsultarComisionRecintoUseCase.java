package com.ticketevents.liquidation.application.usecase;

import com.ticketevents.liquidation.domain.entities.ComisionConfig;
import com.ticketevents.liquidation.domain.repositories.ComisionConsultaRepository;
import com.ticketevents.liquidation.shared.errors.BusinessException;
import com.ticketevents.liquidation.shared.errors.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ConsultarComisionRecintoUseCase {

    private static final String MENSAJE_SIN_COMISION = "El recinto no tiene una comision registrada";
    private static final String MENSAJE_RECINTO_NO_EXISTE = "El recinto no esta registrado";

    private final ComisionConsultaRepository repository;

    public ConsultarComisionRecintoUseCase(ComisionConsultaRepository repository) {
        this.repository = repository;
    }

    public ConsultarComisionRecintoResult ejecutar(Long recintoId) {
        if (!repository.existsRecintoById(recintoId)) {
            throw new BusinessException(ErrorCode.VENUE_NOT_FOUND, MENSAJE_RECINTO_NO_EXISTE);
        }

        return repository.findComisionByRecintoId(recintoId)
                .map(this::withComision)
                .orElseGet(this::withoutComision);
    }

    private ConsultarComisionRecintoResult withComision(ComisionConfig config) {
        return new ConsultarComisionRecintoResult(
                true,
                "Comision configurada",
                config.tipoComision().name(),
                config.valorComision()
        );
    }

    private ConsultarComisionRecintoResult withoutComision() {
        return new ConsultarComisionRecintoResult(false, MENSAJE_SIN_COMISION, null, null);
    }
}
