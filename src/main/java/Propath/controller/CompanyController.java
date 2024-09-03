package Propath.controller;

import Propath.dto.CompanyDto;
import Propath.service.CompanyService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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




}

