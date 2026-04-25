package com.ticketevents.liquidation.application.usecase;

import com.ticketevents.liquidation.infrastructure.adapter.input.rest.response.RegistrarComisionRecintoResponse;
import com.ticketevents.liquidation.domain.entities.ComisionRecinto;
import com.ticketevents.liquidation.domain.entities.TipoComision;
import com.ticketevents.liquidation.domain.repositories.ComisionRecintoRepository;
import com.ticketevents.liquidation.infrastructure.mappers.ComisionRecintoMapper;
import com.ticketevents.liquidation.shared.errors.BusinessException;
import com.ticketevents.liquidation.shared.errors.ErrorCode;
import com.ticketevents.liquidation.shared.errors.TechnicalException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class RegistrarComisionRecintoUseCase {

    private static final Logger log = LoggerFactory.getLogger(RegistrarComisionRecintoUseCase.class);

    private final ComisionRecintoRepository repository;
    private final ComisionRecintoMapper mapper;

    public RegistrarComisionRecintoUseCase(ComisionRecintoRepository repository, 
                                           ComisionRecintoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public RegistrarComisionRecintoResponse execute(Long recintoId, TipoComision tipoComision, 
                                                     BigDecimal valorComision) {
        log.info("Iniciando registro de comision para recinto: {}", recintoId);

        if (recintoId == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "El ID del recinto es requerido");
        }

        if (tipoComision == null) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, "El tipo de comision es requerido");
        }

        if (valorComision == null || valorComision.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST, 
                    "El valor de comision debe ser un numero valido positivo");
        }

        boolean existeRecinto;
        try {
            existeRecinto = repository.existeRecinto(recintoId);
        } catch (Exception e) {
            log.error("Error al verificar existencia del recinto {}: {}", recintoId, e.getMessage());
            throw new TechnicalException(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, e);
        }

        if (!existeRecinto) {
            log.warn("Recinto {} no encontrado", recintoId);
            throw new BusinessException(ErrorCode.RECINTO_NOT_FOUND, 
                    "El recinto no existe en el sistema");
        }

        ComisionRecinto comision = new ComisionRecinto(
            1L,
            recintoId,
            tipoComision,
            valorComision
        );

        try {
            comision.validar();
        } catch (IllegalArgumentException e) {
            log.error("Validacion de comision fallida: {}", e.getMessage());
            throw new BusinessException(ErrorCode.INVALID_REQUEST, e.getMessage());
        }

        ComisionRecinto guardada;
        try {
            Optional<ComisionRecinto> resultado = repository.guardar(comision);
            guardada = resultado.orElseThrow(() -> {
                log.error("Error al guardar comision para recinto {}", recintoId);
                return new TechnicalException(ErrorCode.INVALID_REQUEST, 
                        "Error al guardar la comision del recinto");
            });
        } catch (Exception e) {
            log.error("Error al guardar comision para recinto {}: {}", recintoId, e.getMessage());
            throw new TechnicalException(ErrorCode.EXTERNAL_SERVICE_UNAVAILABLE, e);
        }

        RegistrarComisionRecintoResponse response = mapper.toResponse(guardada);

        log.info("Comision registrada exitosamente para recinto: {} - Tipo: {} - Valor: {}", 
                recintoId, tipoComision, valorComision);
        return response;
    }
}