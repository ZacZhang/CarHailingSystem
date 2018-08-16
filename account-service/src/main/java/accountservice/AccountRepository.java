package accountservice;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccountRepository extends CrudRepository<Account, Long> {
    List<Account> findByFirstName(String firstName);
    List<Account> findByLastName(String lastName);
    List<Account> findByFirstNameAndLastName(String firstName, String lastName);
}
