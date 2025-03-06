package Review.auth.Dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileCreatedDTO {
    private Long userId;
    private Long profileId;
}