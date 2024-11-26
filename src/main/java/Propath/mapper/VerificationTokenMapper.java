package Propath.mapper;

import Propath.dto.VerficationTokenDto;
import Propath.model.VerificationToken;
import Propath.repository.VerficationRepository;
import org.springframework.stereotype.Component;

@Component
public class VerificationTokenMapper {

    private final VerficationRepository verficationRepository;

    public VerificationTokenMapper (VerficationRepository verficationRepository) { this.verficationRepository = verficationRepository;}

    public static VerficationTokenDto maptoVerificationTokenDto(VerificationToken token){
        return new VerficationTokenDto(
             token.getId(),
             token.getToken(),
             token.getExpirationTime(),
             token.getNewEmail(),
             token.getUser()
        );
    }

    public static VerificationToken maptoVeriicationToken(VerficationTokenDto tokenDto){
        return new VerificationToken(
                tokenDto.getId(),
                tokenDto.getToken(),
                tokenDto.getExpirationTime(),
                tokenDto.getNewEmail(),
                tokenDto.getUser()
        );
    }


}
