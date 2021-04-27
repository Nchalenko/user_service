package user_service.controller;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ProducerService producerService;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/filter")
    public List<User> getUsersByCountry(@RequestParam(name = "country") String country) {
    List<Address> addresses = addressRepository.findByCountry(country);

        List<Long> userIds = addresses.stream().map(Address::getUserId).collect(Collectors.toList());

        return userRepository.findByIdIn(userIds);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable(value = "id") Long id) {
        return userRepository.findById(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public User addUser(@RequestBody User user) {
        user.getAddress().setUser(user);
        user.setAddress(user.getAddress());
        User userCreated = userRepository.save(user);

        producerService.produce(new Message("User was created: " + userCreated.toString(), 100));

        return userRepository.findById(userCreated.id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteUser(@PathVariable(value = "id") Long id) throws NotFoundException {
        Optional<User> userRepo = Optional.ofNullable(userRepository.findById(id));

        if (!userRepo.isPresent()) {
            throw new NotFoundException("User with ID: " + id + " Not found");
        }

        userRepository.deleteById(id);

        producerService.produce(new Message("User with ID: " + id + " was removed", 100));
    }

    @PutMapping("/user/{id}")
    public User updateUser(@RequestBody User user, @PathVariable Long id) throws NotFoundException {
        Optional<User> userRepo = Optional.ofNullable(userRepository.findById(id));

        if (!userRepo.isPresent()) {
            throw new NotFoundException("User with ID: " + id + " Not found");
        }

        user.setId(id);

        userRepository.save(user);

        return userRepository.findById(id);
    }
}
