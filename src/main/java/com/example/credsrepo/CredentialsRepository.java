package com.example.credsrepo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CredentialsRepository extends JpaRepository<Credential, Integer> {
    @Query(value = "SELECT c FROM Credential c WHERE c.group in ?1")
    public List<Credential> findByGroups(List<String> groups);


}
