package com.project.request_credit.services;

import java.util.List;

import com.project.request_credit.entities.Credit;
import com.project.request_credit.entities.User;
import com.project.request_credit.repositories.CreditRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CreditService {
    @Autowired
    private CreditRepository creditRepository;

    public Credit addCredit(Credit credit, User user) {
        credit.setUser(user);
        return creditRepository.save(credit);
    }

    public List<Credit> getAllCredit() {
        return (List<Credit>) creditRepository.findAll();
    }

    public Credit getCreditById(long id) {
        return creditRepository.findById(id).orElse(null);
    }

    public void deleteCredit(long id) {
        boolean found = creditRepository.existsById(id);
        if (found == true)
            creditRepository.deleteById(id);
    }

    public Credit updateCredit(Credit credit, long id) {
        boolean found = creditRepository.existsById(id);
        if (found == true)
            return creditRepository.save(credit);
        return null;
    }

    public List<Credit> CreditByidUser(User id) {
        return creditRepository.findCreditsByUser(id);
    }

    public Credit getCreditByUser(User user) {
        return creditRepository.findByUser(user);
    }
}
