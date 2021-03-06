/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.gateway.customer.controllers;

import com.biqasoft.entity.constants.SystemRoles;
import com.biqasoft.entity.customer.Company;
import com.biqasoft.entity.format.BiqaPaginationResultList;
import com.biqasoft.entity.filters.CompanyFilter;
import com.biqasoft.gateway.customer.repositories.CompanyRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Api(value = "Company", description = "company controller, used to control b2b agent, b2b clients, partners etc")
@RestController
@Secured({SystemRoles.COMPANY_ROOT, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
@RequestMapping("/v1/company")
public class CompanyController {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyController(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Secured({SystemRoles.COMPANY_GET_ALL, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "get all companies", notes = "preferred to use '/filter' method ")
    @RequestMapping(method = RequestMethod.GET)
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    @Secured({SystemRoles.COMPANY_ADD, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation("add new company")
    @RequestMapping(method = RequestMethod.POST)
    public Company addNewCompany(@RequestBody Company customer, HttpServletResponse response) {
        companyRepository.addCompany(customer);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return customer;
    }

    @Secured(value = {SystemRoles.COMPANY_EDIT, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "update current company")
    @RequestMapping(method = RequestMethod.PUT)
    public Company updateCompany(@RequestBody Company customer) {
       return companyRepository.updateCompany(customer);
    }

    @Secured(value = {SystemRoles.COMPANY_DELETE, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "delete company by ID")
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void deleteCompanyById(HttpServletResponse response, @PathVariable("id") String id) {
        companyRepository.deleteCompanyById(id);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Secured(value = {SystemRoles.COMPANY_EDIT, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "get company by ID")
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Company findCompanyById(HttpServletResponse response, @PathVariable("id") String id) {
        response.setStatus(HttpServletResponse.SC_OK);
        return companyRepository.findCompanyById(id);
    }

    @Secured(value = {SystemRoles.COMPANY_GET_ALL, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "Get all companies with pagination and filters", notes = "get all companies, according to filters and pagination limits")
    @RequestMapping(value = "filter", method = RequestMethod.POST)
    public BiqaPaginationResultList<Company> getCompanyByFilter(@RequestBody CompanyFilter filter, HttpServletResponse response) {
        return companyRepository.getCompanyByFilter(filter);
    }

}
