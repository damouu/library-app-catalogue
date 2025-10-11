package com.example.demo.memberCard;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberCardRepository extends JpaRepository<MemberCard, Integer> {

    Optional<MemberCard> findMemberCardByUuid(UUID studentCardNumber);
}
