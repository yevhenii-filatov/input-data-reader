package datareader.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InputData {
    private long minPrice;
    private long maxPrice;
    private String area;
}
