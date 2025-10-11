package com.example.demo.memberCard;

import com.example.demo.book.BookMemberCard;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Data
public class MemberCardService {

    private final MemberCardRepository memberCardRepository;


    public ResponseEntity<List<MemberCard>> getMemberCards(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<MemberCard> pages = memberCardRepository.findAll(pageable);
        return ResponseEntity.ok(pages.toList());
    }

    public ResponseEntity<LinkedHashMap<String, Object>> getMemberCard(UUID memberCardUUID) throws ResponseStatusException {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        Optional<MemberCard> MemberCard = Optional.ofNullable(memberCardRepository.findMemberCardByUuid(memberCardUUID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "student card does not exist")));
        Set<BookMemberCard> book = MemberCard.get().getBooks();
        LinkedHashMap<String, Object> data = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> list = new LinkedHashMap<>();
        list.put("book", book.size());
        response.put("status", "success");
        response.put("type", "Collection");
        response.put("size", list);
        data.put("book", book);
        response.put("data", data);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity deleteMemberCard(UUID memberCardUUID) throws ResponseStatusException {
        MemberCard MemberCard = this.memberCardRepository.findMemberCardByUuid(memberCardUUID).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "student card does not exist"));
        memberCardRepository.delete(MemberCard);
        return ResponseEntity.status(204).build();
    }

    public ResponseEntity<MemberCard> postMemberCard(UUID memberCardUUID) throws ResponseStatusException {
        MemberCard memberCard = new MemberCard(memberCardUUID);
        memberCard.setCreated_at(LocalDateTime.now());
        memberCard.setValid_until(LocalDateTime.now().plusYears(2));
        memberCard.setActive(true);
        memberCardRepository.save(memberCard);
        return ResponseEntity.status(HttpStatus.CREATED).location(URI.create("http://localhost:8083/api/studentCard/" + memberCard.getUuid())).body(memberCard);
    }


}
