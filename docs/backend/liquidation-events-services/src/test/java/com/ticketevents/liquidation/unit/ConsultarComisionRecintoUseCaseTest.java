package com.ticketevents.liquidation.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.ticketevents.liquidation.application.usecase.ConsultarComisionRecintoResult;
import com.ticketevents.liquidation.application.usecase.ConsultarComisionRecintoUseCase;
import com.ticketevents.liquidation.domain.entities.ComisionConfig;
import com.ticketevents.liquidation.domain.entities.TipoComision;
import com.ticketevents.liquidation.domain.repositories.ComisionConsultaRepository;
import com.ticketevents.liquidation.shared.errors.BusinessException;
import com.ticketevents.liquidation.shared.errors.ErrorCode;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConsultarComisionRecintoUseCaseTest {

    @Mock
    private ComisionConsultaRepository repository;

    @InjectMocks
    private ConsultarComisionRecintoUseCase useCase;

    @Test
    void debeRetornarComisionConfigurada() {
        when(repository.existsRecintoById(10L)).thenReturn(true);
        when(repository.findComisionByRecintoId(10L))
                .thenReturn(Optional.of(new ComisionConfig(true, TipoComision.FIJA, new BigDecimal("250000.00"))));

        ConsultarComisionRecintoResult response = useCase.ejecutar(10L);

        assertThat(response.configurada()).isTrue();
        assertThat(response.tipoComision()).isEqualTo("FIJA");
        assertThat(response.valorComision()).isEqualByComparingTo("250000.00");
    }

    @Test
    void debeRetornarSinComision() {
        when(repository.existsRecintoById(20L)).thenReturn(true);
        when(repository.findComisionByRecintoId(20L)).thenReturn(Optional.empty());

        ConsultarComisionRecintoResult response = useCase.ejecutar(20L);

        assertThat(response.configurada()).isFalse();
        assertThat(response.tipoComision()).isNull();
        assertThat(response.valorComision()).isNull();
    }

    @Test
    void debeLanzarErrorSiRecintoNoExiste() {
        when(repository.existsRecintoById(99L)).thenReturn(false);

        assertThatThrownBy(() -> useCase.ejecutar(99L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.VENUE_NOT_FOUND);
    }
}
