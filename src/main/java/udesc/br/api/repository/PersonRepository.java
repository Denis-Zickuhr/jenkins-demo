package udesc.br.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import udesc.br.api.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

}
