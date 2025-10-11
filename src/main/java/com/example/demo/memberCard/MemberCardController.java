package com.example.demo.memberCard;

import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

@Data
@Validated
@CrossOrigin(allowedHeaders = "*", origins = "*", methods = {RequestMethod.GET, RequestMethod.POST})
@RestController
@RequestMapping("api/memberCard")
public class MemberCardController {

    private final MemberCardService memberCardService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MemberCard>> getMemberCards(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page, @RequestParam(name = "size", required = false, defaultValue = "5") Integer size) {
        return memberCardService.getMemberCards(page, size);
    }

    @GetMapping(path = "{studentCardUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LinkedHashMap<String, Object>> getMemberCard(@PathVariable("studentCardUuid") UUID studentCardUuid) {
        return memberCardService.getMemberCard(studentCardUuid);
    }

    @DeleteMapping(value = "/{studentCardUuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteMemberCard(@PathVariable("studentCardUuid") UUID studentCardUuid) {
        return memberCardService.deleteMemberCard(studentCardUuid);
    }

    @PostMapping(path = "/{memberCardUUID}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MemberCard> postMemberCard(@PathVariable("memberCardUUID") UUID memberCardUUID) {
        return memberCardService.postMemberCard(memberCardUUID);
    }

}
