package com.xdq.core.dao.impl;

import com.xdq.core.dao.AreaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

/**
 * <b><code>AreaDaoImpl</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/3/14 15:57.
 *
 * @author xiedeqi
 * @since xdq-core-mintaka 0.1.0
 */
@Repository
public class AreaDaoImpl implements AreaDao{
    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Override
    public Integer showCount() {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("select count(1) as count from D_HX_API_AREA");
        rowSet.next();
        return rowSet.getInt("count");
    }
}
