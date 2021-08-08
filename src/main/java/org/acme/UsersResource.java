package org.acme;

import Data.User;
import Database.DatabaseHandler;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/users")
public class UsersResource {
    public static List<User> users = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsers() throws SQLException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        List<User> users = DatabaseHandler.getResource("users");
        return Response.ok(users).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/size")
    public Integer countUsers() {
        return users.size();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUsers(User newUser) {
        users.add(newUser);
        return Response.ok(users).build();
    }

    @PUT
    @Path("{id}/{userName}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(
            @PathParam("id") Long id,
            @PathParam("userName") String userName) {
        users = users.stream().map(user -> {
            if(user.getID_user().equals(id)) {
                user.setLogin(userName);
            }
            return user;
        }).collect(Collectors.toList());
        return Response.ok(users).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteUser(
            @PathParam("id") Integer id) {
        boolean doesUserExist = DatabaseHandler.userExistance(id, "users");
        boolean removed = false;
        if (doesUserExist)
            removed = DatabaseHandler.deleteResource(id, "users");
        if (removed)
            return Response.ok().build();
        else
            return Response.status(Response.Status.NOT_FOUND).build();
    }
}
