package se.ifmo.korotin.springmvc;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface AircraftRepository extends ReactiveCrudRepository<Aircraft, Long> {
}
