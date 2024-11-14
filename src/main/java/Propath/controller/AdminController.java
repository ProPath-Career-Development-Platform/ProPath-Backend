package Propath.controller;

import Propath.dto.CompanyDto;
import Propath.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/admin")

public class AdminController {

    @Autowired
    private CompanyService companyService;

    @Autowired   // This annotation is optional for constructor-based injection
    public AdminController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/RegisterdCompanies")
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
       List<CompanyDto> companies = companyService.getALLCompanies();
       return ResponseEntity.ok(companies);
    }

    @PutMapping("/RegisterdCompanies/verify/{id}")
    public ResponseEntity<CompanyDto> updateCompanyStatus(@PathVariable("id") Long id,
                                                          @RequestBody CompanyDto updatedCompany) {
       CompanyDto companyDto = companyService.updateCompanyStatus(id, updatedCompany);
       return ResponseEntity.ok(companyDto);
    }

    @GetMapping("/companyRequests")
    public ResponseEntity<List<CompanyDto>> getAllRequests() {
        List<CompanyDto> requests = companyService.getAllRequests();
        return ResponseEntity.ok(requests);
    }

}
