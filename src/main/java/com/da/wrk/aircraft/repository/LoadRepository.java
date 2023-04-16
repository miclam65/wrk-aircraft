
package com.da.wrk.aircraft.repository;

import com.da.wrk.aircraft.domain.LoadEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

@Repository
public interface LoadRepository extends JpaRepository<LoadEntity, Long>{

    @QueryHints({
        // @QueryHint(name = "org.hibernate.timeout", value="10"),             // Hibernate timeout in seconds
        // @QueryHint(name = "javax.persistence.query.timeout", value="10000") // JPA timeout in milliseconds
    })
    @Query(value = "SELECT 1 FROM pg_sleep(2)", nativeQuery = true)
    public void sleepQuery();
}