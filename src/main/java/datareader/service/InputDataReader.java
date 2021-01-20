package datareader.service;

import datareader.model.InputData;

import java.util.List;

/**
 * @author Yevhenii Filatov
 * @since 1/20/21
 */

public interface InputDataReader {
    List<InputData> read(String filepath);
}
