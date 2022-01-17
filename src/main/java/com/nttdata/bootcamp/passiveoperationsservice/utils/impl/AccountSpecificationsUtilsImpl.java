package com.nttdata.bootcamp.passiveoperationsservice.utils.impl;

import com.nttdata.bootcamp.passiveoperationsservice.utils.AccountSpecificationsUtils;
import org.springframework.stereotype.Component;

@Component
public class AccountSpecificationsUtilsImpl implements AccountSpecificationsUtils {
    @Override
    public Double roundDouble(Double numberToRound, int decimalPlaces) {
        numberToRound = numberToRound * Math.pow(10, decimalPlaces);
        numberToRound = (double) (Math.round(numberToRound));
        return numberToRound / Math.pow(10, decimalPlaces);
    }

    @Override
    public Double applyInterests(Double amount, Double interestPercentage) {
        return (100 + interestPercentage)/100 * amount;
    }

    @Override
    public Double calculateCommission(Double amount, Double interestPercentage) {
        return interestPercentage/100 * amount;
    }
}
