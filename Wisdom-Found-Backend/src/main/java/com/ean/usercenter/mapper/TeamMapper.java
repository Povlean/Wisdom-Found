package com.ean.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import com.ean.usercenter.model.domain.Team;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Asphyxia
* @description 针对表【team】的数据库操作Mapper
* @createDate 2023-02-12 16:20:30
* @Entity generator.domain.Team
*/
@Mapper
public interface TeamMapper extends BaseMapper<Team> {

}




