package com.ean.usercenter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ean.usercenter.model.domain.User;
import org.apache.ibatis.annotations.Mapper;


/**
* @author Asphyxia
* @description 针对表【user】的数据库操作Mapper
 *
* @createDate 2022-11-23 16:24:04
* @Entity generator.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {
}




