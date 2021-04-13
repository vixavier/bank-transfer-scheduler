package br.com.cvc.banktransferscheduler.usecases.service;

import br.com.cvc.banktransferscheduler.entities.TransferRequest;
import br.com.cvc.banktransferscheduler.entities.TransferResponse;
import br.com.cvc.banktransferscheduler.entities.database.ITransferRepository;
import br.com.cvc.banktransferscheduler.entities.database.TransferEntity;
import br.com.cvc.banktransferscheduler.usecases.fee.IFeeType;
import br.com.cvc.banktransferscheduler.usecases.fee.enums.Fee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransferServiceImpl implements ITransferService {

    @Autowired
    private IFeeType iFeeType;

    @Autowired
    private ITransferRepository iTransferRepository;

    public TransferResponse createTransfer(TransferRequest transferRequest) {

        Fee feeType = iFeeType.setFeeType(transferRequest);

        TransferEntity transferEntity = TransferEntity.builder()
                .originAccount(transferRequest.getOriginAccount())
                .destinationAccount(transferRequest.getDestinationAccount())
                .transferValue(transferRequest.getTransferValue())
                .schedulingDate(LocalDate.now())
                .transferDate(transferRequest.getTransferDate())
                .feeValue(feeType.calculateFeeValue(transferRequest).setScale(2, RoundingMode.DOWN))
                .feeType(feeType)
                .build();
        return new TransferResponse(iTransferRepository.save(transferEntity));
    }

    public Optional<TransferEntity> readTransfer(Long id) {
        Optional<TransferEntity> optional = iTransferRepository.findById(id);
        return optional;
    }

    public List<TransferResponse> getAll() {
        List<TransferResponse> transferResponses = new ArrayList<>();
        List<TransferEntity> transferEntities = iTransferRepository.findAll();
        transferEntities.forEach(ent -> {
            transferResponses.add(new TransferResponse(ent));
        });
        return transferResponses;
    }

    public TransferResponse updateTransfer(Long id, TransferRequest transferRequest) {

        Fee feeType = iFeeType.setFeeType(transferRequest);

        TransferEntity transferEntity = iTransferRepository.getOne(id);
        transferEntity.setOriginAccount(transferRequest.getOriginAccount());
        transferEntity.setDestinationAccount(transferRequest.getDestinationAccount());
        transferEntity.setTransferValue(transferRequest.getTransferValue());
        transferEntity.setSchedulingDate(LocalDate.now());
        transferEntity.setTransferDate(transferRequest.getTransferDate());
        transferEntity.setFeeValue(feeType.calculateFeeValue(transferRequest).setScale(2, RoundingMode.DOWN));
        transferEntity.setFeeType(feeType);

        return new TransferResponse(iTransferRepository.save(transferEntity));
    }

    public boolean deleteTransfer(Long id) {
        Optional<TransferEntity> optional = iTransferRepository.findById(id);
        if (!optional.isPresent()) {
            return false;
        }
        iTransferRepository.delete(optional.get());
        return true;
    }


}
