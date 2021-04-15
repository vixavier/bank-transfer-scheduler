package br.com.cvc.banktransferscheduler.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class TransferResponse {

    private Long id;

    private BigDecimal transferValue;

    private String originAccount;

    private String destinationAccount;

    private BigDecimal feeValue;

    private Enum feeType;

    private LocalDate transferDate;

    private LocalDate schedulingDate;

}
