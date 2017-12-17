package com.fly.crm.service;

import com.fly.crm.domain.Customer;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/customers")
public interface CustomerService {

    //    @Override
//    @POST
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    <S extends Customer> S save(S entity);
//
//    @Override
//    @GET
//    @Path("/{id}")
//    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
//    Customer findOne(@PathParam("id") Integer id);
    @PUT
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/mobilePhone/{mobilePhone}")
    void active(
            @PathParam("mobilePhone") String mobilePhone
    );

    @POST
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    void signUp(Customer customer);

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    List<Customer> findAll();

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/FixedAreaId/{id}")
    List<Customer> findByFixedAreaId(@PathParam("id") String Id);

    @PUT
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/fixedAreaId/{fixedAreaId}/customerIds/{customerIds}")
    void associateCustomersWithFixedArea(
            @PathParam("fixedAreaId") String fixedAreaId,
            @PathParam("customerIds") String customerIds
    );

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/mobilePhone/{mobilePhone}/password/{password}")
    Customer login(
            @PathParam("mobilePhone") String mobilePhone,
            @PathParam("password") String password
    );

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Path("/{id}/fixedAreaId")
    String getFixedAreaId(@PathParam("id") Integer id);
}
