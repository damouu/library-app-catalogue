package com.example.demo.book;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@TypeDef(name = "jsonb", typeClass = JsonType.class)
@Entity
@org.hibernate.annotations.Subselect("select 1")
public class BorrowSummaryView {

    @Id
    private UUID borrowUuid;

    private LocalDate borrowReturnDate;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<Map<String, Object>> books;
}
