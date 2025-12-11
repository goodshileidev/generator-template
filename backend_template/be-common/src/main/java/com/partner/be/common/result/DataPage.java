package com.partner.be.common.result;

import com.google.common.collect.Lists;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Class: DataPage
 * Description: Stores data page
 *
 * Created: January 12, 2018
 * Version: 1.0
 *
 * History: (Version) Author Time Comments
 */
@Data
public class DataPage<T> implements Serializable {

    private static final long serialVersionUID = 5094809247739398697L;

    private List<T> list = Lists.newArrayList();                // list result of this page
    private PageInfo pageInfo = new PageInfo();

}
