package com.nttdata.bootcamp.passiveoperationsservice.utils;

public interface AccountSpecificationsUtils {
    Double roundDouble(Double numberToRound, int decimalPlaces);
    Double applyInterests(Double amount, Double interestPercentage);
    Double calculateCommission(Double amount, Double interestPercentage);
}
