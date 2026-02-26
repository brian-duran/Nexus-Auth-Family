package userapp.brian.duran.userappapi.service;

import userapp.brian.duran.userappapi.dto.CreateUserRequest;
import userapp.brian.duran.userappapi.dto.CreateUserResponse;
import userapp.brian.duran.userappapi.entity.User;

public interface UserService {

  User registerNewUser(User user);
  CreateUserResponse save(CreateUserRequest request);
}
