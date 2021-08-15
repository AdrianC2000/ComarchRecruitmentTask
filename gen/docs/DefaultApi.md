# DefaultApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**tableNameFilterLogicPost**](DefaultApi.md#tableNameFilterLogicPost) | **POST** /{tableName}/filter/{logic} | 
[**tableNameGet**](DefaultApi.md#tableNameGet) | **GET** /{tableName} | 
[**tableNameIdDelete**](DefaultApi.md#tableNameIdDelete) | **DELETE** /{tableName}/{id} | 
[**tableNameIdParameterToChangePut**](DefaultApi.md#tableNameIdParameterToChangePut) | **PUT** /{tableName}/{id}/{parameterToChange} | 
[**tableNamePost**](DefaultApi.md#tableNamePost) | **POST** /{tableName} | 


<a name="tableNameFilterLogicPost"></a>
# **tableNameFilterLogicPost**
> tableNameFilterLogicPost(logic, tableName, body)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    String logic = "logic_example"; // String | 
    String tableName = "tableName_example"; // String | 
    Object body = null; // Object | 
    try {
      apiInstance.tableNameFilterLogicPost(logic, tableName, body);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#tableNameFilterLogicPost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **logic** | **String**|  |
 **tableName** | **String**|  |
 **body** | **Object**|  | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

<a name="tableNameGet"></a>
# **tableNameGet**
> tableNameGet(tableName)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    String tableName = "tableName_example"; // String | 
    try {
      apiInstance.tableNameGet(tableName);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#tableNameGet");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **tableName** | **String**|  |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

<a name="tableNameIdDelete"></a>
# **tableNameIdDelete**
> tableNameIdDelete(id, tableName)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    Integer id = 56; // Integer | 
    String tableName = "tableName_example"; // String | 
    try {
      apiInstance.tableNameIdDelete(id, tableName);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#tableNameIdDelete");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Integer**|  |
 **tableName** | **String**|  |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

<a name="tableNameIdParameterToChangePut"></a>
# **tableNameIdParameterToChangePut**
> tableNameIdParameterToChangePut(id, parameterToChange, tableName, value)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    Integer id = 56; // Integer | 
    String parameterToChange = "parameterToChange_example"; // String | 
    String tableName = "tableName_example"; // String | 
    String value = "value_example"; // String | 
    try {
      apiInstance.tableNameIdParameterToChangePut(id, parameterToChange, tableName, value);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#tableNameIdParameterToChangePut");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **Integer**|  |
 **parameterToChange** | **String**|  |
 **tableName** | **String**|  |
 **value** | **String**|  | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

<a name="tableNamePost"></a>
# **tableNamePost**
> tableNamePost(tableName, body)



### Example
```java
// Import classes:
import org.openapitools.client.ApiClient;
import org.openapitools.client.ApiException;
import org.openapitools.client.Configuration;
import org.openapitools.client.models.*;
import org.openapitools.client.api.DefaultApi;

public class Example {
  public static void main(String[] args) {
    ApiClient defaultClient = Configuration.getDefaultApiClient();
    defaultClient.setBasePath("http://localhost");

    DefaultApi apiInstance = new DefaultApi(defaultClient);
    String tableName = "tableName_example"; // String | 
    Object body = null; // Object | 
    try {
      apiInstance.tableNamePost(tableName, body);
    } catch (ApiException e) {
      System.err.println("Exception when calling DefaultApi#tableNamePost");
      System.err.println("Status code: " + e.getCode());
      System.err.println("Reason: " + e.getResponseBody());
      System.err.println("Response headers: " + e.getResponseHeaders());
      e.printStackTrace();
    }
  }
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **tableName** | **String**|  |
 **body** | **Object**|  | [optional]

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
**200** | OK |  -  |

