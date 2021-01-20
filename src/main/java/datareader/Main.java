package datareader;

import datareader.model.InputData;
import datareader.service.InputDataExcelReader;
import datareader.service.InputDataReader;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String path = "src/main/resources/input_data_sample.xlsx";
        InputDataReader reader = new InputDataExcelReader();
        List<InputData> inputData = reader.read(path);
        inputData.forEach(System.out::println);
    }
}
