package com.swift.swift_service.repository;

import com.swift.swift_service.model.SwiftCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface SwiftRepository extends JpaRepository<SwiftCode, String> {

    SwiftCode findBySwiftCode(String headquartersSwiftCode);

    List<SwiftCode> findBySwiftCodeLike(String branchSwiftCodePattern);

    List<SwiftCode> findAllByHeadquarter(SwiftCode id);

    List<SwiftCode> findAllByCountryIso2(String countryIso2);


}