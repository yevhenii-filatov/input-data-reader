import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class XlsReader {

    final private static String path = "src/main/resources/inputData.xls";

    public List<InputData> read()  {

        try {
            List<InputData> dataList = new ArrayList<>();
            FileInputStream file = new FileInputStream(path);
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                dataList.add(getDataFromRow(row));
            }

            System.out.println(dataList);
            return dataList;
        } catch(Exception ex){
            log.error("File read error "+ path + ex.getMessage());
        }
        return new ArrayList<>();

    }

    private InputData getDataFromRow(Row row) {
        DataFormatter dataFormatter = new DataFormatter();
        int min = 0;
        int max = 0;
        String area = "";

        for (Cell cell : row) {
            switch (cell.getColumnIndex()) {
                case 0:
                    min = Integer.parseInt(dataFormatter.formatCellValue(cell));
                    break;
                case 1:
                    max = Integer.parseInt(dataFormatter.formatCellValue(cell));
                    break;

                case 2:
                    area = dataFormatter.formatCellValue(cell);
                    break;

                default:
                    break;
            }
        }
        return new InputData(min, max, area);
    }
}

