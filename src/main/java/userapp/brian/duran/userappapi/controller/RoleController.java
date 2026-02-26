package userapp.brian.duran.userappapi.controller;

import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import userapp.brian.duran.userappapi.dto.CreateRoleRequest;
import userapp.brian.duran.userappapi.dto.CreateRoleResponse;
import userapp.brian.duran.userappapi.service.RoleService;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

  private final RoleService roleService;

  @PostMapping
  public ResponseEntity<CreateRoleResponse> createRole(
      @Valid @RequestBody CreateRoleRequest request) {

    CreateRoleResponse response = roleService.save(request);
    URI location =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.id())
            .toUri();

    return ResponseEntity.created(location).body(response);
  }
}
