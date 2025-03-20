package com.assessment.banking.enums;

import com.assessment.banking.services.ITransactionTypeService;
import com.assessment.banking.services.impl.DepositTransactionService;
import com.assessment.banking.services.impl.TransferTransactionService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TransactionType {
    DEPOSIT("deposit") {
        @Override
        public Class<? extends ITransactionTypeService> getImplementation() {
            return DepositTransactionService.class;
        }
    },
    TRANSFER("transfer") {
        @Override
        public Class<? extends ITransactionTypeService> getImplementation() {
            return TransferTransactionService.class;
        }
    };

    public abstract Class<? extends ITransactionTypeService> getImplementation();

    private final String value;

    public static TransactionType findByType(String type){
        for(TransactionType v : values()){
            if( v.getValue().equals(type)){
                return v;
            }
        }
        return null;
    }
}
