/*
 * Copyright (c) 2016. com.biqasoft
 */

package com.biqasoft.gateway.customer.controllers;

import com.biqasoft.entity.constants.SystemRoles;
import com.biqasoft.entity.customer.Customer;
import com.biqasoft.entity.customer.DynamicSegment;
import com.biqasoft.entity.customer.SegmentStats;
import com.biqasoft.entity.format.BiqaPaginationResultList;
import com.biqasoft.gateway.customer.repositories.CustomerFilterRequestContextService;
import com.biqasoft.gateway.customer.repositories.SegmentsRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Secured(value = {SystemRoles.SEGMENT_ROOT, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
@Api(value = "Segments - customers & leads ")
@RequestMapping(value = "/v1/segment/dynamic")
public class SegmentsDynamicController {

    private final SegmentsRepository segmentsRepository;
    private final CustomerFilterRequestContextService customerFilterRequestContextService;

    @Autowired
    public SegmentsDynamicController(SegmentsRepository segmentsRepository, CustomerFilterRequestContextService customerFilterRequestContextService) {
        this.segmentsRepository = segmentsRepository;
        this.customerFilterRequestContextService = customerFilterRequestContextService;
    }

    @Secured(value = {SystemRoles.SEGMENT_GET, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "Get all dynamic segments ")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<DynamicSegment> getAllDynamicSegments() {
        return segmentsRepository.findAllDynamicSegments();
    }

    @ApiOperation(value = "add dynamic segment")
    @Secured(value = {SystemRoles.SEGMENT_ADD, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @RequestMapping(value = "", method = RequestMethod.POST)
    public DynamicSegment addDynamicSegment(@RequestBody DynamicSegment dynamicSegment, HttpServletResponse response) {
        if (dynamicSegment.isUsePagination()) {
            dynamicSegment.getCustomerBuilder().setUsePagination(true);
        } else {
            dynamicSegment.getCustomerBuilder().setUsePagination(false);
        }

        dynamicSegment = segmentsRepository.addDynamicSegment(dynamicSegment);
        response.setStatus(HttpServletResponse.SC_CREATED);
        return dynamicSegment;
    }

    @ApiOperation(value = "update dynamic segment")
    @Secured(value = {SystemRoles.SEGMENT_EDIT, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public DynamicSegment updateDynamicSegment(@RequestBody DynamicSegment dynamicSegment, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_CREATED);
        return segmentsRepository.updateDynamicSegment(dynamicSegment);
    }

    @Secured(value = {SystemRoles.CUSTOMER_GET_ALL, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "get customers by dynamic segment id ")
    @RequestMapping(value = "{id}/customers", method = RequestMethod.GET)
    public BiqaPaginationResultList<Customer> customersByDynamicSegment(@PathVariable("id") String id) {
        DynamicSegment customerBuilder = segmentsRepository.findDynamicSegmentById(id);
        return customerFilterRequestContextService.getCustomersByFilter(customerBuilder.getCustomerBuilder());
    }

    @Secured(value = {SystemRoles.CUSTOMER_GET_ALL, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "get dynamic segment info by id ")
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public DynamicSegment getDynamicSegmentMetaInfoById(@PathVariable("id") String id) {
        return segmentsRepository.findDynamicSegmentById(id);
    }

    @Secured(value = {SystemRoles.CUSTOMER_GET_ALL, SystemRoles.ALLOW_ALL_DOMAIN_BASED, SystemRoles.ROLE_ADMIN})
    @ApiOperation(value = "get detailed info by dynamic by id ")
    @RequestMapping(value = "{id}/stats", method = RequestMethod.GET)
    public SegmentStats getStatsByDynamicSegment(@PathVariable("id") String id) {
        return segmentsRepository.getStatsByDynamicSegmentId(id);
    }

}



