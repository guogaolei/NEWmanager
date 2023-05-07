package com.ghl.manage.dao;

import com.ghl.manage.entity.table.WaterGroundEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaterGroundDao {
    List<WaterGroundEntity> getWaterGround();
}
