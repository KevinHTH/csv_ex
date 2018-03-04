package sg.vinova.noticeboard.factory;

import java.util.ArrayList;
import java.util.List;


import sg.vinova.noticeboard.model.Category;

/**
 * Created by Ray on 3/8/17.
 */

public class DataFactory {

    private static DataFactory instance = null;

    public static DataFactory getInstance() {
        if (instance == null)
            instance = new DataFactory();
        return instance;
    }



}
