package com.ef.dao;

import com.ef.model.ServerIllegalAccess;

import java.util.List;

/**
 * Created by yaoquanyu on 11/15/17.
 */
public interface ServerIllegalAccessDao {

    void save(ServerIllegalAccess serverIllegalAccess);
    void saveExceedLimitAccesses(List<ServerIllegalAccess> exceedLimitAccesses);

}
