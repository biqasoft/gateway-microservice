/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.gateway.useraccount.controllers;

import com.biqasoft.entity.constants.SystemRoles;
import com.biqasoft.entity.dto.useraccount.PasswordResetDTO;
import com.biqasoft.entity.dto.useraccount.UserRegisterRequest;
import com.biqasoft.entity.core.useraccount.UserAccount;
import com.biqasoft.gateway.useraccount.MicroserviceUsersPasswordReset;
import com.biqasoft.microservice.common.MicroserviceUsersRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "User Accounts")
@Secured(value = {SystemRoles.USER_ACCOUNT_ROOT, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
@RestController
@RequestMapping(value = "/v1/account")
public class UserAccountController {

    private final MicroserviceUsersRepository microserviceUsersRepository;
    private final MicroserviceUsersPasswordReset microserviceUsersPasswordReset;

    @Autowired
    public UserAccountController(MicroserviceUsersRepository microserviceUsersRepository,
                                 MicroserviceUsersPasswordReset microserviceUsersPasswordReset) {
        this.microserviceUsersRepository = microserviceUsersRepository;
        this.microserviceUsersPasswordReset = microserviceUsersPasswordReset;
    }

    @Secured(value = {SystemRoles.USER_ACCOUNT_GET, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "get all users in current domain")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserAccount> getAllUserAccounts() {
        return microserviceUsersRepository.findAllUsers();
    }

    @Secured(value = {SystemRoles.USER_ACCOUNT_GET, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "get user by id")
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public UserAccount getDetailedUserById(@PathVariable("id") String id) {
        return microserviceUsersRepository.findByUserId(id);
    }

    @Secured(value = {SystemRoles.USER_ACCOUNT_EDIT, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "update user account")
    @RequestMapping(method = RequestMethod.PUT)
    public UserAccount updateUserAccount(@RequestBody UserAccount userPosted) {
        microserviceUsersRepository.updateUserAccount(userPosted);
        return userPosted;
    }

    @Secured(value = {SystemRoles.USER_ACCOUNT_EDIT, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "change password password for some user in current domain")
    @RequestMapping(value = "change_password", method = RequestMethod.PUT)
    public PasswordResetDTO changePassword(@RequestBody UserAccount userPosted) {
        return microserviceUsersPasswordReset.resetPasswordForUserInDomain(userPosted);
    }

    @ApiOperation(value = "add new user")
    @Secured(value = {SystemRoles.USER_ACCOUNT_ADD, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public UserAccount createUserAccount(@RequestBody UserRegisterRequest userAccountAddRequest, HttpServletResponse response) {
        UserAccount userPosted = userAccountAddRequest.getUserAccount();

        UserRegisterRequest requestDTO = new UserRegisterRequest();
        UserAccount user = new UserAccount();
        requestDTO.setUserAccount(user);
        requestDTO.setSendWelcomeEmail(userAccountAddRequest.isSendWelcomeEmail());
        requestDTO.setPassword(userAccountAddRequest.getPassword());

        user.setFirstname(userPosted.getFirstname());
        user.setLastname(userPosted.getLastname());
        user.setUsername(userPosted.getUsername());

        user.setEmail(userPosted.getEmail());
        user.setRoles(userPosted.getRoles());

        microserviceUsersRepository.addUser(requestDTO);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return user;
    }

}
