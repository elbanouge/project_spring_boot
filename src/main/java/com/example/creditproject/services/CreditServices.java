package com.example.creditproject.services;

import com.example.creditproject.entities.Credit;
import com.example.creditproject.entities.User;
import com.example.creditproject.repositories.CreditRepository;
import com.example.creditproject.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CreditServices {

    @Autowired
    private CreditRepository creditRepository;
    @Autowired
    private UserRepository userRepository;

    public Credit addCredit(Credit Credit)
    {
        return creditRepository.save(Credit);
    }

    public List<Credit> getAllCredit()
    {
        return creditRepository.findAll();
    }
    public Credit getCreditById(long id)
    {
        return creditRepository.findById(id).orElse(null);
    }

    public void deleteCredit(long id)
    {
        boolean found=creditRepository.existsById(id);
        if(found==true)
            creditRepository.deleteById(id);
    }

    public Credit updateCredit(Credit Credit,long id)
    {
        boolean found=creditRepository.existsById(id);
        if(found==true)
            return creditRepository.save(Credit);
        return null;
    }
    public List<Credit> CreditByidUser(User id){
        return  creditRepository.findByuser(id);
    }

    public Credit OneCreditByidUser(User id){
        return  creditRepository.findByUser(id);
    }

}
