package user_service.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import user_service.model.Address;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {
    Address findById(int id);
    List<Address> findAll();
    List<Address> findByCountry(String country);
    void deleteById(int id);
}
