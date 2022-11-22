package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.LoanAplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface LoanService {

    public String makeLoan(LoanAplicationDTO loanAplicationDTO, Client client);

    public List<LoanDTO> showAll();
}
