package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.LoanAplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.LoanService;
import com.mindhub.homebanking.services.NotificationService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class LoanServiceImp implements LoanService {


    private LoanRepository loanRepository;


    private AccountService accountService;


    private ClientLoanRepository clientLoanRepository;


    private AccountRepository accountRepository;


    private TransactionService transactionService;


    private ClientRepository clientRepository;


    private NotificationService notificationService;


    public LoanServiceImp(LoanRepository loanRepository,
                          AccountService accountService,
                          ClientLoanRepository clientLoanRepository,
                          AccountRepository accountRepository,
                          TransactionService transactionService,
                          ClientRepository clientRepository,
                          NotificationService notificationService) {
        this.loanRepository = loanRepository;
        this.accountService = accountService;
        this.clientLoanRepository = clientLoanRepository;
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.clientRepository = clientRepository;
        this.notificationService = notificationService;
    }

    @Autowired
    HttpSession session;

    @Override
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

        Account accountDestination = accountRepository.findByNumber(loanAplicationDTO.getToAccountNumber()).orElse(null);

        if(accountDestination.getType().equals(AccountType.CRY)){
            return "mensaje.account.incompatible";
        }


        ClientLoan newclientLoan = new ClientLoan((loanAplicationDTO.getAmount()*1.2), loanAplicationDTO.getPayments(), client, loan);

        client.addClientLoan(newclientLoan);
        loan.addClientLoan(newclientLoan);

        clientLoanRepository.save(newclientLoan);



        transactionService.makeTransactionLoan(accountDestination,loanAplicationDTO.getAmount(),loan);

        if(client.isHasTelegram()){
            notificationService.sendNotification(((Client) session.getAttribute("client")).getEmail(),
                    "El prestamo solicitado para " + loanAplicationDTO.getToAccountNumber() + " ha sido aprobado y depositado.");
        }
        return "mensaje.exito";
    }

    @Override
    public List<LoanDTO> showAll() {
        return loanRepository.findAll().stream().map(LoanDTO::new).collect(toList());
    }
}
