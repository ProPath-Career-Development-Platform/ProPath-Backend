package Propath.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FavoritesJobsDto {
    private Long id;
    private Long jobId;
    private Long companyId;
    private Long userId;
}
