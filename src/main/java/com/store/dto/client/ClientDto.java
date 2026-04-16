package com.store.dto.client;


import jakarta.validation.constraints.*;

public class ClientDto {

    @NotBlank(message = "firstName may not be empty")
    @Size(min = 1, max = 100, message = "firstName must be between 1 and 100 characters")
    private String firstName;

    @NotBlank(message = "lastName may not be empty")
    @Size(min = 1, max = 100, message = "lastName must be between 1 and 100 characters")
    private String lastName;

    @NotBlank(message = "email may not be empty")
    @Email(message = "email must be valid")
    private String email;

    @Size(max = 20, message = "phoneNumber must be at most 20 characters")
    private String phoneNumber;

    @Size(max = 200, message = "address must be at most 200 characters")
    private String address;

    @Size(max = 100, message = "country must be at most 100 characters")
    private String country;

    @Size(max = 100, message = "city must be at most 100 characters")
    private String city;

    @Size(max = 100, message = "state must be at most 100 characters")
    private String state;

    @NotBlank(message = "username may not be empty")
    @Size(min = 4, max = 50, message = "user must be between 4 and 50 characters")
    private String username;

    @NotBlank(message = "password may not be empty")
    @Size(min = 8, max = 100, message = "password must be between 8 and 100 characters")
    private String password;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "ClientDto [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", phoneNumber="
				+ phoneNumber + ", address=" + address + ", country=" + country + ", city=" + city + ", state=" + state
				+ ", username=" + username + ", password=" + password + "]";
	}	
    
}

