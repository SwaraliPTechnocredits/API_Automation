package TestScripts;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import POJO.request.CreateBooking.Bookingdates;
import POJO.request.CreateBooking.CreateBookingRequest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class CreateBookingTest {
	
	//Given: all input details -> URL, Headers, path/ query paramerters, payload, 
	//When -> submit the API[headerType,endpoint]
	//Then -> validate the API 
	String token;
	int bookingid;
	CreateBookingRequest CBR;
	
	@BeforeMethod (enabled=false)
	public void GenerateToken()
	{
		RestAssured.baseURI = "https://restful-booker.herokuapp.com";

		String payload = "{\r\n" 
		+ "    \"username\" : \"admin\",\r\n" 
				+ "    \"password\" : \"password123\"\r\n" 
		+ "}";

		RequestSpecification reqSpec = RestAssured.given();
		reqSpec.baseUri("https://restful-booker.herokuapp.com");
		reqSpec.headers("Content-Type", "application/json");
		reqSpec.body(payload);

		Response res = reqSpec.post("/auth");

		Assert.assertEquals(res.statusCode(), 200);
		String token = res.jsonPath().getString("token");
		
		
	}
	
	
	private void ValidateRespnse(Response res, CreateBookingRequest CBR , String Object)
	{
		Assert.assertEquals(res.jsonPath().getString(Object+"firstname"),CBR.getFirstname() );
		Assert.assertEquals(res.jsonPath().getString(Object+"lastname"),CBR.getLastname() );	
		Assert.assertEquals(res.jsonPath().getString(Object+"totalprice"),CBR.getTotalprice() );
		Assert.assertEquals(res.jsonPath().getString(Object+"depositpaid"),CBR.isDepositpaid() );
		Assert.assertEquals(res.jsonPath().getString(Object+"bookingdates.checkin"),CBR.getBookingdates().getCheckin() );
		Assert.assertEquals(res.jsonPath().getString(Object+"bookingdates.checkout"),CBR.getBookingdates().getCheckout() );
		Assert.assertEquals(res.jsonPath().getString(Object+"additionalneeds"),CBR.getAdditionalneeds() );
	}
	
	@Test (priority=1)
	public void CreateBookingId()
	{
		System.out.println("Techncredits");
		
		RestAssured.baseURI="https://restful-booker.herokuapp.com";
		
		Bookingdates bookingdates=new Bookingdates();
		bookingdates.setCheckin("2018-01-01");		
		bookingdates.setCheckout("2019-01-01");
		
		CreateBookingRequest CBR=new CreateBookingRequest();
		CBR.setAdditionalneeds("Breakfast");
		CBR.setDepositpaid(true);
		CBR.setFirstname("Jim");
		CBR.setLastname("Jacob");
		CBR.setTotalprice(232);
		CBR.setBookingdates(bookingdates);
		Response res=RestAssured.given()
		.headers("Content-Type","application/json")
		.body(CBR)
		.when()
		.post("/booking")
		.then().log().all().extract().response();
		
		
		
		//System.out.println(res.statusCode());
		Assert.assertEquals(res.statusCode(),200);
		//System.out.println(res.body().asPrettyString());
		int bookingid=res.jsonPath().getInt("bookingid");
		Assert.assertTrue(bookingid>0);
		ValidateRespnse(res,CBR,"booking.");
		
	}
	
	@Test (enabled=false)
	public void getBookingId()
	{
		int bookingid=2522;
		RestAssured.baseURI="https://restful-booker.herokuapp.com";
		Response res=RestAssured.given()
				.headers("Content-Type","application/json")
				.when()
				.get("/booking");
		
		List<Integer> bookingIdList=res.jsonPath().getList("bookingid");
		System.out.println(bookingIdList.size());
		Assert.assertTrue(bookingIdList.contains(bookingid));
				
		
	}
	
	@Test (priority=2)
	public void getBookingIdTest()
	{
		
		
		int bookingid=2522;
		RestAssured.baseURI="https://restful-booker.herokuapp.com";
		Response res=RestAssured.given()
				.headers("Content-Type","application/json")
				.when()
				.get("/booking/"+bookingid);
		ValidateRespnse(res,CBR,"");
		
		//CreateBookingRequest respnseBody=res.as(CreateBookingRequest.class);
		
		
		
				
		
	}
	
	
	
	























}
