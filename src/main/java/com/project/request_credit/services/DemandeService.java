package com.project.request_credit.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.request_credit.entities.Credit;
import com.project.request_credit.entities.Demande;
import com.project.request_credit.repositories.DemandeRepository;

@Service
@Transactional
public class DemandeService {
    @Autowired
    private DemandeRepository demandeRepository;

    public Demande addDemande(Demande demande, Credit credit) {
        demande.setCredit(credit);
        return demandeRepository.save(demande);
    }

    public List<Demande> getAllDemande() {
        return (List<Demande>) demandeRepository.findAll();
    }

    public Demande getDemandeById(long id) {
        return demandeRepository.findById(id).orElse(null);
    }

    public void deleteDemande(long id) {
        boolean found = demandeRepository.existsById(id);
        if (found == true) {
            demandeRepository.deleteById(id);
        }
    }

    public Demande updateDemande(Demande demande, long id) {
        boolean found = demandeRepository.existsById(id);
        if (found == true) {
            return demandeRepository.save(demande);
        } else {
            return null;
        }
    }
}
