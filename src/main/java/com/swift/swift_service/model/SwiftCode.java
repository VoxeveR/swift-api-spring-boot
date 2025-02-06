package com.swift.swift_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Builder
@Table(name = "swift_codes")

public class SwiftCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('swift_codes_id_seq'")
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "country_iso2", nullable = false, length = 2)
    private String countryIso2;

    @Column(name = "swift_code", nullable = false, length = 11)
    private String swiftCode;

    @Column(name = "code_type", nullable = false, length = 10)
    private String codeType;

    @Column(name = "institution_name", nullable = false)
    private String institutionName;

    @Column(name = "address", nullable = false, length = Integer.MAX_VALUE)
    private String address;

    @Column(name = "town_name", nullable = false, length = 100)
    private String townName;

    @Column(name = "country_name", nullable = false, length = 100)
    private String countryName;

    @Column(name = "time_zone", nullable = false, length = 50)
    private String timeZone;

    @Column(name = "is_headquarter", nullable = false)
    private Boolean isHeadquarter;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "headquarters_id", foreignKey = @ForeignKey(name = "fk_headquarters"))
    private SwiftCode headquarter;
}