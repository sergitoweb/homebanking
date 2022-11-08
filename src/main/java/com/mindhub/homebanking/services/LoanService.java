package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.dtos.LoanAplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    HttpSession session;

    @Transactional
    public String makeLoan(LoanAplicationDTO loanAplicationDTO, Client client){

        Loan loan= loanRepository.findById(loanAplicationDTO.getLoanId()).orElse(null);

        if(loanAplicationDTO.getAmount()<=0 || loanAplicationDTO.getPayments()<=0 ){
            return "mensaje.field.fail";
        }

        if(loan==null){
            return "mensje.loan.notFound";
        }

        if(loan.getMaxAmount()< loanAplicationDTO.getAmount()){
            return "mensaje.loan.maxAmountViolation";
        }

        if(!(loan.getPayments().stream().anyMatch(integer -> integer==loanAplicationDTO.getPayments()))){
            return "mensaje.loan.feesInvalid";
        }

        if(!accountService.validarCuenta(loanAplicationDTO.getToAccountNumber())){
            return "mensaje.originAccount.invalid";
        }

        if(!accountService.validarCuenta(client,loanAplicationDTO.getToAccountNumber())){
            return "mensaje.destinationAccountNotLogin";
        }

        ClientLoan newclientLoan = new ClientLoan((loanAplicationDTO.getAmount()*1.2), loanAplicationDTO.getPayments(), client, loan);

        client.addClientLoan(newclientLoan);
        loan.addClientLoan(newclientLoan);

        clientLoanRepository.save(newclientLoan);


        Account accountDestination = accountRepository.findByNumber(loanAplicationDTO.getToAccountNumber()).orElse(null);
        transactionService.makeTransactionLoan(accountDestination,loanAplicationDTO.getAmount(),loan);


        return "mensaje.exito";
    }

    public List<LoanDTO> showAll() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());
    }
}
