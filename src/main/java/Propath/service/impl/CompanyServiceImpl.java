package Propath.service.impl;

import Propath.dto.CompanyDto;
import Propath.exception.ResourceNotFoundException;
import Propath.mapper.CompanyMapper;
import Propath.model.Company;
import Propath.repository.CompanyRepository;
import Propath.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

        private CompanyRepository companyRepository;
        private CompanyMapper companyMapper;
    @Override
    public CompanyDto RegisterCompany(CompanyDto companyDto) {

        Company company = CompanyMapper.maptoCompany(companyDto);
        Company newCompany = companyRepository.save(company);

        return CompanyMapper.maptoCompanyDto(newCompany);
    }

    @Override
    public List<CompanyDto> getALLCompanies() {
        List<Company> companies = companyRepository.findAll();
        return companies.stream().map((company) -> CompanyMapper.maptoCompanyDto(company)).collect(Collectors.toList());
    }

    @Override
    public CompanyDto updateCompanyStatus(Long id, CompanyDto updatedCompany) {
        Company company = companyRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Company not found with given ID"+id)
        );
        company.setStatus(updatedCompany.getStatus());
        Company updatedCompanyObj = companyRepository.save(company);

        return CompanyMapper.maptoCompanyDto(updatedCompanyObj);
    }

    @Override
    public CompanyDto getCompanyByUserId(Long userId) {
        Company company = companyRepository.findByUserId(userId);
        return CompanyMapper.maptoCompanyDto(company);
    }
}
