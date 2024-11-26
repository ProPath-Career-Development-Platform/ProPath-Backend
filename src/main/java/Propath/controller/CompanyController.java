package Propath.controller;

import Propath.dto.CompanyDto;
import Propath.service.CompanyService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@CrossOrigin("*")
@RestController
@RequestMapping("/jobprovider")
public class CompanyController {

    @Autowired
    private CompanyService companyService;


    //Create RegisterCompany REST API
    @PostMapping("/Setup")
    public ResponseEntity<CompanyDto> RegisterCompany(@RequestBody CompanyDto companyDto) {
        CompanyDto newCompany = companyService.RegisterCompany(companyDto);
        return new ResponseEntity<>(newCompany, HttpStatus.CREATED);
    }

    @PutMapping("/settings/company")
    public ResponseEntity<String> UpdateCompanyInfo(@RequestBody CompanyDto companyDto) {
        Boolean result = companyService.UpdateCompanyInfo(companyDto);
        if (result) {
            return ResponseEntity.ok("Company Info updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Company Info failed to update!");
        }
    }

    @PutMapping("/settings/companyFoundingInfo")
    public ResponseEntity<String> updateFoundingInfo(@RequestBody CompanyDto companyDto){
        Boolean result = companyService.UpdateFoundingInfo(companyDto);

        if(result) {
            return ResponseEntity.ok("Company Founding Info updated successfully");
        } else {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Company Info failed to update!");
        }

    }

    @PutMapping("/settings/contactInfo")
    public ResponseEntity<String> updateContactInfo(@RequestBody CompanyDto companyDto){

        Boolean result = companyService.UpdateContactInfo(companyDto);

        if(result){
            return ResponseEntity.ok("Contact info updated successfully");
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("contact info failed to update!");
        }
    }

    @PostMapping("/settings/password")
    public ResponseEntity<Boolean> UpdatePassword(@RequestBody CompanyDto companyDto){

        Boolean correctPwd = companyService.UpdateUserPassword(companyDto);

        if (correctPwd) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
    }

    @GetMapping("/company")
    public ResponseEntity<CompanyDto> getComapnyInfo(){

        CompanyDto companyDto = companyService.getCompanyDetails();

        return new ResponseEntity<>(companyDto,HttpStatus.OK);
    }

    @DeleteMapping("/company")
    public ResponseEntity<Boolean> DeleteCompany(){

        Boolean result = companyService.DeleteCompany();

        if (result) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
    }

    @GetMapping("/company/status")
    public ResponseEntity<String> comapanyStatus(){

        String result = companyService.getCompanyStatus();

        if(result.equals("active") || result.equals("pending") || result.equals("none")){
            return ResponseEntity.ok(result);
        }else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error");
        }


    }








}

