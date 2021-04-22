package user_service.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user_service.model.Address;
import user_service.model.Message;
import user_service.model.User;
import user_service.repository.AddressRepository;
import user_service.repository.UserRepository;
import user_service.service.ProducerService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProducerService producerService;

    @GetMapping("/")
    public String welcome() {
        return "<html><body>"
                + "<h1>WELCOME To CloudBeds User Microservice</h1>"
                + "</body></html>";
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable(value = "id") Long id) {
        return userRepository.findById(id);
    }

    @GetMapping("/user/country/{country}")
    public List<User> getUsersByCountry(@PathVariable(value = "country") String country) {
        List<Address> addresses = addressRepository.findByCountry(country);

        // TODO-nik change to one query
        List<Long> userIds = addresses.stream().map(Address::getUserId).collect(Collectors.toList());

        return userRepository.findByIdIn(userIds);
    }

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user) {
        User newUser = new User(user.firstName, user.lastName, user.email, user.password);

        User userCreated = userRepository.save(newUser);

        Address newAddress = new Address(
                userCreated.id,
                user.address.address1,
                user.address.address2,
                user.address.city,
                user.address.state,
                user.address.zip,
                user.address.country
        );

        userCreated.setAddress(newAddress);

        producerService.produce(new Message("User was created: " + userCreated.toString(), 100));

//        newAddress.setUser(userCreated);
//        Address addressCreated = addressRepository.save(newAddress);

        return userRepository.findById(userCreated.id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable(value = "id") Long id) {
        System.out.println(id);

        userRepository.deleteById(id);

        producerService.produce(new Message("User with ID: " + id + " was removed", 100));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<Object> updateUser(@RequestBody User user, @PathVariable Long id) {
        Optional<User> userRepo = Optional.ofNullable(userRepository.findById(id));

        if (userRepo.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        user.setId(id);

        userRepository.save(user);

        return ResponseEntity.noContent().build();
    }
}
