package Propath.service.impl;

import Propath.dto.CompanyDto;
import Propath.mapper.CompanyMapper;
import Propath.model.Company;
import Propath.repository.CompanyRepository;
import Propath.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

        private CompanyRepository companyRepository;
    @Override
    public CompanyDto RegisterCompany(CompanyDto companyDto) {

        Company company = CompanyMapper.maptoCompany(companyDto);
        Company newCompany = companyRepository.save(company);

        return CompanyMapper.maptoCompanyDto(newCompany);
    }
}
