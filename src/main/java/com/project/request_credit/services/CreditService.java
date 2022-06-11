package com.project.request_credit.services;

import java.util.List;

import com.project.request_credit.entities.Credit;
import com.project.request_credit.entities.User;
import com.project.request_credit.repositories.CreditRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

    public List<Credit> getAllCredits() {
        return creditRepository.findAll(Sort.by("id"));
    }

    public Credit getCreditById(long id) {
        return creditRepository.findById(id).orElse(null);
    }

    public void deleteCredit(long id) {
        Credit credit = creditRepository.findById(id).orElse(null);
        if (credit != null) {
            credit.setUser(null);
            creditRepository.delete(credit);
        }
    }

    public Credit updateCredit(Credit credit, long id) {
        boolean found = creditRepository.existsById(id);
        if (found == true) {
            return creditRepository.save(credit);
        } else {
            return null;
        }
    }

    public List<Credit> getCreditsByUser(User user) {
        return creditRepository.findCreditsByUser(user);
    }

    public Credit getCreditByUser(User user) {
        return creditRepository.findByUser(user);
    }
}
