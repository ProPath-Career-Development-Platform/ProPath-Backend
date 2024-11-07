package Propath.service.impl;

import Propath.dto.CompanyDto;
import Propath.exception.ResourceNotFoundException;
import Propath.mapper.CompanyMapper;
import Propath.model.Company;
import Propath.model.User;
import Propath.repository.CompanyRepository;
import Propath.repository.UserRepository;
import Propath.service.CompanyService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//import static Propath.mapper.CompanyMapper.userRepository;

@Service
@AllArgsConstructor
public class CompanyServiceImpl implements CompanyService {

        private CompanyRepository companyRepository;
        private CompanyMapper companyMapper;
        private UserRepository userRepository;
        private PasswordEncoder passwordEncoder;

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

  /*  @Override
    public CompanyDto getCompanyByUserId(int userId) {
        Optional<Company> companyOptional = companyRepository.findByUserId(userId);

        if (companyOptional.isEmpty()) {
            throw new RuntimeException("Company not found for the given user ID");
        }

        return CompanyMapper.maptoCompanyDto(companyOptional.get());
    }*/


    @Override
    public Boolean UpdateCompanyInfo(CompanyDto companyDto) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName(); // Get the username of the logged-in user

            // Find the user by email
            Optional<User> userOptional = userRepository.findByEmail(userEmail);

            if (userOptional.isEmpty()) {
                throw new RuntimeException("User not found");
            }

            // Find the company by user ID
            Optional<Company> companyOptional = companyRepository.findByUserIdAndStatus(userOptional.get().getId(),"active");

            if (companyOptional.isEmpty()) {
                throw new RuntimeException("Company not found");
            }

            // Retrieve the Company object
            Company company = companyOptional.get();

            // Update the company details with values from CompanyDto
            company.setLogoImg(companyDto.getLogoImg());
            company.setBannerImg(companyDto.getBannerImg());
            company.setCompanyName(companyDto.getCompanyName());
            company.setAboutUs(companyDto.getAboutUs());

            // Save the updated company object
            companyRepository.save(company);

            return true;

        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public CompanyDto getCompanyDetails(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Get the username of the logged-in user

        // Find the user by email
        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }

        // Find the company by user ID
        Optional<Company> companyOptional = companyRepository.findByUserIdAndStatus(userOptional.get().getId(),"active");

        Company company;
        if (companyOptional.isEmpty()) {
          //  throw new RuntimeException("Company not found");
             company = new Company();
            company.setIsNew(true);
        }else{
             company = companyOptional.get();
            User user = company.getUser();
            if (user != null) {
                user.clearSensitiveDataForSettings();
            }


            company.setIsNew(false);


        }

        return CompanyMapper.maptoCompanyDto(company);




    }

    @Override
    public Boolean UpdateUserPassword(CompanyDto companyDto){

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName(); // Get the username of the logged-in user

            // Find the user by email
            Optional<User> userOptional = userRepository.findByEmail(userEmail);

            if (userOptional.isEmpty()) {
                throw new RuntimeException("User not found");
            }

            // Find the company by user ID
            Optional<Company> companyOptional = companyRepository.findByUserIdAndStatus(userOptional.get().getId(),"active");

            if (companyOptional.isEmpty()) {
                throw new RuntimeException("Company not found");
            }

            User user = userOptional.get();

            String userPwd = user.getPassword(); // current

            // Check if the entered password matches the current password
            if (passwordEncoder.matches(companyDto.getPwd(), userPwd)) {

                user.setPassword(passwordEncoder.encode(companyDto.getNewPwd())); // re-encode and save the password

                userRepository.save(user);

                return true;
            } else {
                return false;
            }

        } catch (RuntimeException e) {
            return false;
        }

    }

    @Override
    public Boolean UpdateFoundingInfo(CompanyDto companyDto){

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName(); // Get the username of the logged-in user

            // Find the user by email
            Optional<User> userOptional = userRepository.findByEmail(userEmail);

            if (userOptional.isEmpty()) {
                throw new RuntimeException("User not found");
            }

            // Find the company by user ID
            Optional<Company> companyOptional = companyRepository.findByUserIdAndStatus(userOptional.get().getId(),"active");

            if (companyOptional.isEmpty()) {
                throw new RuntimeException("Company not found");
            }

            // Retrieve the Company object
            Company company = companyOptional.get();

            // Update the company details with values from CompanyDto
            company.setOrganizationType(companyDto.getOrganizationType());
            company.setIndustryType(companyDto.getIndustryType());
            company.setEstablishedDate(companyDto.getEstablishedDate());
            company.setCompanyWebsite(companyDto.getCompanyWebsite());
            company.setCompanyVision(companyDto.getCompanyVision());

            // Save the updated company object
            companyRepository.save(company);

            return true;

        } catch (RuntimeException e) {
            return false;
        }

    }

    @Override
    public Boolean UpdateContactInfo(CompanyDto companyDto){

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName(); // Get the username of the logged-in user

            // Find the user by email
            Optional<User> userOptional = userRepository.findByEmail(userEmail);

            if (userOptional.isEmpty()) {
                throw new RuntimeException("User not found");
            }

            // Find the company by user ID
            Optional<Company> companyOptional = companyRepository.findByUserIdAndStatus(userOptional.get().getId(),"active");

            if (companyOptional.isEmpty()) {
                throw new RuntimeException("Company not found");
            }

            // Retrieve the Company object
            Company company = companyOptional.get();

            // Update the company details with values from CompanyDto
            company.setLocation(companyDto.getLocation());
            company.setContactNumber(companyDto.getContactNumber());
            company.setEmail(companyDto.getEmail());

            // Save the updated company object
            companyRepository.save(company);

            return true;

        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public Boolean DeleteCompany(){

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userEmail = authentication.getName(); // Get the username of the logged-in user

            // Find the user by email
            Optional<User> userOptional = userRepository.findByEmail(userEmail);

            if (userOptional.isEmpty()) {
                throw new RuntimeException("User not found");
            }

            // Find the company by user ID
            Optional<Company> companyOptional = companyRepository.findByUserIdAndStatus(userOptional.get().getId(),"active");

            if (companyOptional.isEmpty()) {
                throw new RuntimeException("Company not found");
            }

            // Retrieve the Company object
            Company company = companyOptional.get();

            // Update the company details with values from CompanyDto
            company.setStatus("delete");


            // Save the updated company object
            companyRepository.save(company);

            return true;

        } catch (RuntimeException e) {
            return false;
        }

    }



}
