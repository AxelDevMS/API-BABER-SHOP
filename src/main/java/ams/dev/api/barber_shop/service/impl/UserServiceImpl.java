package ams.dev.api.barber_shop.service.impl;

import ams.dev.api.barber_shop.dto.UserRequestDto;
import ams.dev.api.barber_shop.entity.RoleEntity;
import ams.dev.api.barber_shop.entity.UserEntity;
import ams.dev.api.barber_shop.exceptions.BusinessException;
import ams.dev.api.barber_shop.exceptions.DuplicateResourceException;
import ams.dev.api.barber_shop.mapper.request.UserRequestMapper;
import ams.dev.api.barber_shop.repository.UserRepository;
import ams.dev.api.barber_shop.service.PermissionService;
import ams.dev.api.barber_shop.service.RoleService;
import ams.dev.api.barber_shop.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private UserRequestMapper userRequestMapper;


    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


    @Override
    public UserEntity executeCreateUser(UserRequestDto userRequestDto) {

        this.validateDuplicatedUser(userRequestDto,null);

        RoleEntity role = this.roleService.findRoleById(userRequestDto.getRole().getId());

        if (!userRequestDto.getPassword().equals(userRequestDto.getConfirmPassword()))
            throw new BusinessException("La contraseña y la confirmación de contraseña no coinciden");

        UserEntity userEntity = this.userRequestMapper.toEntity(userRequestDto);
        userEntity.setIsActive(true);
        userEntity.setIsDeleted(false);
        userEntity.setRole(role);
        userEntity = this.userRepository.save(userEntity);
        return userEntity;
    }

    private void validateDuplicatedUser(UserRequestDto userRequestDto, String excludeId){
        Optional<UserEntity> existingUser;

        if (excludeId == null)
            existingUser = userRepository.findByUsername(userRequestDto.getUsername());
        else
            existingUser = userRepository.findByUsernameAndIdNot(userRequestDto.getUsername(), excludeId);

        if (existingUser.isPresent())
            throw new DuplicateResourceException("Role", "nombre", userRequestDto.getUsername());
    }
}
