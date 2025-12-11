package com.karanburadkar.bookstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AddressDto {

    private Long id;

    @NotBlank(message = "Landmark cannot be blank")
    @Size(max = 100, message = "Landmark must be at most 100 characters")
    private String landmark;

    @NotBlank(message = "City cannot be blank")
    @Size(max = 50, message = "City must be at most 50 characters")
    private String city;

    @NotBlank(message = "District cannot be blank")
    @Size(max = 50, message = "District must be at most 50 characters")
    private String district;

    @NotBlank(message = "State cannot be blank")
    @Size(max = 50, message = "State must be at most 50 characters")
    private String state;

    @NotBlank(message = "Country cannot be blank")
    @Size(max = 50, message = "Country must be at most 50 characters")
    private String country;

    @NotBlank(message = "Postal code cannot be blank")
    @Pattern(regexp = "\\d{5,10}", message = "Postal code must be numeric and 5 to 10 digits")
    @JsonProperty("postal_code")
    private String postalCode;

    public AddressDto() {
    }

    public AddressDto(
            String landmark,
            String city,
            String district,
            String state,
            String country,
            String postalCode
    ) {
        this.landmark = landmark;
        this.city = city;
        this.district = district;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
