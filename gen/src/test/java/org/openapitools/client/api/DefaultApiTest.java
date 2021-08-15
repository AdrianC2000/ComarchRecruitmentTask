/*
 * Generated API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package org.openapitools.client.api;

import org.openapitools.client.ApiException;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for DefaultApi
 */
@Ignore
public class DefaultApiTest {

    private final DefaultApi api = new DefaultApi();

    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void helloGetTest() throws ApiException {
        String response = api.helloGet();

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void tableNameFilterLogicPostTest() throws ApiException {
        String logic = null;
        String tableName = null;
        Object body = null;
        api.tableNameFilterLogicPost(logic, tableName, body);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void tableNameGetTest() throws ApiException {
        String tableName = null;
        api.tableNameGet(tableName);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void tableNameIdDeleteTest() throws ApiException {
        Integer id = null;
        String tableName = null;
        api.tableNameIdDelete(id, tableName);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void tableNameIdParameterToChangePutTest() throws ApiException {
        Integer id = null;
        String parameterToChange = null;
        String tableName = null;
        String value = null;
        api.tableNameIdParameterToChangePut(id, parameterToChange, tableName, value);

        // TODO: test validations
    }
    
    /**
     * 
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void tableNamePostTest() throws ApiException {
        String tableName = null;
        Object body = null;
        api.tableNamePost(tableName, body);

        // TODO: test validations
    }
    
}
