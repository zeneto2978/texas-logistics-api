package com.jose.texaslogistics.driver;



public class DriverResponseDTO {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private DriverStatus status;

    public DriverResponseDTO(Driver driver){
        this.id = driver.getId();
        this.name = driver.getName();
        this.email = driver.getEmail();
        this.phone = driver.getPhone();
        this.status = driver.getStatus();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public DriverStatus getStatus() {
        return status;
    }
}
