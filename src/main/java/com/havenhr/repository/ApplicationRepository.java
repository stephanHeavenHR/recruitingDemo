package com.havenhr.repository;

import com.havenhr.entity.Application;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    @Query("SELECT a FROM Application a WHERE a.related.id=?1")
    List<Application> findByOffer(Long offerId);
}
